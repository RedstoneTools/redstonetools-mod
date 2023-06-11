package tools.redstone.redstonetools.features.arguments.serializers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;

import java.util.HashSet;

public class BlockStateArgumentSerializer extends BrigadierSerializer<BlockStateArgument, String> {

    private static final BlockStateArgumentSerializer INSTANCE = new BlockStateArgumentSerializer();

    private BlockStateArgumentSerializer() {
        super(BlockStateArgument.class, BlockStateArgumentType.blockState());
    }

    public static BlockStateArgumentSerializer blockState() {
        return INSTANCE;
    }

    public static BlockStateArgument toArgument(BlockState blockState) {
        return new BlockStateArgument(blockState, new HashSet<>(blockState.getProperties()), new NbtCompound());
    }

    @Override
    public BlockStateArgument deserialize(String serialized) {
        try {
            return deserialize(new StringReader(serialized));
        } catch (CommandSyntaxException e) {
            throw new IllegalStateException("Syntax Exception: " + e.getMessage());
        }
    }

    @Override
    public String serialize(BlockStateArgument value) {
        var state = value.getBlockState();
        var block = state.getBlock();

        var builder = new StringBuilder()
                .append(Registry.BLOCK.getId(block));

        if (state.getProperties().size() == 0) {
            return builder.toString();
        }

        builder.append('[');
        var first = true;
        for (var prop : state.getProperties()) {
            if (first) {
                first = false;
            } else {
                builder.append(',');
            }

            builder.append(prop.getName())
                    .append('=')
                    .append(state.get(prop));
        }
        builder.append(']');

        return builder.toString();
    }

}
