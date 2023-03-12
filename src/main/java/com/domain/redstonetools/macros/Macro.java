package com.domain.redstonetools.macros;

import com.domain.redstonetools.macros.actions.Action;
import com.domain.redstonetools.macros.triggers.Trigger;

import java.util.List;

public class Macro {
    boolean enabled;
    List<Trigger> triggers;
    List<Action> actions;

    public void update() {
        if (!enabled) {
            return;
        }

        for (Trigger trigger : triggers) {
            handleTrigger(trigger);
        }
    }

    private void handleTrigger(Trigger trigger) {
        while (trigger.shouldBeHandled()) {
            runActions();

            trigger.handle();
        }
    }

    private void runActions() {
        for (Action action : actions) {
            action.run();
        }
    }
}
