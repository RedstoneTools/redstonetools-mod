package com.domain.redstonetools.features.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class SetArgumentType<E> implements ArgumentType<E> {

    // the set of arguments
    final List<E> set;
    // if it should match exact only
    final boolean onlyExact;

    public SetArgumentType(List<E> set, boolean onlyExact) {
        this.set = set;
        this.onlyExact = onlyExact;
    }

    /** Get the entire set. */
    public List<E> getSet() {
        return set;
    }

    public boolean isOnlyExact() {
        return onlyExact;
    }

    /** Find an element for string. */
    public E find(String str) throws CommandSyntaxException {
        E r = null;
        for (E elem : set) {
            String elemStr = Objects.toString(elem);
            if ((!onlyExact && elemStr.startsWith(str)) ||
                    elemStr.equals(str)) {
                if (r == null)
                    r = elem;
                else
                    throw new CommandSyntaxException(null,
                            Text.of("Ambiguous option '" + str + "'"));
            }
        }

        if (r == null)
            throw new CommandSyntaxException(null,
                    Text.of("No such option '" + str + "'"));
        return r;
    }

    @Override
    public E parse(StringReader reader) throws CommandSyntaxException {
        return find(reader.readString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (E option : set) {
            builder.suggest(Objects.toString(option));
        }

        return builder.buildFuture();
    }

}
