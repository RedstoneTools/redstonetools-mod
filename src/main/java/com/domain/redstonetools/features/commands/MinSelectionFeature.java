package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.*;

import com.sk89q.worldedit.regions.*;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldedit.math.BlockVector3;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@Feature(id = "minimize-selection", command = "/minsel", description = "Removes all air-only layers from a selection", name = "Minimize Selection")
public class MinSelectionFeature extends CommandFeature {

    @Override
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        var actor = FabricAdapter.adaptPlayer(source.getPlayer());
        var localSession = WorldEdit.getInstance()
                .getSessionManager()
                .get(actor);
        var selectionWorld = localSession.getSelectionWorld();

        Region selection;
        RegionSelector selector;
        try {

            if (selectionWorld == null) {
                throw new IncompleteRegionException();
            }

            selection = localSession.getSelection(selectionWorld);
            selector = localSession.getRegionSelector(selectionWorld);
        } catch (IncompleteRegionException ex) {
            actor.printError(TextComponent.of("Please make a selection with worldedit first."));
            return -1;
        }

        minimiseSelection(selectionWorld, selection);

        selector.learnChanges();
        selector.explainRegionAdjust(actor, localSession);

        actor.printInfo(TextComponent.of("Done."));

        return Command.SINGLE_SUCCESS;
    }

    private void minimiseSelection(World selectionWorld, Region selection)
            throws CommandSyntaxException {
        List<BlockVector3> changes = new ArrayList<>();
        var faces = getFaces(selection);
        var finished = true;

        for (CuboidRegion face : faces) {
            var isOnlyAir = true;

            for (BlockVector3 point : face) {
                if (selectionWorld.getBlock(point).getBlockType().getDefaultState() != BlockTypes.AIR
                        .getDefaultState()) {
                    isOnlyAir = false;
                    break;
                }
            }

            if (!isOnlyAir)
                continue;

            var difference = selection.getCenter().subtract(face.getCenter());
            difference = difference.normalize();

            changes.add(difference.toBlockPoint());

            finished = false;

        }

        try {
            selection.contract(changes.toArray(new BlockVector3[changes.size()]));
        } catch (RegionOperationException e) {
            throw new net.minecraft.command.CommandException(Text.of("There was an error modifying the region."));
        }

        if (!finished)
            minimiseSelection(selectionWorld, selection);

    }

    private List<CuboidRegion> getFaces(Region selection) {
        var faces = new ArrayList<CuboidRegion>();

        var pos1 = selection.getBoundingBox().getPos1();
        var pos2 = selection.getBoundingBox().getPos2();

        var min = selection.getMinimumPoint();
        var max = selection.getMaximumPoint();

        faces.add(new CuboidRegion(pos1.withX(min.getX()), pos2.withX(min.getX())));
        faces.add(new CuboidRegion(pos1.withX(max.getX()), pos2.withX(max.getX())));

        faces.add(new CuboidRegion(pos1.withZ(min.getZ()), pos2.withZ(min.getZ())));
        faces.add(new CuboidRegion(pos1.withZ(max.getZ()), pos2.withZ(max.getZ())));

        faces.add(new CuboidRegion(pos1.withY(min.getY()), pos2.withY(min.getY())));
        faces.add(new CuboidRegion(pos1.withY(max.getY()), pos2.withY(max.getY())));

        return faces;
    }

}
