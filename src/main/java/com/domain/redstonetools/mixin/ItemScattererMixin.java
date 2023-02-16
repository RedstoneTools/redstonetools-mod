package com.domain.redstonetools.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

import static com.domain.redstonetools.RedstoneToolsGameRules.DO_CONTAINER_DROPS;

@org.spongepowered.asm.mixin.Mixin(ItemScatterer.class)
public class ItemScattererMixin {

    @Shadow @Final private static Random RANDOM;


    //there were problems with injecting into static method, so I just overwrote it

    @Overwrite
    public static void spawn(World world, double x, double y, double z, ItemStack stack) {
        if (!world.getGameRules().getBoolean(DO_CONTAINER_DROPS)) return;

        double d = EntityType.ITEM.getWidth();
        double e = 1.0 - d;
        double f = d / 2.0;
        double g = Math.floor(x) + RANDOM.nextDouble() * e + f;
        double h = Math.floor(y) + RANDOM.nextDouble() * e;
        double i = Math.floor(z) + RANDOM.nextDouble() * e + f;

        while(!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(world, g, h, i, stack.split(RANDOM.nextInt(21) + 10));
            itemEntity.setVelocity(RANDOM.nextGaussian() * 0.05000000074505806, RANDOM.nextGaussian() * 0.05000000074505806 + 0.20000000298023224, RANDOM.nextGaussian() * 0.05000000074505806);
            world.spawnEntity(itemEntity);
        }

    }


}
