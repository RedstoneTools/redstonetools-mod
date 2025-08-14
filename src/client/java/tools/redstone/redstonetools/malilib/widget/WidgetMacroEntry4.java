package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetContainer;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WidgetMacroEntry4 extends WidgetMacroEntry {
	Method drawRectMethod;
	Method drawStringMethod;
	Method renderMethod;
	Method postRenderHoveredMethod;

	MethodHandle superRenderHandle;
	MethodHandle superPostRenderHoveredHandle;

	public WidgetMacroEntry4(int x, int y, int width, int height, boolean isOdd, MacroBase macro, int listIndex, WidgetListMacros parent) {
		super(x, y, width, height, isOdd, macro, listIndex, parent);
		try {
			this.drawRectMethod = RenderUtils.class.getDeclaredMethod("drawRect", int.class, int.class, int.class, int.class, int.class);
			this.drawStringMethod = WidgetBase.class.getMethod("drawString", int.class, int.class, int.class, String.class, DrawContext.class);
			this.renderMethod = WidgetContainer.class.getMethod("render", int.class, int.class, boolean.class, DrawContext.class);
			this.postRenderHoveredMethod = WidgetContainer.class.getMethod("postRenderHovered", int.class, int.class, boolean.class, DrawContext.class);
			this.drawRectMethod.setAccessible(true);
			this.drawStringMethod.setAccessible(true);
			this.renderMethod.setAccessible(true);
			this.postRenderHoveredMethod.setAccessible(true);

			MethodHandles.Lookup lookup = MethodHandles.lookup();
			this.superRenderHandle = lookup.unreflectSpecial(renderMethod, WidgetMacroEntry4.class);
			this.superPostRenderHoveredHandle = lookup.unreflectSpecial(postRenderHoveredMethod, WidgetMacroEntry4.class);
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private void invokepostRenderHovered(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
		try {
			MethodHandle bound = superPostRenderHoveredHandle.bindTo(this);
			bound.invokeWithArguments(mouseX, mouseY, selected, drawContext);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private void invokerender(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
		try {
			MethodHandle bound = superRenderHandle.bindTo(this);
			bound.invokeWithArguments(mouseX, mouseY, selected, drawContext);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private void invokedrawRect(int x, int y, int width, int height, int color) {
		try {
			drawRectMethod.invoke(null, x, y, width, height, color);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private void invokedrawString(int x, int y, int color, String text, DrawContext drawContext) {
		try {
			drawStringMethod.invoke(this, x, y, color, text, drawContext);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

//  @Override
	public void render(int mouseX, int mouseY, boolean selected, DrawContext context) {
		if (selected || this.isMouseOver(mouseX, mouseY)) {
			invokedrawRect(this.x, this.y, this.width, this.height, 0x70FFFFFF);
		} else if (this.isOdd) {
			invokedrawRect(this.x, this.y, this.width, this.height, 0x20FFFFFF);
		}
		else {
			invokedrawRect(this.x, this.y, this.width, this.height, 0x50FFFFFF);
		}
		invokedrawString(this.x + 4, this.y + 7, 0xFFFFFFFF, this.macro.getName(), context);

		invokerender(mouseX, mouseY, selected, context);
	}

//  @Override
	public void postRenderHovered(int mouseX, int mouseY, boolean selected, DrawContext context) {
		invokepostRenderHovered(mouseX, mouseY, selected, context);
	}
}
