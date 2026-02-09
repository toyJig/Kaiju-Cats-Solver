import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class KaijuCats{ 
    public static void main (String[] args){
        final double ATTEMPTS = 1000000;
        final double whenToZoneIn = ATTEMPTS/3;
        //----------------------read input file and put it in map-----------------------------
        Tile[][] map = new Tile[5][7];
        List<String> lines = new ArrayList<>();
        Path inputFile = Paths.get("start_input.txt");
        try {
            lines = Files.readAllLines(inputFile);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        int x = 0;
        int y = 0;
        System.out.println();
        for (String line : lines){
            String[] inputTiles = line.split(" ");
            for (String tile : inputTiles){
                if (tile != ""){
                    // System.out.print(tile + " ");
                    if (tile.equals("RStart")){
                        map[y][x] = new Tile(TileType.EMPTY);
                    } else if (tile.equals("GStart")){
                        map[y][x] = new Tile(TileType.EMPTY);
                    } else if (tile.equals("BStart")){
                        map[y][x] = new Tile(TileType.EMPTY);
                    } else if (tile.equals("RBed")){
                        map[y][x] = new Tile(TileType.CAT_BED, CatColor.RED);
                    } else if (tile.equals("GBed")){
                        map[y][x] = new Tile(TileType.CAT_BED, CatColor.GREEN);
                    } else if (tile.equals("BBed")){
                        map[y][x] = new Tile(TileType.CAT_BED, CatColor.BLUE);
                    } else if (tile.equals("hh")){ // small house
                        map[y][x] = new Tile(TileType.SMALL_BUILDING, 2);
                    }else if (tile.equals("HH")){ // big house
                        map[y][x] = new Tile(TileType.BIG_BUILDING, 2);
                    } else if (tile.equals("P")){ // powerplant
                        map[y][x] = new Tile(TileType.POWER_PLANT, 1);
                    } else if (tile.equals(".")){ // nothing 
                        map[y][x] = new Tile(TileType.EMPTY);
                    } else if (tile.equals("#")){ // restricted
                        map[y][x] = new Tile(TileType.WALL);
                    } else if (tile.equals("S")){ // spikes
                        map[y][x] = new Tile(TileType.SPIKE_TRAP);
                    } else if (tile.equals("X")){ // boulder
                        map[y][x] = new Tile(TileType.BOULDER);
                    } else if (tile.equals("M")){ // mud
                        map[y][x] = new Tile(TileType.MUD);
                    }
                    x += 1;
                }
            }
            // System.out.println();
            x = 0;
            y += 1;
        }

        //------------------------------set up simulation-------------------------------------

        
        //------------------------------attempt optimizations---------------------------------
        Thread AI = new AIOptimizerV3(map, ATTEMPTS, whenToZoneIn, false);
        // Thread AI2 = new AIOptimizerV3(map, ATTEMPTS, whenToZoneIn, false);
        // Thread AI3 = new AIOptimizerV3(map, ATTEMPTS, whenToZoneIn, false);

        AI.start();
        // AI2.start();
        // AI3.start();
        try {
            AI.join();
            // AI2.join();
            // AI3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //------------------------------print best result--------------------------------------
        int bestScore = ((AIOptimizerV3) AI).getBestSimulator().getScore();
        System.out.println(((AIOptimizerV3) AI).getBestSimulator());
        for (Tile[] tiles : ((AIOptimizerV3) AI).getBestRunMap()){
            for (Tile tile : tiles){
                System.out.print((tile.getCommandTop()!=null ? tile.getCommandTop().toString() : "null") + "," + (tile.getCommandBottom()!=null ? tile.getCommandBottom().toString() : "null") + "\t");
            }
            System.out.println();
        }
        System.out.println("BEST: " + bestScore);
        System.out.println("BEST AI SCORE: " + ((AIOptimizerV3) AI).getBestSimulator().getAIScore());
        System.out.println("Money Left:" + ((AIOptimizerV3) AI).getBestSimulator().getMoney());
        System.out.println("Composite Scores of Best Runs: " + ((AIOptimizerV3) AI).getBestCompositeScores().toString());
        
    } 
}
