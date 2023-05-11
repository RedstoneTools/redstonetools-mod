package tools.redstone.redstonetools.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import tools.redstone.redstonetools.macros.KeyBindingMixin;

import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixinImpl implements KeyBindingMixin {

    @Shadow @Final private static Map<String, KeyBinding> KEYS_BY_ID;

    @Shadow @Final private static Map<InputUtil.Key, KeyBinding> KEY_TO_BINDINGS;

    @Override
    public void removeKeybinding(KeyBinding keyBinding) {
        KEYS_BY_ID.entrySet().removeIf(entry -> entry.getValue().equals(this));
        KEY_TO_BINDINGS.entrySet().removeIf(entry -> entry.getValue().equals(this));
    }

}
