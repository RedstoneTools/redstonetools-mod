package tools.redstone.redstonetools.features.commands.argumenthelpers;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.BlockColor;
import tools.redstone.redstonetools.utils.DirectionArgument;

public class BlockColorArgumentHelper {
	public static BlockColor getBlockColor(CommandContext<ServerCommandSource> context, String color) throws CommandSyntaxException {
		String blockColorAsString = context.getArgument(color, String.class);
		return switch (blockColorAsString.toUpperCase()) {
			case ("WHITE") -> BlockColor.WHITE;
			case ("ORANGE") -> BlockColor.ORANGE;
			case ("MAGENTA") -> BlockColor.MAGENTA;
			case ("LIGHT_BLUE") -> BlockColor.LIGHT_BLUE;
			case ("YELLOW") -> BlockColor.YELLOW;
			case ("LIME") -> BlockColor.LIME;
			case ("PINK") -> BlockColor.PINK;
			case ("GRAY") -> BlockColor.GRAY;
			case ("LIGHT_GRAY") -> BlockColor.LIGHT_GRAY;
			case ("CYAN") -> BlockColor.CYAN;
			case ("PURPLE") -> BlockColor.PURPLE;
			case ("BLUE") -> BlockColor.BLUE;
			case ("BROWN") -> BlockColor.BROWN;
			case ("GREEN") -> BlockColor.GREEN;
			case ("RED") -> BlockColor.RED;
			case ("BLACK") -> BlockColor.BLACK;
			default -> throw new SimpleCommandExceptionType(Text.literal("Could not resolve block color!")).create();
		};
	}
}
