package tools.redstone.redstonetools;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.redstone.redstonetools.features.commands.argument.BlockColorArgumentType;
import tools.redstone.redstonetools.features.commands.argument.ColoredBlockTypeArgumentType;
import tools.redstone.redstonetools.features.commands.argument.DirectionArgumentType;
import tools.redstone.redstonetools.features.commands.argument.SignalBlockArgumentType;
import tools.redstone.redstonetools.packets.RedstoneToolsPackets;

public class RedstoneTools implements ModInitializer {
	public static final String MOD_ID = "redstonetools";
	public static final String MOD_VERSION = "v3.0.0";
	public static final String MOD_NAME = "Redstone tools";
    public static final Logger LOGGER = LoggerFactory.getLogger(RedstoneTools.MOD_ID);

	public static final ComponentType<String> COMMAND_COMPONENT = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			Identifier.of(RedstoneTools.MOD_ID, "command"),
			ComponentType.<String>builder().codec(Codec.STRING).build()
	);

	@Override
	public void onInitialize() {
		Thread t = new Thread(CheckUpdates::checkUpdates);
		t.start();

		RedstoneToolsPackets.registerPackets();
		RedstoneToolsGameRules.register();
		Commands.registerCommands();
		ArgumentTypeRegistry.registerArgumentType(Identifier.of("blockcolor"), BlockColorArgumentType.class, ConstantArgumentSerializer.of(BlockColorArgumentType::blockcolor));
		ArgumentTypeRegistry.registerArgumentType(Identifier.of("coloredblocktype"), ColoredBlockTypeArgumentType.class, ConstantArgumentSerializer.of(ColoredBlockTypeArgumentType::coloredblocktype));
		ArgumentTypeRegistry.registerArgumentType(Identifier.of("direction"), DirectionArgumentType.class, ConstantArgumentSerializer.of(DirectionArgumentType::direction));
		ArgumentTypeRegistry.registerArgumentType(Identifier.of("signalblock"), SignalBlockArgumentType.class, ConstantArgumentSerializer.of(SignalBlockArgumentType::signalblock));
	}
}
