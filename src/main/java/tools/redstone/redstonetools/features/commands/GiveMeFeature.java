package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
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
//        if (!Objects.requireNonNull(context.getSource().getPlayer()).getGameMode().isCreative()) {
//            throw new SimpleCommandExceptionType(Text.literal("You must be in creative to use this command!")).create();
//        }
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack stack = itemArgument.createStack(count, false);
        if (player != null) {
            stack.setCount(count);
            if (player.getInventory().getEmptySlot() != -1) {
                player.getInventory().setStack(player.getInventory().getEmptySlot(), stack);
//                player.getInventory().markDirty();
                context.getSource().sendMessage(Text.of("Gave %s of %s to %s".formatted(count, itemArgument.getItem().getName().getString(), player.getName().getString())));
            } else {
                context.getSource().sendMessage(Text.of("Inventory full!"));
            }
        } else
            context.getSource().sendMessage(Text.of("Player not found."));
        return 0;
    }
}
