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
import tools.redstone.redstonetools.utils.StringUtils;

import static tools.redstone.redstonetools.utils.StringUtils.unmodifiedCommand;

@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggesterMixin {
	@Final
	@Shadow
	TextFieldWidget textField;

	@Unique
	boolean justEntered;

	@Inject(method = "refresh", at = @At("HEAD"), cancellable = true)
	private void meowww(CallbackInfo ci) {
		if (justEntered) {
			ci.cancel();
			return;
		}
		justEntered = true;
		unmodifiedCommand = textField.getText();
		textField.setText(StringUtils.insertVariables(textField.getText()));
	}

	@Inject(method = "refresh", at = @At("RETURN"))
	private void mrawww(CallbackInfo ci) {
		textField.setText(unmodifiedCommand);
		justEntered = false;
	}
}
