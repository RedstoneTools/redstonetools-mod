package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;
import tools.redstone.redstonetools.utils.WorldEditUtils;

public class BinaryBlockReadFeature extends AbstractFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("binaryblockread")
                .then(CommandManager.argument("offset", IntegerArgumentType.integer(1))
                .then(CommandManager.argument("onBlock", BlockStateArgumentType.blockState(registryAccess))
                .then(CommandManager.argument("toBase", IntegerArgumentType.integer())
                .then(CommandManager.argument("reverseBits", BoolArgumentType.bool())
                .executes(context -> FeatureUtils.getFeature(BinaryBlockReadFeature.class).execute(context))))))));
    }

    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var offset = IntegerArgumentType.getInteger(context, "offset");
        var onBlock = BlockStateArgumentType.getBlockState(context, "onBlock");
        var toBase = IntegerArgumentType.getInteger(context, "toBase");
        var reverseBits = BoolArgumentType.getBool(context, "reverseBits");

        var source = context.getSource();
        Region selection = WorldEditUtils.getSelection(source.getPlayer());

        var boundingBox = selection.getBoundingBox();
        var pos1 = boundingBox.getPos1();
        var pos2 = boundingBox.getPos2();
        var direction = pos2.subtract(pos1).normalize();

        // prevent infinite loop
        if (direction.lengthSq() == 0) {
            direction = BlockVector3.at(0, 0, 1);
        }

        var spacingVector = direction.multiply(offset);

        if (direction.x() + direction.y() + direction.z() > 1) {
            throw new SimpleCommandExceptionType(Text.of("The selection must have 2 axis the same.")).create();
        }

        var bits = new StringBuilder();
        for (BlockVector3 point = pos1; boundingBox.contains(point); point = point.add(spacingVector)) {
            var pos = new BlockPos(point.x(), point.y(), point.z());
            var actualState = source.getWorld().getBlockState(pos);

            var matches = !actualState.equals(onBlock.getBlockState());
            if (matches) {
                for (var property : onBlock.getProperties()) {
                    var propertyValue = onBlock.getBlockState().get(property);

                    if (!actualState.get(property).equals(propertyValue)) {
                        matches = false;
                        break;
                    }
                }
            }

            bits.append(matches ? 1 : 0);
        }

        if (reverseBits) {
            bits.reverse();
        }

        var output = Integer.toString(Integer.parseInt(bits.toString(), 2), toBase);
        source.getPlayer().sendMessage(Text.of(output));
        return 0;
    }
}
