package com.domain.redstonetools.feedback;

import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Represents a piece of feedback to communicate
 * to the user and handler code.
 */
public abstract class Feedback {

    private final @Nullable String message;

    protected Feedback(@Nullable String message) {
        this.message = message;
    }

    @NotNull
    private String formatMessage(String message) {
        return "[RST] " + message;
    }

    /**
     * Get the message or default message if absent.
     *
     * @return The message.
     */
    public final String getMessage() {
        return formatMessage(message != null
                ? message
                : getDefaultMessage());
    }

    public abstract Formatting getFormatting();
    public abstract String getDefaultMessage();
    public abstract FeedbackType getType();

    /*
        Types
     */

    /**
     * Create a feedback of type {@link FeedbackType#NONE}.
     *
     * @return The feedback.
     */
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

    /**
     * Create a successful feedback of type
     * {@link FeedbackType#SUCCESS}.
     *
     * @param message The message.
     * @return The feedback.
     */
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

    /**
     * Create a new warning feedback with
     * the given message of type {@link FeedbackType#WARNING}.
     *
     * @param message The message.
     * @return The feedback.
     */
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

    /**
     * Create a new error feedback with the given
     * message of type {@link FeedbackType#ERROR}.
     *
     * @param message The message.
     * @return The feedback.
     */
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

    /**
     * Create a new invalid usage feedback with the
     * given message of type {@link FeedbackType#ERROR}.
     *
     * @param message The message.
     * @return The feedback.
     */
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
