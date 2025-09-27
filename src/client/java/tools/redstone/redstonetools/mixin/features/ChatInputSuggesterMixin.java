package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.utils.StringUtils;

import static tools.redstone.redstonetools.utils.StringUtils.unmodifiedCommand;

@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggesterMixin {
	@Final
	@Shadow
	TextFieldWidget textField;

	@Inject(method = "refresh", at = @At("HEAD"))
	private void meowww(CallbackInfo ci) {
		if (!Configs.ClientData.ENABLE_MATH_VARIABLES.getBooleanValue()) return;
		unmodifiedCommand.add(textField.getText());
		((TextFieldAccessor)textField).setText2(StringUtils.insertVariablesAndMath(textField.getText()));
	}

	@Inject(method = "refresh", at = @At("RETURN"))
	private void mrawww(CallbackInfo ci) {
		if (!Configs.ClientData.ENABLE_MATH_VARIABLES.getBooleanValue()) return;
		((TextFieldAccessor)textField).setText2(unmodifiedCommand.getLast());
		unmodifiedCommand.removeLast();
	}
}
