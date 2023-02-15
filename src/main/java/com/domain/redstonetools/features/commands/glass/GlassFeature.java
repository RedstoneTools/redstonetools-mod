package com.domain.redstonetools.features.commands.glass;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.features.options.EmptyOptions;
import com.domain.redstonetools.utils.ItemUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;


@Feature(name = "glass")
public class GlassFeature extends CommandFeature<EmptyOptions> {
    //similar to: net.minecraft.client.MinecraftClient/doItemPick()
    @Override
    protected int execute(ServerCommandSource source, EmptyOptions options) throws CommandSyntaxException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            throw new CommandSyntaxException(null, Text.of("This command is client-side only"));
        }

        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            source.sendError(Text.of("You must be looking at a block to use this command"));

            return -1;
        }

        BlockHitResult blockHit = (BlockHitResult) client.crosshairTarget;
        BlockState blockState = client.world.getBlockState(blockHit.getBlockPos());

        Block block = blockState.getBlock();
        ItemStack itemStack = getWoolOrGlassStackFromBlock(block);

        if (itemStack == null) {
            source.sendError(Text.of("Invalid block! Use on wool or glass"));

            return -1;
        }

        PlayerInventory playerInventory = client.player.getInventory();
        playerInventory.addPickBlock(itemStack);

        if (client.interactionManager == null) {
            throw new CommandSyntaxException(null, Text.of("Failed to get interaction manager"));
        }

        client.interactionManager.clickCreativeStack(client.player.getStackInHand(Hand.MAIN_HAND), 36 + playerInventory.selectedSlot);

        return Command.SINGLE_SUCCESS;
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
