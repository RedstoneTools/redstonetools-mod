package com.domain.redstonetools.features.commands.glass;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.features.options.EmptyOptions;
import com.mojang.brigadier.Command;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import static com.domain.redstonetools.utils.ItemUtils.getItemByName;


@Feature(name = "glass")
public class GlassFeature extends CommandFeature<EmptyOptions> {


    //similar to: net.minecraft.client.MinecraftClient/doItemPick()
    @Override
    protected int execute(ServerCommandSource source, EmptyOptions options) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return -1;

        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK || client.world == null || client.interactionManager == null)
            return -1;

        BlockPos blockPos = ((BlockHitResult) client.crosshairTarget).getBlockPos();
        BlockState blockState = client.world.getBlockState(blockPos);
        if (blockState.isAir()) return -1;

        Block block = blockState.getBlock();
        ItemStack itemStack = getStackFromBlock(block);

        if (itemStack == null) {
            client.player.sendMessage(Text.of("Invalid block! Use on wool or glass."), false);
            return -1;
        }

        PlayerInventory playerInventory = client.player.getInventory();
        playerInventory.addPickBlock(itemStack);

        client.interactionManager.clickCreativeStack(client.player.getStackInHand(Hand.MAIN_HAND), 36 + playerInventory.selectedSlot);

        return Command.SINGLE_SUCCESS;
    }

    private ItemStack getStackFromBlock(Block block) {
        String blockString = Registry.BLOCK.getId(block).toString().substring("minecraft:".length());

        Item item = null;
        if (blockString.contains("stained_glass") && !blockString.contains("pane")) {
            String color = blockString.substring(0, blockString.indexOf("_stained_glass"));
            String newItemName = color + "_wool";

           item= getItemByName(newItemName);
        }
        if (blockString.contains("wool")) {
            String color = blockString.substring(0, blockString.indexOf("_wool"));
            String newItemName = color + "_stained_glass";

            item = getItemByName(newItemName);
        }

        if (item != null) return new ItemStack(item);

        return null;
    }


}
