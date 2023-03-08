package com.domain.redstonetools.features.arguments;

import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.state.property.Property;
import net.minecraft.util.registry.Registry;

public class BlockStateArgumentSerializer extends WrappingSerializer<BlockStateArgument> {

    static final BlockStateArgumentSerializer BASE = new BlockStateArgumentSerializer();

    public static BlockStateArgumentSerializer blockState() {
        return BASE;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String toStringBlockState(BlockStateArgument argument) {
        BlockState state = argument.getBlockState();
        StringBuilder b = new StringBuilder();

        // append block id
        b.append(Registry.BLOCK.getId(state.getBlock()));

        // append properties
        StringBuilder b2 = new StringBuilder("[");
        boolean hasProperties = false;
        for (Property property : state.getProperties()) {
            Comparable value;
            try {
                value = state.get(property);
            } catch (IllegalArgumentException e) {
                continue;
            }

            hasProperties = true;

            b2.append(property.getName());
            b2.append("=");
            b2.append(property.name(value));
        }

        if (hasProperties) {
            b2.append("]");
            b.append(b2);
        }

        // todo: maybe append NBT data
        //  if present. not necessary now though

        return b.toString();
    }

    public BlockStateArgumentSerializer() {
        super(BlockStateArgument.class, BlockStateArgumentType.blockState());
    }

    @Override
    public String asString(BlockStateArgument value) {
        return toStringBlockState(value);
    }

}
