package tools.redstone.redstonetools.features.toggleable;

import com.google.auto.service.AutoService;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Colors;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.mixin.accessors.WorldRendererAccessor;
import tools.redstone.redstonetools.utils.ItemUtils;
import tools.redstone.redstonetools.utils.RaycastUtils;

import static tools.redstone.redstonetools.features.arguments.serializers.BoolSerializer.bool;
import static tools.redstone.redstonetools.features.arguments.serializers.FloatSerializer.floatArg;


@AutoService(AbstractFeature.class)
@Feature(name = "Air Place", description = "Allows you to place blocks in the air.", command = "airplace")
public class AirPlaceFeature extends ToggleableFeature {

    public static boolean canAirPlace(PlayerEntity player) {
        ItemStack itemStack = ItemUtils.getMainItem(player);

        // empty slot
        if (itemStack == null || itemStack.getItem() == Items.AIR)
            return false;

        // rocket boost for elytra
        if (itemStack.getItem() == Items.FIREWORK_ROCKET &&
                player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA &&
                player.isGliding())
            return false;

        return true;
    }

    public static HitResult findAirPlacePosition(MinecraftClient client) {
        if (client.player == null)
            return null;
        ClientPlayerEntity player = client.player;

        float reach = AirPlaceFeature.reach.getValue();
        return player.raycast(reach, 0, false);
    }

    public static BlockHitResult findAirPlaceBlockHit(PlayerEntity playerEntity) {
        var hit = RaycastUtils.rayCastFromEye(playerEntity, reach.getValue());
        return new BlockHitResult(hit.getPos(), hit.getSide(), hit.getBlockPos(), false);
    }

    public static final Argument<Float> reach = Argument
            .ofType(floatArg(3.0f))
            .withDefault(5.0f);

    public static final Argument<Boolean> showOutline = Argument
            .ofType(bool())
            .withDefault(true);

    {
        // register ghost block renderer
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, blockOutlineContext) -> {
            if (!isEnabled())
                return true;
            if (showOutline.getValue() != Boolean.TRUE)
                return true;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.interactionManager == null)
                return true;
            if (blockOutlineContext.getType() != HitResult.Type.MISS)
                return true;

            if (!canAirPlace(client.player))
                return true;

            HitResult hitResult = findAirPlacePosition(client);
            if (hitResult == null)
                return true;
            Vec3d pos = hitResult.getPos();
            // WHY is it offset by 1 on the x-axis???
            BlockPos blockPos = new BlockPos((int) pos.x - 1, (int) pos.y, (int) pos.z);

            BlockState blockState = ItemUtils.getUseState(client.player,
                    ItemUtils.getMainItem(client.player),
                    reach.getValue());
            if (blockState == null)
                return true;

            /* render block outline */
            Camera camera = client.gameRenderer.getCamera();
            Vec3d camPos = camera.getPos();

            try {
                VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getLines());

                ((WorldRendererAccessor)context.worldRenderer()).invokeDrawBlockOutline(
                        context.matrixStack(),
                        consumer,
                        client.player,
                        camPos.x,
                        camPos.y,
                        camPos.z,
                        blockPos,
                        blockState,
                        ColorHelper.withAlpha(102, Colors.BLACK)
                );
            } catch (Throwable t) {
                throw new IllegalStateException(t);
            }

            return true;
        });
    }

}
