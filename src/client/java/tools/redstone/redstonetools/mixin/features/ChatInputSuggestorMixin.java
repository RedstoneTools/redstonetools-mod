package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tools.redstone.redstonetools.config.ClientData;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;
import tools.redstone.redstonetools.utils.StringUtils;

@Mixin(CommandSuggestions.class)
public class ChatInputSuggestorMixin {
	@Final
	@Shadow
	EditBox input;

	@Shadow
	@Final
	private Screen screen;

	@WrapMethod(method = "updateCommandInfo")
	private void makeMethodSeeReplacedVariables(Operation<Void> original) {
		boolean shouldReturn = !ClientData.ENABLE_MATH_VARIABLES.getBooleanValue() || StringUtils.insertVariablesAndMath(input.getValue()).length() < input.getValue().length();
		if (shouldReturn) return;
		String originalCommand = input.getValue();
		((TextFieldWidgetAccessor) input).setTextDirectly(StringUtils.insertVariablesAndMath(input.getValue()));
		original.call();
		((TextFieldWidgetAccessor) input).setTextDirectly(originalCommand);
	}

	@ModifyArg(
		method = "showSuggestions",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CommandSuggestions$SuggestionsList;<init>(Lnet/minecraft/client/gui/components/CommandSuggestions;IIILjava/util/List;Z)V"),
		index = 2
	)
	private int modifyY(int y) {
		if (this.screen instanceof GuiMacroEditor) {
			return input.getY() + 20;
		}
		return y;
	}
}
