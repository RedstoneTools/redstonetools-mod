package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BinaryBlockReadFeature extends AbstractFeature {
    public static void registerCommand() {
        BinaryBlockReadFeature bbr = FeatureUtils.getFeature(BinaryBlockReadFeature.class);
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("/binaryblockread")
                    .executes(bbr::parseArgs)
                .then(argument("offset", IntegerArgumentType.integer(1))
                    .executes(bbr::parseArgs)
                .then(argument("onBlock", BlockStateArgumentType.blockState(registryAccess))
                    .executes(bbr::parseArgs)
                .then(argument("toBase", IntegerArgumentType.integer())
                    .executes(bbr::parseArgs)
                .then(argument("reverseBits", BoolArgumentType.bool())
                    .executes(bbr::parseArgs)
                )))))
        );
    }

    // this is horrible, but it works
    protected int parseArgs(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int offset;
        BlockState onBlock;
        int toBase;
        boolean reverseBits;
        try {
            offset = IntegerArgumentType.getInteger(context, "offset");
        } catch (Exception ignored) {
            offset = 2;
        }
        try {
            onBlock = BlockStateArgumentType.getBlockState(context, "onBlock").getBlockState();
        } catch (Exception ignored) {
            onBlock = Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, true);
        }
        try {
            toBase = IntegerArgumentType.getInteger(context, "toBase");
        } catch (Exception ignored) {
            toBase = 10;
        }
        try {
            reverseBits = BoolArgumentType.getBool(context, "reverseBits");
        } catch (Exception ignored) {
            reverseBits = false;
        }
        return execute(context, offset, onBlock, toBase, reverseBits);
    }

    protected int execute(CommandContext<ServerCommandSource> context, int offset, BlockState onBlock, int toBase, boolean reverseBits) throws CommandSyntaxException {
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

            var matches = actualState.equals(onBlock);
            if (matches) {
                for (var property : onBlock.getProperties()) {
                    try {
                        actualState.get(property);
                    } catch (Exception e) { // actualState doesn't have the same properties as onBlock
                                            // so they dont match by default.
                        matches = false;
                        break;
                    }
                    var propertyValue = onBlock.get(property);

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
        source.sendMessage(Text.of(output));
        return 0;
    }
}
