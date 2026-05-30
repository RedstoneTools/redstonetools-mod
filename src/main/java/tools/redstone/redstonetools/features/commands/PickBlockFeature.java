package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import tools.redstone.redstonetools.mixin.features.PlayerInventoryAccessor;
import tools.redstone.redstonetools.utils.BlockInfo;

import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public abstract class PickBlockFeature extends BlockRaycastFeature {
	@Override
	protected int execute(CommandContext<CommandSourceStack> context, BlockInfo blockInfo) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayer();
		if (player == null) {
			throw new SimpleCommandExceptionType(Component.literal("Failed to get player.")).create();
		}

		var stack = getItemStack(context, blockInfo);

		Inventory playerInventory = player.getInventory();
		addPickBlock(playerInventory, stack);

		int i = playerInventory.findSlotMatchingItem(stack);
		if (i != -1) {
			if (Inventory.isHotbarSlot(i)) {
				//? if <=1.21.4 {
				playerInventory.setSelectedHotbarSlot(i);
				//? } else
//				playerInventory.setSelectedSlot(i);
			} else {
				playerInventory.pickSlot(i);
			}
		} else if (player.hasInfiniteMaterials()) {
			playerInventory.addAndPickItem(stack);
		}
		context.getSource().getPlayer().connection.send(new ClientboundSetHeldSlotPacket(((PlayerInventoryAccessor)playerInventory).getSelected()));
		player.inventoryMenu.broadcastChanges();
		return 1;
	}

	// reimplementation from 1.18.2
	public void addPickBlock(Inventory pi, ItemStack stack) {
		int i = pi.findSlotMatchingItem(stack);
		if (Inventory.isHotbarSlot(i)) {
			//? if <=1.21.4 {
			pi.setSelectedHotbarSlot(i);
			//? } else
//			pi.setSelectedSlot(i);
			return;
		}
		if (i == -1) {
			int j;
			//? if <=1.21.4 {
			pi.setSelectedHotbarSlot(pi.getSuitableHotbarSlot());
			//? } else
//			pi.setSelectedSlot(pi.getSuitableHotbarSlot());
			if (!((PlayerInventoryAccessor)pi).getItems().get(((PlayerInventoryAccessor)pi).getSelected()).isEmpty() && (j = pi.getFreeSlot()) != -1) {
				((PlayerInventoryAccessor)pi).getItems().set(j, ((PlayerInventoryAccessor)pi).getItems().get(((PlayerInventoryAccessor)pi).getSelected()));
			}
			((PlayerInventoryAccessor)pi).getItems().set(((PlayerInventoryAccessor)pi).getSelected(), stack);
		} else {
			pi.pickSlot(i);
		}
	}

	protected abstract ItemStack getItemStack(CommandContext<CommandSourceStack> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException;
}
