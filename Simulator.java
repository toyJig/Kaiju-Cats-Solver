import java.util.List;
import java.util.ArrayList;

public class Simulator {
    
    private int turn = 0;
    private List<CatMovement> movements;
    private List<Cat> cats = new ArrayList<>();
    private int catsFinished = 0;
    private int money;
    
    private Tile[][] map;
    
    public Simulator (Cat bCat, Cat rCat, Cat gCat, Tile[][] map, int money){
        cats.add(bCat);
        cats.add(rCat);
        cats.add(gCat); 
        this.map = map;
        this.money = money;
    }

    public void simulateTurn(){
        turn++;

        List<CatMovement> movements = new ArrayList<>();

        for (Cat cat : cats) {
            if (cat.getStatus() == CatStatus.ACTIVE) {
                int targetX = cat.getX() + cat.getDirection().getDx();
                int targetY = cat.getY() + cat.getDirection().getDy();
                if (targetX >= 0 && targetX <= 6 && targetY >= 0 && targetY <= 4){
                    TileType targetTileType = getTile(targetX, targetY).getTileType();
                    if (targetTileType != TileType.WALL && targetTileType != TileType.BOULDER){
                        movements.add(new CatMovement(cat,targetX, targetY)); //dunno what this is for rn
                        cat.setX(targetX);
                        cat.setY(targetY);
                    }
                }else{
                    cat.setDirection(cat.getDirection().getOppositeDirection());
                }
                Tile currentTile = getTile(cat.getX(), cat.getY());
                currentTile.applyEffects(cat, this);
            } else if (cat.getStatus() == CatStatus.STUCK_MUD) {
                cat.setStatus(CatStatus.ACTIVE);
            } else if ( cat.getStatus() == CatStatus.STOMPING) {
                Tile currentTile = getTile(cat.getX(), cat.getY());
                currentTile.applyEffects(cat, this);
                cat.setStatus(CatStatus.ACTIVE);
            }
        }
        
        //resolve cat fights if there is one (very inefficient)
        for (Cat cat : cats) {
            for (Cat comparisonCat : cats) {
                if (cat != comparisonCat){
                    if (cat.getX() == comparisonCat.getX() && cat.getY() == comparisonCat.getY()){
                        if (cat.getPower() < comparisonCat.getPower()){
                            cat.setStatus(CatStatus.DEFEATED);
                        }
                    }
                }
            }
        }
    }

    public void fullSimulation(){
        for (int i = 0; i < 15; i++){
            simulateTurn();
        }
    }

    public int getAIScore(){
        int score = 0;
        for (Cat cat : cats){
            score += cat.getPower();
        }
        return score;
    }

    public int getScore() {
        int score = 0;
        for (Cat cat : cats){
            if (cat.getStatus() == CatStatus.FINISHED)
                score += cat.getPower();
        }
        return score;
    }
    public Tile getTile(int x, int y){ return map[y][x]; }
    public void increaseCatsFinished() { catsFinished+=1; }
    public int getCatsFinished() { return catsFinished; }
    public List<Cat> getCats() { return cats; }
    public int getMoney(){ return money; }
    public void addMoney(int amount){ money += amount; }
    public Tile[][] getMap() { return map; }

    @Override
    public String toString() { //print result formatting
        String string = "";
        int score = 0;
        int AIScore = 0;
        for (Tile[] tileRow : map){
            for (Tile tile : tileRow){
                string += tile.toString() + "\t";
            }
            string += "\n";
        }
        string += "\n";
        for (Cat cat : cats){
            string += cat.getColor().toString() + ": \t(x:" + cat.getX() + " y: " + cat.getY() + ")\t Score:" + cat.getPower() + "\tStatus:" + cat.getStatus().toString() + "\t Direction:" + cat.getDirection().toString() + "\n";
            if (cat.getStatus() == CatStatus.FINISHED)
                score += cat.getPower();
            AIScore += cat.getPower();
        }
        string += "\nTotal Score: " + score;
        // string += "\nAI Score: " + AIScore;
        return string;
    }
}
