package tools.redstone.redstonetools.mixin.update;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.CheckUpdates;

@Mixin(TitleScreen.class)
public class CheckUpdateMixin extends Screen {
	public CheckUpdateMixin() {
		super(Text.of("UpdateText(Bug found, report on Github)"));
	}

	@Inject(method = "init", at = @At("HEAD"))
	public void updateTextInjection(CallbackInfo ci) {
		this.addDrawableChild(new PressableTextWidget(4, 4, textRenderer.getWidth(CheckUpdates.updateStatus), textRenderer.fontHeight, CheckUpdates.updateStatus, button -> Util.getOperatingSystem().open(CheckUpdates.uri), textRenderer));
	}
}
