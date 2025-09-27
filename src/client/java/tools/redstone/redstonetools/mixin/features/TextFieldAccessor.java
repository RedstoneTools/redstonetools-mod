package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextFieldWidget.class)
public interface TextFieldAccessor {
	@Accessor(value = "text")
	void setText2(String s);
}
