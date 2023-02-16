package com.domain.redstonetools.features.commands.binaryread;

import com.domain.redstonetools.features.options.Argument;
import com.domain.redstonetools.features.options.Options;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgument;

import static net.minecraft.command.argument.BlockStateArgumentType.blockState;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.BoolArgumentType.bool;

public class BinaryBlockReadOptions extends Options {
        BlockState litLamp = Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, true);

        public final Argument<Integer> spacing = new Argument<>("spacing", integer(1), 2);
        public final Argument<Integer> toBase = new Argument<>("toBase", integer(2, 36), 2);
        public final Argument<BlockStateArgument> onBlock = new Argument<>("onBlock", blockState(),
                        new BlockStateArgument(litLamp, null, null));
        public final Argument<Boolean> reverseBits = new Argument<>("reverseBits", bool(), false);

}
