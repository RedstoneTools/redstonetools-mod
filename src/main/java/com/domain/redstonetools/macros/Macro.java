package com.domain.redstonetools.macros;

import com.domain.redstonetools.macros.actions.Action;

import java.util.ArrayList;
import java.util.List;

public class Macro {

    public static Macro buildEmpty() {
        return new Macro("",true,-1,new ArrayList<>());
    }


    public String name;
    public int key;
    public boolean enabled;
    public List<Action> actions;

    private final Macro original;

    public Macro(String name, boolean enabled, int key, List<Action> actions) {
        this(name,enabled,key,actions,null);
    }

    public Macro(String name, boolean enabled, int key, List<Action> actions, Macro original) {
        this.name = name;
        this.enabled = enabled;
        this.key = key;
        this.actions = actions;
        this.original = original;
    }

    public void run() {
        if (!enabled) {
            return;
        }

        for (Action action : actions) {
            action.run();
        }
    }

    public void applyChangesToOriginal() {
        assert isCopy();

        original.name = name;
        original.enabled = enabled;
        original.key = key;
        original.actions = new ArrayList<>(actions);
    }

    public boolean isCopy(){
        return original != null;
    }

    public boolean isCopyOf(Macro macro) {
        return original == macro;
    }

    public Macro createCopy() {
        return new Macro(name,enabled,key,new ArrayList<>(actions),this);
    }

}
