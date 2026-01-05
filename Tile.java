// enum TileType {
//     EMPTY,
//     SMALL_BUILDING,
//     BIG_BUILDING,
//     POWER_PLANT,
//     MUD,
//     SPIKE_TRAP,
//     BOULDER,
//     CAT_BED,
//     WALL
// }

public class Tile {
    private TileType tileType;
    private CatColor color;
    private int stompsLeft;
    private Command commandTop = Command.NULL;
    private Command commandBottom = Command.NULL;
    
    public Tile(TileType tileType){ this.tileType = tileType; }
    public Tile(TileType tileType, int stompsLeft){ this.tileType = tileType; this.stompsLeft = stompsLeft; }
    public Tile(TileType tileType, CatColor color){ this.tileType = tileType; this.color = color;}
    public Tile(Tile other) {
        this.tileType = other.tileType;
        this.color = other.color;
        this.stompsLeft = other.stompsLeft;
        this.commandTop = other.commandTop;
        this.commandBottom = other.commandBottom;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void applyEffects(Cat cat, Simulator simulator){
        if (tileType == TileType.CAT_BED){
            if (color == cat.getColor()){
                cat.setStatus(CatStatus.FINISHED);
                if (simulator.getCatsFinished() == 0){
                    cat.increasePower(2000);
                } else if (simulator.getCatsFinished() == 1){
                    cat.increasePower(cat.getPower()*2);
                } else{
                    cat.increasePower(cat.getPower()*4);
                }
            }else{
                cat.setDirection(cat.getDirection().getOppositeDirection());
                cat.setX(cat.getX() + cat.getDirection().getDx());
                cat.setY(cat.getY() + cat.getDirection().getDy());
                simulator.getTile(cat.getX(), cat.getY()).applyEffects(cat, simulator);
            }
        } else if (stompsLeft == 2 && commandTop != null){    // change cat's direction according to the command
            if (commandTop.getDirection() != null){
                cat.setDirection(commandTop.getDirection());
            }else if (commandTop == Command.STOMP){
                cat.setStatus(CatStatus.STOMPING);
            }else if (commandTop == Command.POWERUP){
                cat.increasePower(1000);
            }
        }else if (stompsLeft == 1 && commandBottom != null){
            if (commandBottom.getDirection() != null){
                cat.setDirection(commandBottom.getDirection());
            }else if (commandBottom == Command.STOMP){
                cat.setStatus(CatStatus.STOMPING);
            }else if (commandBottom == Command.POWERUP){
                cat.increasePower(1000);
            }
        }

        switch (tileType) {
            case TileType.EMPTY: break;
            case TileType.SMALL_BUILDING: 
                if (stompsLeft > 0){ cat.increasePower(250); stompsLeft-=1;} break;
            case TileType.BIG_BUILDING:  
                if (stompsLeft > 0){ cat.increasePower(500); stompsLeft-=1;} break;
            case TileType.POWER_PLANT:  
                if (stompsLeft > 0){ cat.increasePower(cat.getPower()); stompsLeft-=1; } break;
            case TileType.MUD: cat.setStatus(CatStatus.STUCK_MUD); break;
            case TileType.SPIKE_TRAP: cat.increasePower(-1*cat.getPower()/2); break;
            case TileType.BOULDER: break;
            case TileType.WALL: break;
            default: break;
        }
    }

    public void setCommandBottom(Command commandBottom) {
        this.commandBottom = commandBottom;
    }
    public void setCommandTop(Command commandTop) {
        this.commandTop = commandTop;
    }
    public Command getCommandBottom() {
        return commandBottom;
    }
    public Command getCommandTop() {
        return commandTop;
    }

    @Override
    public String toString() {
        switch (tileType) {
            case TileType.EMPTY: return ".";
            case TileType.SMALL_BUILDING: if (stompsLeft == 2) {return "h2";} else if (stompsLeft == 1) {return "h1";} return ".";
            case TileType.BIG_BUILDING: if (stompsLeft == 2) {return "H2";} else if (stompsLeft == 1) {return "H1";} return ".";
            case TileType.POWER_PLANT: if (stompsLeft == 1) {return "P";} return ".";
            case TileType.MUD:  return "M";
            case TileType.SPIKE_TRAP:  return "M";
            case TileType.BOULDER:  return "X";
            case TileType.WALL:  return "#";
            default: break;
        }
        return "?";
    }
}
