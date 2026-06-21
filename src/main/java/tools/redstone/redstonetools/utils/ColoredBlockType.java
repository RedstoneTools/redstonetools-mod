package tools.redstone.redstonetools.utils;

import net.minecraft.core.registries.BuiltInRegistries;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//? } else
//import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public enum ColoredBlockType {
	// TODO: Merge some things with the ColoredBlock class so we don't have to repeat the formats and stuff
	WOOL("wool", "minecraft:%s_wool"),
	GLASS("glass", "minecraft:%s_stained_glass"),
	CONCRETE("concrete", "minecraft:%s_concrete"),
	TERRACOTTA("terracotta", "minecraft:%s_terracotta");

	private final String displayName;
	private final String blockIdFormat;

	ColoredBlockType(String displayName, String blockIdFormat) {
		this.displayName = displayName;
		this.blockIdFormat = blockIdFormat;
	}

	@Override
	public String toString() {
		return displayName;
	}

	public ColoredBlock withColor(BlockColor color) {
		return ColoredBlock.fromBlock(toBlock())
				.withColor(color);
	}

	public String toBlockId() {
		return String.format(blockIdFormat, BlockColor.WHITE);
	}

	public Block toBlock() {
		//? if >=1.21.11 {
		return BuiltInRegistries.BLOCK.getValue(Identifier.tryParse(toBlockId()));
		//? } else
		//return BuiltInRegistries.BLOCK.getValue(ResourceLocation.tryParse(toBlockId()));
	}
}
