package tools.redstone.redstonetools.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

	private static Method getStringMethod;
	private static Method getMethod;

	public static String getCommand(ItemStack stack) {
		if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
			NbtCompound data = getCustomData(stack);
			try {
				return data.getString("command", "");
			} catch (NoSuchMethodError ignored) {
				if (getStringMethod == null) {
					try {
						getStringMethod = NbtCompound.class.getMethod("method_10558", String.class);
					} catch (Exception e) {
						throw new RuntimeException("Something went wrong. Contact a redstonetools developer1", e);
					}
				}
				try {
					return (String) getStringMethod.invoke(data, "command");
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new RuntimeException("Something went wrong. Contact a redstonetools developer", e);
				}
			}
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
		NbtCompound data;
		try {
			data = stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
		} catch (NoSuchMethodError ignored) {
			try {
				getMethod = ComponentHolder.class.getMethod("method_57824", ComponentType.class);
			} catch (NoSuchMethodException e) {
				try {
					getMethod = ComponentHolder.class.getMethod("get", ComponentType.class);
				} catch (NoSuchMethodException ex) {
					throw new RuntimeException(ex);
				}
			} finally {
				getMethod.setAccessible(true);
			}
			try {
				data = ((NbtComponent) getMethod.invoke(stack, DataComponentTypes.CUSTOM_DATA)).copyNbt();
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		return data;
	}
}
