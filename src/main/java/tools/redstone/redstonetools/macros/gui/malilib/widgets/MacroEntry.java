package tools.redstone.redstonetools.macros.gui.malilib.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ButtonOnOff;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import tools.redstone.redstonetools.macros.gui.malilib.GuiMacroEditor;

import java.util.List;

public class MacroEntry extends WidgetListEntryBase<MacroBase> {
	private final  MacroBase command;
	private final int buttonsStartX;
	private WidgetListMacros parent;

	public MacroEntry(int x, int y, int width, int height, MacroBase command, int listIndex, WidgetListMacros parent) {
		super(x, y, width, height, command, listIndex);

		this.command = command;
		this.parent  = parent;
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

	private int createButtonOnOff(int xRight, int y, boolean isCurrentlyOn, ButtonListener.Type type) {
		ButtonOnOff button = new ButtonOnOff(xRight, y, -1, true, type.getTranslationKey(), isCurrentlyOn);
		this.addButton(button, new ButtonListener(type, this));

		return button.getWidth() + 2;
	}

	@Override
	public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
		return super.canSelectAt(mouseX, mouseY, mouseButton) && mouseX < this.buttonsStartX;
	}

	@Override
	public void render(int mouseX, int mouseY, boolean selected, DrawContext context) {
		RenderUtils.color(1f, 1f, 1f, 1f);
		if (this.listIndex%2==1) {
			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x20FFFFFF);
		}
		// Draw a slightly lighter background for even entries
		else {
			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x50FFFFFF);
		}

		String name = this.command.getName();
		this.drawString(this.x + 4, this.y + 7, 0xFFFFFFFF, name, context);

		RenderUtils.color(1f, 1f, 1f, 1f);

		super.render(mouseX, mouseY, selected, context);
	}

	private static class ButtonListener implements IButtonActionListener
	{
		private final Type type;
		private final MacroEntry widget;

		public ButtonListener(Type type, MacroEntry widget)
		{
			this.type = type;
			this.widget = widget;
		}

		@Override
		public void actionPerformedWithButton(ButtonBase button, int mouseButton)
		{
			if (this.type == Type.CONFIGURE)
			{
				GuiMacroEditor gui = new GuiMacroEditor(this.widget.command);
				gui.setParent(GuiUtils.getCurrentScreen());
				GuiBase.openGui(gui);
			}
			else if (this.type == Type.REMOVE)
			{
				// TODO: do something here
				this.widget.parent.refreshEntries();
			}
		}

		public enum Type
		{
			CONFIGURE   ("minihud.gui.button.configure"),
			ENABLED     ("minihud.gui.button.shape_entry.enabled"),
			REMOVE      ("minihud.gui.button.remove");

			private final String translationKey;

			private Type(String translationKey)
			{
				this.translationKey = translationKey;
			}

			public String getTranslationKey()
			{
				return this.translationKey;
			}

			public String getDisplayName(Object... args)
			{
				return StringUtils.translate(this.translationKey, args);
			}
		}
	}
}