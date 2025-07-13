package tools.redstone.redstonetools.features.commands.argumenthelpers;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.ColoredBlockType;

public class ColoredBlockTypeArgumentHelper {
	public static ColoredBlockType getColoredBlockType(CommandContext<FabricClientCommandSource> context, String blockType) throws CommandSyntaxException {
		String coloredBlockTypeAsString = context.getArgument(blockType, String.class);
		return switch (coloredBlockTypeAsString.toUpperCase()) {
			case ("WOOL") -> ColoredBlockType.WOOL;
			case ("GLASS") -> ColoredBlockType.GLASS;
			case ("CONCRETE") -> ColoredBlockType.CONCRETE;
			case ("TERRACOTTA") -> ColoredBlockType.TERRACOTTA;
			default -> throw new SimpleCommandExceptionType(Text.literal("Could not resolve block color!")).create();
		};
	}
}
