package tools.redstone.redstonetools.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import tools.redstone.redstonetools.utils.HasWorld;

@Mixin(Entity.class)
public class EntityMixin implements HasWorld {
	@Override
	public World getWorld() {
		Entity self = (Entity) (Object) this;
		//? if <1.21.10 {
		return self.getWorld();
		//?} else {
		/*return self.getEntityWorld();
		*///?}
	}
}
