package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CommandSuggestions.class)
public interface ChatInputSuggestorAccessor {
	@Accessor
	EditBox getInput();

	@Accessor
	@Nullable CommandSuggestions.SuggestionsList getSuggestions();
}