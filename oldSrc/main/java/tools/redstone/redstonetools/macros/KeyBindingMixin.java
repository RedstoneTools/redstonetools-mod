package tools.redstone.redstonetools.macros;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;

public interface KeyBindingMixin {

    void removeKeybinding(KeyBinding keyBinding);

    KeyBinding getBindingFromKey(Key key);

}
