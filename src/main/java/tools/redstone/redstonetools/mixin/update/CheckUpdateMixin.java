package tools.redstone.redstonetools.mixin.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
    @Unique
    private static boolean updateChecked = false;

    @Unique
    private static MutableText updateStatus = (MutableText) Text.of("Redstone Tools Version: " + MOD_VERSION + "(Bug found, report on Github)");
    @Unique
    private static URI uri;
    public CheckUpdateMixin() {
        super(Text.of("UpdateText(Bug found, report on Github)"));
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void checkUpdate(CallbackInfo ci) {
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
            uri = new URI(release.get("html_url").getAsString());
            String newVersion = release.get("tag_name").getAsString();

            LOGGER.info("Found latest version: " + newVersion);
            if (newVersion.contains("alpha") || newVersion.contains("beta")) {
                LOGGER.info("Not showing an update popup for alpha or beta release, current version: " + MOD_VERSION + ", new version: " + newVersion);
                return;
            }

            Style underline = Style.EMPTY;
            if (RedstoneToolsClient.MOD_VERSION.equals(newVersion)) {
                LOGGER.info("Already up to date, current version: " + MOD_VERSION);
                updateStatus = (MutableText) Text.of("Redstone Tools " + MOD_VERSION);
            } else {
                LOGGER.info("Found newer version, current version: " + RedstoneToolsClient.MOD_VERSION + ", new version: " + newVersion);
                updateStatus = (MutableText) Text.of("Redstone Tools " + MOD_VERSION + " (");
                updateStatus.append(Text.of("Click to Update").getWithStyle(underline.withUnderline(true)).getFirst());
                updateStatus.append(")");
            }

        } catch (Exception e) {
            LOGGER.warn("Failed to check for RedstoneTools updates");
            e.printStackTrace();
        } finally {
            updateChecked = true;
        }
    }

    @Inject(method="init", at = @At("HEAD"))
    public void updateTextInjection(CallbackInfo ci){
        this.addDrawableChild(new PressableTextWidget(4, 4, textRenderer.getWidth(updateStatus), textRenderer.fontHeight, updateStatus, button -> Util.getOperatingSystem().open(uri), textRenderer));
    }
}
