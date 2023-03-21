package com.domain.redstonetools.macros;

import com.domain.redstonetools.macros.actions.Action;

import java.util.ArrayList;
import java.util.List;

public class Macro {
    public String name;
    public int key;
    public boolean enabled;
    public List<Action> actions;

    public Macro(String name, boolean enabled, int key, List<Action> actions) {
        this.name = name;
        this.enabled = enabled;
        this.key = key;
        this.actions = actions;
    }

    public void run() {
        if (!enabled) {
            return;
        }

        for (Action action : actions) {
            action.run();
        }
    }

    public static Macro buildEmpty() {
        return new Macro("",true,-1,new ArrayList<>());
    }

}
