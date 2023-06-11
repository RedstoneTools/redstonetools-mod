package tools.redstone.redstonetools.features.feedback;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tools.redstone.redstonetools.RedstoneToolsClient;

import javax.annotation.Nullable;

import static tools.redstone.redstonetools.utils.TextUtils.format;
import static tools.redstone.redstonetools.utils.TextUtils.join;

public abstract class Feedback {

    private static final MutableText PREFIX = new LiteralText("%s[%sRST%s]%s ".formatted(Formatting.GRAY, Formatting.RED, Formatting.GRAY, Formatting.RESET));

    private final @Nullable MutableText message;
    private final @Nullable Object[] values;

    protected Feedback(@Nullable Text message, @Nullable Object... values) {
        this.message = message instanceof MutableText mt ? mt :
                (message == null ? null : message.copy());
        this.values = values;
    }

    private MutableText formatMessage(MutableText message) {
        return join(PREFIX, message);
    }

    /**
     * Manually send this feedback as an intermediate message to the recipient.
     *
     * @param recipient The recipient.
     */
    public void sendIntermediate(ServerCommandSource recipient) {
        RedstoneToolsClient.INJECTOR
                .getInstance(FeedbackSender.class)
                .sendIntermediateFeedback(recipient, this);
    }

    public void sendIntermediate(CommandContext<ServerCommandSource> context) {
        sendIntermediate(context.getSource());
    }

    /**
     * Manually send this feedback as an intermediate message to the recipient.
     *
     * @param recipient The recipient.
     * @return The command status code.
     */
    public int send(ServerCommandSource recipient) {
        RedstoneToolsClient.INJECTOR
                .getInstance(FeedbackSender.class)
                .sendFeedback(recipient, this);

        return getType().getCode();
    }

    public int send(CommandContext<ServerCommandSource> context) {
        return send(context.getSource());
    }

    public final MutableText getMessage() {
        if (message != null) {
            MutableText sentMessage = format(message, values);
            return formatMessage(sentMessage);
        } else {
            return formatMessage(getDefaultMessage());
        }
    }

    public abstract Formatting getFormatting();
    public abstract MutableText getDefaultMessage();
    public abstract FeedbackType getType();

    public static None none() {
        return new None();
    }

    public static class None extends Feedback {
        public None() {
            super(null);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.WHITE;
        }

        @Override
        public MutableText getDefaultMessage() {
            return new LiteralText("");
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.NONE;
        }
    }

    public static Success success(@Nullable Text message, @Nullable Object... values) {
        return new Success(message, values);
    }

    public static Success success(@Nullable String message, @Nullable Object... values) {
        return new Success(Text.of(message), values);
    }

    public static class Success extends Feedback {
        public Success(@Nullable Text message, @Nullable Object... values) {
            super(message, values);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.WHITE;
        }

        @Override
        public MutableText getDefaultMessage() {
            return new LiteralText("Success").setStyle(Style.EMPTY.withFormatting(Formatting.GREEN));
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.SUCCESS;
        }
    }

    public static Warning warning(@Nullable Text message, @Nullable Object... values) {
        return new Warning(message, values);
    }

    public static Warning warning(@Nullable String message, @Nullable Object... values) {
        return new Warning(Text.of(message), values);
    }

    public static class Warning extends Feedback {
        public Warning(@Nullable Text message, @Nullable Object... values) {
            super(message, values);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.YELLOW;
        }

        @Override
        public MutableText getDefaultMessage() {
            return new LiteralText("Warning").setStyle(Style.EMPTY.withFormatting(Formatting.YELLOW));
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.WARNING;
        }
    }

    public static Error error(@Nullable String message, @Nullable Object... values) {
        return new Error(Text.of(message), values);
    }

    public static Error error(@Nullable Text message, @Nullable Object... values) {
        return new Error(message, values);
    }

    public static class Error extends Feedback {
        public Error(@Nullable Text message, @Nullable Object... values) {
            super(message, values);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.RED;
        }

        @Override
        public MutableText getDefaultMessage() {
            return new LiteralText("Error").setStyle(Style.EMPTY.withFormatting(Formatting.RED));
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.ERROR;
        }
    }

    public static InvalidUsage invalidUsage(@Nullable String message, @Nullable Object... values) {
        return new InvalidUsage(Text.of(message), values);
    }

    public static InvalidUsage invalidUsage(@Nullable Text message, @Nullable Object... values) {
        return new InvalidUsage(message, values);
    }

    public static class InvalidUsage extends Feedback {
        public InvalidUsage(@Nullable Text message, @Nullable Object... values) {
            super(message, values);
        }

        @Override
        public Formatting getFormatting() {
            return Formatting.RED;
        }

        @Override
        public MutableText getDefaultMessage() {
            return new LiteralText("Invalid usage").setStyle(Style.EMPTY.withFormatting(Formatting.RED));
        }

        @Override
        public FeedbackType getType() {
            return FeedbackType.ERROR;
        }
    }
}
