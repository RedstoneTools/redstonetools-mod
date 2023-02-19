package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.options.Argument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.fabric.FabricPlayer;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import static com.domain.redstonetools.features.arguments.BlockColorArgumentType.blockColor;
import static com.mojang.brigadier.arguments.BoolArgumentType.bool;

@Feature(name = "Color Code", description = "Color codes all colorable blocks in your WorldEdit selection.", command = "/colorcode")
public class ColorCodeFeature extends CommandFeature {
    private static final java.util.regex.Pattern MATCH_TARGET_PATH_PATTERN = java.util.regex.Pattern.compile(
    "(_wool$)|(_concrete$)|(_stained_glass$)|(_terracotta$)|(_concrete_powder$)|(_glazed_terracotta$)"
    );

    public static final Argument<String> color = Argument
            .ofType(blockColor());
    public static final Argument<Boolean> onlyWhite = Argument
            .ofType(bool())
            .withDefault(false);

    private boolean shouldBeColored(World world, BlockVector3 pos, boolean onlyWhite) {
        BlockState state = world.getBlock(pos);
        if (state == null)
            return false;

        var blockColor = getBlockColor(state);
        if (blockColor == null)
            return false;

        if (onlyWhite && !blockColor.equals("white"))  // TODO: Creating an enum for colors would be less error prone
            return false;

        return true;
    }

    private int getColorEndIndex(String blockId) {
        var matcher = MATCH_TARGET_PATH_PATTERN.matcher(blockId);
        if (!matcher.find())
            return -1;

        return matcher.start();
    }

    private String getBlockColor(BlockState state) {
        var blockId = state.getBlockType().getId();

        var colorEndIndex = getColorEndIndex(blockId);

        if (colorEndIndex == -1)
            return null;

        return blockId.substring(0, colorEndIndex);
    }

    private String getColorlessBlockId(BlockState state) {
        var blockId = state.getBlockType().getId();

        var colorEndIndex = getColorEndIndex(blockId);

        if (colorEndIndex == -1)
            return null;

        return blockId.substring(colorEndIndex);
    }

    private BaseBlock setBlockColor(World world, BlockVector3 pos, String color) {
        BlockState state = world.getBlock(pos);

        var colorlessBlockId = getColorlessBlockId(state);
        var coloredBlockId = "minecraft:" + color + colorlessBlockId;

        BlockType blockType = BlockType.REGISTRY.get(coloredBlockId);
        if (blockType == null)
            return state.toBaseBlock();

        return blockType.getDefaultState().toBaseBlock();
    }

    @Override
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        final WorldEdit worldEdit = WorldEdit.getInstance();

        ServerPlayerEntity player = source.getPlayer();
        FabricPlayer wePlayer = FabricAdapter.adaptPlayer(player);
        LocalSession playerSession = worldEdit.getSessionManager().getIfPresent(wePlayer);

        Region selection = null;
        if (playerSession != null) {
            try {
                selection = playerSession.getSelection();
            } catch (Exception ignored) { }
        }

        if (selection == null) {
            source.sendError(Text.of("Please make a worldedit selection first."));

            return -1;
        }

        // for each block in the selection
        final World world = FabricAdapter.adapt(player.getWorld());
        try (EditSession session = worldEdit.newEditSession(FabricAdapter.adapt(player.getWorld()))) {
            // create mask and pattern and execute block set
            int blocksColored = session.replaceBlocks(selection,
                new Mask() {
                    @Override
                    public boolean test(BlockVector3 vector) {
                        return shouldBeColored(world, vector, onlyWhite.getValue());
                    }

                    @Nullable
                    @Override
                    public Mask2D toMask2D() { return null; }
                },
                new Pattern() {
                    @Override
                    public BaseBlock applyBlock(BlockVector3 position) {
                        return setBlockColor(world, position, color.getValue());
                    }
                }
            );

            Operations.complete(session.commit());

            // call remember to allow undo
            playerSession.remember(session);

            source.sendFeedback(Text.of("Successfully colored " + blocksColored + " blocks " + color.getValue()), false);
        } catch (Exception e) {
            source.sendError(Text.of("An error occurred while coloring the blocks."));

            e.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }

}
