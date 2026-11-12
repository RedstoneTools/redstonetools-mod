package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
//? if fabric {
/*import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
 *///? }
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import tools.redstone.redstonetools.RedstoneTools;
import net.minecraft.world.InteractionHand;

import static net.minecraft.commands.Commands.literal;

public class ClickContainerFeature extends ToggleableFeature {
	public static final ClickContainerFeature INSTANCE = new ClickContainerFeature();

	protected ClickContainerFeature() {
	}

	private static long lastTime = -1;

	//? if fabric {
	/*static {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (!(player instanceof ServerPlayer serverPlayer)) {
				return InteractionResult.PASS;
			}
			return handleUse(serverPlayer, world, hand, hitResult.getBlockPos())
				? InteractionResult.SUCCESS
				: InteractionResult.PASS;
		});
	}
	*///? }

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

	public static boolean handleUse(ServerPlayer player, Level world, InteractionHand hand, BlockPos pos) {
		if (world == null || world.isClientSide()) {
			return false;
		}
		if (!INSTANCE.isEnabled(player)) {
			return false;
		}

		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty() || stack.getItem() instanceof BlockItem) {
			return false;
		}

		if (world.getGameTime() == lastTime) {
			return false;
		}
		lastTime = world.getGameTime();

		BlockState state = world.getBlockState(pos);

		if (state.is(Blocks.WATER_CAULDRON) || state.is(Blocks.LAVA_CAULDRON) || state.is(Blocks.POWDER_SNOW_CAULDRON)) {
			if (state.hasProperty(BlockStateProperties.LEVEL_CAULDRON)) {
				handleIntLevelProperty(world, pos, state, BlockStateProperties.LEVEL_CAULDRON, Blocks.CAULDRON, player);
				return true;
			}
		}

		if (state.is(Blocks.CAULDRON)) {
			world.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 1));
			return true;
		}

		if (state.is(Blocks.COMPOSTER)) {
			handleIntLevelProperty(world, pos, state, BlockStateProperties.LEVEL_COMPOSTER, Blocks.COMPOSTER, player);
			return true;
		}

		return false;
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection registrationEnvironment) {
		dispatcher.register(literal("clickcontainers").requires(tools.redstone.redstonetools.Commands.getPerm("clickcontainer")).executes(this::toggle));
//		dispatcher.register(literal("cauldron").executes(this::toggle));
	}

	@Override
	public String getName() {
		return "ClickContainers";
	}
}
