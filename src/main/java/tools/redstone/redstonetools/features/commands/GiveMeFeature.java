package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class GiveMeFeature extends AbstractFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("g")
                .then(argument("item", ItemStackArgumentType.itemStack(registryAccess))
                .executes(context -> FeatureUtils.getFeature(GiveMeFeature.class).execute(context, ItemStackArgumentType.getItemStackArgument(context, "item"), 1))
                .then(argument("count", IntegerArgumentType.integer(0, 99))
                .executes(context -> FeatureUtils.getFeature(GiveMeFeature.class).execute(context, ItemStackArgumentType.getItemStackArgument(context, "item"), IntegerArgumentType.getInteger(context, "count")))))));
    }

    private int execute(CommandContext<ServerCommandSource> context, ItemStackArgument itemArgument, int count) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack stack = itemArgument.createStack(1, false);
        stack.setCount(count);
        if (player != null) {
            PlayerInventory inventory = player.getInventory();
            int emptySlot = inventory.getEmptySlot();
            ItemStack selectedSlot = inventory.getStack(inventory.getSelectedSlot());
            int slotInHotbarOfSameType = checkForSlotInHotbarOfSameType(inventory, stack);
            if (selectedSlot.isEmpty()) {
                player.sendMessage(Text.literal("1"));
                inventory.setSelectedStack(stack);
            } else if (selectedSlot.getItem().equals(stack.getItem())) {
                if (selectedSlot.getCount() + stack.getCount() < 99) { // same item, sum is valid
                    player.sendMessage(Text.literal("2"));
                    selectedSlot.setCount(selectedSlot.getCount() + stack.getCount());
                } else { // same item, sum is not valid
                    player.sendMessage(Text.literal("3"));
                    stack.setCount(selectedSlot.getCount() + stack.getCount() - 99);
                    selectedSlot.setCount(99);
                    inventory.setStack(emptySlot, stack);
                }
            } else if (slotInHotbarOfSameType != -1) { // slot in hotbar of same type, sum is valid
                ItemStack sameType = inventory.getStack(slotInHotbarOfSameType);
                player.sendMessage(Text.literal("4"));
                inventory.setSelectedSlot(slotInHotbarOfSameType);
                inventory.updateItems();
                sameType.setCount(sameType.getCount() + stack.getCount());
                selectedSlot.setCount(selectedSlot.getCount() + stack.getCount());
            } else if (PlayerInventory.isValidHotbarIndex(emptySlot)) { // empty slot in hotbar
                player.sendMessage(Text.literal("5"));
                inventory.setSelectedSlot(emptySlot);
                inventory.setSelectedStack(stack);
                inventory.updateItems();
            } else if (emptySlot != -1) { // empty slot in inventory
                player.sendMessage(Text.literal("6"));
                inventory.setStack(emptySlot, selectedSlot);
                inventory.setSelectedStack(stack);
            } else {
                throw new SimpleCommandExceptionType(Text.literal("Inventory full!")).create();
            }
            inventory.markDirty();
        } else
            context.getSource().sendMessage(Text.of("Player not found."));
        return 0;
    }

    private int checkForSlotInHotbarOfSameType(PlayerInventory inventory, ItemStack stack) {
        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem() == stack.getItem() &&
            inventory.getStack(i).getCount() + stack.getCount() < 99) {
                return i;
            }
        }
        return -1;
    }
}
