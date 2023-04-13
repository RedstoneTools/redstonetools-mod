package tools.redstone.redstonetools.features.feedback;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class FeedbackSender extends AbstractFeedbackSender {

    @Override
    public void sendFeedback(ServerCommandSource source, Feedback feedback) {
        if (feedback.getType() == FeedbackType.NONE) {
            return;
        }

        source.sendFeedback(new LiteralText(feedback.getMessage())
                .formatted(feedback.getFormatting()), false);
    }

}
