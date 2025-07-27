package tools.redstone.redstonetools.features.commands.serializer;

import com.google.gson.JsonObject;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import tools.redstone.redstonetools.features.commands.argument.ColoredBlockTypeArgumentType;

public class ColoredBlockTypeArgumentSerializer implements ArgumentSerializer<ColoredBlockTypeArgumentType, ColoredBlockTypeArgumentSerializer.Properties> {
    public void writePacket(ColoredBlockTypeArgumentSerializer.Properties properties, PacketByteBuf packetByteBuf) {
    }

    public ColoredBlockTypeArgumentSerializer.Properties fromPacket(PacketByteBuf packetByteBuf) {
        return new ColoredBlockTypeArgumentSerializer.Properties();
    }

    public void writeJson(ColoredBlockTypeArgumentSerializer.Properties properties, JsonObject jsonObject) {
    }

    public ColoredBlockTypeArgumentSerializer.Properties getArgumentTypeProperties(ColoredBlockTypeArgumentType blockColorArgumentType) {
        return new ColoredBlockTypeArgumentSerializer.Properties();
    }

    public final class Properties implements ArgumentSerializer.ArgumentTypeProperties<ColoredBlockTypeArgumentType> {
        Properties() {
        }

        public ColoredBlockTypeArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
            return ColoredBlockTypeArgumentType.coloredblocktype();
        }

        @Override
        public ArgumentSerializer<ColoredBlockTypeArgumentType, ?> getSerializer() {
            return ColoredBlockTypeArgumentSerializer.this;
        }
    }
}

