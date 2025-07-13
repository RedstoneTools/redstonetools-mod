package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.commands.argumenthelpers.SignalBlockArgumentHelper;
import tools.redstone.redstonetools.utils.SignalBlock;

import java.util.Random;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SignalStrengthBlockFeature extends AbstractFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("ssb")
                .then(argument("signalStrength", IntegerArgumentType.integer())
                .then(argument("block", StringArgumentType.string())
                .executes(context -> new SignalStrengthBlockFeature().execute(context))))));
    }

    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int signalStrength = IntegerArgumentType.getInteger(context, "signalStrength");
        SignalBlock block = SignalBlockArgumentHelper.getSignalBlock(context, "block");
        try {
            ItemStack itemStack = block.getItemStack(signalStrength);
            context.getSource().getPlayer().giveItemStack(itemStack);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new SimpleCommandExceptionType(Text.literal(e.getMessage())).create();
        }

        // who intentionally doesnt put a space between the last / and the message??
        //funny
        if(signalStrength == 0) {
            String[] funny = {
                    "Why would you want this??", "Wtf are you going to use this for?", "What for?",
                    "... Ok, if you're sure.", "I'm 99% sure you could just use any other block.",
                    "This seems unnecessary.", "Is that a typo?", "Do you just like the glint?",
                    "Wow, what a fancy but otherwise useless barrel.", "For decoration?"};
            context.getSource().sendMessage(Text.literal(funny[new Random().nextInt(funny.length)]));
            return 1;
        }

        return 1;
    }

}
