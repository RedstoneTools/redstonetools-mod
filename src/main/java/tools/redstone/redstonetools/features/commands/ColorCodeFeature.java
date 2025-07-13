package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.commands.argumenthelpers.BlockColorArgumentHelper;
import tools.redstone.redstonetools.utils.BlockColor;
import tools.redstone.redstonetools.utils.ColoredBlock;
import tools.redstone.redstonetools.utils.WorldEditUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ColorCodeFeature extends AbstractFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("colorcode")
                .then(argument("color", StringArgumentType.string())
                .then(argument("onlyColor", StringArgumentType.string()))
                .executes(ColorCodeFeature::execute))));
    }
    public static BlockColor color;
    public static BlockColor onlyColor;

    private static boolean shouldBeColored(World world, BlockVector3 pos, BlockColor onlyColor) {
        var state = world.getBlock(pos);
        var blockId = state.getBlockType().id();

        var coloredBlock = ColoredBlock.fromBlockId(blockId);
        if (coloredBlock == null) return false;

        if (onlyColor == null) return true;

        var blockColor = coloredBlock.color;
        return blockColor == onlyColor;
    }

    private static BaseBlock getColoredBlock(World world, BlockVector3 pos, BlockColor color) {
        var state = world.getBlock(pos);
        var blockId = state.getBlockType().id();

        var coloredBlock = ColoredBlock.fromBlockId(blockId);
        if (coloredBlock == null) return state.toBaseBlock();

        var blockType = BlockType.REGISTRY.get(coloredBlock.withColor(color).toBlockId());
        assert blockType != null;

        return blockType.getDefaultState().toBaseBlock();
    }

    protected static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ColorCodeFeature.color = BlockColorArgumentHelper.getBlockColor(context, "color");
        var player = context.getSource().getPlayer();

        var selection = WorldEditUtils.getSelection(player);

        var worldEdit = WorldEdit.getInstance();
	    assert player != null;
	    var wePlayer = FabricAdapter.adaptPlayer(player);
        var playerSession = worldEdit.getSessionManager().get(wePlayer);

        // for each block in the selection
        final World world = FabricAdapter.adapt(player.getWorld());
        try (EditSession session = worldEdit.newEditSession(FabricAdapter.adapt(player.getWorld()))) {
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
