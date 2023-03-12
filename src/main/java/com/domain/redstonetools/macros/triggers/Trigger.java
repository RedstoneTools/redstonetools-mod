package com.domain.redstonetools.macros.triggers;

public abstract class Trigger {
    private int triggerCount;

    protected void trigger() {
        ++triggerCount;
    }

    public void handle() {
        --triggerCount;
    }

    public boolean shouldBeHandled() {
        return triggerCount > 0;
    }

    public abstract void enable();
    public abstract void disable();
}
