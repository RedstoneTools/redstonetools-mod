package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.utils.BlockColorUtils;
import com.domain.redstonetools.utils.ItemUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

import static com.domain.redstonetools.RedstoneToolsClient.LOGGER;


@Feature(name = "Glass", description = "Converts glass to wool and vice versa.", command = "glass")
public class GlassFeature extends PickBlockFeature {


    @Override
    protected ItemStack getItemStack(ServerCommandSource source, BlockHitResult blockHit) throws CommandSyntaxException {
        return getWoolOrGlassStackFromBlock(FabricAdapter.adapt(source.getPlayer().world), blockHit.getBlockPos());
    }

    private ItemStack getWoolOrGlassStackFromBlock(World world, BlockPos pos) {
        BlockVector3 vectorPos = BlockVector3.at(pos.getX(),pos.getY(),pos.getZ());

        if (!BlockColorUtils.shouldBeColored(world,vectorPos,false))
            return new ItemStack(Items.WHITE_WOOL);

        String color = BlockColorUtils.getBlockColor(world.getBlock(vectorPos)).substring("minecraft:".length());
        String colorlessBlock = BlockColorUtils.getColorlessBlockId(world.getBlock(vectorPos));

        if (colorlessBlock.contains("wool")) {
            LOGGER.info(color + "_stained_glass");
            return new ItemStack(ItemUtils.getItemByName(color + "_stained_glass"));
        }

        return new ItemStack(ItemUtils.getItemByName(color + "_wool"));
    }

}
