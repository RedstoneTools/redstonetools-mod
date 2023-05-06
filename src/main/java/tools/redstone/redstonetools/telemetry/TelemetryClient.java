package tools.redstone.redstonetools.telemetry;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import tools.redstone.redstonetools.telemetry.dto.TelemetryAuth;
import tools.redstone.redstonetools.telemetry.dto.TelemetryCommand;
import tools.redstone.redstonetools.telemetry.dto.TelemetryException;

import java.io.IOException;
import java.net.ConnectException;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;
import static tools.redstone.redstonetools.RedstoneToolsClient.LOGGER;

public class TelemetryClient {
    private static final String BASE_URL = "https://redstone.tools/api/v1";
    private static final int SESSION_REFRESH_TIME_SECONDS = 60 * 5 - 10; // 5 minutes - 10 seconds
    private static final int REQUEST_SEND_TIME_MILLISECONDS = 50;

    private static volatile Instant lastAuthorization = Instant.MIN;

    private final Gson gson = new Gson();
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Queue<Request.Builder> requestQueue = new ConcurrentLinkedQueue<>();

    private volatile String token;

    public TelemetryClient() {
        LOGGER.info("Initializing telemetry client");

        Executors.newSingleThreadExecutor()
                .execute(this::refreshSessionThread);

        Executors.newSingleThreadExecutor()
                .execute(this::sendQueuedRequestsAsync);
    }

    public void sendCommand(TelemetryCommand command) {
        if (INJECTOR.getInstance(TelemetryManager.class).telemetryEnabled) {
            requestQueue.add(createRequest("/command", command));
        }
    }

    public void sendException(TelemetryException exception) {
        if (INJECTOR.getInstance(TelemetryManager.class).telemetryEnabled) {
            requestQueue.add(createRequest("/exception", exception));
        }
    }

    public synchronized void waitForQueueToEmpty() {
        while (!requestQueue.isEmpty()) {
            try {
                TimeUnit.MILLISECONDS.sleep(REQUEST_SEND_TIME_MILLISECONDS);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private Request.Builder createRequest(String path, Object body) {
        return new Request.Builder()
                .url(BASE_URL + path)
                .post(RequestBody.create(body == null ? "" : gson.toJson(body), MediaType.parse("application/json")));
    }

    private synchronized CompletableFuture<Void> sendQueuedRequestsAsync() {
        return CompletableFuture.runAsync(() -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(REQUEST_SEND_TIME_MILLISECONDS);
                } catch (InterruptedException ignored) {
                }

                while (!requestQueue.isEmpty()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(250);
                    } catch (InterruptedException ignored) {
                    }

                    var request = requestQueue.peek();
                    assert request != null;

                    if (token != null) {
                        request.addHeader("Authorization", token);
                    }

                    var response = sendPostRequestAsync(request).join();

                    if (response == null || !response.isSuccessful()) {
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
            }
        });
    }

    private synchronized @NotNull CompletableFuture<Response> sendPostRequestAsync(Request.Builder request) {
        LOGGER.trace("Sending telemetry request to " + request.build().url());

        return CompletableFuture.supplyAsync(() -> {
            try {
                return httpClient.newCall(request.build()).execute();
            } catch (ConnectException e) {
                // Either the server is down or the user isn't connected to the internet
            } catch (IOException e) {
                LOGGER.error("Failed to send telemetry request", e);
            }

            return null;
        });
    }

    private void refreshSessionThread() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ignored) {
            }

            var nextAuthorization = lastAuthorization.plusSeconds(SESSION_REFRESH_TIME_SECONDS);
            if (Instant.now().isAfter(nextAuthorization)) {
                refreshSessionAsync().join();
            }
        }
    }

    private synchronized CompletableFuture<Boolean> refreshSessionAsync(boolean forceCreateNew) {
        return CompletableFuture.supplyAsync(() -> {
            var response = sendPostRequestAsync(createRequest(token == null || forceCreateNew ? "/session/create" : "/session/refresh", getAuth())).join();

            if (response == null || !response.isSuccessful()) {
                if (response != null && responseIsUnauthorized(response)) {
                    token = null;
                }

                return false;
            }

            try (var body = response.body()) {
                token = body.string();
            } catch (IOException e) {
                LOGGER.error("Failed to read telemetry session response", e);
                return false;
            }

            LOGGER.debug("Refreshed telemetry session");
            lastAuthorization = Instant.now();
            return true;
        });
    }

    private synchronized CompletableFuture<Boolean> refreshSessionAsync() {
        return refreshSessionAsync(false);
    }

    private synchronized CompletableFuture<Boolean> createSessionAsync() {
        return refreshSessionAsync(true);
    }

    private TelemetryAuth getAuth() {
        var session = MinecraftClient.getInstance().getSession();

        return new TelemetryAuth(
            session.getUuid(),
            session.getProfile().getId().toString(),
            session.getAccessToken()
        );
    }

    private static boolean responseIsUnauthorized(Response response) {
        return response.code() == 401
            || response.code() == 403;
    }
}
