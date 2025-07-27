package tools.redstone.redstonetools.features.commands.serializer;

import com.google.gson.JsonObject;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import tools.redstone.redstonetools.features.commands.argument.BlockColorArgumentType;

public class BlockColorArgumentSerializer implements ArgumentSerializer<BlockColorArgumentType, BlockColorArgumentSerializer.Properties> {
    public void writePacket(BlockColorArgumentSerializer.Properties properties, PacketByteBuf packetByteBuf) {
    }

    public BlockColorArgumentSerializer.Properties fromPacket(PacketByteBuf packetByteBuf) {
        return new BlockColorArgumentSerializer.Properties();
    }

    public void writeJson(BlockColorArgumentSerializer.Properties properties, JsonObject jsonObject) {
    }

    public BlockColorArgumentSerializer.Properties getArgumentTypeProperties(BlockColorArgumentType blockColorArgumentType) {
        return new BlockColorArgumentSerializer.Properties();
    }

    public final class Properties implements ArgumentSerializer.ArgumentTypeProperties<BlockColorArgumentType> {
        Properties() {
        }

        public BlockColorArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
            return BlockColorArgumentType.blockcolor();
        }

        @Override
        public ArgumentSerializer<BlockColorArgumentType, ?> getSerializer() {
            return BlockColorArgumentSerializer.this;
        }
    }
}
