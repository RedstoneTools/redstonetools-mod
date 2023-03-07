package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import static com.domain.redstonetools.features.arguments.DirectionArgumentType.*;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

@Feature(name = "rstack", description = "Stacks with custom distance", command = "/rstack")
public class RStackFeature extends CommandFeature {
    public static final Argument<Integer> count = Argument
            .ofType(integer())
            .withDefault(1);

    public static final Argument<String> direction = Argument
            .ofType(directionArgument())
            .withDefault("me");

    public static final Argument<Integer> spacing = Argument
            .ofType(integer())
            .withDefault(2);

    @Override
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        var actor = FabricAdapter.adaptPlayer(source.getPlayer());

        var localSession = WorldEdit.getInstance()
                .getSessionManager()
                .get(actor);
        var selectionWorld = localSession.getSelectionWorld();

        Region selection;
        try {
            if (selectionWorld == null) {
                throw new IncompleteRegionException();
            }

            selection = localSession.getSelection(selectionWorld);
        } catch (IncompleteRegionException ex) {
            actor.printError(TextComponent.of("Please make a selection with worldedit first."));
            return -1;
        }

        var z = actor.getLocation().getDirectionEnum();

        return 0;
    }
}
