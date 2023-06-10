package tools.redstone.redstonetools.features.toggleable;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;

@AutoService(AbstractFeature.class)
@Feature(name = "Big Dust", description = "Change the size of redstone's hitbox.", command = "bigdust")
public class BigDustFeature extends ToggleableFeature {
}
