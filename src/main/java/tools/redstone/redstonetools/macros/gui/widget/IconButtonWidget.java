package tools.redstone.redstonetools.macros.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class IconButtonWidget extends ButtonWidget {

    public static final Identifier CROSS_ICON = Identifier.of("redstonetools", "gui/cross.png");
    public static final Identifier PENCIL_ICON = Identifier.of("redstonetools", "gui/pencil.png");

    private final Identifier texture;
    public IconButtonWidget(Identifier texture ,int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
        this.texture = texture;
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(texture);
        RenderSystem.setShaderTexture(0, abstractTexture.getGlTextureView());
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, this.texture, this.getX(), this.getY(), 0, 0, 20, this.height, 20, 20);
    }

}
