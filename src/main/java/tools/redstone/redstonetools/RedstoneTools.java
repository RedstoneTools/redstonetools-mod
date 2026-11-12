package tools.redstone.redstonetools;

//~ if paper 'net.fabricmc.api.ModInitializer' -> 'org.bukkit.plugin.java.JavaPlugin'
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.redstone.redstonetools.config.ServerConfig;
import tools.redstone.redstonetools.packets.RedstoneToolsPackets;
import tools.redstone.redstonetools.utils.TickScheduler;

//~ if paper 'implements ModInitializer' -> 'extends JavaPlugin'
public class RedstoneTools extends JavaPlugin {
	public static final String MOD_ID = "redstonetools";
	public static final String MOD_NAME = "Redstone tools";
	public static final Logger LOGGER = LoggerFactory.getLogger(RedstoneTools.MOD_ID);

	@Override
	//~ if paper 'onInitialize' -> 'onEnable'
	public void onEnable() {
		registerConfig();
		registerNetworking();
		registerGameRules();
		registerScheduler();
		registerCommands();
		registerListeners();
	}

	/** Server-side configuration. Fabric uses a shared config dir, Paper the plugin's folder. */
	private void registerConfig() {
		//? if fabric {
		/*ServerConfig.load(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("redstonetools.properties"));
		*///? } else {
		ServerConfig.load(getDataFolder().toPath().resolve("config.yml"));
		//? }
	}

	/** Feature toggle sync with the client mod. Fabric uses custom payloads, Paper plugin messaging. */
	private void registerNetworking() {
		RedstoneToolsPackets.registerPackets(this);
	}

	/**
	 * Vanilla registries are frozen before plugins are enabled, so custom game rules
	 * cannot be registered on Paper. doContainerDrops still needs a Bukkit-side
	 * reimplementation, most likely through BlockDropItemEvent.
	 */
	private void registerGameRules() {
		//? if fabric
		//RedstoneToolsGameRules.register();
	}

	private void registerCommands() {
		//? if fabric {
		/*Commands.registerCommands();
		*///? } else {
		Commands.registerCommands(this.getLifecycleManager());
		//? }
	}

	/** Tick pump used by the incremental flood fill of //that. */
	private void registerScheduler() {
		TickScheduler.init(this);
	}

	/** Feature hooks. Paper goes through Bukkit events, Fabric through FAPI callbacks. */
	private void registerListeners() {
		//? if fabric {
		/*net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.BEFORE.register(
			(world, player, pos, state, blockEntity) ->
				!(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)
					|| !tools.redstone.redstonetools.utils.BlockBreakCapture.consume(serverPlayer, pos));
		*///? } else {
		getServer().getPluginManager().registerEvents(new RedstoneToolsListener(), this);
		//? }
	}
}
