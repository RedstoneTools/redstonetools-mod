package tools.redstone.redstonetools.features.toggleable;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//? if <1.21.10 {
/*import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
 *///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
//?}
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
//? if <=1.21.10  {
/*import net.minecraft.client.renderer.RenderType;
*///? } else {
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
//? }
//? if =1.21.10
//import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
		dispatcher.register(literal("airplace")
			.requires(ClientCommands.PERMISSION_LEVEL_2)
			.executes(this::toggle));
	}

	public static float reach;

	public static boolean canAirPlace(Player player) {
		if (player.isSpectator())
			return false;
		ItemStack itemStack = player.getMainHandItem();

		// empty slot
		if (itemStack == null || itemStack.getItem() == Items.AIR)
			return false;

		// itembind in hand
		if (ItemUtils.containsCommand(itemStack))
			return false;

		// TODO: shouldn't offhand also be checked?
		// rocket boost for elytra
		return itemStack.getItem() != Items.FIREWORK_ROCKET ||
				player.getItemBySlot(EquipmentSlot.CHEST).getItem() != Items.ELYTRA ||
				!player.isFallFlying();
	}

	public static BlockHitResult findAirPlaceBlockHit(Player playerEntity) {
		reach = (float) playerEntity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getBaseValue();
		var hit = RaycastUtils.rayCastFromEye(playerEntity, reach);
		return new BlockHitResult(hit.getLocation(), hit.getDirection(), hit.getBlockPos(), false);
	}

	{

		BiConsumer<WorldRenderContext, HitResult> listener = (context, crosshairTarget) -> {
			if (!isEnabled())
				return;
			if (!General.AIRPLACE_SHOW_OUTLINE.getBooleanValue())
				return;

			Minecraft client = Minecraft.getInstance();
			if (client.player == null || client.gameMode == null)
				return;
			if (crosshairTarget == null)
				return;
			if (crosshairTarget.getType() != HitResult.Type.MISS)
				return;

			if (!canAirPlace(client.player))
				return;

			BlockHitResult hitResult = findAirPlaceBlockHit(client.player);
			BlockPos blockPos = hitResult.getBlockPos();

			if (!client.level.getBlockState(blockPos).isAir())
				return;

			if (blockPos.getY() > 319 || blockPos.getY() < -64)
				return;

			BlockState blockState = ItemUtils.getUseState(client.player, client.player.getMainHandItem(), reach);
			if (AutoRotateClient.isEnabled.getBooleanValue()) {
				blockState = blockState.rotate(Rotation.CLOCKWISE_180);
			}
			if (blockState == null)
				return;

			/* render block outline */
			Camera camera = client.gameRenderer.getMainCamera();
			Vec3 camPos = camera
				//? if <=1.21.5 {
				/*.getPosition();
				*///? } else if <=1.21.10 {
				/*.position();
			    *///?} else {
			    .position();
			    //?}

			VertexConsumer consumer = context.consumers().getBuffer(
				//? if <=1.21.10 {
				/*RenderType.lines()
				*///?} else {
				RenderTypes.lines()
				//?}
			);

			//? if <1.21.10 {
			/*((WorldRendererInvoker) context.worldRenderer()).invokeRenderHitOutline(
				context.matrixStack(),
				consumer,
				client.player,
				camPos.x, camPos.y, camPos.z,
				blockPos,
				blockState,
				CommonColors.BLACK
			);
			*///?} else {
			((WorldRendererInvoker) context.worldRenderer()).invokeRenderHitOutline(
				context.matrices(),
				consumer,
				camPos.x, camPos.y, camPos.z,
				new BlockOutlineRenderState(
					blockPos,
					false,
					false,
					blockState.getShape(client.level, blockPos)
				),
				CommonColors.BLACK/*? if >=1.21.11 {*/, client.getWindow().getAppropriateLineWidth()/*?}*/
			);
			//?}
		};

		//? if <1.21.10 {
		/*WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, hitResult) -> {
			listener.accept(context, hitResult);
			return true;
		});
		*///?} else {
		WorldRenderEvents.END_MAIN.register(context -> listener.accept(context, Minecraft.getInstance().hitResult));
		//?}
	}
}
