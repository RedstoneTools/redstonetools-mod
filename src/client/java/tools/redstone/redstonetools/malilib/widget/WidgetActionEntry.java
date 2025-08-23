package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class WidgetActionEntry extends WidgetListEntryBase<CommandActionBase>  {
	private final WidgetListActions parent;
	public final CommandActionBase commandAction;
	protected final int buttonsStartX;
	private final TextFieldWidget commandTextField;

	public WidgetActionEntry(int x, int y, int width, int height,
	                         CommandActionBase commandAction, int listIndex, WidgetListActions parent) {
		super(x, y, width, height, commandAction, listIndex);
		this.commandAction = commandAction;
		this.parent = parent;
		this.commandTextField = new TextFieldWidget(this.textRenderer, x, y, width - 60, height - 2, null, Text.of(""));
		this.commandTextField.active = true;
		this.commandTextField.visible = true;

//		this.add

		y += 1;

		int posX = x + width - 2;

		posX -= this.addButton(posX, y, (t, g) -> {
			this.parent.getAllEntries().remove(this.commandAction);
			this.parent.refreshEntries();
		}, "Remove");
		this.buttonsStartX = posX;
	}

	protected int addButton(int x, int y, IButtonActionListener runs, String name) {
		ButtonGeneric button = new ButtonGeneric(x, y, -1, true, name);
		this.addButton(button, runs);

		return button.getWidth() + 1;
	}

	@Override
	public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
		return super.canSelectAt(mouseX, mouseY, mouseButton) && mouseX < this.buttonsStartX;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, boolean selected) {
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, this.commandAction.command, this.x + 4, this.y + 7, 0xFFFFFFFF);
		super.render(context, mouseX, mouseY, selected);
		this.commandTextField.render(context, mouseX, mouseY, 1);
		this.commandAction.command = this.commandTextField.getText();
	}
}
