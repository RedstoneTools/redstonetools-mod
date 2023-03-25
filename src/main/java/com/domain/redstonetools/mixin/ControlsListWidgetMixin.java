package com.domain.redstonetools.mixin;

import com.domain.redstonetools.macros.gui.InvisibleKeyBinding;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Arrays;
import java.util.List;

@Mixin(ControlsListWidget.class)
public class ControlsListWidgetMixin {

     @ModifyVariable(method = "<init>", at = @At("STORE"), ordinal = 0)
     public KeyBinding[] removeInvisibleKeyBinds(KeyBinding[] keyBinds) {
         List<KeyBinding> keyBindingList = new java.util.ArrayList<>(Arrays.stream(keyBinds).toList());
         keyBindingList.removeIf(keyBinding -> keyBinding instanceof InvisibleKeyBinding);

         return keyBindingList.toArray(new KeyBinding[0]);
     }

}
