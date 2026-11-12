package tools.redstone.redstonetools.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.network.Filterable;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
//? if >=1.21.10 {
import net.minecraft.world.item.component.TypedEntityData;
//?} else {
/*import net.minecraft.world.item.component.CustomData;
*///?}
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.block.Block;
//? if >=26.2 {
/*import net.minecraft.world.level.block.entity.BlockEntityTypes;
*///?} else if >=1.21.10 {
import net.minecraft.world.level.block.entity.BlockEntityType;
//?}
import net.minecraft.world.level.block.Blocks;

import java.util.Collections;

@FunctionalInterface
public interface SignalBlockSupplier {
	static SignalBlockSupplier lectern() {
		return signalStrength -> {
			ItemStack book = Items.WRITTEN_BOOK.getDefaultInstance();
			book.set(
				DataComponents.WRITTEN_BOOK_CONTENT,
				WrittenBookContent.EMPTY.withReplacedPages(
					Collections.nCopies(15, Filterable.passThrough(Component.empty()))
				)
			);
			CompoundTag blockEntityData = new CompoundTag();
			blockEntityData.put("Book", ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, book).getOrThrow());
			blockEntityData.putInt("Page", signalStrength-1);

			CompoundTag blockState = new CompoundTag();
			blockState.putBoolean("has_book", true);

			ItemStack lectern = Items.LECTERN.getDefaultInstance();

			//? if <1.21.10  {
			/*blockEntityData.putString("id", "minecraft:lectern");
			lectern.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityData));
			*///?} else {
			lectern.set(
				DataComponents.BLOCK_ENTITY_DATA,
				TypedEntityData.of(
					//~ if >=26.2 'BlockEntityType' -> 'BlockEntityTypes'
					BlockEntityType.LECTERN,
					blockEntityData
				)
			);
			//?}

			return lectern;
		};
	}

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

			//? if <1.21.10  {
			/*blockEntityNbt.putString("id", "minecraft:command_block");
			commandBlockStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityNbt));
			*///?} else {
			commandBlockStack.set(
				DataComponents.BLOCK_ENTITY_DATA,
				//~ if >=26.2 'BlockEntityType' -> 'BlockEntityTypes'
				TypedEntityData.of(BlockEntityType.COMMAND_BLOCK, blockEntityNbt)
			);
			//?}
			return commandBlockStack;
		};
	}

	static SignalBlockSupplier jukeBox() {
		return (signalStrength) -> {
			if (isInvalidSignalStrength(signalStrength, 15))
				throw new IllegalArgumentException("Can't provide jukebox with signal strength " + signalStrength);
			ItemStack stack = new ItemStack(Blocks.JUKEBOX);

			CompoundTag blockEntityData = new CompoundTag();
			
			String diskId = switch (signalStrength) {
				case 1 -> "minecraft:music_disc_13";
				case 2 -> "minecraft:music_disc_cat";
				case 3 -> "minecraft:music_disc_blocks";
				case 4 -> "minecraft:music_disc_chirp";
				case 5 -> "minecraft:music_disc_far";
				case 6 -> "minecraft:music_disc_mall";
				case 7 -> "minecraft:music_disc_mellohi";
				case 8 -> "minecraft:music_disc_stal";
				case 9 -> "minecraft:music_disc_strad";
				case 10 -> "minecraft:music_disc_ward";
				case 11 -> "minecraft:music_disc_11";
				case 12 -> "minecraft:music_disc_wait";
				case 13 -> "minecraft:music_disc_pigstep";
				case 14 -> "minecraft:music_disc_otherside";
				case 15 -> "minecraft:music_disc_5";
				default -> null;
			};

			if (diskId != null) {
				CompoundTag record = new CompoundTag();
				record.putString("id", diskId);
				record.putByte("count", (byte) 1);

				blockEntityData.put("RecordItem", record);

				blockEntityData.putInt("ticks_since_song_started", 6 * 60 * 20);
			}

			//? if <1.21.10 {
			/*blockEntityData.putString("id", "minecraft:jukebox");
			stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityData));
			*///?} else {
			stack.set(
				DataComponents.BLOCK_ENTITY_DATA,
				//? if >=26.2 {
				/*TypedEntityData.of(BlockEntityTypes.JUKEBOX, blockEntityData)
				 *///?} else {
				TypedEntityData.of(BlockEntityType.JUKEBOX, blockEntityData)
				//?}
			);
			//?}

			return stack;
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
			//? if <26.2 {
			return Items.WHITE_SHULKER_BOX;
			//?} else
			//return Items.DYED_SHULKER_BOX.white();
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
