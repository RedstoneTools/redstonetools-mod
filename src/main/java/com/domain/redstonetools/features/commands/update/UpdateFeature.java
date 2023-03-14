package com.domain.redstonetools.features.commands.update;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.feedback.Feedback;
import com.domain.redstonetools.utils.WorldEditUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.history.change.BlockChange;
import com.sk89q.worldedit.history.change.Change;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Iterator;

import static net.minecraft.server.command.CommandManager.literal;

@Feature(name = "Update", description = "Forces block updates in the selected area.", command = "/update")
public class UpdateFeature extends CommandFeature {

    public static EditSession lastEdit = null;

    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(literal("/update").then(literal("last").executes(this::updateLastEdit)));

        super.registerCommands(dispatcher, dedicated);
    }

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {
        var selectionOrFeedback = WorldEditUtils.getSelection(source.getPlayer());
        if (selectionOrFeedback.right().isPresent()) {
            return selectionOrFeedback.right().get();
        }

        assert selectionOrFeedback.left().isPresent();
        var selection = selectionOrFeedback.left().get();

        RegionUpdater.updateRegion(source.getWorld(), source.getPlayer(), selection.getMinimumPoint(), selection.getMaximumPoint());

        return Feedback.none();
    }

    private int updateLastEdit(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) throws CommandSyntaxException {
        ServerCommandSource source = serverCommandSourceCommandContext.getSource();

        if (lastEdit == null) {
            source.sendError(Text.of("No edits were made!"));
            return -1;
        }

        ServerPlayerEntity player = source.getPlayer();

        boolean containsBlocks = false;
        BlockVector3 minPos = BlockVector3.at(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        BlockVector3 maxPos = BlockVector3.at(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (Iterator<Change> it = lastEdit.getChangeSet().forwardIterator(); it.hasNext(); ) {
            Change change = it.next();

            if (change instanceof BlockChange blockChange) {
                containsBlocks = true;

                BlockVector3 pos = blockChange.getPosition();

                if (pos.getX() <= minPos.getX() && pos.getY() <= minPos.getY() && pos.getZ() <= minPos.getZ()) {
                    minPos = pos;
                }

                if (pos.getX() >= maxPos.getX() && pos.getY() >= maxPos.getY() && pos.getZ() >= maxPos.getZ()) {
                    maxPos = pos;
                }

            }
        }

        World world = FabricAdapter.adapt(lastEdit.getWorld());
        if (containsBlocks)
            RegionUpdater.updateRegion(world, player, minPos, maxPos);

        return 0;
    }

}