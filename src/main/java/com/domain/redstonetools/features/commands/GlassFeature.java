package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.utils.BlockColor;
import com.domain.redstonetools.utils.BlockInfo;
import com.domain.redstonetools.utils.ColoredBlock;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Feature(name = "Glass", description = "Converts colored blocks to their glass variant and glass to wool.", command = "glass")
public class GlassFeature extends PickBlockFeature {
    @Override
    protected ItemStack getItemStack(ServerCommandSource source, BlockInfo blockInfo) throws CommandSyntaxException {
        var coloredBlock = getGlassOrSolidVariant(blockInfo.block);
        if (coloredBlock == null) {
            source.sendError(Text.of("Invalid block! Use on a colored block."));

            return null;
        }

        return new ItemStack(coloredBlock);
    }

    private Block getGlassOrSolidVariant(Block block) {
        var blockId = Registry.BLOCK.getId(block).toString();

        var coloredBlock = ColoredBlock.fromBlockId(blockId);
        if (coloredBlock == null) return null;

        var coloredResult = (isGlass(coloredBlock)
            ? getColoredWool()
            : getColoredGlass()
        ).withColor(coloredBlock.color);

        return coloredResult.toBlock();
    }

    private boolean isGlass(ColoredBlock coloredBlock) {
        var whiteVariant = coloredBlock.withColor(BlockColor.WHITE).toBlockId();

        var whiteVariantId = Identifier.tryParse(whiteVariant);

        return Registry.BLOCK.get(whiteVariantId).equals(Blocks.WHITE_STAINED_GLASS);
    }

    private ColoredBlock getColoredWool() {
        return ColoredBlock.fromBlock(Blocks.WHITE_WOOL);
    }

    private ColoredBlock getColoredGlass() {
        return ColoredBlock.fromBlock(Blocks.WHITE_STAINED_GLASS);
    }
}
