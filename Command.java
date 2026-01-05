enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    Direction(){};

    public int getDx() {
        switch(this){
            case EAST: return 1;
            case WEST: return -1;
            default: return 0;
        }
    }
    public int getDy() {
        switch(this){
            case SOUTH: return 1;
            case NORTH: return -1;
            default: return 0;
        }
    }
    public Direction getOppositeDirection() {
        switch(this){
            case SOUTH: return NORTH;
            case NORTH: return SOUTH;
            case EAST: return WEST;
            case WEST: return EAST;
            default: System.err.println("stupid");; return null;
        }
    }
}

enum Command {
    TURN_N(10),
    TURN_S(10),
    TURN_E(10),
    TURN_W(10),
    STOMP(20),
    POWERUP(30),
    NULL(0);

    private final int cost;

    Command(int cost){this.cost = cost;}

    public int getCost(){return cost;}

    public Direction getDirection() {
        switch (this){
            case TURN_N: return Direction.NORTH;
            case TURN_E: return Direction.EAST;
            case TURN_S: return Direction.SOUTH;
            case TURN_W: return Direction.WEST;
            default: return null;
        }
    }
    public Command getRandomCommand(){
        int randomNum = (int) (Math.random()*7);
        if (randomNum == 0){
            return TURN_N;
        }
        if (randomNum == 1){
            return TURN_S;
        }
        if (randomNum == 2){
            return TURN_E;
        }
        if (randomNum == 3){
            return TURN_W;
        }
        if (randomNum == 4){
            return STOMP;
        }
        if (randomNum == 5){
            return POWERUP;
        }
        if (randomNum == 6){
            return NULL;
        }
        return null;
    }
    @Override
    public String toString() {
        switch (this){
            case TURN_N: return "TURN_N";
            case TURN_E: return "TURN_E";
            case TURN_S: return "TURN_S";
            case TURN_W: return "TURN_W";
            case STOMP: return "STOMP";
            case POWERUP: return "POWERUP";
            case NULL: return "null";
            default: return "null";
        }
    }
}

enum TileType {
    EMPTY,
    SMALL_BUILDING,
    BIG_BUILDING,
    POWER_PLANT,
    MUD,
    SPIKE_TRAP,
    BOULDER,
    CAT_BED,
    WALL
}

enum CatColor {
    RED,
    GREEN,
    BLUE;

    @Override
    public String toString() {
        switch (this){
            case RED: return "RED";
            case GREEN: return "GREEN";
            case BLUE: return "BLUE";
            default: return "?";
        }
    }
}

enum CatStatus {
    ACTIVE,
    STUCK_MUD,
    STOMPING,
    FINISHED,
    DEFEATED
}