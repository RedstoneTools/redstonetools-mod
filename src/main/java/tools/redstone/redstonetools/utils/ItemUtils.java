package tools.redstone.redstonetools.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;

public class ItemUtils {
	public static boolean isEmpty(ItemStack itemStack) {
		return itemStack == null || itemStack.getItem() == Items.AIR || itemStack.getCount() == 0;
	}

	public static ItemStack getMainItem(PlayerEntity player) {
		ItemStack stack = player.getMainHandStack();
		return isEmpty(stack) ? player.getOffHandStack() : stack;
	}

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

}
