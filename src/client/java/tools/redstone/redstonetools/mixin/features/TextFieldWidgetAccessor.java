package tools.redstone.redstonetools.mixin.features;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.client.gui.components.EditBox;

@Mixin(EditBox.class)
public interface TextFieldWidgetAccessor {
	@Accessor("value")
	void setTextDirectly(String s);

	//? if >=1.21.10 {
	@Accessor
	List<EditBox.TextFormatter> getFormatters();
	//?}
}
