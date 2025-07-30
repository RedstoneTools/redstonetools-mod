package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;

import java.util.List;
import java.util.Objects;

import static net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ItemBindFeature extends AbstractFeature {
    public static void registerCommand() {
        EVENT.register((dispatcher, registryAccess, enviroment) -> dispatcher.register(literal("itembind")
                .executes(context -> FeatureUtils.getFeature(ItemBindFeature.class).execute(context))
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
                                return FeatureUtils.getFeature(ItemBindFeature.class).execute(context);
                            }
                        }))));
    }
    public static boolean waitingForCommand = false;
    private static ServerPlayerEntity player;

    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
//        if (!Objects.requireNonNull(context.getSource().getPlayer()).getGameMode().isCreative()) {
//            throw new SimpleCommandExceptionType(Text.literal("You must be in creative to use this command!")).create();
//        }
        ServerCommandSource source = context.getSource();
        player = source.getPlayer();
        waitingForCommand = true;

        player.sendMessage(Text.literal("Please run any command and hold the item you want the command be bound to in your main hand"), false);
        return 1;
    }

    public static void addCommand(String command) {
        if (!waitingForCommand) return;

        if (player == null) {
            waitingForCommand = false;
            return;
        }

        ItemStack mainHandStack = player.getMainHandStack().copy();
        if (mainHandStack == null || mainHandStack.getItem() == Items.AIR) {
            if (player.getOffHandStack() == null)
                player.sendMessage(Text.literal("You need to be holding an item!"));
            else player.sendMessage(Text.literal("You need to be holding an item in your main hand!"));
        }

        mainHandStack.set(RedstoneTools.COMMAND_COMPONENT, command);
        //                                                                                                   `command` here doesn't start with a / for some
        //                                                                                                   reason, so we add it ourselves so its more clear
        mainHandStack.set(DataComponentTypes.LORE, new LoreComponent(List.of(Text.of("Has command: /" + command))));

        waitingForCommand = false;

        player.getInventory().setStack(player.getInventory().getEmptySlot(), mainHandStack);
        player.sendMessage(Text.literal("Successfully bound command: '%s' to this item (%s)!".formatted(command, mainHandStack.getItem().getName().getString())), false);

    }
}

