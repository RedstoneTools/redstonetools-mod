package com.domain.redstonetools.features.commands.update;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.features.options.EmptyOptions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.fabric.FabricPlayer;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Feature(name = "/update")
public class UpdateFeature extends CommandFeature<EmptyOptions> {



    @Override
    protected int execute(ServerCommandSource source, EmptyOptions options) throws CommandSyntaxException {
        WorldEdit worldEdit = WorldEdit.getInstance();

        ServerPlayerEntity player = source.getPlayer();
        FabricPlayer wePlayer = FabricAdapter.adaptPlayer(player);
        LocalSession playerSession = worldEdit.getSessionManager().getIfPresent(wePlayer);

        Region selection = null;
        if (playerSession != null) {
            try {
                selection = playerSession.getSelection();
            } catch (Exception ignored) { }
        }

        if (selection == null) {
            source.sendError(Text.of("Please make a selection.").getWithStyle(Style.EMPTY.withColor(Formatting.RED)).get(0));

            return -1;
        }

        RegionUpdater.updateRegion(source.getWorld(), source.getPlayer(),selection.getMinimumPoint(), selection.getMaximumPoint());

        return 0;
    }



}
