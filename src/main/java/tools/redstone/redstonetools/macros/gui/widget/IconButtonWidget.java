package tools.redstone.redstonetools.macros.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IconButtonWidget extends ButtonWidget {

    public static Identifier CROSS_ICON = new Identifier("redstonetools","gui/cross.png");
    public static Identifier PENCIL_ICON = new Identifier("redstonetools","gui/pencil.png");
    //public static Identifier CHECK_ICON = new Identifier("redstonetools","gui/check_mark.png");

    private final Identifier texture;
    public IconButtonWidget(Identifier texture ,int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
        this.texture = texture;
    }

    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);


        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableDepthTest();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        drawTexture(matrices, this.x, this.y, 0,0, 20, this.height, 20, 20);
    }

}
