package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.mixin.features.PlayerInventoryAccessor;
import tools.redstone.redstonetools.utils.BlockInfo;

import javax.annotation.Nullable;

public abstract class PickBlockFeature extends BlockRaycastFeature {
	protected int execute(CommandContext<ServerCommandSource> context, BlockInfo blockInfo) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayer();
		if (player == null) {
			throw new SimpleCommandExceptionType(Text.literal("Failed to get player.")).create();
		}

		var stack = getItemStack(context, blockInfo);

		PlayerInventory playerInventory = player.getInventory();
		addPickBlock(playerInventory, stack);

		int i = playerInventory.getSlotWithStack(stack);
		if (i != -1) {
			if (PlayerInventory.isValidHotbarIndex(i)) {
				playerInventory.setSelectedSlot(i);
			} else {
				playerInventory.swapSlotWithHotbar(i);
			}
		} else if (player.isInCreativeMode()) {
			playerInventory.swapStackWithHotbar(stack);
		}
		context.getSource().getPlayer().networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(((PlayerInventoryAccessor) playerInventory).getSelectedSlot()));
		player.playerScreenHandler.sendContentUpdates();
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
			if (!((PlayerInventoryAccessor) pi).getMain().get(((PlayerInventoryAccessor) pi).getSelectedSlot()).isEmpty() && (j = pi.getEmptySlot()) != -1) {
				((PlayerInventoryAccessor) pi).getMain().set(j, ((PlayerInventoryAccessor) pi).getMain().get(((PlayerInventoryAccessor) pi).getSelectedSlot()));
			}
			((PlayerInventoryAccessor) pi).getMain().set(((PlayerInventoryAccessor) pi).getSelectedSlot(), stack);
		} else {
			pi.swapSlotWithHotbar(i);
		}
	}

	protected abstract ItemStack getItemStack(CommandContext<ServerCommandSource> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException;
}
