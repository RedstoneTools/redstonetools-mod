package tools.redstone.redstonetools.features.arguments.serializers;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.utils.NumberArg;

import java.util.Optional;

@AutoService(GenericArgumentType.class)
public class NumberSerializer extends IntLikeArgumentType<NumberArg> {
    private static final NumberSerializer INSTANCE = new NumberSerializer(null, null);

    public static NumberSerializer numberArg() {
        return INSTANCE;
    }

    public static NumberSerializer numberArg(NumberArg min) {
        return new NumberSerializer(min, null);
    }

    public static NumberSerializer numberArg(NumberArg min, NumberArg max) {
        return new NumberSerializer(min, max);
    }

    private NumberSerializer(NumberArg min, NumberArg max) {
        super(NumberArg.class, min, max);
    }

    @Override
    protected Optional<NumberArg> tryParseOptional(String string, int radix) {
        try {
            return Optional.of(new NumberArg(string, radix));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }
}
