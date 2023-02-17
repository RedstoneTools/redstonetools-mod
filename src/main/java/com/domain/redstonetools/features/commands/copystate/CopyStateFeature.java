package com.domain.redstonetools.features.commands.copystate;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.features.options.EmptyOptions;
import com.domain.redstonetools.utils.BlockStateNbtUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Method;

import static com.domain.redstonetools.RedstoneToolsClient.LOGGER;

@Feature(name = "copystate")
public class CopyStateFeature extends CommandFeature<EmptyOptions> {


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
        BlockPos blockPos = blockHit.getBlockPos();
        BlockState blockState = client.world.getBlockState(blockHit.getBlockPos());

        Block block = blockState.getBlock();
        ItemStack itemStack = block.getPickStack(client.world, blockPos, blockState);

        BlockEntity blockEntity = null;
        if (blockState.hasBlockEntity()) {
            blockEntity = client.world.getBlockEntity(blockPos);
        }

        if (blockEntity != null) {
            addBlockEntityNbt(itemStack, blockEntity);
        }
        addBlockStateText(itemStack);

        NbtCompound nbt = itemStack.getNbt();
        if (nbt == null) nbt = new NbtCompound();

        nbt.putString("blockstate",BlockStateNbtUtil.blockStateToString(blockState));

        PlayerInventory playerInventory = client.player.getInventory();
        playerInventory.addPickBlock(itemStack);

        if (client.interactionManager == null) {
            throw new CommandSyntaxException(null, Text.of("Failed to get interaction manager"));
        }

        client.interactionManager.clickCreativeStack(client.player.getStackInHand(Hand.MAIN_HAND), 36 + playerInventory.selectedSlot);

        return Command.SINGLE_SUCCESS;
    }

    private void addBlockStateText(ItemStack itemStack) {
        NbtCompound displayNbt = itemStack.getSubNbt("display");
        NbtList loreList = new NbtList();

        if (displayNbt == null) {
            displayNbt = new NbtCompound();
        } else {
            loreList = (NbtList) displayNbt.get("Lore");
        }

        loreList.add(NbtString.of("\"(+BlockState)\""));
        displayNbt.put("Lore", loreList);
        itemStack.setSubNbt("display", displayNbt);
    }

    private void addBlockEntityNbt(ItemStack itemStack, BlockEntity blockEntity) {
        try {
            Class<? extends MinecraftClient> clientClass = MinecraftClient.class;
            MinecraftClient clientClassInstance = MinecraftClient.getInstance();
            Method m = clientClass.getDeclaredMethod("addBlockEntityNbt", ItemStack.class, BlockEntity.class);

            m.setAccessible(true);
            m.invoke(clientClassInstance, itemStack, blockEntity);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
