//package tools.redstone.redstonetools.features.arguments.serializers;
//
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.arguments.ArgumentType;
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import com.mojang.brigadier.suggestion.Suggestions;
//import com.mojang.brigadier.suggestion.SuggestionsBuilder;
//
//import java.util.Collection;
//import java.util.concurrent.CompletableFuture;
//
//public abstract class BrigadierSerializer<T, S> extends TypeSerializer<T, S> {
//
//    // the wrapped brigadier argument type
//    private final ArgumentType<T> argumentType;
//
//    public BrigadierSerializer(Class<T> clazz, ArgumentType<T> argumentType) {
//        super(clazz);
//        this.argumentType = argumentType;
//    }
//
//    @Override
//    public T deserialize(StringReader reader) throws CommandSyntaxException {
//        return argumentType.parse(reader);
//    }
//
//    @Override
//    public <R> CompletableFuture<Suggestions> listSuggestions(CommandContext<R> context, SuggestionsBuilder builder) {
//        return argumentType.listSuggestions(context, builder);
//    }
//
//    @Override
//    public Collection<String> getExamples() {
//        return argumentType.getExamples();
//    }
//
//}
