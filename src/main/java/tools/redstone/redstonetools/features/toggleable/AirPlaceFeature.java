package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Colors;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tools.redstone.redstonetools.mixin.features.WorldRendererInvoker;
import tools.redstone.redstonetools.utils.FeatureUtils;
import tools.redstone.redstonetools.utils.ItemUtils;
import tools.redstone.redstonetools.utils.RaycastUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AirPlaceFeature extends ToggleableFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("airplace")
                .executes(context -> FeatureUtils.getFeature(AirPlaceFeature.class).toggle(context))
                .then(argument("reach", FloatArgumentType.floatArg(3.0f))
                .executes(context -> FeatureUtils.getFeature(AirPlaceFeature.class).toggle(context))
                .then(argument("showOutline", BoolArgumentType.bool())
                .executes(context -> FeatureUtils.getFeature(AirPlaceFeature.class).toggle(context))))));
    }

    @Override
    public int toggle(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        float r;
        boolean o;
        try {
            r = FloatArgumentType.getFloat(context, "reach");
        } catch (IllegalArgumentException e) {
            r = 5.0f;
        }
        try {
            o = BoolArgumentType.getBool(context, "showOutline");
        } catch (IllegalArgumentException e) {
            o = true;
        }
        AirPlaceFeature.showOutline = o;
        AirPlaceFeature.reach = r;
        return toggle(context.getSource());
    }


    public static float reach;
    public static boolean showOutline;

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
    {
        // register ghost block renderer
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, blockOutlineContext) -> {
            if (!isEnabled())
                return true;
            if (!showOutline)
                return true;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.interactionManager == null)
                return true;
            if (blockOutlineContext == null)
                return true;
            if (blockOutlineContext.getType() != HitResult.Type.MISS)
                return true;

            if (!canAirPlace(client.player))
                return true;

            HitResult hitResult = findAirPlaceBlockHit(client.player);
            BlockPos blockPos = new BlockPos((int)hitResult.getPos().x, (int)hitResult.getPos().y, (int)hitResult.getPos().z);

            BlockState blockState = ItemUtils.getUseState(client.player,
                    ItemUtils.getMainItem(client.player),
                    reach);
            if (blockState == null)
                return true;

            /* render block outline */
            Camera camera = client.gameRenderer.getCamera();
            Vec3d camPos = camera.getPos();

            try {
                VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getLines());

                ((WorldRendererInvoker)(context.worldRenderer())).invokeDrawBlockOutline(
                        context.matrixStack(),
                        consumer,
                        client.player,
                        camPos.x, camPos.y, camPos.z,
                        blockPos,
                        blockState,
                        Colors.BLACK
                );
            } catch (Throwable t) {
                throw new IllegalStateException(t);
            }

            return true;
        });
    }

}
