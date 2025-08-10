package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class WidgetMacroEntry4 extends WidgetMacroEntry {
	public WidgetMacroEntry4(int x, int y, int width, int height, boolean isOdd, MacroBase macro, int listIndex, WidgetListMacros parent) {
		super(x, y, width, height, isOdd, macro, listIndex, parent);
	}

//	@Override
//	public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
//		return super.canSelectAt(mouseX, mouseY, mouseButton) && mouseX < this.buttonsStartX;
//	}
//
//	@Override
//	public void render(int mouseX, int mouseY, boolean selected, DrawContext context) {
//		if (selected || this.isMouseOver(mouseX, mouseY)) {
//			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x70FFFFFF);
//		} else if (this.isOdd) {
//			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x20FFFFFF);
//		}
//		else {
//			RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x50FFFFFF);
//		}
//		this.drawString(this.x + 4, this.y + 7, 0xFFFFFFFF, this.macro.getName(),context);
//
//		super.render(mouseX, mouseY, selected, context);
//	}
//
//	@Override
//	public void postRenderHovered(int mouseX, int mouseY, boolean selected, DrawContext context) {
//		super.postRenderHovered(mouseX, mouseY, selected, context);
//	}
}
