package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.minecraft.command.CommandSource.SUGGESTION_MATCH_PREFIX;

@Mixin(CommandSource.class)
public interface CommandSourceMixin {
	@ModifyVariable(method = "suggestMatching(Ljava/lang/Iterable;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), argsOnly = true)
	private static Iterable<String> mrow(Iterable<String> candidates, @Local(argsOnly = true) SuggestionsBuilder builder) {
		String remaining = builder.getRemainingLowerCase();
		if (remaining.startsWith("m{"))
			return meowww(remaining.substring(2));
		return candidates;
	}

	@ModifyVariable(method = "suggestMatching([Ljava/lang/String;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), argsOnly = true)
	private static String[] mrawww(String[] candidates, @Local(argsOnly = true) SuggestionsBuilder builder) {
		String remaining = builder.getRemainingLowerCase();
		if (remaining.startsWith("m{"))
			return meowww(remaining.substring(2)).toArray(new String[0]);
		return candidates;
	}

	@ModifyVariable(method = "suggestMatching(Ljava/util/stream/Stream;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), argsOnly = true)
	private static Stream<String> mewww(Stream<String> candidates, @Local(argsOnly = true) SuggestionsBuilder builder) {
		String remaining = builder.getRemainingLowerCase();
		if (remaining.startsWith("m{"))
			return meowww(remaining.substring(2)).stream();
		return candidates;
	}

	@Unique
	private static List<String> meowww(String expression) {
		List<String> list = new ArrayList<>();
		list.add(MathUtils.handleMat(expression));
		return list;
	}

	@Inject(method = "shouldSuggest", at = @At("HEAD"), cancellable = true)
	private static void meow(String remaining, String candidate, CallbackInfoReturnable<Boolean> cir) {
		if (remaining.startsWith("m,")) {
			cir.setReturnValue(true);
			return;
		}
		if (!Configs.General.BOOLEAN_IMPROVED_COMMAND_SUGGESTIONS.getBooleanValue()) return;
		if (candidate == null) {
			cir.setReturnValue(false);
			return;
		}
		if (remaining.isEmpty()) {
			cir.setReturnValue(true);
			return;
		}

		int n = candidate.length();
		List<Integer> starts = new ArrayList<>();
		starts.add(0);
		for (int i = 0; i < n; i++) {
			if (SUGGESTION_MATCH_PREFIX.matches(candidate.charAt(i))) {
				starts.add(i + 1);
			}
		}

		for (int start : starts) {
			if (isSubsequenceFrom(remaining, candidate, start)) {
				cir.setReturnValue(true);
				return;
			}
		}
		cir.setReturnValue(false);
	}

	@Unique
	private static boolean isSubsequenceFrom(String remaining, String candidate, int startIdx) {
		int j = 0;
		for (int i = startIdx; i < candidate.length() && j < remaining.length(); i++) {
			if (candidate.charAt(i) == remaining.charAt(j)) {
				j++;
			}
		}
		return j == remaining.length();
	}
}
