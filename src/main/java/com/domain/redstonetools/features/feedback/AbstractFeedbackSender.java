package com.domain.redstonetools.features.feedback;

import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractFeedbackSender {
    public abstract void sendFeedback(ServerCommandSource source, Feedback feedback);
}
