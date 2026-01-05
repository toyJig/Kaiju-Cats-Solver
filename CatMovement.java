public class CatMovement {
    private int x;
    private int y;
    private Cat cat;

    public CatMovement(Cat cat, int x, int y){
        this.x = x;
        this.y = y;
        this.cat = cat;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Cat getCat() {
        return cat;
    }
}
