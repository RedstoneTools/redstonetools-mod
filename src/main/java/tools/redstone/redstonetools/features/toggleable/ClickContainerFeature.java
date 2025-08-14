package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tools.redstone.redstonetools.packets.SetFeatureEnabledS2CPayload;
import tools.redstone.redstonetools.utils.FeatureUtils;

import static net.minecraft.server.command.CommandManager.literal;

public class ClickContainerFeature extends ToggleableFeature {
	private static long lasttime = -1;
	static {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (world == null) return ActionResult.PASS;
			if(world.isClient) return ActionResult.PASS;
			if (!FeatureUtils.getFeature(ClickContainerFeature.class).isEnabled((ServerPlayerEntity) player)) return ActionResult.PASS;
			if (!player.getStackInHand(hand).isEmpty()) return ActionResult.PASS;

			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);

			if(world.getTime() == lasttime) {
				System.out.println("time = lasttime");
				return ActionResult.PASS;
			}
			lasttime = world.getTime();

			if (state.isOf(Blocks.WATER_CAULDRON) || state.isOf(Blocks.LAVA_CAULDRON) || state.isOf(Blocks.POWDER_SNOW_CAULDRON)) {
				if (state.contains(Properties.LEVEL_3)) {
					handleIntLevelProperty(world, pos, state, Properties.LEVEL_3, Blocks.CAULDRON);
					return ActionResult.SUCCESS;
				}
			}

			if (state.isOf(Blocks.CAULDRON)) {
				BlockState newState = Blocks.WATER_CAULDRON.getDefaultState().with(Properties.LEVEL_3, 1);
				world.setBlockState(pos, newState);
				return ActionResult.SUCCESS;
			}

			if (state.isOf(Blocks.COMPOSTER)) {
				handleIntLevelProperty(world, pos, state, Properties.LEVEL_8, Blocks.COMPOSTER);
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		});
	}

	public static void handleIntLevelProperty(World world, BlockPos pos, BlockState state, IntProperty prop, net.minecraft.block.Block resetBlock) {
		if (prop == null) return;
		Integer current;
		try {
			current = state.get(prop);
		} catch (Exception e) {
			System.err.println("[ClickContainerFeature] Failed to read property " + prop.getName() + " for block " + state.getBlock() + " at " + pos + ": " + e);
			return;
		}
		if (current == null) return;

		int max = prop.getValues().stream().max(Integer::compareTo).orElse(current);

		if (current >= max) {
			world.setBlockState(pos, resetBlock.getDefaultState());
			return;
		}

		int next = current + 1;
		world.setBlockState(pos, state.with(prop, next), 3);
	}

	public static void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("clickcontainers")
				.executes(context -> FeatureUtils.getFeature(ClickContainerFeature.class).execute(context))));
	}

	private int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var payload = new SetFeatureEnabledS2CPayload("ClickContainer" + (this.isEnabled(context.getSource().getPlayerOrThrow()) ? "1" : "0"));
		ServerPlayNetworking.send(context.getSource().getPlayerOrThrow(), payload);
		return this.toggle(context);
	}
}
