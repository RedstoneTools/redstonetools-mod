package tools.redstone.redstonetools.macros;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class InvisibleKeyBinding extends KeyBinding {

    public InvisibleKeyBinding(String translationKey, int code, String category) {
        super(translationKey, code, category);
    }

    public InvisibleKeyBinding(String translationKey, InputUtil.Type type, int code, String category) {
        super(translationKey, type, code, category);
    }
}
