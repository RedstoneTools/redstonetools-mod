package tools.redstone.redstonetools.mixin.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.RedstoneToolsClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static tools.redstone.redstonetools.RedstoneToolsClient.LOGGER;
import static tools.redstone.redstonetools.RedstoneToolsClient.MOD_VERSION;

@Mixin(TitleScreen.class)
public class CheckUpdateMixin extends Screen {
    public boolean updateChecked = false;

    public CheckUpdateMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (updateChecked)
            return;

        try {
            LOGGER.info("Checking for updates...");

            HttpClient client = HttpClient.newBuilder()
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/repositories/597142955/releases/latest"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() < 200 || 299 < response.statusCode()) {
                LOGGER.error("Got status code " + response.statusCode() + " while trying to check for updates");
                return;
            }

            Gson gson = new Gson();
            JsonObject release = gson.fromJson(responseBody, JsonObject.class);
            URI uri = new URI(release.get("html_url").getAsString());
            String newVersion = release.get("tag_name").getAsString();

            LOGGER.info("Found latest version: " + newVersion);
            if (newVersion.contains("alpha") || newVersion.contains("beta")) {
                LOGGER.info("Not showing an update popup for alpha or beta release, current version: " + MOD_VERSION + ", new version: " + newVersion);
                return;
            }

            if (RedstoneToolsClient.MOD_VERSION.equals(newVersion)) {
                LOGGER.info("Already up to date, current version: " + MOD_VERSION);
                return;
            }

            LOGGER.info("Found newer version, current version: " + RedstoneToolsClient.MOD_VERSION + ", new version: " + newVersion);

            var parentScreen = MinecraftClient.getInstance().currentScreen;
            var popup = new ConfirmScreen(confirmed -> {
                MinecraftClient.getInstance().setScreen(parentScreen);

                if (confirmed) {
                    Util.getOperatingSystem().open(uri);
                }
            }, Text.of("Update Available"), Text.of("An update is available for redstone tools! You are on version " + MOD_VERSION + " but version " + newVersion + " is available."), Text.of("Go to release"), Text.of("Ignore"));

            MinecraftClient.getInstance().setScreen(popup);
        } catch (Exception e) {
            LOGGER.warn("Failed to check for RedstoneTools updates");
            e.printStackTrace();
        } finally {
            updateChecked = true;
        }
    }
}
