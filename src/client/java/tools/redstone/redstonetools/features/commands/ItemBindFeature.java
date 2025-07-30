package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;

import java.util.List;
import java.util.Objects;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;
public class ItemBindFeature extends AbstractFeature {
	public static void registerCommand() {
		EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("itembind")
				.executes(context -> ClientFeatureUtils.getFeature(ItemBindFeature.class).execute(context))
                .then(argument("reset", BoolArgumentType.bool())
                .executes(context -> {
                    if (!Objects.requireNonNull(context.getSource().getPlayer()).getGameMode().isCreative()) {
                        throw new SimpleCommandExceptionType(Text.literal("You must be in creative to use this command!")).create();
                    }
                    if (BoolArgumentType.getBool(context, "reset")) {
                        var player = context.getSource().getPlayer();
                        if (player != null) {
                            var mainStack = player.getMainHandStack();
                            mainStack.remove(RedstoneTools.COMMAND_COMPONENT);
                            mainStack.set(DataComponentTypes.LORE, mainStack.getItem().getDefaultStack().get(DataComponentTypes.LORE));
                            context.getSource().getPlayer().sendMessage(Text.of("Successfully removed command from the item in your main hand"), false);
                        } else {
                            context.getSource().getPlayer().sendMessage(Text.of("You need to be holding an item in your main hand!"), false);
                        }
                        return 1;
                    } else {
                        return ClientFeatureUtils.getFeature(ItemBindFeature.class).execute(context);
                    }
                }))));
	}
    public static boolean waitingForCommand = false;
    private static ClientPlayerEntity player;

    protected int execute(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        if (!Objects.requireNonNull(context.getSource().getPlayer()).getGameMode().isCreative()) {
            throw new SimpleCommandExceptionType(Text.literal("You must be in creative to use this command!")).create();
        }
		FabricClientCommandSource source = context.getSource();
        player = source.getPlayer();
        waitingForCommand = true;

		source.getPlayer().sendMessage(Text.literal("Please run any command and hold the item you want the command be bound to in your main hand"), false);
        return 1;
    }

    public static void addCommand(String command) throws CommandSyntaxException {
        if (!waitingForCommand || MinecraftClient.getInstance().getServer() == null) return;

        if (player == null) {
            waitingForCommand = false;
            return;
        }

        ItemStack mainHandStack = player.getMainHandStack().copy();
        if (mainHandStack == null || mainHandStack.getItem() == Items.AIR) {
            if (player.getOffHandStack() == null)
                 throw new SimpleCommandExceptionType(Text.literal("You need to be holding an item!")).create();
            else throw new SimpleCommandExceptionType(Text.literal("You need to be holding an item in your main hand!")).create();
        }

        MinecraftClient client = MinecraftClient.getInstance();
	    assert client.world != null;
        mainHandStack.set(RedstoneTools.COMMAND_COMPONENT, command);
        //                                                                                                   `command` here doesn't start with a / for some
        //                                                                                                   reason, so we add it ourselves so its more clear
        mainHandStack.set(DataComponentTypes.LORE, new LoreComponent(List.of(Text.of("Has command: /" + command))));

        waitingForCommand = false;

        player.getInventory().setStack(player.getInventory().getEmptySlot(), mainHandStack);
		player.sendMessage(Text.literal("Successfully bound command: '%s' to this item (%s)!".formatted(command, mainHandStack.getItem().getName().getString())), false);

    }
}
