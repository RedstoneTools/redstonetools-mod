package tools.redstone.redstonetools.utils;

import net.minecraft.client.option.KeyBinding;
import tools.redstone.redstonetools.macros.KeyBindingMixin;

public class KeyBindingUtils {

    public static void removeKeyBinding(KeyBinding keyBinding) {
        ((KeyBindingMixin)keyBinding).removeKeybinding(keyBinding);
    }

}
