package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.utils.BlockInfo;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

/* TODO: i think there should be an @Feature here? */
public abstract class PickBlockFeature extends BlockRaycastFeature {
    @Override
    protected final int execute(ServerCommandSource source, BlockInfo blockInfo) throws CommandSyntaxException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return -1;
        }

        ItemStack stack = getItemStack(source, blockInfo);
        if (stack == null) {
            return -1;
        }

        PlayerInventory playerInventory = client.player.getInventory();
        playerInventory.addPickBlock(stack);

        if (client.interactionManager == null) {
            throw new CommandSyntaxException(null, Text.of("Failed to get interaction manager"));
        }

        client.interactionManager.clickCreativeStack(client.player.getStackInHand(Hand.MAIN_HAND), 36 + playerInventory.selectedSlot);

        return Command.SINGLE_SUCCESS;
    }

    protected abstract ItemStack getItemStack(ServerCommandSource source, BlockInfo blockInfo) throws CommandSyntaxException;
}
