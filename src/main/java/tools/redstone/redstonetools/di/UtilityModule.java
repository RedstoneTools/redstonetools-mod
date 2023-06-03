package tools.redstone.redstonetools.di;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.feedback.AbstractFeedbackSender;
import tools.redstone.redstonetools.features.feedback.FeedbackSender;
import com.google.inject.AbstractModule;

@AutoService(AbstractModule.class)
public class UtilityModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AbstractFeedbackSender.class).to(FeedbackSender.class).asEagerSingleton();
    }
}
