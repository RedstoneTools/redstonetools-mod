package com.domain.redstonetools.mixin;

import com.domain.redstonetools.features.commands.update.RegionUpdater;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.history.change.BlockChange;
import com.sk89q.worldedit.history.change.Change;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

import static com.domain.redstonetools.RedstoneToolsGameRules.DO_BLOCK_UPDATES_AFTER_EDIT;

@Mixin(LocalSession.class)
public class LocalSessionMixin {

    @Inject(method = "remember", at = @At("TAIL"), remap = false)
    public void remember(EditSession editSession, CallbackInfo ci) {
        World world =FabricAdapter.adapt(editSession.getWorld());

        if (editSession.getChangeSet().size() > 0 && world.getGameRules().getBoolean(DO_BLOCK_UPDATES_AFTER_EDIT)) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            boolean containsBlocks = false;
            BlockVector3 minPos = BlockVector3.at(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            BlockVector3 maxPos = BlockVector3.at(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

            for (Iterator<Change> it = editSession.getChangeSet().forwardIterator(); it.hasNext(); ) {
                Change change = it.next();

                if (change instanceof BlockChange blockChange) {
                    containsBlocks = true;

                    BlockVector3 pos = blockChange.getPosition();

                    if (pos.getX() <= minPos.getX() && pos.getY() <= minPos.getY() && pos.getZ() <= minPos.getZ()) {
                        minPos = pos;
                    }

                    if (pos.getX() >= maxPos.getX() && pos.getY() >= maxPos.getY() && pos.getZ() >= maxPos.getZ()) {
                        maxPos = pos;
                    }

                }
            }
            if (containsBlocks)
                RegionUpdater.updateRegionNextTick(world, player, minPos, maxPos);
        }
    }
}
