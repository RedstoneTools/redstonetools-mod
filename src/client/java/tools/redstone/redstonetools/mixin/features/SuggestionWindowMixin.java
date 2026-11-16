package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import tools.redstone.redstonetools.utils.StringUtils;

@Mixin(CommandSuggestions.SuggestionsList.class)
public class SuggestionWindowMixin {
	@Shadow
	@Final
	//? if <26.1 {
	/*CommandSuggestions field_21615;
	*///? } else
	CommandSuggestions this$0;

	@WrapMethod(method = "useSuggestion")
	private void expandVariablesToo(Operation<Void> original) {
		//? if <26.1 {
		/*EditBox textField = ((ChatInputSuggestorAccessor) this.field_21615).getInput();
		*///? } else
		EditBox textField = ((ChatInputSuggestorAccessor) this.this$0).getInput();
		String beforeComplete = textField.getValue();
		original.call();
		if (StringUtils.expand(beforeComplete, textField.getValue()).equals(textField.getValue())) return;
		textField.setValue(StringUtils.expand(beforeComplete, textField.getValue()));
	}
}
