package tools.redstone.redstonetools.features.feedback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import javax.inject.Singleton;

@Singleton
public class FeedbackSender extends AbstractFeedbackSender {
    @Override
    public void sendFeedback(ServerCommandSource source, Feedback feedback) {
        if (feedback.getType() == FeedbackType.NONE) {
            return;
        }

        MinecraftClient.getInstance().player.networkHandler.sendChatCommand(feedback.getMessage().formatted(feedback.getFormatting()));
    }
}
