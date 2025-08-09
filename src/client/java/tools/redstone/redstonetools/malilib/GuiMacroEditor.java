package tools.redstone.redstonetools.malilib;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.gui.*;
import fi.dy.masa.malilib.gui.button.*;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;
import fi.dy.masa.malilib.util.*;
import fi.dy.masa.malilib.util.position.PositionUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.config.MacroManager;
import tools.redstone.redstonetools.malilib.widget.MacroBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class GuiMacroEditor extends GuiConfigsBase
{
	private final MacroBase macro;
	private final ConfigStringList commands;

	public GuiMacroEditor(MacroBase macro)
	{
		super(10, 50, RedstoneTools.MOD_ID, null, macro.getName(), "");
		this.macro = macro;
		this.title = macro.getName();
		this.commands = new ConfigStringList("Commands", ImmutableList.of());
		commands.setStrings(macro.actionsAsStringList);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		int x = 10;

		ButtonGeneric button = new ButtonGeneric(x, this.height - 24, -1, 20, GuiConfigs.ConfigGuiTab.MACROS.getDisplayName());
		this.addButton(button, (a, b) -> {
			updateConfigs();
			GuiBase.openGui(new GuiMacroManager());
		});
	}

	@Override
	public void close() {
		updateConfigs();
		GuiBase.openGui(new GuiMacroManager());
	}

	private void updateConfigs() {
		this.macro.setName(this.configName.getName());
		this.macro.setEnabled(this.configEnabled.getBooleanValue());
		this.macro.keybind = this.configHotkey.getKeybind();
		this.macro.actions.clear();
		for (String s : this.configCommands.getStrings()) {
			this.macro.actions.add(new CommandAction(s));
		}
		MacroManager.saveChanges();
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

	private ConfigBoolean    configEnabled;
	private ConfigHotkey     configHotkey;
	private ConfigStringList configCommands;
	private ConfigString     configName;

	// I'm sorry
	@Override
	public List<ConfigOptionWrapper> getConfigs() {
		this.configEnabled  = new ConfigBoolean("enabled", this.macro.isEnabled(), "Whether or not to enable the hotkey", "Enabled");
		this.configHotkey   = new ConfigHotkey("hotkey", this.macro.keybind.getStringValue(), "Pressing this hotkey will activate the macro", "Hotkey");
		this.configCommands = new ConfigStringList("commands", ImmutableList.copyOf(this.macro.actionsAsStringList), "Commands executed by this macro", "Commands");
		this.configName     = new ConfigString("name", this.macro.getName(), "Name of the macro", "Name");

		List<ConfigBoolean> configsB = new ArrayList<>();
		configsB.add(configEnabled);

		List<ConfigHotkey> configsH = new ArrayList<>();
		configsH.add(configHotkey);

		List<ConfigStringList> configsSL = new ArrayList<>();
		configsSL.add(configCommands);

		List<ConfigString> configsS = new ArrayList<>();
		configsS.add(configName);

		List<ConfigOptionWrapper> configOptionWrappers = new ArrayList<>();
		configOptionWrappers.addAll(immutableListToList(ConfigOptionWrapper.createFor(configsS)));
		configOptionWrappers.addAll(immutableListToList(ConfigOptionWrapper.createFor(configsH)));
		configOptionWrappers.addAll(immutableListToList(ConfigOptionWrapper.createFor(configsSL)));
		configOptionWrappers.addAll(immutableListToList(ConfigOptionWrapper.createFor(configsB)));
		return ImmutableList.copyOf(configOptionWrappers);
	}

	private <E> List<E> immutableListToList(List<E> aFor) {
		if (aFor instanceof ImmutableList<E> IL) {
			return new ArrayList<>(IL);
		} else {
			throw new IllegalStateException("Trying to convert a " + aFor.getClass() + " to List");
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
