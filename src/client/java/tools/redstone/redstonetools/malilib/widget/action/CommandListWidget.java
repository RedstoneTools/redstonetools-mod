package tools.redstone.redstonetools.malilib.widget.action;

import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
/*$ click_and_inputs_imports {*///
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.input.CharInput;/*$}*/
import net.minecraft.text.Text;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;

public class CommandListWidget extends EntryListWidget<CommandListWidget.CommandEntry> {
	private final GuiMacroEditor parent;
	private final MacroBase macro;

	public CommandListWidget(GuiMacroEditor parent, MinecraftClient mc, int width, int height, int y, int itemHeight, MacroBase macro) {
		super(mc, width, height, y, itemHeight);
		this.parent = parent;
		this.macro = macro;
		this.macro.actions.forEach((t) -> this.children().add(new CommandEntry(t, this)));
	}

	@Override
	protected void clearEntries() {
		this.children().clear();
		super.clearEntries();
	}

	@Override
	public int getRowWidth() {
		return this.width - 50;
	}

	public void addEntry() {
		this.macro.actions.addFirst(new CommandAction(""));
		this.addEntryToTop(new CommandEntry(this.macro.actions.getFirst(), this));
		this.centerScrollOn(this.getFirst());
	}

	CommandEntry previousSelected = this.getSelectedOrNull();

	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		if (this.isSelected()) {
			if (this.getSelectedOrNull() != null) {
				this.getSelectedOrNull().setFocused(true);
				if (previousSelected != null)
					this.previousSelected.setFocused(false);
			}
		} else {
			if (this.getSelectedOrNull() != null)
				this.getSelectedOrNull().setFocused(false);
			if (this.previousSelected != null)
				this.previousSelected.setFocused(false);
			this.setSelected(null);
		}
		this.previousSelected = this.getSelectedOrNull();
		super.renderWidget(context, mouseX, mouseY, deltaTicks);
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {

	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		if (this.getSelectedOrNull() != null) {
			this.getSelectedOrNull().mouseMoved(mouseX, mouseY);
		}
		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(/*$ mouse_clicked_params {*/Click click, boolean doubleClick/*$}*/) {
		if (this.getSelectedOrNull() != null) {
			this.getSelectedOrNull().mouseClicked(/*$ mouse_clicked_args {*/click, doubleClick/*$}*/);
		}
		return super.mouseClicked(/*$ mouse_clicked_args {*/click, doubleClick/*$}*/);
	}

	@Override
	public boolean mouseReleased(/*$ dragged_released_params {*/Click click/*$}*/) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().mouseReleased(/*$ dragged_released_args {*/click/*$}*/);
		}
		return super.mouseReleased(/*$ dragged_released_args {*/click/*$}*/);
	}

	@Override
	public boolean mouseDragged(/*$ dragged_released_params {*/Click click/*$}*/, double deltaX, double deltaY) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().mouseDragged(/*$ dragged_released_args {*/click/*$}*/, deltaX, deltaY);
		}
		return super.mouseDragged(/*$ dragged_released_args {*/click/*$}*/, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		}
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean keyPressed(/*$ keyinput_params {*/KeyInput input/*$}*/) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().keyPressed(/*$ keyinput_args {*/input/*$}*/);
		}
		return super.keyPressed(/*$ keyinput_args {*/input/*$}*/);
	}

	@Override
	public boolean keyReleased(/*$ keyinput_params {*/KeyInput input/*$}*/) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().keyReleased(/*$ keyinput_args {*/input/*$}*/);
		}
		return super.keyReleased(/*$ keyinput_args {*/input/*$}*/);
	}

	@Override
	public boolean charTyped(/*$ charinput_params {*/CharInput input/*$}*/) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().charTyped(/*$ charinput_args {*/input/*$}*/);
		}
		return super.charTyped(/*$ charinput_args {*/input/*$}*/);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().isMouseOver(mouseX, mouseY);
		}
		return super.isMouseOver(mouseX, mouseY);
	}

	public static class CommandEntry extends EntryListWidget.Entry<CommandListWidget.CommandEntry> {
		public final CommandAction command;
		public final TextFieldWidget commandWidget;
		private final CommandListWidget parent;
		private boolean isFirst = true;
		private ButtonBase removeButton = new ButtonGeneric(0, 0, 0, 0, "Remove");

		public CommandEntry(CommandAction command, CommandListWidget parent) {
			this.parent = parent;
			this.command = command;
			this.commandWidget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 250, 50, Text.of(""));
			this.commandWidget.setMaxLength(256);
			this.commandWidget.setText(command.command);
			var commandSuggester = new ChatInputSuggestor(MinecraftClient.getInstance(), this.parent.parent, this.commandWidget, MinecraftClient.getInstance().textRenderer, false, false, 0, 7, false, Integer.MIN_VALUE) {
				@Override
				public void refresh() {
					if (parent.client == null) return;
					if (parent.client.getNetworkHandler() == null) return;
					super.refresh();
				}
			};
			commandSuggester.setWindowActive(true);
			commandSuggester.refresh();
		}

		@Override
		public void render(DrawContext context, /*? if <1.21.10 {*/ /*int index, int y, int x, int entryWidth, int entryHeight, *//*?}*/ int mouseX, int mouseY, boolean hovered, float tickProgress) {
			if (isFirst) {
				isFirst = false;
				this.removeButton = new ButtonGeneric(x + entryWidth, y, -1, 20, "Remove");
				this.removeButton.setActionListener((t, g) -> {
					this.parent.macro.actions.remove(this.parent.children().indexOf(this));
					this.parent.removeEntry(this);
				});
				this.removeButton.setX(removeButton.getX() - (removeButton.getWidth() + 10));
			}
			removeButton.setY(y + 6);

			if (this.isFocused()) {
				this.parent.parent.macro.setName(this.parent.parent.nameWidget.getText());
				MinecraftClient.getInstance().setScreen(new CommandEditScreen(parent.parent, this.commandWidget));
				this.parent.setSelected(null);
			}

			commandWidget.setFocused(this.isFocused());
			commandWidget.setPosition(x + 4, y + 3);
			commandWidget.setWidth(entryWidth - 100);
			commandWidget.setHeight(26);
			commandWidget.render(context, mouseX, mouseY, tickProgress);

			//? if <=1.21.5 {
			/*removeButton.render(mouseX, mouseY, removeButton.isMouseOver(), context);
			*///?} else {
			removeButton.render(context, mouseX, mouseY, removeButton.isMouseOver());
			//?}

			command.command = commandWidget.getText();
		}

		@Override
		public void mouseMoved(double mouseX, double mouseY) {
			commandWidget.mouseMoved(mouseX, mouseY);
			super.mouseMoved(mouseX, mouseY);
		}

		@Override
		public boolean mouseClicked(/*$ mouse_clicked_params {*/Click click, boolean doubleClick/*$}*/) {
			if (commandWidget.mouseClicked(/*$ mouse_clicked_args {*/click, doubleClick/*$}*/)) return true;
			else return removeButton.onMouseClicked(/*$ on_mouse_clicked_args {*/click, doubleClick/*$}*/);
		}

		@Override
		public boolean mouseReleased(/*$ dragged_released_params {*/Click click/*$}*/) {
			return commandWidget.mouseReleased(/*$ dragged_released_args {*/click/*$}*/);
		}

		@Override
		public boolean mouseDragged(/*$ dragged_released_params {*/Click click/*$}*/, double deltaX, double deltaY) {
			return commandWidget.mouseDragged(/*$ dragged_released_args {*/click/*$}*/, deltaX, deltaY);
		}

		@Override
		public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
			return commandWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		}

		@Override
		public boolean keyPressed(/*$ keyinput_params {*/KeyInput input/*$}*/) {
			return commandWidget.keyPressed(/*$ keyinput_args {*/input/*$}*/);
		}

		@Override
		public boolean keyReleased(/*$ keyinput_params {*/KeyInput input/*$}*/) {
			return commandWidget.keyReleased(/*$ keyinput_args {*/input/*$}*/);
		}

		@Override
		public boolean charTyped(/*$ charinput_params {*/CharInput input/*$}*/) {
			return commandWidget.charTyped(/*$ charinput_args {*/input/*$}*/);
		}

		@Override
		public boolean isMouseOver(double mouseX, double mouseY) {
			return commandWidget.isMouseOver(mouseX, mouseY);
		}
	}
}
