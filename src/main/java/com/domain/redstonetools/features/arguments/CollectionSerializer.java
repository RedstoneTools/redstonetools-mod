package com.domain.redstonetools.features.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Serializes any collection type as a list,
 * and handles it specially for configuration.
 *
 * @param <E> The element type.
 * @param <C> The collection type.
 */
public class CollectionSerializer<E, C extends Collection<E>>
        extends TypeSerializer<C>
{

    public static <E> CollectionSerializer<E, List<E>> listOf(TypeSerializer<E> element,
                                                              SaveMode saveMode) {
        return new CollectionSerializer<>(List.class, element, ArrayList::new, saveMode);
    }

    public static <E> CollectionSerializer<E, List<E>> listOf(TypeSerializer<E> element) {
        return listOf(element, SaveMode.BLOCK);
    }

    public static <E> CollectionSerializer<E, Set<E>> setOf(TypeSerializer<E> element,
                                                            SaveMode saveMode) {
        return new CollectionSerializer<>(Set.class, element, HashSet::new, saveMode);
    }

    public static <E> CollectionSerializer<E, Set<E>> setOf(TypeSerializer<E> element) {
        return setOf(element, SaveMode.BLOCK);
    }

    public enum SaveMode {
        /**
         * It should save the list as a compact string
         * in the configuration.
         */
        STRING,

        /**
         * It should save the list as a block in the
         * configuration.
         */
        BLOCK
    }

    final TypeSerializer<E> elementType;
    final Function<Collection<E>, C> collectionFactory;
    final SaveMode saveMode;

    // cache example because
    // its relatively expensive
    // to compute correctly
    final String example;

    @SuppressWarnings("unchecked")
    protected CollectionSerializer(Class<?> clazz,
                                   TypeSerializer<E> elementType,
                                   Function<Collection<E>, C> collectionFactory,
                                   SaveMode saveMode) {
        super((Class<C>) clazz);
        this.elementType = elementType;
        this.collectionFactory = collectionFactory;
        this.saveMode = saveMode;

        // build example
        StringBuilder b = new StringBuilder("[");
        for (String elemStr : elementType.getExamples()) {
            b.append(elemStr);
            b.append(", ");
        }

        this.example = b.delete(b.length() - 3, b.length()).append("]").toString();
    }

    public TypeSerializer<E> getElementType() {
        return elementType;
    }

    @Override
    public String serialize(C value) {
        StringBuilder b = new StringBuilder("[");

        for (E element : value) {
            b.append(elementType.serialize(element));
            b.append(", ");
        }

        return b
                /* remove trailing comma and space */
                .delete(b.length() - 3, b.length())
                /* close list value */
                .append("]")
                .toString();
    }

    @Override
    public C deserialize(StringReader reader) throws CommandSyntaxException {
        List<E> list = new ArrayList<>();
        reader.expect('[');
        reader.skipWhitespace();
        if (reader.peek() == ']') { // empty list
            reader.skip();
            return collectionFactory.apply(list);
        }

        while (reader.canRead()) {
            E element = elementType.deserialize(reader);
            list.add(element);
            reader.skipWhitespace();
            if (reader.peek() == ']')
                break;
            reader.expect(',');
            reader.skipWhitespace();
        }

        reader.skipWhitespace();
        reader.expect(']');

        return collectionFactory.apply(list);
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("[a, b, c]");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String remaining = builder.getRemaining();

        if (remaining.isBlank()) {
            // suggest opening the list
            builder.suggest("[");
        } else {
            builder.suggest(",");
            builder.suggest("]");

            // suggest element options
            // the start index is the index
            // of the last comma if open, otherwise
            // it will be -1 and no elements will
            // be suggested
            StringReader inputParser = new StringReader(remaining);
            int startIndex = -1;
            try {
                inputParser.skip(); // [
                while (inputParser.canRead()) {
                    // skip element
                    int oldCursor = inputParser.getCursor();
                    try {
                        elementType.deserialize(inputParser);
                    } catch (CommandSyntaxException ignored) { }
                    if (oldCursor == inputParser.getCursor())
                        break;
                    inputParser.skipWhitespace();

                    // skip and register comma
                    if (inputParser.peek() == ',') {
                        startIndex = inputParser.getCursor();
                    }

                    inputParser.skip();
                    if (!inputParser.canRead())
                        break;

                    // end on closer
                    if (inputParser.peek() == ']') {
                        startIndex = -1;
                        break;
                    }

                    inputParser.skipWhitespace();
                }
            } catch (Exception e) {
                startIndex = -1;
            }

            if (startIndex != -1) {
                SuggestionsBuilder b2 = new SuggestionsBuilder(
                        remaining,
                        remaining.toLowerCase(),
                        startIndex + 1);

                elementType.listSuggestions(context, b2);
                builder.add(b2);
            }
        }

        return builder.buildFuture();
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public C load(Object in) {
        if (in instanceof String str) {
            try {
                return deserialize(new StringReader(str));
            } catch (CommandSyntaxException e) {
                throw new IllegalStateException("Syntax Exception in list: " + e.getMessage());
            }
        }

        if (in instanceof List list) {
            List<E> elementList = list.stream()
                    .map(elementType::load)
                    .toList();
            return collectionFactory.apply(elementList);
        }

        throw new IllegalArgumentException("Value Error: unsupported list type in configuration: " +
                (in == null ? "null" : in.getClass().getName()));
    }

    @Override
    public Object save(C value) {
        return switch (saveMode) {
            case STRING -> serialize(value);
            case BLOCK -> value
                    .stream()
                    .map(elementType::save)
                    .toList();
        };
    }

}
