package tools.redstone.redstonetools.features.commands.serializer;

import com.google.gson.JsonObject;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import tools.redstone.redstonetools.features.commands.argument.SignalBlockArgumentType;

public class SignalBlockArgumentSerializer  implements ArgumentSerializer<SignalBlockArgumentType, SignalBlockArgumentSerializer.Properties> {
    public void writePacket(SignalBlockArgumentSerializer.Properties properties, PacketByteBuf packetByteBuf) {
    }

    public SignalBlockArgumentSerializer.Properties fromPacket(PacketByteBuf packetByteBuf) {
        return new SignalBlockArgumentSerializer.Properties();
    }

    public void writeJson(SignalBlockArgumentSerializer.Properties properties, JsonObject jsonObject) {
    }

    public SignalBlockArgumentSerializer.Properties getArgumentTypeProperties(SignalBlockArgumentType signalBlockArgumentType) {
        return new SignalBlockArgumentSerializer.Properties();
    }

    public final class Properties implements ArgumentSerializer.ArgumentTypeProperties<SignalBlockArgumentType> {
        Properties() {
        }

        public SignalBlockArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
            return SignalBlockArgumentType.signalblock();
        }

        @Override
        public ArgumentSerializer<SignalBlockArgumentType, ?> getSerializer() {
            return SignalBlockArgumentSerializer.this;
        }
    }
}
