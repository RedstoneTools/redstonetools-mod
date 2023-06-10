package tools.redstone.redstonetools.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

public enum SignalBlock {
    COMPOSTER(Blocks.COMPOSTER, SignalBlockSupplier.composter()),
    BARREL(Blocks.BARREL, SignalBlockSupplier.container_27()),
    CHEST(Blocks.CHEST, SignalBlockSupplier.container_27()),
    SHULKER_BOX(Blocks.SHULKER_BOX, SignalBlockSupplier.container_27()),
    DISPENSER(Blocks.DISPENSER, SignalBlockSupplier.container_9()),
    DROPPER(Blocks.DROPPER, SignalBlockSupplier.container_9()),
    HOPPER(Blocks.HOPPER, SignalBlockSupplier.container_5()),
    BREWING_STAND(Blocks.BREWING_STAND, SignalBlockSupplier.container_5()),
    FURNACE(Blocks.FURNACE, SignalBlockSupplier.container_3()),
    SMOKER(Blocks.SMOKER, SignalBlockSupplier.container_3()),
    BLAST_FURNACE(Blocks.BLAST_FURNACE, SignalBlockSupplier.container_3()),
    COMMAND_BLOCK(Blocks.COMMAND_BLOCK, SignalBlockSupplier.command_block()),
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

        return supplier.get(block, signal);
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
