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

@Mixin(ChatInputSuggestor.SuggestionWindow.class)
public class SuggestionWindowMixin {
	@Shadow
	@Final
	ChatInputSuggestor field_21615;

	@Unique
	private static String beforeComplete;

	@Inject(method = "complete", at = @At("HEAD"))
	private void mroeww(CallbackInfo ci) {
		beforeComplete = ((ChatInputSuggestorAccessor) this.field_21615).getTextField().getText();
	}

	@Inject(method = "complete", at = @At("RETURN"))
	private void mrawww(CallbackInfo ci) {
		if (!beforeComplete.contains("'")) return;
		TextFieldWidget textField = ((ChatInputSuggestorAccessor) this.field_21615).getTextField();

		System.out.println(beforeComplete);
		System.out.println(textField.getText());
		System.out.println(StringUtils.expand(beforeComplete, textField.getText()));

		textField.setText(StringUtils.expand(beforeComplete, textField.getText()));
	}
}
