package com.domain.redstonetools.mixin;

import com.domain.redstonetools.macroStuff.MacroSelectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsOptionsScreen.class)
public class OptionsScreenMixin extends GameOptionsScreen {
    public OptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    //  @Shadow @Final private GameOptions settings;



    @Inject(method = "init", at = @At("TAIL"))
    public void addMacroButton(CallbackInfo ci) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height / 6 + 36, 150, 20, Text.of("Macros..."), (button) -> {
            this.client.setScreen(new MacroSelectScreen(this,super.gameOptions,Text.of("Macros")));
        }));
    }

}
