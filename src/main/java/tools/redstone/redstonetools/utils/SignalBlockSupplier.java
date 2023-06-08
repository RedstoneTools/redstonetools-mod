package tools.redstone.redstonetools.utils;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

@FunctionalInterface
public interface SignalBlockSupplier {

    NbtCompound build(int signal);

    default ItemStack get(Block block, int signal) {
        if (signal < 0) throw new IllegalArgumentException("Signal cannot be negative");
        ItemStack item = new ItemStack(block);
        NbtCompound nbt = build(signal);
        nbt.putBoolean("HideFlags", true);
        item.setNbt(nbt);

        MutableText text = new LiteralText(String.valueOf(signal));
        text.setStyle(text.getStyle().withColor(Formatting.RED));
        item.setCustomName(text);
        item.addEnchantment(Enchantment.byRawId(0),0);

        return item;
    }

    static SignalBlockSupplier container(int slots) {
        return signal -> {
            if (signal > 1779) throw new IllegalArgumentException("Container signal must be 0-1779");
            NbtCompound tags = new NbtCompound();
            NbtList itemsTag = new NbtList();
            Item item = signal > 15 ? Items.WHITE_SHULKER_BOX : slots >= 15 ? Items.WOODEN_SHOVEL : Items.STICK;
            int stack_size = signal > 15 ? signal > 897 ? 127 : 64 : item.getMaxCount();

            for (
                    int slot = 0,
                    count = Math.max(signal, (int) Math.ceil(slots * (signal - 1) / 14D * item.getMaxCount()));
                    count > 0;
                    slot++, count -= stack_size
            ) {
                NbtCompound slotTag = new NbtCompound();
                slotTag.putByte("Slot", (byte) slot);
                slotTag.putString("id", Registry.ITEM.getId(item).toString());
                slotTag.putByte("Count", (byte) Math.min(stack_size, count));
                itemsTag.add(slotTag);
            }

            NbtCompound tag = new NbtCompound();
            tag.put("Items", itemsTag);
            tags.put("BlockEntityTag", tag);

            return tags;
        };
    }

    static SignalBlockSupplier container_27() {
        return container(27);
    }

    static SignalBlockSupplier container_9() {
        return container(9);
    }

    static SignalBlockSupplier container_5() {
        return container(5);
    }

    static SignalBlockSupplier container_3() {
        return container(3);
    }

    static SignalBlockSupplier composter() {
        return signal -> {
            if (signal > 8 || signal == 7) throw new IllegalArgumentException("Composter signal must be 0-6 or 8");
            NbtCompound tags = new NbtCompound();
            NbtCompound tag = new NbtCompound();
            tag.putInt("level", signal);
            tags.put("BlockStateTag", tag);
            return tags;
        };
    }

    static SignalBlockSupplier command_block() {
        return signal -> {
            NbtCompound tags = new NbtCompound();
            NbtCompound tag = new NbtCompound();
            tag.putInt("SuccessCount", signal);
            tags.put("BlockEntityTag", tag);
            return tags;
        };
    }

}
