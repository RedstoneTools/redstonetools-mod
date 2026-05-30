package tools.redstone.redstonetools.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface SignalBlockSupplier {

	ItemStack createItem(int signalStrength);

	default ItemStack getItemStack(Block ignoredBlock, int signalStrength) {
		ItemStack item = this.createItem(signalStrength);
		setItemName(item, signalStrength);
		item.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
		return item;
	}

	static SignalBlockSupplier container(int slots, Item containerType) {
		return signalStrength -> {
			if (isInvalidSignalStrength(signalStrength, 1387))
				throw new IllegalArgumentException("Container signal must be 0-1387");

			Item item = getBestItem(signalStrength, slots);
			int stackSize = getStackSize(signalStrength, item);
			int itemsNeeded = getItemsNeeded(slots, signalStrength, item);

			ItemStack stack = new ItemStack(containerType);
			NonNullList<ItemStack> inventoryItems = NonNullList.withSize(slots, ItemStack.EMPTY);

			for (int slot = 0, count = itemsNeeded; count > 0; slot++, count -= stackSize) {
				inventoryItems.set(slot, new ItemStack(item, Math.min(stackSize, count)));
			}
			ItemContainerContents containerComponent = ItemContainerContents.fromItems(inventoryItems);

			stack.set(DataComponents.CONTAINER, containerComponent);

			return stack;
		};
	}

	private static int getItemsNeeded(int slots, int signalStrength, Item item) {
		int itemsNeeded = Math.max(0, signalStrength == 1
				? 1
				: (int) Math.ceil(slots * (signalStrength - 1) / 14D * item.getDefaultMaxStackSize()));

		// Check that the calculated number of items is correct.
		// This is to prevent problems with items that have a maximum stack size of 1 but stackSize > 1.
		// TODO: This can be improved by removing an item and adding stackable items up to the desired signal strength.
		// Even with the improvement, this will still fail for inventories with no available slots.
		if (calculateComparatorOutput(itemsNeeded, slots, item.getDefaultMaxStackSize()) != signalStrength)
			throw new IllegalStateException("This signal strength cannot be achieved with the selected container");
		return itemsNeeded;
	}

	static SignalBlockSupplier commandBlock() {
		return signalStrength -> {
			if (isInvalidSignalStrength(signalStrength, Integer.MAX_VALUE))
				throw new IllegalArgumentException("Command block signal must be positive");

			ItemStack commandBlockStack = new ItemStack(Items.COMMAND_BLOCK);

			CompoundTag blockEntityNbt = new CompoundTag();
			blockEntityNbt.putInt("SuccessCount", signalStrength);

			CustomData blockEntityData = CustomData.of(blockEntityNbt);

			commandBlockStack.set(DataComponents.CUSTOM_DATA, blockEntityData);
			return commandBlockStack;
		};
	}

	private static boolean isInvalidSignalStrength(int signalStrength, int maxSignalStrength) {
		return signalStrength < 0 || signalStrength > maxSignalStrength;
	}

	private static int calculateComparatorOutput(int items, int slots, int item$getMaxCount) {
		float f = (float) items / (float) item$getMaxCount;
		return Mth.floor(f / (float) slots * 14.0f) + (items > 0 ? 1 : 0);
	}

	private static Item getBestItem(int signalStrength, int slots) {
		if (signalStrength > 15)
			return Items.WHITE_SHULKER_BOX;
		else if (slots >= 15)
			return Items.WOODEN_SHOVEL;
		else
			return Items.STICK;
	}

	private static int getStackSize(int signalStrength, Item item) {
		if (signalStrength > 897)
			return 99;
		else if (signalStrength > 15)
			return 64;
		else
			return item.getDefaultMaxStackSize();
	}

	private static void setItemName(ItemStack item, int signalStrength) {
		MutableComponent text = Component.literal(String.valueOf(signalStrength));
		text.setStyle(text.getStyle().withColor(ChatFormatting.RED));
		item.set(DataComponents.CUSTOM_NAME, text);
	}

}
