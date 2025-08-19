package tools.redstone.redstonetools.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

public enum SignalBlock {
	// cba to do composters.
	BARREL(Blocks.BARREL, SignalBlockSupplier.container(27, Blocks.BARREL.asItem())),
	CHEST(Blocks.CHEST, SignalBlockSupplier.container(27, Blocks.CHEST.asItem())),
	SHULKER_BOX(Blocks.SHULKER_BOX, SignalBlockSupplier.container(27, Blocks.SHULKER_BOX.asItem())),
	DISPENSER(Blocks.DISPENSER, SignalBlockSupplier.container(9, Blocks.DISPENSER.asItem())),
	DROPPER(Blocks.DROPPER, SignalBlockSupplier.container(9, Blocks.DROPPER.asItem())),
	HOPPER(Blocks.HOPPER, SignalBlockSupplier.container(5, Blocks.HOPPER.asItem())),
	BREWING_STAND(Blocks.BREWING_STAND, SignalBlockSupplier.container(5, Blocks.BREWING_STAND.asItem())),
	FURNACE(Blocks.FURNACE, SignalBlockSupplier.container(3, Blocks.FURNACE.asItem())),
	SMOKER(Blocks.SMOKER, SignalBlockSupplier.container(3, Blocks.SMOKER.asItem())),
	BLAST_FURNACE(Blocks.BLAST_FURNACE, SignalBlockSupplier.container(3, Blocks.BLAST_FURNACE.asItem())),
	COMMAND_BLOCK(Blocks.COMMAND_BLOCK, SignalBlockSupplier.commandBlock()),
	AUTO(null, null);

	private final Block block;
	private final SignalBlockSupplier supplier;

	SignalBlock(Block block, SignalBlockSupplier supplier) {
		this.block = block;
		this.supplier = supplier;
	}

	public static SignalBlock getBestBlock(int signal) {
		return signal < 1780
				? BARREL
				: COMMAND_BLOCK;
	}

	public ItemStack getItemStack(int signal) {
		if (block == null || supplier == null)
			return getBestBlock(signal).getItemStack(signal);

		return supplier.getItemStack(block, signal);
	}

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}

}
