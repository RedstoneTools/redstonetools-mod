package tools.redstone.redstonetools.mixin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static tools.redstone.redstonetools.features.commands.update.UpdateFeature.lastEdit;

@Mixin(LocalSession.class)
public class LocalSessionMixin {

    @Inject(method = "remember", at = @At("TAIL"), remap = false)
    public void remember(EditSession editSession, CallbackInfo ci) {
        lastEdit = editSession;
    }
}
