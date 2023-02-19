package com.domain.redstonetools.features.commands.binaryread;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@Feature(name = "/read")
public class BinaryBlockReadFeature extends CommandFeature<BinaryBlockReadOptions> {
    @Override
    protected int execute(ServerCommandSource source, BinaryBlockReadOptions options) throws CommandSyntaxException {
        var actor = FabricAdapter.adaptPlayer(source.getPlayer());
        var localSession = WorldEdit.getInstance()
                .getSessionManager()
                .get(actor);
        var selectionWorld = localSession.getSelectionWorld();

        Region selection;
        try {
            if (selectionWorld == null)  {
                throw new IncompleteRegionException();
            }

            selection = localSession.getSelection(selectionWorld);
        } catch (IncompleteRegionException ex) {
            actor.printError(TextComponent.of("Please make a selection with worldedit first."));

            return -1;
        }

        var boundingBox = selection.getBoundingBox();
        var pos1 = boundingBox.getPos1();
        var pos2 = boundingBox.getPos2();
        var direction = pos2.subtract(pos1).normalize();
        var spacing = direction.multiply(options.spacing.getValue());

        if (direction.getX() + direction.getBlockY() + direction.getBlockZ() > 1) {
            actor.printError(TextComponent.of("The selection must have 2 axis the same"));

            return -1;
        }

        var onBlockState = options.onBlock.getValue().getBlockState();

        var bits = new StringBuilder();
        for (BlockVector3 point = pos2; boundingBox.contains(point); point = point.subtract(spacing)) {
            var pointBlockState = FabricAdapter.adapt(selectionWorld.getBlock(point));

            bits.append(pointBlockState.equals(onBlockState) ? 1 : 0);
        }

        if (options.reverseBits.getValue()) {
            bits.reverse();
        }

        var output = Integer.toString(Integer.parseInt(bits.toString(), 2), options.toBase.getValue());
        source.sendFeedback(Text.of(output), false);

        return Command.SINGLE_SUCCESS;
    }

}