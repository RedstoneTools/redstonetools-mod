package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import java.util.Random;

import static tools.redstone.redstonetools.features.arguments.serializers.IntegerSerializer.integer;

@AutoService(AbstractFeature.class)
@Feature(name = "Signal Strength", description = "Creates a command block with the specified signal strength.", command = "ss")
public class SignalStrengthFeature extends CommandFeature {

    private final String[] zeroSignalStrengthPrompts = {
        "Why would you want this??", "Wtf are you going to use this for?", "What for?",
        "... Ok, if you're sure.", "I'm 99% sure you could just use any other block.",
        "This seems unnecessary.", "Is that a typo?", "Do you just like the glint?",
        "Wow, what a fancy but otherwise useless barrel.", "For decoration?"};

    public static final Argument<Integer> signalStrength = Argument
            .ofType(integer(0, 2147483647))
            .withDefault(15);

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {
        var stack = new ItemStack(Items.COMMAND_BLOCK);

        stack.getOrCreateSubNbt("BlockEntityTag").putInt("SuccessCount", signalStrength.getValue());
        stack.setCustomName(Text.of( "§d" + signalStrength.getValue().toString() ));
        stack.addEnchantment(Enchantment.byRawId(0),0);
        stack.getOrCreateNbt().putBoolean("HideFlags", true);

        source.getPlayer().giveItemStack(stack);

        // the funny
        if (signalStrength.getValue() == 0) {
            return Feedback.success( zeroSignalStrengthPrompts[new Random().nextInt(zeroSignalStrengthPrompts.length)] );
        }

        return Feedback.none();
    }
}
