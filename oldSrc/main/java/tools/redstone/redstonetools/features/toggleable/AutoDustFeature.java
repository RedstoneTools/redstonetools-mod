package tools.redstone.redstonetools.features.toggleable;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;

@AutoService(AbstractFeature.class)
@Feature(name = "Auto Dust", description = "Automatically places redstone on top of colored blocks.", command = "autodust")
public class AutoDustFeature extends ToggleableFeature {

}
