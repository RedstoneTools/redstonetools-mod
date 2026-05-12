package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tools.redstone.redstonetools.config.ClientData;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;
import tools.redstone.redstonetools.utils.StringUtils;

@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggestorMixin {
	@Final
	@Shadow
	TextFieldWidget textField;

	@Shadow
	@Final
	private Screen owner;

	@WrapMethod(method = "refresh")
	private void makeMethodSeeReplacedVariables(Operation<Void> original) {
		boolean shouldReturn = !ClientData.ENABLE_MATH_VARIABLES.getBooleanValue() || StringUtils.insertVariablesAndMath(textField.getText()).length() < textField.getText().length();
		if (shouldReturn) return;
		String originalCommand = textField.getText();
		((TextFieldWidgetAccessor) textField).setTextDirectly(StringUtils.insertVariablesAndMath(textField.getText()));
		original.call();
		((TextFieldWidgetAccessor) textField).setTextDirectly(originalCommand);
	}

	@ModifyArg(
		method = "show",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatInputSuggestor$SuggestionWindow;<init>(Lnet/minecraft/client/gui/screen/ChatInputSuggestor;IIILjava/util/List;Z)V"),
		index = 2
	)
	private int modifyY(int y) {
		if (this.owner instanceof GuiMacroEditor) {
			return textField.getY() + 20;
		}
		return y;
	}
}
