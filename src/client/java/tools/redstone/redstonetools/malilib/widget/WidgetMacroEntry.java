package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import tools.redstone.redstonetools.malilib.GuiMacroEditor2;
import tools.redstone.redstonetools.malilib.config.MacroManager;

public class WidgetMacroEntry extends WidgetListEntryBase<MacroBase> {
	private final WidgetListMacros parent;
	public final MacroBase macro;
	protected final boolean isOdd;
	protected final int buttonsStartX;

	@Override
	public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
		return super.canSelectAt(mouseX, mouseY, mouseButton) && mouseX < this.buttonsStartX;
	}

	public WidgetMacroEntry(int x, int y, int width, int height, boolean isOdd,
							MacroBase macro, int listIndex, WidgetListMacros parent) {
		super(x, y, width, height, macro, listIndex);
		this.macro = macro;
		this.isOdd = isOdd;
		this.parent = parent;
		y += 1;

		int posX = x + width - 2;

		posX -= this.addButton(posX, y, ButtonListener.Type.REMOVE);
		posX -= this.addButton(posX, y, ButtonListener.Type.CONFIGURE);
		this.buttonsStartX = posX;
	}

	protected int addButton(int x, int y, ButtonListener.Type type) {
		ButtonGeneric button = new ButtonGeneric(x, y, -1, true, type.getDisplayName());
		this.addButton(button, new ButtonListener(type, this));

		return button.getWidth() + 1;
	}

	private static class ButtonListener implements IButtonActionListener {
		private final Type type;
		private final WidgetMacroEntry widget;

		public ButtonListener(Type type, WidgetMacroEntry widget) {
			this.type = type;
			this.widget = widget;
		}

		@Override
		public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
			if (this.type == Type.CONFIGURE) {
				GuiMacroEditor2 gui = new GuiMacroEditor2(this.widget.macro);
				gui.setParent(GuiUtils.getCurrentScreen());
				GuiBase.openGui(gui);
				this.widget.parent.refreshEntries();
			} else if (this.type == Type.REMOVE) {
				InputEventHandler.getKeybindManager().unregisterKeybindProvider(this.widget.macro.handler);
				InputEventHandler.getInputManager().unregisterKeyboardInputHandler(this.widget.macro.handler);
				InputEventHandler.getInputManager().unregisterMouseInputHandler(this.widget.macro.handler);
				InputEventHandler.getKeybindManager().updateUsedKeys();
				MacroManager.removeMacro(this.widget.macro);
				MacroManager.saveChanges();
				this.widget.parent.refreshEntries();
			}
		}

		public enum Type {
			CONFIGURE("Edit"),
			REMOVE("Remove");

			private final String name;

			Type(String name) {
				this.name = name;
			}

			public String getDisplayName(Object... args) {
				return StringUtils.translate(this.name, args);
			}
		}
	}
}
