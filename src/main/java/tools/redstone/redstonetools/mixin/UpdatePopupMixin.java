package tools.redstone.redstonetools.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.*;

import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.gui.UpdatePopupScreen;

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
            String currentVersion = RedstoneToolsClient.MOD_VERSION;

            if (currentVersion.split("-")[2] == "beta")
                return;

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(timeout, TimeUnit.MILLISECONDS);
            client.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
            Request request = new Request.Builder()
                    .url("https://api.github.com/repos/RedstoneTools/redstonetools/releases/latest")
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            Gson gson = new Gson();
            JsonObject release = gson.fromJson(responseBody, JsonObject.class);
            String newVersion = release.get("tag_name").getAsString();

            if (currentVersion.equals(newVersion))
                return;

            MinecraftClient.getInstance().setScreen(new UpdatePopupScreen(this));
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        } finally {
            updateChecked = true;
        }

    }
}
