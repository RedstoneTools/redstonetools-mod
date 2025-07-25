package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.hit.BlockHitResult;
import tools.redstone.redstonetools.utils.ItemUtils;
import tools.redstone.redstonetools.utils.RaycastUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AirPlaceFeature extends ToggleableFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("airplace")
                .then(argument("reach", FloatArgumentType.floatArg(3.0f))
                .executes(context -> new AirPlaceFeature().toggle(context)))));
    }

    @Override
    public int toggle(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        AirPlaceFeature.reach = FloatArgumentType.getFloat(context, "reach");
        return toggle(context.getSource());
    }


    public static float reach;

    public static boolean canAirPlace(PlayerEntity player) {
        ItemStack itemStack = ItemUtils.getMainItem(player);

        // empty slot
        if (itemStack == null || itemStack.getItem() == Items.AIR)
            return false;

        // TODO: shouldn't offhand also be checked?
        // rocket boost for elytra
        return itemStack.getItem() != Items.FIREWORK_ROCKET ||
                player.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA ||
                !player.isGliding();
    }

    public static BlockHitResult findAirPlaceBlockHit(PlayerEntity playerEntity) {
        var hit = RaycastUtils.rayCastFromEye(playerEntity, reach);
        return new BlockHitResult(hit.getPos(), hit.getSide(), hit.getBlockPos(), false);
    }
}
