package tools.redstone.redstonetools.features.toggleable;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;

@AutoService(AbstractFeature.class)
@Feature(name = "Slab Keeper", description = "Only destroy the slab you're looking at when breaking double slabs", command = "slabkeeper")
public class SlabKeeperFeature extends ToggleableFeature {
}
