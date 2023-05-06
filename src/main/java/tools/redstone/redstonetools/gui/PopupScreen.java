package tools.redstone.redstonetools.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

public abstract class PopupScreen extends GameOptionsScreen {
    private static final float MESSAGE_WIDTH_MULTIPLIER = 0.85f;
    private static final int PADDING = 24;
    private static final int TITLE_Y = PADDING;

    private final String message;
    private int messageY;
    private MultilineText messageText;

    public PopupScreen(Screen parent, String title, String message) {
        super(parent, MinecraftClient.getInstance().options, Text.of(title));

        this.message = message;
    }

    protected abstract void addButtons(int y);

    protected int getButtonsHeight()
    {
        return 20;
    }

    @Override
    protected void init() {
        super.init();

        messageText = MultilineText.create(textRenderer, StringVisitable.plain(message), (int)(this.width * MESSAGE_WIDTH_MULTIPLIER));

        var messageHeight = messageText.count() * 9;
        messageY = this.height / 2 - messageHeight / 2;

        int buttonsY = this.height - PADDING - getButtonsHeight();
        this.addButtons(buttonsY);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, TITLE_Y, 0xffffff);
        messageText.drawCenterWithShadow(matrices, this.width / 2, messageY);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
