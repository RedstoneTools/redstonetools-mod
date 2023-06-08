package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.RedstoneUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import java.util.Random;

import static tools.redstone.redstonetools.features.arguments.serializers.IntegerSerializer.integer;

@AutoService(AbstractFeature.class)
@Feature(name = "Signal Strength Barrel", description = "Creates a barrel with the specified signal strength.", command = "ssb")
public class SsBarrelFeature extends CommandFeature {
    private static final int BARREL_CONTAINER_SLOTS = 27;

    public static final Argument<Integer> signalStrength = Argument
            .ofType(integer(0, 15))
            .withDefault(15);

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {
        var stack = new ItemStack(Items.BARREL);

        // {BlockEntityTag:{Items:[{Slot:0,id:redstone,Count:3},{Slot:1,id:redstone,Count:61}]}}
        var items = new NbtList();

        for (int i = 0; i < RedstoneUtils.signalStrengthToNonStackableItemCount(signalStrength.getValue(), BARREL_CONTAINER_SLOTS); i++) {
            var item = new NbtCompound();
            item.putByte("Slot", (byte) i);
            item.putString("id", Registry.ITEM.getId(Items.TOTEM_OF_UNDYING).toString());
            item.putByte("Count", (byte) 1);
            items.add(item);
        }

        stack.getOrCreateSubNbt("BlockEntityTag").put("Items", items);
        stack.setCustomName(Text.of(signalStrength.getValue().toString()));
        stack.addEnchantment(Enchantment.byRawId(0),0);
        stack.getOrCreateNbt().putBoolean("HideFlags", true);

        source.getPlayer().giveItemStack(stack);

        //funny
        if (signalStrength.getValue() == 0)
        {
            String[] funny = {
                    "Why would you want this??", "Wtf are you going to use this for?", "What for?",
                    "... Ok, if you're sure.", "I'm 99% sure you could just use any other block.",
                    "This seems unnecessary.", "Is that a typo?", "Do you just like the glint?",
                    "Wow, what a fancy but otherwise useless barrel.", "For decoration?"};

            return Feedback.success(funny[new Random().nextInt(funny.length)]);
        }

        return Feedback.none();
    }
}
