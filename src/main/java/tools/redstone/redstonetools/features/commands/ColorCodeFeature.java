package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.utils.ArgumentUtils;
import tools.redstone.redstonetools.utils.BlockColor;
import tools.redstone.redstonetools.utils.ColoredBlock;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ColorCodeFeature {
	public static final ColorCodeFeature INSTANCE = new ColorCodeFeature();

	protected ColorCodeFeature() {
	}

	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
		dispatcher.register(literal("/colorcode")
			.requires(source -> source.hasPermissionLevel(2))
			.then(argument("color", StringArgumentType.string()).suggests(ArgumentUtils.BLOCK_COLOR_SUGGESTION_PROVIDER)
				.executes(this::execute)
				.then(argument("onlyColor", StringArgumentType.string()).suggests(ArgumentUtils.BLOCK_COLOR_SUGGESTION_PROVIDER)
					.executes(this::execute))));
	}

	public BlockColor color;
	public BlockColor onlyColor;

	private boolean shouldBeColored(World world, BlockVector3 pos, BlockColor onlyColor) {
		var state = world.getBlock(pos);
		var blockId = state.getBlockType().id();

		var coloredBlock = ColoredBlock.fromBlockId(blockId);
		if (coloredBlock == null) return false;

		if (onlyColor == null) return true;

		var blockColor = coloredBlock.color;
		return blockColor == onlyColor;
	}

	private BaseBlock getColoredBlock(World world, BlockVector3 pos, BlockColor color) {
		var state = world.getBlock(pos);
		var blockId = state.getBlockType().id();

		var coloredBlock = ColoredBlock.fromBlockId(blockId);
		if (coloredBlock == null) return state.toBaseBlock();

		var blockType = BlockType.REGISTRY.get(coloredBlock.withColor(color).toBlockId());
		assert blockType != null;

		return blockType.getDefaultState().toBaseBlock();
	}

	protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		color = ArgumentUtils.parseBlockColor(context, "color");
		try {
			onlyColor = ArgumentUtils.parseBlockColor(context, "onlyColor");
		} catch (Exception ignored) {
			onlyColor = null;
		}
		var player = context.getSource().getPlayer();

		var selection = WorldEditUtils.getSelection(player);

		var worldEdit = WorldEdit.getInstance();
		assert player != null;
		var wePlayer = FabricAdapter.adaptPlayer(player);
		var playerSession = worldEdit.getSessionManager().get(wePlayer);

		// for each block in the selection
		final World world = FabricAdapter.adapt(player.world());
		try (EditSession session = worldEdit.newEditSession(world)) {
			// create mask and pattern and execute block set
			int blocksColored = session.replaceBlocks(selection,
				new Mask() {
					@Override
					public boolean test(BlockVector3 vector) {
						return shouldBeColored(world, vector, onlyColor);
					}

					@Nullable
					@Override
					public Mask2D toMask2D() {
						return null;
					}
				},
				new com.sk89q.worldedit.function.pattern.Pattern() {
					@Override
					public BaseBlock applyBlock(BlockVector3 position) {
						return getColoredBlock(world, position, color);
					}
				}
			);

			Operations.complete(session.commit());

			// call remember to allow undo
			playerSession.remember(session);


			context.getSource().sendMessage(Text.literal("Successfully colored %s block(s) %s.".formatted(blocksColored, color)));
		} catch (Exception e) {
			throw new SimpleCommandExceptionType(Text.literal("An error occurred while coloring the block(s).")).create();
		}
		return 1;
	}
}
