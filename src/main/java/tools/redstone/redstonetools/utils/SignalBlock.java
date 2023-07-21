package tools.redstone.redstonetools.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

public enum SignalBlock {
    COMPOSTER(Blocks.COMPOSTER, SignalBlockSupplier.composter()),
    BARREL(Blocks.BARREL, SignalBlockSupplier.container(27)),
    CHEST(Blocks.CHEST, SignalBlockSupplier.container(27)),
    SHULKER_BOX(Blocks.SHULKER_BOX, SignalBlockSupplier.container(27)),
    DISPENSER(Blocks.DISPENSER, SignalBlockSupplier.container(9)),
    DROPPER(Blocks.DROPPER, SignalBlockSupplier.container(9)),
    HOPPER(Blocks.HOPPER, SignalBlockSupplier.container(5)),
    BREWING_STAND(Blocks.BREWING_STAND, SignalBlockSupplier.container(5)),
    FURNACE(Blocks.FURNACE, SignalBlockSupplier.container(3)),
    SMOKER(Blocks.SMOKER, SignalBlockSupplier.container(3)),
    BLAST_FURNACE(Blocks.BLAST_FURNACE, SignalBlockSupplier.container(3)),
    COMMAND_BLOCK(Blocks.COMMAND_BLOCK, SignalBlockSupplier.commandBlock()),
    LECTERN(Blocks.LECTERN, SignalBlockSupplier.lectern()),
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
