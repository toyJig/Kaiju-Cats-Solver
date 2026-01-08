import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class KaijuCats{ 
    public static void main (String[] args){
        final int ATTEMPTS = 10000000;
        final int whenToZoneIn = ATTEMPTS/3;
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
        AIOptimizerV3 AI = new AIOptimizerV3(map, ATTEMPTS, whenToZoneIn, false);
        AI.highHeatOptimization();
        
        
        
        
        // //refine best run 1
        // AIOptimizerV2 AI1 = new AIOptimizerV2(bestRunMap, ATTEMPTS, whenToZoneIn, true);
        // for (int i = 0; i<whenToZoneIn; i++){
        //     Tile[][] mapClone = new Tile[5][7];
        //     for (int row = 0; row < bestRunMap.length; row++) {
        //         for (int col = 0; col < bestRunMap[row].length; col++) {
        //             mapClone[row][col] = new Tile(bestRunMap[row][col]); 
        //         }
        //     }
            
        //     AI1.guessOptimization();

        //     Cat bCat = new Cat(CatColor.BLUE);
        //     Cat rCat = new Cat(CatColor.RED);
        //     Cat gCat = new Cat(CatColor.GREEN);    

        //     Simulator simulator = new Simulator(bCat, rCat, gCat, mapClone);
        //     int score = simulator.fullSimulationScore();
        //     // System.out.println(simulator);
        //     // System.out.println(score);

        //     int distancePenalty = 0;
        //     for (Cat cat : simulator.getCats()){
        //         distancePenalty += 500 * (6 - cat.getX());
        //         if (cat.getColor() == CatColor.BLUE)
        //             distancePenalty += 500 * Math.abs(1 - cat.getY());
        //         if (cat.getColor() == CatColor.RED)
        //             distancePenalty += 500 * Math.abs(2 - cat.getY());
        //         if (cat.getColor() == CatColor.GREEN)
        //             distancePenalty += 500 * Math.abs(3 - cat.getY());
        //     }

        //     if (score - distancePenalty > bestScore){
        //         bestScore = score;
        //         bestRunMap = mapClone;
        //         bestSimulator = simulator;
        //         bestScoreMoney = AI.getMoney();
        //     }
        // }
        // //refine best run 2
        // AIOptimizerV2 AI2 = new AIOptimizerV2(bestRunMap2, ATTEMPTS, whenToZoneIn, true);
        // for (int i = 0; i<whenToZoneIn; i++){
        //     Tile[][] mapClone = new Tile[5][7];
        //     for (int row = 0; row < bestRunMap2.length; row++) {
        //         for (int col = 0; col < bestRunMap2[row].length; col++) {
        //             mapClone[row][col] = new Tile(bestRunMap2[row][col]); 
        //         }
        //     }
            
        //     AI2.guessOptimization();

        //     Cat bCat = new Cat(CatColor.BLUE);
        //     Cat rCat = new Cat(CatColor.RED);
        //     Cat gCat = new Cat(CatColor.GREEN);    

        //     Simulator simulator = new Simulator(bCat, rCat, gCat, mapClone);
        //     int score = simulator.fullSimulationScore();
        //     // System.out.println(simulator);
        //     // System.out.println(score);

        //     int distancePenalty = 0;
        //     for (Cat cat : simulator.getCats()){
        //         distancePenalty += 500 * (6 - cat.getX());
        //         if (cat.getColor() == CatColor.BLUE)
        //             distancePenalty += 500 * Math.abs(1 - cat.getY());
        //         if (cat.getColor() == CatColor.RED)
        //             distancePenalty += 500 * Math.abs(2 - cat.getY());
        //         if (cat.getColor() == CatColor.GREEN)
        //             distancePenalty += 500 * Math.abs(3 - cat.getY());
        //     }

        //     if (score - distancePenalty > bestScore){
        //         bestScore2 = score;
        //         bestRunMap2 = mapClone;
        //         bestSimulator2 = simulator;
        //         bestScoreMoney2 = AI.getMoney();
        //     }
        // }
        // //refine best run 3
        // AIOptimizerV2 AI3 = new AIOptimizerV2(bestRunMap3, ATTEMPTS, whenToZoneIn, true);
        // for (int i = 0; i<whenToZoneIn; i++){
        //     Tile[][] mapClone = new Tile[5][7];
        //     for (int row = 0; row < bestRunMap3.length; row++) {
        //         for (int col = 0; col < bestRunMap3[row].length; col++) {
        //             mapClone[row][col] = new Tile(bestRunMap3[row][col]); 
        //         }
        //     }

        //     AI3.guessOptimization();

        //     Cat bCat = new Cat(CatColor.BLUE);
        //     Cat rCat = new Cat(CatColor.RED);
        //     Cat gCat = new Cat(CatColor.GREEN);    

        //     Simulator simulator = new Simulator(bCat, rCat, gCat, mapClone);
        //     int score = simulator.fullSimulationScore();
        //     // System.out.println(simulator);
        //     // System.out.println(score);

        //     int distancePenalty = 0;
        //     for (Cat cat : simulator.getCats()){
        //         distancePenalty += 500 * (6 - cat.getX());
        //         if (cat.getColor() == CatColor.BLUE)
        //             distancePenalty += 500 * Math.abs(1 - cat.getY());
        //         if (cat.getColor() == CatColor.RED)
        //             distancePenalty += 500 * Math.abs(2 - cat.getY());
        //         if (cat.getColor() == CatColor.GREEN)
        //             distancePenalty += 500 * Math.abs(3 - cat.getY());
        //     }

        //     if (score - distancePenalty > bestScore){
        //         bestScore3 = score;
        //         bestRunMap3 = mapClone;
        //         bestSimulator3 = simulator;
        //         bestScoreMoney3 = AI.getMoney();
        //     }
        // }
        //------------------------------output best run----------------------------------------
        // int bestOverallScore = Math.max(bestScore, Math.max(bestScore2, bestScore3));
        // if (bestOverallScore == bestScore2){
        //     bestScore = bestScore2;
        //     bestRunMap = bestRunMap2;
        //     bestSimulator = bestSimulator2;
        //     bestScoreMoney = bestScoreMoney2;
        // } else if (bestOverallScore == bestScore3){
        //     bestScore = bestScore3;
        //     bestRunMap = bestRunMap3;
        //     bestSimulator = bestSimulator3;
        //     bestScoreMoney = bestScoreMoney3;
        // }

        int bestScore = AI.getBestSimulator().getScore();
        // if (bestScore <= 0){
        //     System.out.println("Not possible to optimize :(");
        //     return;
        // }
        System.out.println(AI.getBestSimulator());
        for (Tile[] tiles : AI.getBestRunMap()){
            for (Tile tile : tiles){
                System.out.print((tile.getCommandTop()!=null ? tile.getCommandTop().toString() : "null") + "," + (tile.getCommandBottom()!=null ? tile.getCommandBottom().toString() : "null") + "\t");
            }
            System.out.println();
        }
        System.out.println("BEST: " + bestScore);
        System.out.println("BEST AI SCORE: " + AI.getBestSimulator().getAIScore());
        System.out.println("Money Left:" + AI.getBestSimulator().getMoney());
        
    } 
}
