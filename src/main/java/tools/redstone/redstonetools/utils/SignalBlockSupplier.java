package tools.redstone.redstonetools.utils;

import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;

@FunctionalInterface
public interface SignalBlockSupplier {

    ItemStack createItem(int signalStrength);

    default ItemStack getItemStack(Block ignoredBlock, int signalStrength) {
        ItemStack item = this.createItem(signalStrength);
        setItemName(item, signalStrength);
        item.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
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
            DefaultedList<ItemStack> inventoryItems = DefaultedList.ofSize(slots, ItemStack.EMPTY);

            for (int slot = 0, count = itemsNeeded; count > 0; slot++, count -= stackSize) {
                inventoryItems.set(slot, new ItemStack(item, Math.min(stackSize, count)));
            }
            ContainerComponent containerComponent = ContainerComponent.fromStacks(inventoryItems);

            stack.set(DataComponentTypes.CONTAINER, containerComponent);

            return stack;
        };
    }

    private static int getItemsNeeded(int slots, int signalStrength, Item item) {
        int itemsNeeded = Math.max(0, signalStrength == 1
                ? 1
                : (int) Math.ceil(slots * (signalStrength - 1) / 14D * item.getMaxCount()));

        // Check that the calculated number of items is correct.
        // This is to prevent problems with items that have a maximum stack size of 1 but stackSize > 1.
        // TODO: This can be improved by removing an item and adding stackable items up to the desired signal strength.
        // Even with the improvement, this will still fail for inventories with no available slots.
        if (calculateComparatorOutput(itemsNeeded, slots, item.getMaxCount()) != signalStrength)
            throw new IllegalStateException("This signal strength cannot be achieved with the selected container");
        return itemsNeeded;
    }

    static SignalBlockSupplier commandBlock() {
        return signalStrength -> {
            if (isInvalidSignalStrength(signalStrength, Integer.MAX_VALUE))
                throw new IllegalArgumentException("Command block signal must be positive");

            ItemStack commandBlockStack = new ItemStack(Items.COMMAND_BLOCK);

            NbtCompound blockEntityNbt = new NbtCompound();
            blockEntityNbt.putInt("SuccessCount", signalStrength);

            NbtComponent blockEntityData = NbtComponent.of(blockEntityNbt);

            commandBlockStack.set(DataComponentTypes.CUSTOM_DATA, blockEntityData);
            return commandBlockStack;
        };
    }

    private static boolean isInvalidSignalStrength(int signalStrength, int maxSignalStrength) {
        return signalStrength < 0 || signalStrength > maxSignalStrength;
    }

    private static int calculateComparatorOutput(int items, int slots, int item$getMaxCount) {
        float f = (float) items / (float) item$getMaxCount;
        return MathHelper.floor(f / (float) slots * 14.0f) + (items > 0 ? 1 : 0);
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
            return item.getMaxCount();
    }

    private static void setItemName(ItemStack item, int signalStrength) {
        MutableText text = Text.literal(String.valueOf(signalStrength));
        text.setStyle(text.getStyle().withColor(Formatting.RED));
        item.set(DataComponentTypes.CUSTOM_NAME, text);
    }

}
