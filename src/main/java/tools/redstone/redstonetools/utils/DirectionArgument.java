package tools.redstone.redstonetools.utils;

public enum DirectionArgument {

    /** Indicates to use the direction of the target. */
    ME,

    /* Directions */
    FORWARD,
    BACK,
    NORTH,
    EAST,
    SOUTH,
    WEST,
    NORTHEAST,
    NORTHWEST,
    SOUTHEAST,
    SOUTHWEST,
    UP,
    DOWN,
    LEFT,
<<<<<<< HEAD
    RIGHT

=======
    RIGHT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
>>>>>>> dev
}