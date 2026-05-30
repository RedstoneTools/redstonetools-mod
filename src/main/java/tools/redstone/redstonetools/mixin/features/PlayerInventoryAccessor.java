package tools.redstone.redstonetools.mixin.features;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Inventory.class)
public interface PlayerInventoryAccessor {
	@Accessor
	NonNullList<ItemStack> getItems();

	@Accessor
	int getSelected();
}