package tools.redstone.redstonetools.features.commands;


import com.google.auto.service.AutoService;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.collection.DefaultedList;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.mixin.accessors.PlayerInventoryAccessor;
import tools.redstone.redstonetools.utils.ItemUtils;

@AutoService(AbstractFeature.class)
@Feature(command = "itembind", description = "Allows you to bind command to a specific item", name = "Item Bind")
public class ItemBindFeature extends CommandFeature{

    public static boolean waitingForCommand = false;
    private static ItemStack bindStack = null;


    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {

        ServerPlayerEntity player = source.getPlayer();
        ItemStack mainHandStack = player.getMainHandStack();
        if (mainHandStack == null || mainHandStack.getItem() == Items.AIR) {
            return Feedback.error("You need to be holding an item!");
        }

        bindStack = mainHandStack;
        waitingForCommand = true;


        return Feedback.success("Please run the command you want to bind to this item (" + mainHandStack.getItem().toString()+")");
    }

    public static Feedback addCommand(ClientPlayerEntity player, String command) {
        if (!waitingForCommand) return null;

        if (!doesContainStack(player.getInventory(),bindStack)) {
            bindStack = null;
            waitingForCommand = false;
            return null;
        }

        bindStack.getOrCreateNbt().put("command", NbtString.of(command));
        ItemUtils.addExtraNBTText(bindStack,"Command");

        waitingForCommand = false;

        return Feedback.success("Successfully bound command: '" + command + "' to this item (" + bindStack.getItem().toString() + ")!");
    }

    private static boolean doesContainStack(PlayerInventory inventory, ItemStack itemStack) {
        for (DefaultedList<ItemStack> list : ((PlayerInventoryAccessor) inventory).getCombinedInventory()) {
            for (ItemStack inventoryStack : list) {
                if (inventoryStack == itemStack) return true;
            }
        }
        return false;
    }

}
