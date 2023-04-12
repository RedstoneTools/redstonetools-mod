package tools.redstone.redstonetools.features.commands.update;

import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.commands.CommandFeature;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.features.feedback.FeedbackSender;
import tools.redstone.redstonetools.utils.WorldEditUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.history.change.BlockChange;
import com.sk89q.worldedit.history.change.Change;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import java.util.Iterator;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;
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


        return RegionUpdater.updateRegion(source.getWorld(), selection.getMinimumPoint(), selection.getMaximumPoint());
    }

    private int updateLastEdit(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) throws CommandSyntaxException {
        ServerCommandSource source = serverCommandSourceCommandContext.getSource();

        if (lastEdit == null) {
            INJECTOR.getInstance(FeedbackSender.class).sendFeedback(source, Feedback.error("No edits were made!"));
            return -1;
        }

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
        if (containsBlocks) {
            Feedback feedback = RegionUpdater.updateRegion(world, minPos, maxPos);
            INJECTOR.getInstance(FeedbackSender.class).sendFeedback(source, feedback);
            return 0;
        }

        INJECTOR.getInstance(FeedbackSender.class).sendFeedback(source, Feedback.error("Edit doesn't contain any blocks!"));
        return -1;

    }

}