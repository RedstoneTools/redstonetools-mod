package tools.redstone.redstonetools.features.feedback;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Texts;

import javax.inject.Singleton;

@Singleton
public class FeedbackSender extends AbstractFeedbackSender {

    @Override
    public void sendIntermediateFeedback(ServerCommandSource source, Feedback feedback) {
        sendFeedback(source, feedback);
    }

    @Override
    public void sendFeedback(ServerCommandSource source, Feedback feedback) {
        if (feedback.getType() == FeedbackType.NONE) {
            return;
        }

        source.sendFeedback(
                Texts.setStyleIfAbsent(feedback.getMessage(),
                        Style.EMPTY.withFormatting(feedback.getFormatting())
        ), false);
    }

}
