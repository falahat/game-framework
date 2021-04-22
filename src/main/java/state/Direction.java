package state;

public enum Direction {
    NORTH, SOUTH, WEST, EAST;

    public Direction clockwise() {
        switch (this) {
            case NORTH:
                return EAST;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            case EAST:
                return SOUTH;
            default:
                return null;
        }
    }

    public Direction counterClockwise() {
        switch (this) {
            case NORTH:
                return WEST;
            case SOUTH:
                return EAST;
            case WEST:
                return SOUTH;
            case EAST:
                return NORTH;
            default:
                return null;
        }
    }
}
