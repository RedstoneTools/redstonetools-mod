package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.gui.*;
import fi.dy.masa.malilib.gui.button.*;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;
import fi.dy.masa.malilib.util.*;
import fi.dy.masa.malilib.util.position.PositionUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import tools.redstone.redstonetools.malilib.widget.MacroBase;

import java.util.Locale;
import java.util.function.*;

public class GuiMacroEditor  extends GuiBase
{
	private final MacroBase macro;
	private ConfigOptionList configBlockSnap;
	private int colorY;

	public GuiMacroEditor(MacroBase macro)
	{
		this.macro = macro;
		this.title = StringUtils.translate("Macro editor", macro.getName());
		this.configBlockSnap = new ConfigOptionList("blockSnap", BlockSnap.NONE, "");
	}

	@Override
	public void initGui()
	{
		super.initGui();

		int x = 10;
		int y = 20;

		this.createMacroEditorElements(x, y);

		ButtonGeneric button = new ButtonGeneric(x, this.height - 24, -1, 20, GuiConfigs.ConfigGuiTab.MACROS.getDisplayName());
		this.addButton(button, new GuiMacroManager.ButtonListenerTab(GuiConfigs.ConfigGuiTab.MACROS));
	}

	private void createMacroEditorElements(int x, int y)
	{
		this.addLabel(x, y, -1, 14, 0xFFFFFFFF, StringUtils.translate("Name:"));
		y += 12;

		GuiTextFieldGeneric textField = new GuiTextFieldGeneric(x, y, 240, 17, this.textRenderer);
		textField.setText(this.macro.getName());
		var textField2 = this.addTextField(textField, (txtFld) -> { this.macro.setName(txtFld.getText()); return true; });
		y += textField2.getTextField().getHeight();

		this.addLabel(x, y, -1, 14, 0xFFFFFFFF, this.macro.isEnabled() ? "Enabled" : "Disabled");
	}

	protected void addBoxInput(int x, int y, int textFieldWidth, DoubleSupplier coordinateSource,
	                           DoubleConsumer coordinateConsumer)
	{
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		GuiTextFieldGeneric textField = new GuiTextFieldGeneric(x, y + 1, textFieldWidth, 14, textRenderer);
		textField.setText("" + coordinateSource.getAsDouble());

		this.addTextFieldAndButtonForBoxCoordinate(x + textFieldWidth + 4, y, textField,
				coordinateSource, coordinateConsumer);
	}

	protected int addLabel(int x, int y, PositionUtils.CoordinateType type)
	{
		String label = type.name() + ":";
		int labelWidth = 12;
		this.addLabel(x, y, labelWidth, 20, 0xFFFFFFFF, label);
		return labelWidth;
	}

	protected void addTextFieldAndButtonForBoxCoordinate(int x, int y, GuiTextFieldGeneric textField,
	                                                     DoubleSupplier coordinateSource,
	                                                     DoubleConsumer coordinateConsumer)
	{
		this.addTextField(textField, new TextFieldListenerDouble(coordinateConsumer));

		String hover = StringUtils.translate("malilib.gui.button.hover.plus_minus_tip");
		ButtonGeneric button = new ButtonGeneric(x, y, MaLiLibIcons.BTN_PLUSMINUS_16, hover);
		this.addButton(button, new ButtonListenerDoubleModifier(coordinateSource, (v) -> {
			coordinateConsumer.accept(v);
			textField.setText("" + coordinateSource.getAsDouble());
		}));
	}

	private String capitalize(String str)
	{
		if (str.length() > 1)
		{
			return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
		}

		return str.length() > 0 ? str.toUpperCase(Locale.ROOT) : str;
	}

	private void createShapeEditorElementIntField(int x, int y, IntSupplier supplier, IntConsumer consumer, String translationKey, boolean addButton)
	{
		this.addLabel(x + 12, y, -1, 12, 0xFFFFFFFF, translationKey);
		y += 11;

		GuiTextFieldInteger txtField = new GuiTextFieldInteger(x + 12, y, 40, 14, this.textRenderer);
		txtField.setText(String.valueOf(supplier.getAsInt()));
		this.addTextField(txtField, new TextFieldListenerInteger(consumer));

		if (addButton)
		{
			String hover = StringUtils.translate("malilib.gui.button.hover.plus_minus_tip");
			ButtonGeneric button = new ButtonGeneric(x + 54, y - 1, MaLiLibIcons.BTN_PLUSMINUS_16, hover);
			this.addButton(button, new ButtonListenerIntModifier(supplier, new ChainedIntConsumer(consumer, (val) -> txtField.setText(String.valueOf(supplier.getAsInt())) )));
		}
	}

	public static class MutableWrapperBox
	{
		protected final Consumer<Box> boxConsumer;
		protected double minX;
		protected double minY;
		protected double minZ;
		protected double maxX;
		protected double maxY;
		protected double maxZ;

		public MutableWrapperBox(Box box, Consumer<Box> boxConsumer)
		{
			this.minX = box.minX;
			this.minY = box.minY;
			this.minZ = box.minZ;
			this.maxX = box.maxX;
			this.maxY = box.maxY;
			this.maxZ = box.maxZ;
			this.boxConsumer = boxConsumer;
		}

		public double getMinX()
		{
			return this.minX;
		}

		public double getMinY()
		{
			return this.minY;
		}

		public double getMinZ()
		{
			return this.minZ;
		}

		public double getMaxX()
		{
			return this.maxX;
		}

		public double getMaxY()
		{
			return this.maxY;
		}

		public double getMaxZ()
		{
			return this.maxZ;
		}

		public void setMinX(double minX)
		{
			this.minX = minX;
			this.updateBox();
		}

		public void setMinY(double minY)
		{
			this.minY = minY;
			this.updateBox();
		}

		public void setMinZ(double minZ)
		{
			this.minZ = minZ;
			this.updateBox();
		}

		public void setMaxX(double maxX)
		{
			this.maxX = maxX;
			this.updateBox();
		}

		public void setMaxY(double maxY)
		{
			this.maxY = maxY;
			this.updateBox();
		}

		public void setMaxZ(double maxZ)
		{
			this.maxZ = maxZ;
			this.updateBox();
		}

		public void setMinCorner(Vec3d pos)
		{
			this.minX = pos.x;
			this.minY = pos.y;
			this.minZ = pos.z;
			this.updateBox();
		}

		public void setMaxCorner(Vec3d pos)
		{
			this.maxX = pos.x;
			this.maxY = pos.y;
			this.maxZ = pos.z;
			this.updateBox();
		}

		protected void updateBox()
		{
			Box box = new Box(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
			this.boxConsumer.accept(box);
		}
	}

	private record TextFieldListenerInteger(IntConsumer consumer) implements ITextFieldListener<GuiTextFieldInteger>
	{
		@Override
		public boolean onTextChange(GuiTextFieldInteger textField)
		{
			try
			{
				this.consumer.accept(Integer.parseInt(textField.getText()));
				return true;
			}
			catch (Exception ignore) {}

			return false;
		}
	}

	private record TextFieldListenerDouble(DoubleConsumer consumer) implements ITextFieldListener<GuiTextFieldGeneric>
	{
		@Override
		public boolean onTextChange(GuiTextFieldGeneric textField)
		{
			try
			{
				this.consumer.accept(Double.parseDouble(textField.getText()));
				return true;
			}
			catch (Exception ignore) {}

			return false;
		}
	}

	private record ChainedDoubleConsumer(DoubleConsumer consumerOne, DoubleConsumer consumerTwo) implements DoubleConsumer
	{
		@Override
		public void accept(double value)
		{
			this.consumerOne.accept(value);
			this.consumerTwo.accept(value);
		}
	}

	private record ChainedIntConsumer(IntConsumer consumerOne, IntConsumer consumerTwo) implements IntConsumer
	{
		@Override
		public void accept(int value)
		{
			this.consumerOne.accept(value);
			this.consumerTwo.accept(value);
		}
	}

	public static class ButtonListenerIntModifier implements IButtonActionListener
	{
		protected final IntSupplier supplier;
		protected final IntConsumer consumer;
		protected final int modifierShift;
		protected final int modifierControl;
		protected final int modifierAlt;

		public ButtonListenerIntModifier(IntSupplier supplier, IntConsumer consumer)
		{
			this(supplier, consumer, 8, 1, 4);
		}

		public ButtonListenerIntModifier(IntSupplier supplier, IntConsumer consumer, int modifierShift, int modifierControl, int modifierAlt)
		{
			this.supplier = supplier;
			this.consumer = consumer;
			this.modifierShift = modifierShift;
			this.modifierControl = modifierControl;
			this.modifierAlt = modifierAlt;
		}

		@Override
		public void actionPerformedWithButton(ButtonBase button, int mouseButton)
		{
			int amount = mouseButton == 1 ? -1 : 1;

			if (GuiBase.isShiftDown()) { amount *= this.modifierShift; }
			if (GuiBase.isCtrlDown())  { amount *= this.modifierControl; }
			if (GuiBase.isAltDown())   { amount *= this.modifierAlt; }

			this.consumer.accept(this.supplier.getAsInt() + amount);
		}
	}

	public static class ButtonListenerDoubleModifier implements IButtonActionListener
	{
		protected final DoubleSupplier supplier;
		protected final DoubleConsumer consumer;
		protected final int modifierShift;
		protected final int modifierControl;
		protected final int modifierAlt;

		public ButtonListenerDoubleModifier(DoubleSupplier supplier, DoubleConsumer consumer)
		{
			this(supplier, consumer, 8, 1, 4);
		}

		public ButtonListenerDoubleModifier(DoubleSupplier supplier, DoubleConsumer consumer, int modifierShift, int modifierControl, int modifierAlt)
		{
			this.supplier = supplier;
			this.consumer = consumer;
			this.modifierShift = modifierShift;
			this.modifierControl = modifierControl;
			this.modifierAlt = modifierAlt;
		}

		@Override
		public void actionPerformedWithButton(ButtonBase button, int mouseButton)
		{
			int amount = mouseButton == 1 ? -1 : 1;

			if (GuiBase.isShiftDown()) { amount *= this.modifierShift; }
			if (GuiBase.isCtrlDown())  { amount *= this.modifierControl; }
			if (GuiBase.isAltDown())   { amount *= this.modifierAlt; }

			this.consumer.accept(this.supplier.getAsDouble() + amount);
		}
	}
}
