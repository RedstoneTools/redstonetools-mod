package com.domain.redstonetools.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BlockColorArgumentType implements ArgumentType<String> {

    static final BlockColorArgumentType INSTANCE = new BlockColorArgumentType();

    public static BlockColorArgumentType blockColor() {
        return INSTANCE;
    }

    /////////////////////

    static final Set<String> COLORS = Set.of(
            "white",
            "orange",
            "magenta",
            "light_blue",
            "yellow",
            "lime",
            "pink",
            "gray",
            "light_gray",
            "cyan",
            "purple",
            "blue",
            "brown",
            "green",
            "red",
            "black"
    );

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.readString();
        if (!COLORS.contains(str)) {
            throw new CommandSyntaxException(null, Text.of("No such color '" + str + "'"));
        }

        return str;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String str : COLORS) {
            builder.suggest(str);
        }

        return builder.buildFuture();
    }

}
