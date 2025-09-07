package tools.redstone.redstonetools.mixin.features;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(value = ArgumentType.class, remap = false)
public interface ArgumentTypeMixin {
	@Inject(method = "listSuggestions", at = @At("HEAD"), cancellable = true)
	private void dummySuggestions(CommandContext<Object> context, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
		cir.setReturnValue(CommandSource.suggestMatching(new String[0], builder));
	}
}
