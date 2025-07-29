package tools.redstone.redstonetools.features.commands;


import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;

import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ItemBindFeature extends AbstractFeature {
	public static void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("itembind")
				.executes(context -> FeatureUtils.getFeature(ItemBindFeature.class).execute(context))
                .then(argument("reset", BoolArgumentType.bool())
                .executes(context -> {
                    if (BoolArgumentType.getBool(context, "reset")) {
                        var player = context.getSource().getPlayer();
                        if (player != null) {
                            var mainStack = player.getMainHandStack();
                            mainStack.remove(RedstoneToolsClient.COMMAND_COMPONENT);
                            mainStack.set(DataComponentTypes.LORE, mainStack.getItem().getDefaultStack().get(DataComponentTypes.LORE));
                            context.getSource().sendMessage(Text.of("Successfully removed command from the item in your main hand"));
                        } else {
                            context.getSource().sendMessage(Text.of("You need to be holding an item in your main hand!"));
                        }
                        return 1;
                    } else {
                        return FeatureUtils.getFeature(ItemBindFeature.class).execute(context);
                    }
                }))));
	}
    public static boolean waitingForCommand = false;
    private static ServerPlayerEntity player;

    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
        player = source.getPlayer();
        waitingForCommand = true;

		source.sendMessage(Text.literal("Please run any command and hold the item you want the command be bound to in your main hand"));
        return 1;
    }

    public static void addCommand(String command) throws CommandSyntaxException {
        if (!waitingForCommand || MinecraftClient.getInstance().getServer() == null) return;

        if (player == null || MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid()) != player) {
            waitingForCommand = false;
            return;
        }

        ItemStack mainHandStack = player.getMainHandStack();
        if (mainHandStack == null || mainHandStack.getItem() == Items.AIR) {
            if (player.getOffHandStack() == null)
                 throw new SimpleCommandExceptionType(Text.literal("You need to be holding an item!")).create();
            else throw new SimpleCommandExceptionType(Text.literal("You need to be holding an item in your main hand!")).create();
        }

        MinecraftClient client = MinecraftClient.getInstance();
	    assert client.world != null;
        mainHandStack.set(RedstoneToolsClient.COMMAND_COMPONENT, command);
        //                                                                                                   `command` here doesn't start with a / for some
        //                                                                                                   reason, so we add it ourselves so its more clear
        mainHandStack.set(DataComponentTypes.LORE, new LoreComponent(List.of(Text.of("Has command: /" + command))));

        waitingForCommand = false;

		player.sendMessage(Text.literal("Successfully bound command: '%s' to this item (%s)!".formatted(command, mainHandStack.getItem().getName().getString())));

    }
}
