package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.command.argument.BlockStateArgumentType.blockState;

@Feature(name = "Redstone Dust Placer", description = "Places redstone dust on top of a block when you place the block.", command = "dustplacer")
public class DustPlacerFeature extends CommandFeature {

    public static final Argument<BlockStateArgument> block = Argument.ofType(blockState()).withDefault(null); //new Argument<>("block", BlockStateArgumentType.blockState(), null);
    static public Boolean isActive = false;
    static public List<Identifier> blockList = new ArrayList<>();


    @Override
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        if(blockList.isEmpty())
            addDefaultBlocks();

        if (block.getValue() == null)
            isActive = !isActive;

        else {
            Identifier blockIdentifier = block.getValue().getBlockState().getBlock().getLootTableId();
            if(blockList.contains(blockIdentifier))
                blockList.remove(blockIdentifier);
            else
                blockList.add(blockIdentifier);
        }
        return 0;
    }

    private static void addDefaultBlocks(){
        blockList.add(Blocks.WHITE_WOOL.getLootTableId());
        blockList.add(Blocks.ORANGE_WOOL.getLootTableId());
        blockList.add(Blocks.MAGENTA_WOOL.getLootTableId());
        blockList.add(Blocks.LIGHT_BLUE_WOOL.getLootTableId());
        blockList.add(Blocks.YELLOW_WOOL.getLootTableId());
        blockList.add(Blocks.LIME_WOOL.getLootTableId());
        blockList.add(Blocks.PINK_WOOL.getLootTableId());
        blockList.add(Blocks.GRAY_WOOL.getLootTableId());
        blockList.add(Blocks.LIGHT_GRAY_WOOL.getLootTableId());
        blockList.add(Blocks.CYAN_WOOL.getLootTableId());
        blockList.add(Blocks.PURPLE_WOOL.getLootTableId());
        blockList.add(Blocks.BLUE_WOOL.getLootTableId());
        blockList.add(Blocks.BROWN_WOOL.getLootTableId());
        blockList.add(Blocks.GREEN_WOOL.getLootTableId());
        blockList.add(Blocks.RED_WOOL.getLootTableId());
        blockList.add(Blocks.BLACK_WOOL.getLootTableId());

        blockList.add(Blocks.GLASS.getLootTableId());
        blockList.add(Blocks.WHITE_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.ORANGE_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.MAGENTA_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.LIGHT_BLUE_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.YELLOW_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.LIME_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.PINK_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.GRAY_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.LIGHT_GRAY_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.CYAN_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.PURPLE_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.BLUE_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.BROWN_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.GREEN_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.RED_STAINED_GLASS.getLootTableId());
        blockList.add(Blocks.BLACK_STAINED_GLASS.getLootTableId());
    }
}
