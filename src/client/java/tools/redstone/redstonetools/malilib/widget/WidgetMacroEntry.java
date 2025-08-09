package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.*;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;
import tools.redstone.redstonetools.malilib.config.MacroManager;

public class WidgetMacroEntry extends WidgetListEntryBase<MacroBase> {
	private final WidgetListMacros parent;
	public final MacroBase macro;
	private final boolean isOdd;
	private final int buttonsStartX;

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

	@Override
	public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
		return super.canSelectAt(mouseX, mouseY, mouseButton) && mouseX < this.buttonsStartX;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, boolean selected) {
		// Draw a lighter background for the hovered and the selected entry
		if (selected || this.isMouseOver(mouseX, mouseY)) {
			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x70FFFFFF);
		} else if (this.isOdd) {
			RenderUtils.drawRect(context, this.x, this.y, this.width, this.height, 0x20FFFFFF);
		}
		// Draw a slightly lighter background for even entries
		else {
			RenderUtils.drawRect(context, this.x, this.y, this.width, this.height, 0x50FFFFFF);
		}
		this.drawString(context, this.x + 4, this.y + 7, 0xFFFFFFFF, this.macro.getName());

		super.render(context, mouseX, mouseY, selected);
	}

	@Override
	public void postRenderHovered(DrawContext context, int mouseX, int mouseY, boolean selected) {
		super.postRenderHovered(context, mouseX, mouseY, selected);
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
				GuiMacroEditor gui = new GuiMacroEditor(this.widget.macro, this.widget.parent);
				gui.setParent(GuiUtils.getCurrentScreen());
				GuiBase.openGui(gui);
				this.widget.parent.refreshEntries();
			} else if (this.type == Type.REMOVE) {
				MacroManager.removeMacro(this.widget.macro);
				this.widget.parent.refreshEntries();
				InputEventHandler.getKeybindManager().unregisterKeybindProvider(this.widget.macro.handler);
				InputEventHandler.getInputManager().unregisterKeyboardInputHandler(this.widget.macro.handler);
				InputEventHandler.getInputManager().unregisterMouseInputHandler(this.widget.macro.handler);
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
