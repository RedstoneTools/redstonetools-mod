package com.domain.redstonetools.telemetry;

import com.domain.redstonetools.telemetry.dto.TelemetryAuth;
import com.domain.redstonetools.telemetry.dto.TelemetryCommand;
import com.domain.redstonetools.telemetry.dto.TelemetryException;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.domain.redstonetools.RedstoneToolsClient.LOGGER;

public class TelemetryClient {
    private static final String BASE_URL = "https://redstone.tools/api/v1";
    private static final int SESSION_EXPIRE_TIME_SECONDS = 60 * 5 - 10; // 5 minutes - 10 seconds
    private static final int FAILED_REQUEST_RETRY_TIME_MILLIS = 1000 * 5; // 5 seconds

    private final Gson gson = new Gson();
    private final OkHttpClient httpClient = new OkHttpClient();

    private volatile String token;

    public TelemetryClient() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::refreshSessionAsync, 0, SESSION_EXPIRE_TIME_SECONDS, TimeUnit.SECONDS);
    }

    public CompletableFuture<Response> sendCommandAsync(TelemetryCommand command) {
        return sendPostRequestAsync("/command", command);
    }

    public CompletableFuture<Response> sendExceptionAsync(TelemetryException exception) {
        return sendPostRequestAsync("/exception", exception);
    }

    private synchronized CompletableFuture<Response> sendPostRequestAsync(String path, Object body) {
        var request = new Request.Builder()
                .url(BASE_URL + path)
                .post(RequestBody.create(MediaType.parse("application/json"), body == null ? "" : gson.toJson(body)));

        if (token != null) {
            request.addHeader("Authorization", token);
        }

        return CompletableFuture.supplyAsync(() -> {
            Response response = null;
            try {
                response = httpClient.newCall(request.build()).execute();
            } catch (ConnectException e) {
                // Either the server is down or the user is offline
            } catch (IOException e) {
                LOGGER.error("Failed to send telemetry request", e);
            }

            if (response != null && response.isSuccessful()) {
                return response;
            }

            try {
                Thread.sleep(FAILED_REQUEST_RETRY_TIME_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (response != null && responseIsUnauthorized(response)) {
                LOGGER.error("Failed to send telemetry request because the session was invalid, creating new session");

                createSessionAsync().join();
            }

            return sendPostRequestAsync(path, body).join();
        });
    }

    private boolean responseIsUnauthorized(Response response) {
        return response.code() == 401
            || response.code() == 403;
    }

    private synchronized CompletableFuture<Void> refreshSessionAsync(boolean forceCreateNew) {
        if (forceCreateNew) {
            token = null;
        }

        return CompletableFuture.runAsync(() -> {
            var response = sendPostRequestAsync(token == null ? "/session/create" : "/session/refresh", getAuth()).join();

            if (response == null) {
                return;
            }

            try {
                token = response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            LOGGER.debug("Refreshed telemetry session");
        });
    }

    private synchronized CompletableFuture<Void> refreshSessionAsync() {
        return refreshSessionAsync(false);
    }

    private synchronized CompletableFuture<Void> createSessionAsync() {
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
}
