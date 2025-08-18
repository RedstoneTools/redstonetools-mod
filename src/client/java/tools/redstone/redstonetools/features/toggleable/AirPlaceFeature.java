package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Colors;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.mixin.features.WorldRendererInvoker;
import tools.redstone.redstonetools.utils.BlockUtils;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;
import tools.redstone.redstonetools.utils.ItemUtils;
import tools.redstone.redstonetools.utils.RaycastUtils;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class AirPlaceFeature extends ClientToggleableFeature {
	public static void registerCommand() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("airplace")
				.executes(context -> ClientFeatureUtils.getFeature(AirPlaceFeature.class).toggle(context))
					.then(argument("showOutline", BoolArgumentType.bool())
						.executes(context -> ClientFeatureUtils.getFeature(AirPlaceFeature.class).toggle(context)))));
	}

	@Override
	public int toggle(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
		boolean hasDifferentArguments = true;
		boolean o;
		try {
			o = BoolArgumentType.getBool(context, "showOutline");
			if (o == AirPlaceFeature.showOutline) {
				return super.toggle(context.getSource());
			}
		} catch (IllegalArgumentException e) {
			hasDifferentArguments = false;
			o = true;
		}
		AirPlaceFeature.showOutline = o;
		if (hasDifferentArguments && isEnabled()) {
			return 1;
		}
		return super.toggle(context.getSource());
	}


	public static float reach;
	public static boolean showOutline;

	public static boolean canAirPlace(PlayerEntity player) {
		if (player.isSpectator())
			return false;
		ItemStack itemStack = ItemUtils.getMainItem(player);

		// empty slot
		if (itemStack == null || itemStack.getItem() == Items.AIR)
			return false;

		// itembind in hand
		if (itemStack.contains(RedstoneTools.COMMAND_COMPONENT)) return false;

		// TODO: shouldn't offhand also be checked?
		// rocket boost for elytra
		return itemStack.getItem() != Items.FIREWORK_ROCKET ||
				player.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA ||
				!player.isGliding();
	}

	public static BlockHitResult findAirPlaceBlockHit(PlayerEntity playerEntity) {
		reach = (float) playerEntity.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE).getBaseValue();
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

			BlockHitResult hitResult = findAirPlaceBlockHit(client.player);
			BlockPos blockPos = hitResult.getBlockPos();

			BlockState blockState = ItemUtils.getUseState(client.player,
					ItemUtils.getMainItem(client.player),
					reach);
			if (AutoRotateClient.isEnabled) {
				blockState = BlockUtils.rotate(blockState);
			}
			if (blockState == null)
				return true;

			/* render block outline */
			Camera camera = client.gameRenderer.getCamera();
			Vec3d camPos = camera.getPos();

			VertexConsumer consumer = context.consumers().getBuffer(RenderLayer.getLines());

			((WorldRendererInvoker) (context.worldRenderer())).invokeDrawBlockOutline(
					context.matrixStack(),
					consumer,
					client.player,
					camPos.x, camPos.y, camPos.z,
					blockPos,
					blockState,
					Colors.BLACK
			);

			return true;
		});
	}

}
