import java.util.List;
import java.util.ArrayList;

public class AIOptimizerV1 { // do random shit

    private Tile[][] map;
    private List<Tile> buildingTiles = new ArrayList<>();

    public AIOptimizerV1(Tile[][] map){
        this.map = map;
        for (Tile[] tiles : map){
            for (Tile tile : tiles){
                if (tile.getTileType() == TileType.BIG_BUILDING || tile.getTileType() == TileType.SMALL_BUILDING || tile.getTileType() == TileType.POWER_PLANT){
                    buildingTiles.add(tile);
                    // System.out.println(tile);
                }
            }
        }
    }

    public void guessOptimization (){
        Tile tile = buildingTiles.get((int) (Math.random() * buildingTiles.size()));
        if ((int) (Math.random() * 2) == 0){
            tile.setCommandBottom(Command.POWERUP.getRandomCommand()); // sorta scuffed way to get random command bc it wont let me do it normally
            // System.out.println("Into (bottom): " + tile.getCommandBottom().toString());
        }else{
            tile.setCommandTop(Command.POWERUP.getRandomCommand());
            // System.out.println("Into (top): " + tile.getCommandTop().toString());
        }
    }
    
}
