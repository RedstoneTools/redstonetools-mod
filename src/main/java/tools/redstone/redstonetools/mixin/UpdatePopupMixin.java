package tools.redstone.redstonetools.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.gui.UpdatePopupScreen;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

@Mixin(TitleScreen.class)

public class UpdatePopupMixin extends Screen {
    private static long timeout = 250;

    public boolean updateChecked = false;

    public UpdatePopupMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (updateChecked)
            return;

        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(timeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url("https://api.github.com/repos/RedstoneTools/redstonetools/releases/latest")
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            if (response.code() != 200)
                return;

            Gson gson = new Gson();
            JsonObject release = gson.fromJson(responseBody, JsonObject.class);
            URI uri = new URI(release.get("html_url").getAsString());
            String newVersion = release.get("tag_name").getAsString();

            if (RedstoneToolsClient.MOD_VERSION.equals(newVersion) || newVersion.contains("alpha") || newVersion.contains("beta"))
                return;

            MinecraftClient.getInstance().setScreen(new UpdatePopupScreen(this, uri, newVersion));
        } catch (JsonSyntaxException | IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            updateChecked = true;
        }
    }
}
