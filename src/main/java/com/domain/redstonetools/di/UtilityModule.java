package com.domain.redstonetools.di;

import com.domain.redstonetools.feedback.AbstractFeedbackSender;
import com.domain.redstonetools.feedback.FeedbackSender;
import com.google.inject.AbstractModule;

public class UtilityModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AbstractFeedbackSender.class).to(FeedbackSender.class).asEagerSingleton();
    }
}
