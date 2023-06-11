package tools.redstone.redstonetools.features.feedback;

import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractFeedbackSender {

    public abstract void sendIntermediateFeedback(ServerCommandSource source, Feedback feedback);
    public abstract void sendFeedback(ServerCommandSource source, Feedback feedback);

}
