package tools.redstone.redstonetools.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import tools.redstone.redstonetools.macros.KeyBindingMixin;

public class KeyBindingUtils {

    public static void removeKeyBinding(KeyBinding keyBinding) {
        ((KeyBindingMixin)keyBinding).removeKeybinding(keyBinding);
    }

    public static boolean isKeyAlreadyBound(Key key) {
        KeyBinding[] keyBindings = MinecraftClient.getInstance().options.allKeys;
        for (KeyBinding keyBinding : keyBindings) {
            if (keyBinding.getDefaultKey() == key) { return true; }
        }
        return false;
    }

}
