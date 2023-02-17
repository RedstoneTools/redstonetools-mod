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

public abstract class SetArgumentType<E> implements ArgumentType<E>, TypeProvider {

    protected abstract List<E> getSet();
    protected abstract boolean isOnlyExact();

    protected SetArgumentType() {

    }

    /** Find an element for string. */
    public E find(String str) throws CommandSyntaxException {
        E r = null;
        for (E elem : getSet()) {
            String elemStr = Objects.toString(elem);
            if ((!isOnlyExact() && elemStr.startsWith(str)) ||
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
        for (E option : getSet()) {
            builder.suggest(Objects.toString(option));
        }

        return builder.buildFuture();
    }

}
