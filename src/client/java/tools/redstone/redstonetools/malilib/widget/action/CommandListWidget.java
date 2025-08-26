package tools.redstone.redstonetools.malilib.widget.action;

import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.GuiMacroEditor2;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandListWidget extends EntryListWidget<CommandListWidget.CommandEntry> {
	private final GuiMacroEditor2 parent;
	private final MacroBase macro;

	public CommandListWidget(GuiMacroEditor2 parent, MinecraftClient mc, int width, int height, int y, int itemHeight, MacroBase macro) {
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
		this.macro.actions.add(new CommandAction(""));
		this.addEntryToTop(new CommandEntry(this.macro.actions.getLast(), this));
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
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.getSelectedOrNull() != null) {
			this.getSelectedOrNull().mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().mouseReleased(mouseX, mouseY ,button);
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		}
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().keyReleased(keyCode, scanCode, modifiers);
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		if (this.getSelectedOrNull() != null) {
			return this.getSelectedOrNull().charTyped(chr, modifiers);
		}
		return super.charTyped(chr, modifiers);
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
		private ButtonBase removeButton;

		public CommandEntry(CommandAction command, CommandListWidget parent) {
			this.parent = parent;
			this.command = command;
			this.commandWidget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 250, 50, Text.of(""));
			this.commandWidget.setText(command.command);
			this.commandWidget.setMaxLength(256);
			var commandSuggestor = new ChatInputSuggestor(MinecraftClient.getInstance(), this.parent.parent, this.commandWidget, MinecraftClient.getInstance().textRenderer, false, false, 0, 7, false, Integer.MIN_VALUE) {
				@Override
				public void refresh() {
					if (parent.client == null) return;
					if (parent.client.getNetworkHandler() == null) return;
					super.refresh();
				}
			};
			commandSuggestor.setWindowActive(true);
			commandSuggestor.refresh();
		}

		private static Method m;

		@Override
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
			if (isFirst) {
				isFirst = false;
				this.removeButton = new ButtonGeneric(x + entryWidth, y, -1, 20, "Remove");
				this.removeButton.setActionListener((t, g) -> {
					this.parent.macro.actions.remove(this.parent.children().indexOf(this));
					this.parent.removeEntry(this);
				});
				this.removeButton.setX(removeButton.getX() - (removeButton.getWidth() + 10));
			}
			removeButton.setY(y + 3);

			if (this.isFocused()) {
				MinecraftClient.getInstance().setScreen(new CommandEditScreen(parent.parent, this.commandWidget));
				this.parent.setSelected(null);
			}

			commandWidget.setFocused(this.isFocused());
			commandWidget.setPosition(x + 4, y + 3);
			commandWidget.setWidth(entryWidth - 100);
			commandWidget.setHeight(25);
			commandWidget.render(context, mouseX, mouseY, tickProgress);
			try {
				removeButton.render(context, mouseX, mouseY, removeButton.isMouseOver());
			} catch (NoSuchMethodError ignored) {
				if (m == null) {
					try {
						m = ButtonBase.class.getMethod("render", int.class, int.class, boolean.class, DrawContext.class);
					} catch (Exception e) {
						throw new RuntimeException("Something went wrong. Contact a redstonetools developer", e);
					}
				}
				try {
					m.invoke(removeButton, mouseX, mouseY, removeButton.isMouseOver(), context);
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new RuntimeException("Something went wrong. Contact a redstonetools developer", e);
				}
			}
			command.command = commandWidget.getText();
		}

		@Override
		public void mouseMoved(double mouseX, double mouseY) {
			commandWidget.mouseMoved(mouseX, mouseY);
			super.mouseMoved(mouseX, mouseY);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (commandWidget.mouseClicked(mouseX, mouseY, button)) return true;
			else return removeButton.onMouseClicked((int)mouseX, (int)mouseY, button);
		}

		@Override
		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return commandWidget.mouseReleased(mouseX, mouseY ,button);
		}

		@Override
		public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
			return commandWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}

		@Override
		public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
			return commandWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		}

		@Override
		public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
			return commandWidget.keyPressed(keyCode, scanCode, modifiers);
		}

		@Override
		public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
			return commandWidget.keyReleased(keyCode, scanCode, modifiers);
		}

		@Override
		public boolean charTyped(char chr, int modifiers) {
			return commandWidget.charTyped(chr, modifiers);
		}

		@Override
		public boolean isMouseOver(double mouseX, double mouseY) {
			return commandWidget.isMouseOver(mouseX, mouseY);
		}
	}
}
