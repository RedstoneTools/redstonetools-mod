package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.util.Direction;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
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
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.commands.argument.DirectionArgumentType;
import tools.redstone.redstonetools.utils.FeatureUtils;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static tools.redstone.redstonetools.utils.DirectionUtils.directionToBlock;
import static tools.redstone.redstonetools.utils.DirectionUtils.matchDirection;

public class RStackFeature extends AbstractFeature {
    public static boolean update = true;
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("/rstack")
                .then(argument("count", IntegerArgumentType.integer())
                .then(argument("direction", DirectionArgumentType.direction())
                .then(argument("offset", IntegerArgumentType.integer())
                .executes(context -> FeatureUtils.getFeature(RStackFeature.class).pareseArguments(context))
                .then(argument("update", BoolArgumentType.bool())
                .executes(context -> FeatureUtils.getFeature(RStackFeature.class).pareseArguments(context))))))));
    }

    protected int pareseArguments(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return execute(context);
    }

    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int count = IntegerArgumentType.getInteger(context, "count");
        var direction = DirectionArgumentType.getDirection(context, "direction");
        int offset = IntegerArgumentType.getInteger(context, "offset");
        boolean update;
        try {
            update = BoolArgumentType.getBool(context, "update");
        } catch (Exception e) {
            update = false;
        }
        RStackFeature.update = update;
        var actor = FabricAdapter.adaptPlayer(Objects.requireNonNull(context.getSource().getPlayer()));

        var localSession = WorldEdit.getInstance()
                .getSessionManager()
                .get(actor);

        final var selectionWorld = localSession.getSelectionWorld();
        assert selectionWorld != null;

        final Region selection;
        try {
            selection = localSession.getSelection(selectionWorld);
        } catch (IncompleteRegionException ex) {
            throw new SimpleCommandExceptionType(Text.literal("Please make a selection with WorldEdit first.")).create();
        }

        final Mask airFilter = new Mask() {
            @Override
            public boolean test(BlockVector3 vector) {
                return !"minecraft:air".equals(selectionWorld.getBlock(vector).getBlockType().id());
            }

            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };

        var playerFacing = actor.getLocation().getDirectionEnum();
        Direction stackDirection;
        try {
            stackDirection = matchDirection(direction, playerFacing);
        } catch (Exception e) {
            throw new SimpleCommandExceptionType(Text.literal(e.getMessage().formatted(e))).create();
        }
        var stackVector = directionToBlock(stackDirection);


        try (var editSession = localSession.createEditSession(actor)) {
            for (var i = 1; i <= count; i++) {
                var copy = new ForwardExtentCopy(
                        editSession,
                        selection,
                        editSession,
                        selection.getMinimumPoint().add(Objects.requireNonNull(stackVector).multiply(i * offset))
                );
                copy.setSourceMask(airFilter);
                copy.setSourceFunction(position -> false);
                Operations.complete(copy);
            }
            localSession.remember(editSession);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        context.getSource().sendMessage(Text.literal("Stacked %s time(s).".formatted(count)));
        return 1;
    }
}
