package tools.redstone.redstonetools.features.commands;


import com.google.auto.service.AutoService;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.feedback.Feedback;
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
        if (!player.getInventory().contains(bindStack)) {
            bindStack = null;
            waitingForCommand = false;
            return null;
        }

        bindStack.getOrCreateNbt().put("command", NbtString.of(command));
        ItemUtils.addExtraNBTText(bindStack,"Command");

        waitingForCommand = false;

        return Feedback.success("Successfully bound command: '" + command + "' to this item (" + bindStack.getItem().toString() + ")!");
    }

}
