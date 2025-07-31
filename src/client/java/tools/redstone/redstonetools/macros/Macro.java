package tools.redstone.redstonetools.macros;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.gui.GuiModConfigs;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import tools.redstone.redstonetools.macros.actions.Action;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;
import tools.redstone.redstonetools.macros.actions.CommandAction;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class Macro {
    public void setName(String nname) {
        this.name = nname;
    }
    public static Macro buildEmpty() {
        return new Macro("",true,InputUtil.UNKNOWN_KEY,new ArrayList<>());
    }

    private KeyBinding keyBinding;
    public String name;
    private Key key;
    public boolean enabled;
    public List<CommandAction> actions;
    transient boolean _wasPressed = false;
    public List<String> actionsAsStringList = new AbstractList<>() {
        @Override
        public String get(int index) {
            return actions.get(index).command;
        }
        @Override
        public String set(int index, String element) {
            CommandAction action = actions.get(index);
            String old = action.command;
            action.command = element;
            return old;
        }
        @Override
        public int size() {
            return actions.size();
        }
        @Override
        public void add(int index, String element) {
            actions.add(index, new CommandAction(element));
        }
        @Override
        public String remove(int index) {
            String old = actions.get(index).command;
            actions.remove(index);
            return old;
        }
    };

    public final ConfigStringList config;
    public final GuiModConfigs configGui;

    public void fromStringList(List<String> stringList) {
        List<CommandAction> newActions = new ArrayList<>();
	    for (String s : stringList) {
		    newActions.add(new CommandAction(s));
	    }
        actions = newActions;
    }

    public Macro(String name, boolean enabled, Key key, List<CommandAction> actions) {
        this.name = name;
        this.enabled = enabled;
        this.key = key;
        this.actions = actions;
	    this.config = new ConfigStringList(this.name, ImmutableList.copyOf(actionsAsStringList));
        this.configGui = new GuiModConfigs("redstonetools", null, false, this.name);
    }

    public void run() {
        if (!enabled) {
            return;
        }

        for (Action action : actions) {
            action.run();
        }
    }

    public void setKey(Key key) {
        this.key = key;
        changeKeyBindingKeyCode();
    }

    public void changeKeyBindingKeyCode() {

        if (this.keyBinding != null) {

            keyBinding.setBoundKey(key);
            KeyBinding.updateKeysByCode();
        }

    }

    public Key getKey(){
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Macro macro) {

            if (actions.size() != macro.actions.size()) return false;
            for (int i = 0; i < actions.size(); i++) {
                if (!actions.get(i).equals(macro.actions.get(i))) return false;
            }

            return macro.name.equals(name) && macro.key.equals(key) && macro.enabled == enabled;
        }

        return super.equals(obj);
    }

}
