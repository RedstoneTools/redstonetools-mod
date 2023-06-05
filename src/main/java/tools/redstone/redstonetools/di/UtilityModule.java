package tools.redstone.redstonetools.di;

import com.google.auto.service.AutoService;
import rip.hippo.inject.DoctorModule;
import rip.hippo.inject.binding.Binder;
import tools.redstone.redstonetools.features.feedback.AbstractFeedbackSender;
import tools.redstone.redstonetools.features.feedback.FeedbackSender;

@AutoService(DoctorModule.class)
public class UtilityModule implements DoctorModule {
    @Override
    public void configure(Binder binder) {
        binder.bind(AbstractFeedbackSender.class).to(FeedbackSender.class);
    }
}
