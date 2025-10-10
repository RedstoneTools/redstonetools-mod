package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

	@Unique
	private boolean shouldReturn = false;

	@Inject(method = "refresh", at = @At("HEAD"))
	private void meowww(CallbackInfo ci) {
		shouldReturn = !Configs.ClientData.ENABLE_MATH_VARIABLES.getBooleanValue() || StringUtils.insertVariablesAndMath(textField.getText()).length() < textField.getText().length();
		if (shouldReturn) return;
		unmodifiedCommand.add(textField.getText());
		((TextFieldAccessor)textField).setText2(StringUtils.insertVariablesAndMath(textField.getText()));
	}

	@Inject(method = "refresh", at = @At("RETURN"))
	private void mrawww(CallbackInfo ci) {
		if (shouldReturn) return;
		((TextFieldAccessor)textField).setText2(unmodifiedCommand.getLast());
		unmodifiedCommand.removeLast();
	}
}
