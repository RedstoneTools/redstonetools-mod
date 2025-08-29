package tools.redstone.redstonetools.malilib.widget.macro;

import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class WidgetMacroEntry8 extends WidgetMacroEntry {
	public WidgetMacroEntry8(int x, int y, int width, int height, boolean isOdd, MacroBase macro, int listIndex, WidgetListMacros parent) {
		super(x, y, width, height, isOdd, macro, listIndex, parent);
		try {
			RenderUtils.drawRect(new DrawContext(MinecraftClient.getInstance(), null), 0, 0, 0, 0, 0x70FFFFFF)
			; // theres probably a better way to check for this method being a thing or not.
		} catch (NullPointerException ignored) {
		}
	}

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
}
