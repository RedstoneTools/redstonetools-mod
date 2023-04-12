package tools.redstone.redstonetools.macros;

import tools.redstone.redstonetools.macros.gui.commandsuggestor.WorldlessCommandSource;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.server.command.CommandManager;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

import static net.minecraft.world.dimension.DimensionType.OVERWORLD_ID;

public class WorldlessCommandHelper {

    public static final ClientPlayNetworkHandler dummyNetworkHandler;
    public static final WorldlessCommandSource commandSource;
    public static final ClientPlayerEntity dummyPlayer;
    public static boolean registered = false;


    static {
        dummyNetworkHandler = new ClientPlayNetworkHandler(MinecraftClient.getInstance(), null,new ClientConnection(NetworkSide.CLIENTBOUND) , new GameProfile(null, "Player0"), null);
        commandSource = new WorldlessCommandSource();

        new CommandManager(CommandManager.RegistrationEnvironment.DEDICATED); //registers commands using CommandDispatcherMixin
        registered = true;

        dummyPlayer = new ClientPlayerEntity(MinecraftClient.getInstance(),new ClientWorld(dummyNetworkHandler,new ClientWorld.Properties(Difficulty.EASY,false,false),null,new RegistryEntry.Direct<>(DimensionType.create(OptionalLong.empty(), true, false, false, true, 1.0, false, false, true, false, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, OVERWORLD_ID, 0.0F)),0,0,null,null,true,0 ), dummyNetworkHandler, null,null,false,false);

    }

}
