package tools.redstone.redstonetools.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.gui.UpdatePopupScreen;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static tools.redstone.redstonetools.RedstoneToolsClient.LOGGER;

@Mixin(TitleScreen.class)

public class UpdatePopupMixin extends Screen {
    public boolean updateChecked = false;

    public UpdatePopupMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (updateChecked)
            return;

        try {
            LOGGER.info("Checking for updates...");

            // timeout before aborting connection
            // gh took quite long to respond on some machines
            // so i think the timeout should be 1s
            final long timeout = 1000;

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.of(timeout, ChronoUnit.MILLIS))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/repos/RedstoneTools/redstonetools/releases/latest"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() != 200)
                return;

            Gson gson = new Gson();
            JsonObject release = gson.fromJson(responseBody, JsonObject.class);
            URI uri = new URI(release.get("html_url").getAsString());
            String newVersion = release.get("tag_name").getAsString();

            LOGGER.info("Found latest version: " + newVersion);
            if (RedstoneToolsClient.MOD_VERSION.equals(newVersion) || newVersion.contains("alpha") || newVersion.contains("beta")) {
                LOGGER.info("Already up to date, current version: " + RedstoneToolsClient.MOD_VERSION + ", new version: " + newVersion);
                return;
            }

            LOGGER.info("Found newer version, current version: " + RedstoneToolsClient.MOD_VERSION + ", new version: " + newVersion);
            MinecraftClient.getInstance().setScreen(new UpdatePopupScreen(this, uri, newVersion));
        } catch (Exception e) {
            LOGGER.warn("Failed to check for RedstoneTools updates");
            e.printStackTrace();
        } finally {
            updateChecked = true;
        }
    }
}
