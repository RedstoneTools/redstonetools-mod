package tools.redstone.redstonetools.utils;

/**
 * Runs work on a later server tick. The only platform-specific piece of the incremental
 * flood fill and of pin pulses, and the first slice of a future platform layer.
 */
public class TickScheduler {
	//? if fabric {
	/*private static final java.util.List<Scheduled> SCHEDULED = new java.util.ArrayList<>();
	private static final java.util.List<Scheduled> PENDING = new java.util.ArrayList<>();

	private static final class Scheduled {
		private final Runnable task;
		private long remaining;

		private Scheduled(Runnable task, long remaining) {
			this.task = task;
			this.remaining = remaining;
		}
	}

	public static void init(Object ignored) {
		net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> {
			// Tasks scheduled during this tick only start counting down from the next one.
			SCHEDULED.addAll(PENDING);
			PENDING.clear();

			java.util.List<Runnable> due = new java.util.ArrayList<>();
			java.util.Iterator<Scheduled> iterator = SCHEDULED.iterator();

			while (iterator.hasNext()) {
				Scheduled scheduled = iterator.next();

				if (--scheduled.remaining <= 0) {
					due.add(scheduled.task);
					iterator.remove();
				}
			}

			due.forEach(Runnable::run);
		});

		net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			SCHEDULED.clear();
			PENDING.clear();
		});
	}

	public static void runLater(Runnable task, long ticks) {
		PENDING.add(new Scheduled(task, Math.max(1L, ticks)));
	}
	*///? } else {
	private static org.bukkit.plugin.Plugin plugin;

	public static void init(org.bukkit.plugin.Plugin owner) {
		plugin = owner;
	}

	public static void runLater(Runnable task, long ticks) {
		org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, task, Math.max(1L, ticks));
	}
	//? }

	public static void runNextTick(Runnable task) {
		runLater(task, 1L);
	}
}