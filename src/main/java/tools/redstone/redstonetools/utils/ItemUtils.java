package tools.redstone.redstonetools.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;

public class ItemUtils {
	public static BlockState getPlacementState(PlayerEntity player, ItemStack stack, float reach) {
		Item type = stack.getItem();
		if (!(type instanceof BlockItem blockItem))
			return null;
		return blockItem.getBlock().getPlacementState(new ItemPlacementContext(
				player,
				player.getActiveHand(),
				stack,
				RaycastUtils.rayCastFromEye(player, reach)
		));
	}

	public static BlockState getUseState(PlayerEntity player, ItemStack stack, float reach) {
		if (stack == null || stack.getItem() == Items.AIR)
			return null;
		BlockState state = getPlacementState(player, stack, reach);
		if (state == null)
			state = Blocks.BEDROCK.getDefaultState();
		return state;
	}

	public static String getCommand(ItemStack stack) {
		if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
			NbtCompound data = getCustomData(stack);
			//? if >=1.21.5 {
			return data.getString("command", "");
			//?} else {
			/*return data.getString("command");
			*///?}
		}
		return "";
	}

	public static void setCommand(ItemStack stack, String command) {
		NbtCompound data = new NbtCompound();
		data.putString("command", command);
		stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(data));
	}

	public static boolean containsCommand(ItemStack stack) {
		if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
			NbtCompound data = getCustomData(stack);
			return data.contains("command");
		}
		return false;
	}

	public static void removeCommand(ItemStack stack) {
		if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
			NbtCompound data = getCustomData(stack);
			data.remove("command");
			stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(data));
		}
		System.out.println(stack);
	}

	private static NbtCompound getCustomData(ItemStack stack) {
		return stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
	}
}
