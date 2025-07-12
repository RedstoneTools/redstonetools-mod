//package tools.redstone.redstonetools.features.arguments.serializers;
//
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.arguments.IntegerArgumentType;
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import com.mojang.brigadier.suggestion.Suggestions;
//import com.mojang.brigadier.suggestion.SuggestionsBuilder;
//import net.minecraft.text.Text;
//import tools.redstone.redstonetools.utils.NumberBase;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.concurrent.CompletableFuture;
//
//public class NumberBaseSerializer extends TypeSerializer<Integer, String> {
//    private static final IntegerArgumentType INT_SERIALIZER = IntegerArgumentType.integer(2, 36);
//    private static final NumberBaseSerializer INSTANCE = new NumberBaseSerializer();
//
//    public static NumberBaseSerializer numberBase() {
//        return INSTANCE;
//    }
//
//    protected NumberBaseSerializer() {
//        super(Integer.class);
//    }
//
//    @Override
//    public Integer deserialize(StringReader reader) throws CommandSyntaxException {
//        var input = reader.readUnquotedString();
//
//        try {
//            return deserialize(input);
//        } catch (IllegalArgumentException e) {
//            throw new CommandSyntaxException(null, Text.of(e.getMessage()));
//        }
//    }
//
//    @Override
//    public Integer deserialize(String serialized) {
//        try {
//            return NumberBase.fromString(serialized)
//                    .map(NumberBase::toInt)
//                    .orElseGet(() -> IntegerArgumentType.getInteger());
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Invalid base '" + serialized + "'.", e);
//        }
//    }
//
//    @Override
//    public String serialize(Integer value) {
//        return NumberBase.fromInt(value)
//                .map(NumberBase::toString)
//                .orElseGet(() -> INT_SERIALIZER.(value));
//    }
//
//    public String serialize(NumberBase value) {
//        return serialize(value.toInt());
//    }
//
//    @Override
//    public Collection<String> getExamples() {
//        return Arrays.stream(NumberBase.values())
//                .map(this::serialize)
//                .toList();
//    }
//
//    @Override
//    public <R> CompletableFuture<Suggestions> listSuggestions(CommandContext<R> context, SuggestionsBuilder builder) {
//        for (var value : NumberBase.values()) {
//            builder = builder.suggest(serialize(value));
//        }
//
//        return builder.buildFuture();
//    }
//}
