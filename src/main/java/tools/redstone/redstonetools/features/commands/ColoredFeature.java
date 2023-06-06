package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.BlockInfo;
import tools.redstone.redstonetools.utils.ColoredBlock;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.registry.Registry;
import tools.redstone.redstonetools.utils.ColoredBlockType;

import javax.annotation.Nullable;

import static tools.redstone.redstonetools.features.arguments.ColoredBlockTypeSerializer.coloredBlockType;

@AutoService(AbstractFeature.class)
@Feature(name = "Colored", description = "Gives the player specified variant of block being looked at, with the same color. Default is White.", command = "colored")
public class ColoredFeature extends PickBlockFeature {
    public static final Argument<ColoredBlockType> blockType = Argument.ofType(coloredBlockType());
    @Override
    protected boolean requiresBlock() {
        return false;
    }

    @Override
    protected Either<ItemStack, Feedback> getItemStack(ServerCommandSource source, @Nullable BlockInfo blockInfo) {
        if (blockInfo == null || ColoredBlock.fromBlock(blockInfo.block) == null) {
            return switch (blockType.getValue()) {
                case GLASS -> Either.left(new ItemStack(Blocks.WHITE_STAINED_GLASS));
                case WOOL -> Either.left(new ItemStack(Blocks.WHITE_WOOL));
                case CONCRETE -> Either.left(new ItemStack(Blocks.WHITE_CONCRETE));
                case TERRACOTTA -> Either.left(new ItemStack(Blocks.WHITE_TERRACOTTA));
                case GLAZED_TERRACOTTA -> Either.left(new ItemStack(Blocks.WHITE_GLAZED_TERRACOTTA));
            };
        }

        var coloredBlock = getColorVariant(blockInfo.block);

        return Either.left(coloredBlock == null
            ? new ItemStack(Blocks.GLASS)
            : new ItemStack(coloredBlock.toBlock()));
    }

    private ColoredBlock getColorVariant(Block block) {
        var blockId = Registry.BLOCK.getId(block).toString();

        var coloredBlock = ColoredBlock.fromBlockId(blockId);
        if (coloredBlock == null) return null;

        return switch (blockType.getValue()) {
            case GLASS -> getColoredGlass().withColor(coloredBlock.color);
            case WOOL -> getColoredWool().withColor(coloredBlock.color);
            case CONCRETE -> getColoredConcrete().withColor(coloredBlock.color);
            case TERRACOTTA -> getColoredTerracotta().withColor(coloredBlock.color);
            case GLAZED_TERRACOTTA -> getColoredGlazedTerracotta().withColor(coloredBlock.color);
        };
    }


    private ColoredBlock getColoredWool() {
        return ColoredBlock.fromBlock(Blocks.WHITE_WOOL);
    }

    private ColoredBlock getColoredGlass() {
        return ColoredBlock.fromBlock(Blocks.WHITE_STAINED_GLASS);
    }

    private ColoredBlock getColoredConcrete() {
        return ColoredBlock.fromBlock(Blocks.WHITE_CONCRETE);
    }

    private ColoredBlock getColoredTerracotta() {
        return ColoredBlock.fromBlock(Blocks.WHITE_TERRACOTTA);
    }

    private ColoredBlock getColoredGlazedTerracotta() {
        return ColoredBlock.fromBlock(Blocks.WHITE_GLAZED_TERRACOTTA);
    }
}
