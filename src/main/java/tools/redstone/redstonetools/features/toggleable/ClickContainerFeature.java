package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import tools.redstone.redstonetools.RedstoneTools;

import static net.minecraft.commands.Commands.literal;

public class ClickContainerFeature extends ToggleableFeature {
	public static final ClickContainerFeature INSTANCE = new ClickContainerFeature();

	protected ClickContainerFeature() {
	}

	private static long lasttime = -1;

	static {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (world == null) return InteractionResult.PASS;
			if (world.isClientSide()) return InteractionResult.PASS;
			if (!ClickContainerFeature.INSTANCE.isEnabled((ServerPlayer) player)) return InteractionResult.PASS;

			ItemStack stack = player.getItemInHand(hand);
			if (!stack.isEmpty() || stack.getItem() instanceof BlockItem) return InteractionResult.PASS;

			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);

			if (world.getGameTime() == lasttime) {
				return InteractionResult.PASS;
			}
			lasttime = world.getGameTime();

			if (state.is(Blocks.WATER_CAULDRON) || state.is(Blocks.LAVA_CAULDRON) || state.is(Blocks.POWDER_SNOW_CAULDRON)) {
				if (state.hasProperty(BlockStateProperties.LEVEL_CAULDRON)) {
					handleIntLevelProperty(world, pos, state, BlockStateProperties.LEVEL_CAULDRON, Blocks.CAULDRON, (ServerPlayer) player);
					return InteractionResult.SUCCESS;
				}
			}

			if (state.is(Blocks.CAULDRON)) {
				BlockState newState = Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 1);
				world.setBlockAndUpdate(pos, newState);
				return InteractionResult.SUCCESS;
			}

			if (state.is(Blocks.COMPOSTER)) {
				handleIntLevelProperty(world, pos, state, BlockStateProperties.LEVEL_COMPOSTER, Blocks.COMPOSTER, (ServerPlayer) player);
				return InteractionResult.SUCCESS;
			}

			return InteractionResult.PASS;
		});
	}

	public static void handleIntLevelProperty(Level world, BlockPos pos, BlockState state, IntegerProperty prop, Block resetBlock, ServerPlayer player) {
		if (prop == null) return;
		Integer current;
		try {
			current = state.getValue(prop);
		} catch (Exception e) {
			RedstoneTools.LOGGER.error("[ClickContainerFeature] Failed to read property " + prop.getName() + " for block " + state.getBlock() + " at " + pos + ": " + e);
			return;
		}
		if (current == null) return;

		int max = prop.getPossibleValues().stream().max(Integer::compareTo).orElse(current);

		if (current >= max) {
			world.setBlockAndUpdate(pos, resetBlock.defaultBlockState());
			return;
		}

		int next = current + 1;
		world.setBlock(pos, state.setValue(prop, next), 3);

		player.sendSystemMessage(Component.nullToEmpty("§2[ClickContainers] §6Increased level!"));
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection registrationEnvironment) {
		dispatcher.register(literal("clickcontainers").executes(this::toggle));
	}

	@Override
	public String getName() {
		return "ClickContainers";
	}
}
