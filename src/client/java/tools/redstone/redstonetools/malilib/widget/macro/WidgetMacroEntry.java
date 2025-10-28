package tools.redstone.redstonetools.malilib.widget.macro;

import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;
import tools.redstone.redstonetools.malilib.config.MacroManager;
//? if >=1.21.10 {
import net.minecraft.client.gui.Click;
//?}

public class WidgetMacroEntry extends WidgetListEntryBase<MacroBase> {
	private final WidgetListMacros parent;
	public final MacroBase macro;
	protected final boolean isOdd;
	protected final int buttonsStartX;

	//? if <1.21.10 {
	/*@Override
	public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
		return super.canSelectAt(mouseX, mouseY, mouseButton) && mouseX < this.buttonsStartX;
	}
	*///?} else {
	@Override
	public boolean canSelectAt(Click click) {
		return super.canSelectAt(click) && click.x() < this.buttonsStartX;
	}
	//?}


	//? if >=1.21.8 {
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, boolean selected) {
		if (selected || this.isMouseOver(mouseX, mouseY)) {
			RenderUtils.drawRect(context, this.x, this.y, this.width, this.height, 0x70FFFFFF);
		} else if (this.isOdd) {
			RenderUtils.drawRect(context, this.x, this.y, this.width, this.height, 0x20FFFFFF);
		} else {
			RenderUtils.drawRect(context, this.x, this.y, this.width, this.height, 0x50FFFFFF);
		}
		this.drawString(context, this.x + 4, this.y + 7, 0xFFFFFFFF, this.macro.getName());

		super.render(context, mouseX, mouseY, selected);
	}

	@Override
	public void postRenderHovered(DrawContext context, int mouseX, int mouseY, boolean selected) {
		super.postRenderHovered(context, mouseX, mouseY, selected);
	}
	//?} else {
	/*@Override
	public void render(int mouseX, int mouseY, boolean selected, DrawContext context) {
		if (selected || this.isMouseOver(mouseX, mouseY)) {
			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x70FFFFFF);
		} else if (this.isOdd) {
			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x20FFFFFF);
		} else {
			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x50FFFFFF);
		}
		this.drawString(this.x + 4, this.y + 7, 0xFFFFFFFF, this.macro.getName(), context);

		super.render(mouseX, mouseY, selected, context);
	}

	@Override
	public void postRenderHovered(int mouseX, int mouseY, boolean selected, DrawContext context) {
		super.postRenderHovered(mouseX, mouseY, selected, context);
	}
	*///?}

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
				GuiMacroEditor gui = new GuiMacroEditor(Text.of(this.widget.macro.name), this.widget.macro, this.widget.parent.parent);
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
