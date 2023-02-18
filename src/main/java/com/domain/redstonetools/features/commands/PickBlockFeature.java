package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.options.Options;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public abstract class PickBlockFeature<O extends Options> extends RayCastFeature<O> {


    @Override
    protected int execute(ServerCommandSource source, Options options, BlockHitResult blockHit) throws CommandSyntaxException {
        MinecraftClient client = MinecraftClient.getInstance();
        ItemStack stack = getItemStack(source, options, blockHit);

        if (stack == null) {//sending error message should be handled by extending class
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


    protected abstract ItemStack getItemStack(ServerCommandSource source, Options options, BlockHitResult blockHit) throws CommandSyntaxException;


}
