package tools.redstone.redstonetools.features.feedback;

import net.minecraft.util.Formatting;

import javax.annotation.Nullable;

public abstract class Feedback {
    private final @Nullable String message;
    private final @Nullable String[] values;

    protected Feedback(@Nullable String message) {
        this.message = message;
        this.values = new String[0];
    }

    protected Feedback(@Nullable String message, @Nullable String[] values) {
        this.message = message;
        this.values = values;
    }

    private String formatMessage(String message) {
        return String.format("%s[%sRST%s]%s ", Formatting.GRAY, Formatting.RED, Formatting.GRAY, Formatting.RESET) + message;
    }

    public final String getMessage() {
        if (message != null) {
            for (int i = 0; i < values.length; i++) {
                values[i] = Formatting.RED + values[i] + getFormatting();
            }

            return formatMessage(String.format(message, (Object[]) values));
        } else {
            return formatMessage(getDefaultMessage());
        }
    }

    public abstract Formatting getFormatting();
    public abstract String getDefaultMessage();
    public abstract FeedbackType getType();

    public static None none() {
        return new None();
    }

    private static class None extends Feedback {
        public None() {
            super(null);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.WHITE;
        }

        @Override
        public String getDefaultMessage() {
            return "";
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.NONE;
        }
    }

    public static Success success(@Nullable String message) {
        return new Success(message);
    }
    public static Success success(@Nullable String message, @Nullable String[] values) {
        return new Success(message, values);
    }

    private static class Success extends Feedback {
        public Success(@Nullable String message) {
            super(message);
        }
        public Success(@Nullable String message, @Nullable String[] values) {
            super(message, values);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.WHITE;
        }

        @Override
        public String getDefaultMessage() {
            return "Success";
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.SUCCESS;
        }
    }

    public static Warning warning(@Nullable String message) {
        return new Warning(message);
    }

    private static class Warning extends Feedback {
        public Warning(@Nullable String message) {
            super(message);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.YELLOW;
        }

        @Override
        public String getDefaultMessage() {
            return "Warning";
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.WARNING;
        }
    }

    public static Error error(@Nullable String message) {
        return new Error(message);
    }

    private static class Error extends Feedback {
        public Error(@Nullable String message) {
            super(message);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.RED;
        }

        @Override
        public String getDefaultMessage() {
            return "Error";
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.ERROR;
        }
    }

    public static InvalidUsage invalidUsage(@Nullable String message) {
        return new InvalidUsage(message);
    }

    private static class InvalidUsage extends Feedback {
        public InvalidUsage(@Nullable String message) {
            super(message);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.RED;
        }

        @Override
        public String getDefaultMessage() {
            return "Invalid usage";
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.ERROR;
        }
    }
}
