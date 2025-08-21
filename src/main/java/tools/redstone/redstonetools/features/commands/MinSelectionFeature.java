package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.minecraft.server.command.CommandManager.literal;

public class MinSelectionFeature {
	public static final MinSelectionFeature INSTANCE = new MinSelectionFeature();

	protected MinSelectionFeature() {
	}

	public void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(literal("/minsel").executes(this::execute)));
	}

	protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var selection = WorldEditUtils.getSelection(context.getSource().getPlayer());
		var selectionWorld = selection.getWorld();

		var actor = FabricAdapter.adaptPlayer(Objects.requireNonNull(context.getSource().getPlayer()));

		var localSession = WorldEdit.getInstance()
				.getSessionManager()
				.get(actor);

		var selector = localSession.getRegionSelector(selectionWorld);

		assert selectionWorld != null;
		assert BlockTypes.AIR != null;
		boolean isEmpty = true;
		for (BlockVector3 point : selection) {
			if (!selectionWorld.getBlock(point).equals(BlockTypes.AIR.getDefaultState()))
				isEmpty = false;
		}

		if (isEmpty) {
			throw new SimpleCommandExceptionType(Text.of("Cannot minimize empty selections.")).create();
		}

		minimiseSelection(selectionWorld, selection);

		selector.learnChanges();
		selector.explainRegionAdjust(actor, localSession);

		context.getSource().sendMessage(Text.literal("Minimized selection."));
		return 1;
	}

	private void minimiseSelection(World selectionWorld, Region selection)
			throws CommandSyntaxException {
		List<BlockVector3> changes = new ArrayList<>();
		var faces = getFaces(selection);
		var finished = true;

		for (CuboidRegion face : faces) {
			var isOnlyAir = true;

			for (BlockVector3 point : face) {
				assert BlockTypes.AIR != null;
				if (selectionWorld.getBlock(point).getBlockType().getDefaultState() != BlockTypes.AIR
						.getDefaultState()) {
					isOnlyAir = false;
					break;
				}
			}

			if (!isOnlyAir)
				continue;

			var difference = selection.getCenter().subtract(face.getCenter());
			difference = difference.normalize();

			changes.add(difference.toBlockPoint());

			finished = false;

		}

		try {
			selection.contract(changes.toArray(new BlockVector3[0]));
		} catch (RegionOperationException e) {
			throw new CommandSyntaxException(null, Text.literal("There was an error modifying the region."));
		}

		if (!finished)
			minimiseSelection(selectionWorld, selection);

	}

	private List<CuboidRegion> getFaces(Region selection) {
		var faces = new ArrayList<CuboidRegion>();

		var pos1 = selection.getBoundingBox().getPos1();
		var pos2 = selection.getBoundingBox().getPos2();

		var min = selection.getMinimumPoint();
		var max = selection.getMaximumPoint();

		faces.add(new CuboidRegion(pos1.withX(min.x()), pos2.withX(min.x())));
		faces.add(new CuboidRegion(pos1.withX(max.x()), pos2.withX(max.x())));

		faces.add(new CuboidRegion(pos1.withZ(min.z()), pos2.withZ(min.z())));
		faces.add(new CuboidRegion(pos1.withZ(max.z()), pos2.withZ(max.z())));

		faces.add(new CuboidRegion(pos1.withY(min.y()), pos2.withY(min.y())));
		faces.add(new CuboidRegion(pos1.withY(max.y()), pos2.withY(max.y())));

		return faces;
	}
}
