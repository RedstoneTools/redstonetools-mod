package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.BlockColor;
import tools.redstone.redstonetools.utils.BlockInfo;
import com.mojang.datafixers.util.Either;
import net.minecraft.item.ItemStack;
import tools.redstone.redstonetools.utils.ColoredBlockType;

import javax.annotation.Nullable;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;
import static tools.redstone.redstonetools.features.arguments.serializers.ColoredBlockTypeSerializer.coloredBlockType;

public class ColoredFeature extends PickBlockFeature {
    public static void registerCommand() {
        EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("colored")
                .then(ClientCommandManager.argument("inputNum", IntegerArgumentType.integer())
                .then(ClientCommandManager.argument("toBase",   IntegerArgumentType.integer())
                .executes(BaseConvertFeature::execute)))));
    }
    public static final Argument<ColoredBlockType> blockType = Argument.ofType(coloredBlockType());
    protected boolean requiresBlock() {
        return false;
    }

    protected Either<ItemStack, Feedback> getItemStack(CommandContext<FabricClientCommandSource> context, @Nullable BlockInfo blockInfo) {
        var color = blockInfo == null
                ? BlockColor.WHITE
                : BlockColor.fromBlock(blockInfo.block);

        var coloredBlock = blockType.getValue().withColor(color);

        return Either.left(new ItemStack(coloredBlock.toBlock()));
    }
}
