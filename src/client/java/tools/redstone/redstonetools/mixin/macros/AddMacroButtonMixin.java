package tools.redstone.redstonetools.mixin.macros;

import kr1v.malilibApi.InternalMalilibApi;
import kr1v.malilibApi.MalilibApi;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.config.Macros;

@Mixin(ControlsScreen.class)
public abstract class AddMacroButtonMixin extends OptionsSubScreen {
	public AddMacroButtonMixin(Screen parent, Options gameOptions, Component title) {
		super(parent, gameOptions, title);
	}

	@Inject(method = "addOptions", at = @At("TAIL"))
	public void addMacroButton(CallbackInfo ci) {
		this.list.addSmall(new Button.Builder(Component.nullToEmpty("Macros..."), button -> {
			InternalMalilibApi.getMod(RedstoneTools.MOD_ID).setActiveTab(Macros.getTab());
			MalilibApi.openScreenFor(RedstoneTools.MOD_ID);
		}).build(), null);
	}
}
