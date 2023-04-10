package com.domain.redstonetools.features.feedback;

import net.minecraft.util.Formatting;

import javax.annotation.Nullable;

public abstract class Feedback {
    private final @Nullable String message;

    protected Feedback(@Nullable String message) {
        this.message = message;
    }

    private String formatMessage(String message) {
        return "[RST] " + message;
    }

    public final String getMessage() {
        return formatMessage(message != null
                ? message
                : getDefaultMessage());
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

    private static class Success extends Feedback {
        public Success(@Nullable String message) {
            super(message);
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
