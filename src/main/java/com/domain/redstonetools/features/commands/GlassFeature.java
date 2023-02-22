package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.PickBlockFeature;
import com.domain.redstonetools.features.options.EmptyOptions;
import com.domain.redstonetools.features.options.Options;
import com.domain.redstonetools.utils.ItemUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.registry.Registry;


@Feature(name = "Glass", description = "Converts glass to wool and vice versa.", command = "glass")
public class GlassFeature extends PickBlockFeature {
    @Override
    protected ItemStack getItemStack(ServerCommandSource source, BlockHitResult blockHit) throws CommandSyntaxException {
        ItemStack stack = getWoolOrGlassStackFromBlock(source.getPlayer().world.getBlockState(blockHit.getBlockPos()).getBlock());
        if (stack == null) source.sendError(Text.of("Invalid block! Use on wool or glass"));

        return stack;
    }

    private ItemStack getWoolOrGlassStackFromBlock(Block block) {
        String blockString = Registry.BLOCK.getId(block).toString().substring("minecraft:".length());

        Item item = null;

        if (blockString.contains("stained_glass") && !blockString.contains("pane")) {
            String color = blockString.substring(0, blockString.indexOf("_stained_glass"));
            String newItemName = color + "_wool";

            item = ItemUtils.getItemByName(newItemName);
        } else if (blockString.contains("wool")) {
            String color = blockString.substring(0, blockString.indexOf("_wool"));
            String newItemName = color + "_stained_glass";

            item = ItemUtils.getItemByName(newItemName);
        }

        if (item != null) {
            return new ItemStack(item);
        }

        return null;
    }

}
