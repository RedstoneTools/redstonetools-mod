package com.domain.redstonetools.mixin;

import com.domain.redstonetools.gui.screens.MacrosScreen;
import com.domain.redstonetools.utils.ReflectionUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin {
    private final OptionsScreen self = (OptionsScreen) (Object) this;

    private static final Method addDrawableChildMethod;

    static {
        addDrawableChildMethod = Arrays.stream(Screen.class.getMethods())
                .filter(method -> method.getName().equals("addDrawableChild"))
                .findFirst()
                .orElseThrow();

        addDrawableChildMethod.setAccessible(true);
    }

    @Shadow @Final private GameOptions settings;

    private MinecraftClient getClient() {
        return (MinecraftClient) ReflectionUtils.getFieldValue(Screen.class, self, "client");
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        addDrawableChild(new ButtonWidget(self.width / 2 - 310 / 2, self.height / 6 + 144 - 6, 310, 20, new LiteralText("Redstone Tools Settings..."), (button) -> {
            getClient().setScreen(new MacrosScreen(self, settings));
        }));
    }

    private <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        try {
            return (T) addDrawableChildMethod.invoke(this, drawableElement);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
