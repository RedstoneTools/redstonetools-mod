package tools.redstone.redstonetools.mixin.macros;

import net.minecraft.client.MinecraftClient;
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
import tools.redstone.redstonetools.malilib.GuiConfigs;
import tools.redstone.redstonetools.malilib.GuiMacroManager;

@Mixin(ControlsOptionsScreen.class)
public abstract class AddMacroButtonMixin extends GameOptionsScreen {
	public AddMacroButtonMixin(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Inject(method = "addOptions", at = @At("TAIL"))
	public void addMacroButton(CallbackInfo ci) {
		this.body.addWidgetEntry(new ButtonWidget.Builder(Text.of("Macros..."), button -> {

			GuiConfigs.tab = GuiConfigs.ConfigGuiTab.MACROS;
			MinecraftClient.getInstance().setScreen(
					new GuiMacroManager());
		}).build(), null);
	}
}
