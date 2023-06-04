package tools.redstone.redstonetools.features.toggleable;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;

@AutoService(AbstractFeature.class)
@Feature(name = "Slab Select", description = "Only destroy the slab you're looking at when breaking double slabs", command = "slabselect")
public class SlabSelectFeature extends ToggleableFeature {
}
