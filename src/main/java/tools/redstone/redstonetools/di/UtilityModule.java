package tools.redstone.redstonetools.di;

import tools.redstone.redstonetools.features.feedback.AbstractFeedbackSender;
import tools.redstone.redstonetools.features.feedback.FeedbackSender;
import com.google.inject.AbstractModule;

public class UtilityModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AbstractFeedbackSender.class).to(FeedbackSender.class).asEagerSingleton();
    }
}
