package tools.redstone.redstonetools.telemetry;

import com.google.gson.Gson;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.telemetry.dto.TelemetryAuth;
import tools.redstone.redstonetools.telemetry.dto.TelemetryCommand;
import tools.redstone.redstonetools.telemetry.dto.TelemetryException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;
import static tools.redstone.redstonetools.RedstoneToolsClient.LOGGER;

public class TelemetryClient {
    private static final String BASE_URL = FabricLoader.getInstance().isDevelopmentEnvironment()
            ? "https://localhost/api/telemetry/v1"
            : "https://redstone.tools/api/telemetry/v1";
    private static final int SESSION_REFRESH_TIME_SECONDS = 60 * 5 - 10; // 5 minutes - 10 seconds
    private static final int REQUEST_SEND_TIME_MILLISECONDS = 50;
    private static final int REQUEST_VALID_FOR_SECONDS = 30;

    private final TelemetryManager manager = INJECTOR.getInstance(TelemetryManager.class);

    private static volatile Instant lastAuthorization = Instant.MIN;

    private final Gson gson = new Gson();
    private final HttpClient httpClient;
    private final Queue<Pair<HttpRequest.Builder, Instant>> requestQueue = new ConcurrentLinkedQueue<>();

    private volatile String token;

    public TelemetryClient() {
        LOGGER.info("Initializing telemetry client");

        httpClient = HttpClient.newBuilder()
                .build();

        Executors.newSingleThreadExecutor()
                .execute(this::refreshSessionThread);

        Executors.newSingleThreadExecutor()
                .execute(this::sendQueuedRequestsAsync);
    }

    public void sendCommand(TelemetryCommand command) {
        if (manager.telemetryEnabled) {
            addRequest(createRequest("/command", command));
        }
    }

    public void sendException(TelemetryException exception) {
        if (manager.telemetryEnabled) {
            addRequest(createRequest("/exception", exception));
        }
    }

    private void addRequest(HttpRequest.Builder request) {
        requestQueue.add(Pair.of(request, Instant.now()));
    }

    public synchronized void waitForQueueToEmpty() {
        while (!requestQueue.isEmpty()) {
            try {
                TimeUnit.MILLISECONDS.sleep(REQUEST_SEND_TIME_MILLISECONDS);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private HttpRequest.Builder createRequest(String path, Object body) {
        final String bodyData = body == null ? "" : gson.toJson(body);
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(BASE_URL + path))
                .POST(HttpRequest.BodyPublishers.ofString(bodyData));
    }

    private boolean isSuccessful(HttpResponse<?> response) {
        if (response == null) return false;
        return response.statusCode() >= 200 && response.statusCode() < 300;
    }

    private synchronized CompletableFuture<Void> sendQueuedRequestsAsync() {
        return CompletableFuture.runAsync(() -> {
            while (true) {
                while (!requestQueue.isEmpty()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(REQUEST_SEND_TIME_MILLISECONDS);
                    } catch (InterruptedException ignored) {
                    }

                    var pair = Objects.requireNonNull(requestQueue.peek());
                    var request = pair.first();
                    var queuedAt = pair.second();

                    if (queuedAt.plusSeconds(REQUEST_VALID_FOR_SECONDS).isBefore(Instant.now())) {
                        requestQueue.remove();

                        continue;
                    }

                    if (token != null) {
                        request.header("Authorization", token);
                    }

                    var response = sendPostRequest(request);

                    if (response == null || !isSuccessful(response)) {
                        if (response != null && responseIsUnauthorized(response)) {
                            lastAuthorization = Instant.MIN;
                        }

                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException ignored) {
                        }

                        continue;
                    }

                    requestQueue.remove();
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(REQUEST_SEND_TIME_MILLISECONDS);
                } catch (InterruptedException ignored) {
                }
            }
        });
    }

    private synchronized HttpResponse<String> sendPostRequest(HttpRequest.Builder request) {
        LOGGER.trace("Sending telemetry request to " + request.build().uri());

        // this doesnt have to be async as the only place this is
        // ever called is from another thread

//        return CompletableFuture.supplyAsync(() -> {
            try {
                return httpClient.send(request.build()
                        /* the json as a string */,
                        HttpResponse.BodyHandlers.ofString());
            } catch (ConnectException e) {
                // Either the server is down or the user isn't connected to the internet
                LOGGER.debug("Failed to send telemetry request: " + e.getMessage());
            } catch (InterruptedException | IOException e) {
                LOGGER.error("Failed to send telemetry request", e);
            }

            return null;
//        });
    }

    private void refreshSessionThread() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ignored) {
            }

            var nextAuthorization = lastAuthorization.plusSeconds(SESSION_REFRESH_TIME_SECONDS);
            if (Instant.now().isAfter(nextAuthorization)) {
                refreshSessionAsync().join();
            }
        }
    }

    private synchronized CompletableFuture<Boolean> refreshSessionAsync() {
        return CompletableFuture.supplyAsync(() -> {
            var request = createRequest(token == null ? "/session/create" : "/session/refresh", getAuth());

            if (token != null) {
                request.header("Authorization", token);
            }

            var response = sendPostRequest(request);

            if (response == null)
                return false;
            if (!isSuccessful(response)) {
                LOGGER.warn("Failed to refresh telemetry session, response code: " + response.statusCode() + ", message: " + response.body());
                if (responseIsUnauthorized(response)) {
                    token = null;
                }

                return false;
            }

            token = response.body();

            LOGGER.info("Refreshed telemetry session");
            lastAuthorization = Instant.now();
            return true;
        });
    }

    private TelemetryAuth getAuth() {
        var session = MinecraftClient.getInstance().getSession();

        return new TelemetryAuth(
            session.getUuid(),
            session.getProfile().getId().toString(),
            session.getAccessToken()
        );
    }

    private static boolean responseIsUnauthorized(HttpResponse<?> response) {
        return response.statusCode() == 401
            || response.statusCode() == 403;
    }
}
