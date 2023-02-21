package com.domain.redstonetools.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;

import java.util.function.Consumer;

/**
 * A renderer which renders relative to a specified
 * camera position using a provided tessellator.
 */
public class CameraRelativeRenderer {

    // the camera position
    double camX;
    double camY;
    double camZ;

    Tessellator tessellator;
    BufferBuilder buffer;

    public CameraRelativeRenderer(Tessellator tessellator) {
        this.tessellator = tessellator;
        this.buffer = tessellator.getBuffer();
    }

    public void camera(double x, double y, double z) {
        this.camX = x;
        this.camY = y;
        this.camZ = z;
    }

    public BufferBuilder getBuffer() {
        return buffer;
    }

    public Tessellator getTessellator() {
        return tessellator;
    }

    public void beginLines(float lineWidth) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.lineWidth(lineWidth);
        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
    }

    public VertexConsumer vertex(double x, double y, double z) {
        return buffer.vertex(x - camX, y - camY, z - camZ);
    }

    public void vertex(double x, double y, double z, Consumer<VertexConsumer> consumer) {
        VertexConsumer v = vertex(x, y, z);
        consumer.accept(v);
        v.next();
    }

    public void line(double sx, double sy, double sz,
                     double ex, double ey, double ez,
                     Consumer<VertexConsumer> consumer) {
        vertex(sx, sy, sz, consumer);
        vertex(ex, ey, ez, consumer);
    }

    public void line(double sx, double sy, double sz,
                     double ex, double ey, double ez,
                     float r, float g, float b, float a) {
        vertex(sx, sy, sz).color(r, g, b, a).next();
        vertex(ex, ey, ez).color(r, g, b, a).next();
    }

    public void cuboidOutline(double xAA, double yAA, double zAA,
                              double xBB, double yBB, double zBB,
                              Consumer<VertexConsumer> consumer) {
        // y1 x-> h
        line(xAA, yAA, zAA, xBB, yAA, zAA, consumer);
        line(xAA, yAA, zBB, xBB, yAA, zBB, consumer);

        // y1 z-> h
        line(xAA, yAA, zAA, xAA, yAA, zBB, consumer);
        line(xBB, yAA, zAA, xBB, yAA, zBB, consumer);

        // y2 x-> h
        line(xAA, yBB, zAA, xBB, yBB, zAA, consumer);
        line(xAA, yBB, zBB, xBB, yBB, zBB, consumer);

        // y2 z-> h
        line(xAA, yBB, zAA, xAA, yBB, zBB, consumer);
        line(xBB, yBB, zAA, xBB, yBB, zBB, consumer);

        // x1z1 y-> v
        line(xAA, yAA, zAA, xAA, yBB, zAA, consumer);
        // x1z2 y-> v
        line(xAA, yAA, zBB, xAA, yBB, zBB, consumer);
        // x2z1 y-> v
        line(xBB, yAA, zAA, xBB, yBB, zAA, consumer);
        // x2z2 y-> v
        line(xBB, yAA, zBB, zBB, yBB, zBB, consumer);
    }

    public void draw() {
        tessellator.draw();
    }

}
