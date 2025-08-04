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
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.mixin.features.PlayerInventoryAccessor;
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
            ItemStack selectedStack = inventory.getStack(((PlayerInventoryAccessor)inventory).getSelectedSlot());
            int selectedSlot = ((PlayerInventoryAccessor) inventory).getSelectedSlot();
            int firstSlotOfSameType = firstSlotOfSameType(inventory, stack);
            if (selectedStack.getItem().equals(stack.getItem())) {
                if (selectedStack.getCount() + stack.getCount() < 99) { // same item, sum is valid
                    selectedStack.setCount(selectedStack.getCount() + stack.getCount());
                } else { // same item, sum is not valid
                    stack.setCount(selectedStack.getCount() + stack.getCount() - 99);
                    selectedStack.setCount(99);
                    inventory.setStack(emptySlot, stack);
                }
            } else if (PlayerInventory.isValidHotbarIndex(firstSlotOfSameType)) { // slot in hotbar of same type, sum is valid
                ItemStack sameType = inventory.getStack(firstSlotOfSameType);
                player.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(firstSlotOfSameType));
                inventory.setSelectedSlot(firstSlotOfSameType);
                sameType.setCount(sameType.getCount() + stack.getCount());
            } else if (firstSlotOfSameType != -1) { // slot in inventory of same type, sum is valid
                ItemStack sameType = inventory.getStack(firstSlotOfSameType);
                sameType.setCount(sameType.getCount() + stack.getCount());
                inventory.setStack(firstSlotOfSameType, selectedStack);
                inventory.setStack(selectedSlot, sameType);
            } else if (selectedStack.isEmpty()) {
                inventory.setStack(selectedSlot, stack);
            } else if (PlayerInventory.isValidHotbarIndex(emptySlot)) { // empty slot in hotbar
                player.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(emptySlot));
                inventory.setSelectedSlot(emptySlot);
                inventory.setStack(selectedSlot, stack);
            } else if (emptySlot != -1) { // empty slot in inventory
                inventory.setStack(emptySlot, selectedStack);
                inventory.setStack(selectedSlot, stack);
            } else {
                throw new SimpleCommandExceptionType(Text.literal("Inventory full!")).create();
            }
            inventory.markDirty();
        } else
            context.getSource().sendMessage(Text.of("Player not found."));
        return 0;
    }

    private int firstSlotOfSameType(PlayerInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).getItem() == stack.getItem() &&
            inventory.getStack(i).getCount() + stack.getCount() < 99) {
                return i;
            }
        }
        return -1;
    }
}
