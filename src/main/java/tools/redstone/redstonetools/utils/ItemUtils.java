package tools.redstone.redstonetools.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ItemUtils {
	public static BlockState getPlacementState(Player player, ItemStack stack, float reach) {
		Item type = stack.getItem();
		if (!(type instanceof BlockItem blockItem))
			return null;
		return blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(
				player,
				player.getUsedItemHand(),
				stack,
				RaycastUtils.rayCastFromEye(player, reach)
		));
	}

	public static BlockState getUseState(Player player, ItemStack stack, float reach) {
		if (stack == null || stack.getItem() == Items.AIR)
			return null;
		BlockState state = getPlacementState(player, stack, reach);
		if (state == null)
			state = Blocks.BEDROCK.defaultBlockState();
		return state;
	}

	public static String getCommand(ItemStack stack) {
		if (stack.has(DataComponents.CUSTOM_DATA)) {
			CompoundTag data = getCustomData(stack);
			//? if >=1.21.5 {
			return data.getStringOr("command", "");
			//?} else {
			/*return data.getString("command");
			*///?}
		}
		return "";
	}

	public static void setCommand(ItemStack stack, String command) {
		CompoundTag data = new CompoundTag();
		data.putString("command", command);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
	}

	public static boolean containsCommand(ItemStack stack) {
		if (stack.has(DataComponents.CUSTOM_DATA)) {
			CompoundTag data = getCustomData(stack);
			return data.contains("command");
		}
		return false;
	}

	public static void removeCommand(ItemStack stack) {
		if (stack.has(DataComponents.CUSTOM_DATA)) {
			CompoundTag data = getCustomData(stack);
			data.remove("command");
			stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
		}
	}

	private static CompoundTag getCustomData(ItemStack stack) {
		return stack.get(DataComponents.CUSTOM_DATA).copyTag();
	}
}
