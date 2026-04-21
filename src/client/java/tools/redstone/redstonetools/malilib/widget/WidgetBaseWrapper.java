package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;

import java.util.Collection;

public class WidgetBaseWrapper implements Element, Drawable, Selectable {
	private final WidgetBase wrapped;

	public WidgetBaseWrapper(WidgetBase wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return wrapped.onMouseClicked((int) mouseX, (int) mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		wrapped.onMouseReleased((int) mouseX, (int) mouseY, button);
		return Element.super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return Element.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		return wrapped.onMouseScrolled((int) mouseX, (int) mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (wrapped instanceof ConfigButtonKeybind configButtonKeybind) configButtonKeybind.onKeyPressed(keyCode);
		return wrapped.onKeyTyped(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return Element.super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		return wrapped.onCharTyped(chr, modifiers);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return wrapped.isMouseOver((int) mouseX, (int) mouseY);
	}

	@Override
	public void setFocused(boolean focused) {
		if (wrapped instanceof ConfigButtonKeybind configButtonKeybind) {
			if (focused) {
				configButtonKeybind.onSelected();
			} else {
				configButtonKeybind.onClearSelection();
			}
		}
	}

	@Override
	public boolean isFocused() {
		if (wrapped instanceof ConfigButtonKeybind configButtonKeybind) {
			return configButtonKeybind.isSelected();
		}
		return false;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		//? if <=1.21.5 {
		/*wrapped.render(mouseX, mouseY, this.isFocused(), context);
		*///? } else
		wrapped.render(context, mouseX, mouseY, this.isFocused());
	}

	@Override
	public SelectionType getType() {
		if (this.isFocused()) return SelectionType.FOCUSED;
		return SelectionType.NONE;
	}

	@Override
	public boolean isNarratable() {
		return Selectable.super.isNarratable();
	}

	@Override
	public Collection<? extends Selectable> getNarratedParts() {
		return Selectable.super.getNarratedParts();
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}
}
