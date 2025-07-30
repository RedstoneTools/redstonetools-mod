package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.mixin.features.PlayerInventoryAccessor;
import tools.redstone.redstonetools.utils.BlockInfo;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import javax.annotation.Nullable;

public abstract class PickBlockFeature extends BlockRaycastFeature {
    protected int execute(CommandContext<ServerCommandSource> context, BlockInfo blockInfo) throws CommandSyntaxException {
        var player = context.getSource().getPlayer();
        if (player == null) {
            throw new SimpleCommandExceptionType(Text.literal("Failed to get player.")).create();
        }

        var stack = getItemStack(context, blockInfo);

        PlayerInventory playerInventory = player.getInventory();
        addPickBlock(playerInventory, stack);

        boolean didSwitch = false;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = playerInventory.getStack(i);
            if (itemStack.getItem().equals(stack.getItem())) {
                playerInventory.setSelectedSlot(i);
                didSwitch = true;
                break;
            }
        }
        if (!didSwitch) {
            playerInventory.setSelectedStack(stack);
        }
//        MinecraftClient.getInstance().interactionManager.clickCreativeStack(MinecraftClient.getInstance().player.getStackInHand(Hand.MAIN_HAND), 36 + ((PlayerInventoryAccessor)playerInventory).getSelectedSlot());

        return 1;
    }

    // reimplementation from 1.18.2
    public void addPickBlock(PlayerInventory pi, ItemStack stack) {
        int i = pi.getSlotWithStack(stack);
        if (PlayerInventory.isValidHotbarIndex(i)) {
            pi.setSelectedSlot(i);
            return;
        }
        if (i == -1) {
            int j;
            pi.setSelectedSlot(pi.getSwappableHotbarSlot());
            if (!((PlayerInventoryAccessor)pi).getMain().get(((PlayerInventoryAccessor)pi).getSelectedSlot()).isEmpty() && (j = pi.getEmptySlot()) != -1) {
                ((PlayerInventoryAccessor)pi).getMain().set(j, ((PlayerInventoryAccessor)pi).getMain().get(((PlayerInventoryAccessor)pi).getSelectedSlot()));
            }
            ((PlayerInventoryAccessor)pi).getMain().set(((PlayerInventoryAccessor)pi).getSelectedSlot(), stack);
        } else {
            pi.swapSlotWithHotbar(i);
        }
    }

	protected abstract ItemStack getItemStack(CommandContext<ServerCommandSource> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException;
}
