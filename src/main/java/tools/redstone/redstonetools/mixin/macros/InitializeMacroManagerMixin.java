package tools.redstone.redstonetools.mixin.macros;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.macros.MacroManager;
import tools.redstone.redstonetools.utils.FeatureUtils;

@Mixin(MinecraftClient.class)
public class InitializeMacroManagerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void registerMacros(RunArgs args, CallbackInfo ci){
        FeatureUtils.getFeature(MacroManager.class);// should register macro keybinds
    }
}
