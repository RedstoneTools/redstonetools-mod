package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tools.redstone.redstonetools.utils.StringUtils;

@Mixin(ChatInputSuggestor.SuggestionWindow.class)
public class SuggestionWindowMixin {
	@ModifyArg(method = "complete", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/suggestion/Suggestion;apply(Ljava/lang/String;)Ljava/lang/String;"))
	private String mreow(String original) {
		return StringUtils.unmodifiedCommand;
	}
}
