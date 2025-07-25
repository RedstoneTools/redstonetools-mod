package tools.redstone.redstonetools.features.commands.argumenthelpers;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.SignalBlock;

public class SignalBlockArgumentHelper {
	public static SignalBlock getSignalBlock(CommandContext<ServerCommandSource> context, String block) throws CommandSyntaxException {
		String signalBlockAsString = context.getArgument(block, String.class);
		return switch (signalBlockAsString.toUpperCase()) {
			case ("BARREL") -> SignalBlock.BARREL;
			case ("CHEST") -> SignalBlock.CHEST;
			case ("SHULKER_BOX") -> SignalBlock.SHULKER_BOX;
			case ("DISPENSER") -> SignalBlock.DISPENSER;
			case ("DROPPER") -> SignalBlock.DROPPER;
			case ("HOPPER") -> SignalBlock.HOPPER;
			case ("BREWING_STAND") -> SignalBlock.BREWING_STAND;
			case ("FURNACE") -> SignalBlock.FURNACE;
			case ("SMOKER") -> SignalBlock.SMOKER;
			case ("BLAST_FURNACE") -> SignalBlock.BLAST_FURNACE;
			case ("COMMAND_BLOCK") -> SignalBlock.COMMAND_BLOCK;
			case ("AUTO") -> SignalBlock.AUTO;
			default -> throw new SimpleCommandExceptionType(Text.literal("Could not resolve signal block!")).create();
		};
	}
}
