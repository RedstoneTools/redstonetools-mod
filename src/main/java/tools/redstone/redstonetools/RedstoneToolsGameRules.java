package tools.redstone.redstonetools;

//? if <1.21.11 {
/*import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
*/
//?}

public class RedstoneToolsGameRules {
	private RedstoneToolsGameRules() {
	}

	//? if <1.21.11 {
	/*public static GameRules.Key<GameRules.BooleanRule> DO_CONTAINER_DROPS;

	public static void register() {
		DO_CONTAINER_DROPS = GameRuleRegistry.register("doContainerDrops", GameRules.Category.DROPS, GameRuleFactory.createBooleanRule(true));
	}
	*/
	//?} else {
	// Game rules are now a registry in 1.21.11 - this feature is not yet ported
	public static void register() {
		// TODO: Port to new game rule registry system
	}
	//?}
}
