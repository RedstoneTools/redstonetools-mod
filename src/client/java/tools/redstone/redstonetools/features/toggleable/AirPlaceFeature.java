package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//? if <1.21.10 {
/*import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.render.state.OutlineRenderState;
//?}
//? if <=1.21.10 {
/*import net.minecraft.client.render.RenderLayer;
*///?} else {
import net.minecraft.client.render.RenderLayers;
//?}
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Colors;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tools.redstone.redstonetools.ClientCommands;
import tools.redstone.redstonetools.config.General;
import tools.redstone.redstonetools.config.Toggles;
import tools.redstone.redstonetools.mixin.features.WorldRendererInvoker;
import tools.redstone.redstonetools.utils.ItemUtils;
import tools.redstone.redstonetools.utils.RaycastUtils;

import java.util.function.BiConsumer;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class AirPlaceFeature extends ClientToggleableFeature {
	public static final AirPlaceFeature INSTANCE = new AirPlaceFeature();

	protected AirPlaceFeature() {
		super(Toggles.AIRPLACE);
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(literal("airplace")
			.requires(ClientCommands.PERMISSION_LEVEL_2)
			.executes(this::toggle));
	}

	public static float reach;

	public static boolean canAirPlace(PlayerEntity player) {
		if (player.isSpectator())
			return false;
		ItemStack itemStack = player.getMainHandStack();

		// empty slot
		if (itemStack == null || itemStack.getItem() == Items.AIR)
			return false;

		// itembind in hand
		if (ItemUtils.containsCommand(itemStack))
			return false;

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

		BiConsumer<WorldRenderContext, HitResult> listener = (context, crosshairTarget) -> {
			if (!isEnabled())
				return;
			if (!General.AIRPLACE_SHOW_OUTLINE.getBooleanValue())
				return;

			MinecraftClient client = MinecraftClient.getInstance();
			if (client.player == null || client.interactionManager == null)
				return;
			if (crosshairTarget == null)
				return;
			if (crosshairTarget.getType() != HitResult.Type.MISS)
				return;

			if (!canAirPlace(client.player))
				return;

			BlockHitResult hitResult = findAirPlaceBlockHit(client.player);
			BlockPos blockPos = hitResult.getBlockPos();

			if (!client.world.getBlockState(blockPos).isAir())
				return;

			if (blockPos.getY() > 319 || blockPos.getY() < -64)
				return;

			BlockState blockState = ItemUtils.getUseState(client.player, client.player.getMainHandStack(), reach);
			if (AutoRotateClient.isEnabled.getBooleanValue()) {
				blockState = blockState.rotate(BlockRotation.CLOCKWISE_180);
			}
			if (blockState == null)
				return;

			/* render block outline */
			Camera camera = client.gameRenderer.getCamera();
			Vec3d camPos = camera
				//? if <=1.21.10 {
				/*.getPos();
			    *///?} else {
			    .getCameraPos();
			    //?}

			VertexConsumer consumer = context.consumers().getBuffer(
				//? if <=1.21.10 {
				/*RenderLayer.getLines()
				*///?} else {
				RenderLayers.lines()
				//?}
			);

			//? if <1.21.10 {
			/*((WorldRendererInvoker) context.worldRenderer()).invokeDrawBlockOutline(
				context.matrixStack(),
				consumer,
				client.player,
				camPos.x, camPos.y, camPos.z,
				blockPos,
				blockState,
				Colors.BLACK
			);
			*///?} else {
			((WorldRendererInvoker) context.worldRenderer()).invokeDrawBlockOutline(
				context.matrices(),
				consumer,
				camPos.x, camPos.y, camPos.z,
				new OutlineRenderState(
					blockPos,
					false,
					false,
					blockState.getOutlineShape(client.world, blockPos)
				),
				Colors.BLACK/*? if >=1.21.11 {*/, client.getWindow().getMinimumLineWidth()/*?}*/
			);
			//?}
		};

		//? if <1.21.10 {
		/*WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, hitResult) -> {
			listener.accept(context, hitResult);
			return true;
		});
		*///?} else {
		WorldRenderEvents.END_MAIN.register(context -> listener.accept(context, MinecraftClient.getInstance().crosshairTarget));
		//?}
	}
}
