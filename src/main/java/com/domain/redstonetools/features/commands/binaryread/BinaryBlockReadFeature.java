package com.domain.redstonetools.features.commands.binaryread;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;

import net.minecraft.block.BlockState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@Feature(name = "/read")
public class BinaryBlockReadFeature extends CommandFeature<BinaryBlockReadOptions> {

    @Override
    protected int execute(ServerCommandSource source, BinaryBlockReadOptions options) throws CommandSyntaxException {

        Integer SPACING = options.spacing.getValue();

        Player actor = FabricAdapter.adaptPlayer(source.getPlayer());
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);

        Region region;
        World selectionWorld = localSession.getSelectionWorld();

        try {
            if (selectionWorld == null)
                throw new IncompleteRegionException();
            region = localSession.getSelection(selectionWorld);
        } catch (IncompleteRegionException ex) {
            actor.printError(TextComponent.of("Please make a selection with worldedit first."));
            return 0;
        }

        CuboidRegion regionBox = region.getBoundingBox();
        BlockVector3 pos1 = regionBox.getPos1();
        BlockVector3 pos2 = regionBox.getPos2();
        BlockVector3 direction = pos2.subtract(pos1).normalize();
        BlockVector3 spacer = direction.multiply(SPACING);

        if ((direction.getX() + direction.getBlockY() + direction.getBlockZ()) > 1) {
            source.sendError(Text.of("The selection must have 2 axis the same"));
            return 0;
        }

        StringBuilder bits = new StringBuilder();

        for (BlockVector3 point = pos1; regionBox.contains(point); point = point.add(spacer)) {
            BlockState pointBlock = FabricAdapter.adapt(selectionWorld.getBlock(point));

            if (options.onBlock.getValue().getBlockState() == pointBlock) {
                bits.append(1);
            } else {
                bits.append(0);
            }

        }

        if (!options.reverseBits.getValue()) {
            bits.reverse();
        }

        String result = Integer.toString(
                Integer.parseInt(bits.toString(), 2),
                options.toBase.getValue());

        source.sendFeedback(Text.of(result), false);

        return 1;
    }

}