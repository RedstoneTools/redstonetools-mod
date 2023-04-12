package tools.redstone.redstonetools.macros;

import tools.redstone.redstonetools.macros.gui.commandsuggestor.WorldlessCommandSource;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.server.command.CommandManager;

public class WorldlessCommandHelper {

    public static final ClientPlayNetworkHandler dummyNetworkHandler;
    public static final WorldlessCommandSource commandSource;
    public static boolean registered = false;


    static {
        dummyNetworkHandler = new ClientPlayNetworkHandler(MinecraftClient.getInstance(), null,new ClientConnection(NetworkSide.CLIENTBOUND) , new GameProfile(null, "Player0"), null);
        commandSource = new WorldlessCommandSource();

        new CommandManager(CommandManager.RegistrationEnvironment.DEDICATED); //registers commands using CommandDispatcherMixin
        registered = true;
    }

}
