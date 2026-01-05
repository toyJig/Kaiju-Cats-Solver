import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



class KaijuCats{
    public static void main (String[] args){

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
        
        Tile[][] bestRun = new Tile[5][7];
        Simulator bestSimulator = new Simulator(null, null, null, map);
        int bestScore = 0;
        
        AIOptimizerV1 AI = new AIOptimizerV1(map);

        for (int i = 0; i<1000000; i++){
            Tile[][] mapClone = new Tile[5][7];
            for (int row = 0; row < map.length; row++) {
                for (int col = 0; col < map[row].length; col++) {
                    mapClone[row][col] = new Tile(map[row][col]); 
                }
            }
            
            for (int j = 0; j<25; j++){AI.guessOptimization();}

            Cat bCat = new Cat(CatColor.BLUE);
            Cat rCat = new Cat(CatColor.RED);
            Cat gCat = new Cat(CatColor.GREEN);            

            Simulator simulator = new Simulator(bCat, rCat, gCat, mapClone);
            int score = simulator.fullSimulationScore();
            if (score>bestScore){
                bestScore = score;
                bestRun = mapClone;
                bestSimulator = simulator;
            }
        }
        System.out.println(bestSimulator);
        for (Tile[] tiles : bestRun){
            for (Tile tile : tiles){
                System.out.print((tile.getCommandTop()!=null ? tile.getCommandTop().toString() : "null") + "," + (tile.getCommandBottom()!=null ? tile.getCommandBottom().toString() : "null") + "\t");
            }
            System.out.println();
        }
        System.out.println("BEST: " + bestScore);
        
    } 
}
