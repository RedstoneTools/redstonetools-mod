package tools.redstone.redstonetools.features.toggleable;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.literal;

public class ClickContainerFeature extends ToggleableFeature {
	public static final ClickContainerFeature INSTANCE = new ClickContainerFeature();

	protected ClickContainerFeature() {
	}

	private static long lasttime = -1;

	static {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (world == null) return ActionResult.PASS;
			if (world.isClient()) return ActionResult.PASS;
			if (!ClickContainerFeature.INSTANCE.isEnabled((ServerPlayerEntity) player)) return ActionResult.PASS;

			ItemStack stack = player.getStackInHand(hand);
			if (!stack.isEmpty() ||
			stack.getItem() instanceof BlockItem) return ActionResult.PASS;

			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);

			if (world.getTime() == lasttime) {
				return ActionResult.PASS;
			}
			lasttime = world.getTime();

			if (state.isOf(Blocks.WATER_CAULDRON) || state.isOf(Blocks.LAVA_CAULDRON) || state.isOf(Blocks.POWDER_SNOW_CAULDRON)) {
				if (state.contains(Properties.LEVEL_3)) {
					handleIntLevelProperty(world, pos, state, Properties.LEVEL_3, Blocks.CAULDRON, (ServerPlayerEntity) player);
					return ActionResult.SUCCESS;
				}
			}

			if (state.isOf(Blocks.CAULDRON)) {
				BlockState newState = Blocks.WATER_CAULDRON.getDefaultState().with(Properties.LEVEL_3, 1);
				world.setBlockState(pos, newState);
				return ActionResult.SUCCESS;
			}

			if (state.isOf(Blocks.COMPOSTER)) {
				handleIntLevelProperty(world, pos, state, Properties.LEVEL_8, Blocks.COMPOSTER, (ServerPlayerEntity) player);
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		});
	}

	public static void handleIntLevelProperty(World world, BlockPos pos, BlockState state, IntProperty prop, net.minecraft.block.Block resetBlock, ServerPlayerEntity player) {
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

		player.sendMessage(Text.of("§2[ClickContainers] §6Increased level!"));
	}

	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
			dispatcher.register(literal("clickcontainers").executes(this::toggle));
	}

	@Override
	public String getName() {
		return "ClickContainers";
	}
}
