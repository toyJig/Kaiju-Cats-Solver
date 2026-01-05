public class Cat {
    private Direction direction;
    private CatStatus status;
    private CatColor color;
    private int power;
    private int x;
    private int y;

    public Cat (CatColor color){
        this.color = color;
        direction = Direction.EAST;
        status = CatStatus.ACTIVE;
        power = 0;
        x = 0;
        if (color == CatColor.BLUE){ y=0; }
        if (color == CatColor.RED){ y=2; }
        if (color == CatColor.GREEN){ y=4; }
    }

    public void setStatus(CatStatus status) {
        this.status = status;
        if (status == CatStatus.DEFEATED){
            power = 0;
        }
    }
    public void increasePower(int power) {
        this.power += power;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public CatColor getColor() {
        return color;
    }
    public Direction getDirection() {
        return direction;
    }
    public CatStatus getStatus() {
        return status;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getPower() {
        return power;
    }
}
