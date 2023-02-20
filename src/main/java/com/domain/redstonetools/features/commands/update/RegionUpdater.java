package com.domain.redstonetools.features.commands.update;

import com.sk89q.worldedit.math.BlockVector3;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RegionUpdater implements ClientTickEvents.EndTick {

    private static final ArrayList<UpdateRegionArgs> updateLaterList = new ArrayList<>();

    public static void updateRegion(World world, PlayerEntity feedBackReceiver, BlockVector3 minPos, BlockVector3 maxPos) {
        long blockCount = 0;

        for (int x = minPos.getX() - 1; x <= maxPos.getX() + 1; x++) {
            for (int y = minPos.getY() - 1; y <= maxPos.getY() + 1; y++) {
                for (int z = minPos.getZ() - 1; z <= maxPos.getZ() + 1; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    world.updateNeighbors(new BlockPos(x, y, z), world.getBlockState(pos).getBlock());
                    blockCount++;

                }
            }
        }

        feedBackReceiver.sendMessage(Text.of("Successfully forced block updates for " + blockCount + " blocks"), false);
    }

    public static void updateRegionNextTick(World world, PlayerEntity feedBackReceiver, BlockVector3 minPos, BlockVector3 maxPos) {
        updateLaterList.add(new UpdateRegionArgs(world, feedBackReceiver, minPos, maxPos));
    }


    @Override
    public void onEndTick(MinecraftClient client) {
        for (UpdateRegionArgs args : updateLaterList) {
            updateRegion(args.world, args.feedBackReceiver, args.minPos, args.maxPos);
        }

        updateLaterList.clear();
    }

    private record UpdateRegionArgs(World world, PlayerEntity feedBackReceiver, BlockVector3 minPos, BlockVector3 maxPos) {
    }

}
