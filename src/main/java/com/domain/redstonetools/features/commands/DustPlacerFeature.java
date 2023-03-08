package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.command.argument.BlockStateArgumentType.blockState;

@Feature(name = "Redstone Dust Placer", description = "Places redstone dust on top of a block when you place the block.", command = "dustplacer")
public class DustPlacerFeature extends CommandFeature {
    static public Boolean isActive = false;


    @Override
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        isActive = !isActive;
        return 1;
    }
}
