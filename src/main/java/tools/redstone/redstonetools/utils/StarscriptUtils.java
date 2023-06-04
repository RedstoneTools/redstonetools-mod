package tools.redstone.redstonetools.utils;

import meteordevelopment.starscript.StandardLib;
import meteordevelopment.starscript.Starscript;
import meteordevelopment.starscript.value.Value;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.Heightmap;

public class StarscriptUtils {
    private StarscriptUtils() {}

    public static void initialize(Starscript starscript) {
        StandardLib.init(starscript);

        var mc = MinecraftClient.getInstance();
        starscript.set("username", () -> Value.string(MinecraftClient.getInstance().getSession().getUsername()));
        starscript.set("x", () -> Value.number(mc.player == null ? 0 : mc.player.getX()));
        starscript.set("y", () -> Value.number(mc.player == null ? 0 : mc.player.getY()));
        starscript.set("z", () -> Value.number(mc.player == null ? 0 : mc.player.getZ()));
        starscript.set("yaw", () -> Value.number(mc.player == null ? 0 : mc.player.getYaw()));
        starscript.set("pitch", () -> Value.number(mc.player == null ? 0 : mc.player.getPitch()));
        starscript.set("onGround", () -> Value.bool(mc.player != null && mc.player.isOnGround()));
        starscript.set("sneaking", () -> Value.bool(mc.player != null && mc.player.isSneaking()));
        starscript.set("sprinting", () -> Value.bool(mc.player != null && mc.player.isSprinting()));

        starscript.set("surfaceY", () -> Value.number(mc.world.getTopY(Heightmap.Type.MOTION_BLOCKING, (int) mc.player.getX(), (int) mc.player.getZ())));
    }
}
