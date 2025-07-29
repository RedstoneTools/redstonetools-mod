package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tools.redstone.redstonetools.features.commands.argument.ColoredBlockTypeArgumentType;
import tools.redstone.redstonetools.utils.BlockColor;
import tools.redstone.redstonetools.utils.BlockInfo;
import net.minecraft.item.ItemStack;
import tools.redstone.redstonetools.utils.ColoredBlockType;
import tools.redstone.redstonetools.utils.FeatureUtils;

import javax.annotation.Nullable;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;

public class ColoredFeature extends PickBlockFeature {
    public static void registerCommand() {
        EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("colored")
                .then(ClientCommandManager.argument("blockType", ColoredBlockTypeArgumentType.coloredblocktype())
                .executes(context -> FeatureUtils.getFeature(ColoredFeature.class).execute(context)))));
    }
    public static ColoredBlockType blockType;
    protected boolean requiresBlock() {
        return false;
    }

    protected ItemStack getItemStack(CommandContext<FabricClientCommandSource> context, @Nullable BlockInfo blockInfo) {
        var color = blockInfo == null
                ? BlockColor.WHITE
                : BlockColor.fromBlock(blockInfo.block);

        var coloredBlock = blockType.withColor(color);

        return new ItemStack(coloredBlock.toBlock());
    }
    @Override
    protected int execute(CommandContext<FabricClientCommandSource> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException {
        blockType = ColoredBlockTypeArgumentType.getColoredBlockType(context, "blockType");
        return super.execute(context, blockInfo);
    }
}
