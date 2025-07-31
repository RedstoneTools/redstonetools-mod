package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.features.commands.argument.ColoredBlockTypeArgumentType;
import tools.redstone.redstonetools.utils.BlockColor;
import tools.redstone.redstonetools.utils.BlockInfo;
import net.minecraft.item.ItemStack;
import tools.redstone.redstonetools.utils.ColoredBlockType;
import tools.redstone.redstonetools.utils.FeatureUtils;

import javax.annotation.Nullable;

public class ColoredFeature extends PickBlockFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("colored")
                .then(CommandManager.argument("blockType", ColoredBlockTypeArgumentType.coloredblocktype())
                .executes(context -> FeatureUtils.getFeature(ColoredFeature.class).execute(context)))));
    }
    public static ColoredBlockType blockType;
    protected boolean requiresBlock() {
        return false;
    }

    protected ItemStack getItemStack(CommandContext<ServerCommandSource> context, @Nullable BlockInfo blockInfo) {
        var color = blockInfo == null
                ? BlockColor.WHITE
                : BlockColor.fromBlock(blockInfo.block);

        var coloredBlock = blockType.withColor(color);

        return new ItemStack(coloredBlock.toBlock());
    }
    @Override
    protected int execute(CommandContext<ServerCommandSource> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException {
//        if (!Objects.requireNonNull(context.getSource().getPlayer()).getGameMode().isCreative()) {
//            throw new SimpleCommandExceptionType(Text.literal("You must be in creative to use this command!")).create();
//        }
        blockType = ColoredBlockTypeArgumentType.getColoredBlockType(context, "blockType");
        return super.execute(context, blockInfo);
    }
}
