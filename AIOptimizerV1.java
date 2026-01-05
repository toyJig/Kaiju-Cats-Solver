import java.util.List;
import java.util.ArrayList;

public class AIOptimizerV1 { // do random shit

    private Tile[][] map;
    private List<Tile> buildingTiles = new ArrayList<>();
    private int money = 200;

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


        Command randoCommand = Command.POWERUP.getRandomCommand();

        if ((int) (Math.random() * 2) == 0 && tile.getTileType() != TileType.POWER_PLANT){
            if (money - randoCommand.getCost() + tile.getCommandBottom().getCost() >= 0){
                money -= randoCommand.getCost() - tile.getCommandBottom().getCost();
                tile.setCommandBottom(randoCommand); // sorta scuffed way to get random command bc it wont let me do it normally
            }else{
                money += tile.getCommandBottom().getCost();
                tile.setCommandBottom(Command.NULL);
            }
            // System.out.println("Into (bottom): " + tile.getCommandBottom().toString() + " Money change:" + (randoCommand.getCost() - tile.getCommandBottom().getCost()) + " to " + money);
        }else{
            if (money - randoCommand.getCost() + tile.getCommandTop().getCost() >= 0){
                money -= randoCommand.getCost() - tile.getCommandTop().getCost();
                tile.setCommandTop(randoCommand);
            }else{
                money += tile.getCommandTop().getCost();
                tile.setCommandTop(Command.NULL);
            }
            // System.out.println("Into (top): " + tile.getCommandTop().toString() + " Money change:" + (randoCommand.getCost() - tile.getCommandTop().getCost()) + " to " + money);
        }
        
    }

    public int getMoney() {
        return money;
    }
    
}
