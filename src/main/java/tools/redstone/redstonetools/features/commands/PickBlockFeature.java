package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
//? if fabric {
/*import tools.redstone.redstonetools.mixin.features.PlayerInventoryAccessor;
 *///? }
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
				/*playerInventory.setSelectedHotbarSlot(i);
				*///? } else
				playerInventory.setSelectedSlot(i);
			} else {
				//? if fabric {
				/*playerInventory.pickSlot(i);
				 *///? } else {
				playerInventory.pickSlot(i, playerInventory.getSuitableHotbarSlot());
				//?}
			}
		} else if (player.hasInfiniteMaterials()) {
			//? if fabric {
			/*playerInventory.addAndPickItem(stack);
			 *///? } else {
			playerInventory.addAndPickItem(stack, playerInventory.getSuitableHotbarSlot());
			//?}
		}
		//? if fabric {
		/*int selected = ((PlayerInventoryAccessor) playerInventory).getSelected();
		 *///? } else {
		int selected = playerInventory.getSelectedSlot();
		//?}
		context.getSource().getPlayer().connection.send(new ClientboundSetHeldSlotPacket(selected));
		player.inventoryMenu.broadcastChanges();
		return 1;
	}

	// reimplementation from 1.18.2
	public void addPickBlock(Inventory pi, ItemStack stack) {
		//? if fabric {
		/*var accessor = (PlayerInventoryAccessor) pi;
		 *///? }
		int i = pi.findSlotMatchingItem(stack);
		if (Inventory.isHotbarSlot(i)) {
			//? if <=1.21.4 {
			/*pi.setSelectedHotbarSlot(i);
			 *///? } else
			pi.setSelectedSlot(i);
			return;
		}
		if (i == -1) {
			int j;
			//? if <=1.21.4 {
			/*pi.setSelectedHotbarSlot(pi.getSuitableHotbarSlot());
			 *///? } else
			pi.setSelectedSlot(pi.getSuitableHotbarSlot());
			//? if fabric {
			/*if (!accessor.getItems().get(accessor.getSelected()).isEmpty() && (j = pi.getFreeSlot()) != -1) {
				accessor.getItems().set(j, accessor.getItems().get(accessor.getSelected()));
			}
			accessor.getItems().set(accessor.getSelected(), stack);
			*///? } else {
			var items = pi.getNonEquipmentItems();
			int selected = pi.getSelectedSlot();
			if (!items.get(selected).isEmpty() && (j = pi.getFreeSlot()) != -1) {
				items.set(j, items.get(selected));
			}
			items.set(selected, stack);
			//? }
		} else {
			//? if fabric {
			/*pi.pickSlot(i);
			 *///? } else
			pi.pickSlot(i, pi.getSuitableHotbarSlot());
		}
	}

	protected abstract ItemStack getItemStack(CommandContext<CommandSourceStack> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException;
}
