package tools.redstone.redstonetools.features.commands.serializer;

import com.google.gson.JsonObject;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import tools.redstone.redstonetools.features.commands.argument.DirectionArgumentType;

public class DirectionArgumentSerializer implements ArgumentSerializer<DirectionArgumentType, DirectionArgumentSerializer.Properties> {
    public void writePacket(DirectionArgumentSerializer.Properties properties, PacketByteBuf packetByteBuf) {
    }

    public DirectionArgumentSerializer.Properties fromPacket(PacketByteBuf packetByteBuf) {
        return new DirectionArgumentSerializer.Properties();
    }

    public void writeJson(DirectionArgumentSerializer.Properties properties, JsonObject jsonObject) {
    }

    public DirectionArgumentSerializer.Properties getArgumentTypeProperties(DirectionArgumentType DirectionArgumentType) {
        return new DirectionArgumentSerializer.Properties();
    }

    public final class Properties implements ArgumentSerializer.ArgumentTypeProperties<DirectionArgumentType> {
        Properties() {
        }

        public DirectionArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
            return DirectionArgumentType.direction();
        }

        @Override
        public ArgumentSerializer<DirectionArgumentType, ?> getSerializer() {
            return DirectionArgumentSerializer.this;
        }
    }
}
