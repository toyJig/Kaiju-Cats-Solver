import java.util.List;
import java.util.ArrayList;

public class AIOptimizerV3 extends Thread{ //weighted random shit - Features: heat based acceptance, distance and finish penalty

    private Tile[][] map = new Tile[5][7];

    private int zoneInHeat = 500;
    private double zoneInAttempts;
    private int attempts = 0;
    
    public double allowedAttempts;
    
    //AI Weight Settings
    private final int initHeat = 100000;
    private double heat = initHeat;
    private int distancePenaltyMod = 500; // 500
    private int catsFinishedPenaltyMod = 0; //2000

    // Best run tracking
    public ArrayList<Simulator> bestSimulators = new ArrayList<>();
    public ArrayList<Integer> bestCompositeScores = new ArrayList<>();
    public ArrayList<Tile[][]> bestRunMaps = new ArrayList<>();
    public ArrayList<Integer> bestScoreMoneys = new ArrayList<>();

    boolean zonedIn = false;

    public void run(){
        System.out.println("Thread is being prepared by " + Thread.currentThread().getName());
        highHeatOptimization();
    }

    public AIOptimizerV3(Tile[][] map, double allowedAttempts, double zoneInAttempts, boolean zonedIn){
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                this.map[row][col] = new Tile(map[row][col]); 
            }
        }
        this.allowedAttempts = allowedAttempts;
        this.zoneInAttempts = zoneInAttempts;
        this.zonedIn = zonedIn;
    }

    public void highHeatOptimization(){
        // System.out.println("starting");
        int currentScoreMoney = 200;
        int currentBestScore = Integer.MIN_VALUE; 
        int restartThreshold = 0;
        int currentRunAttempts = 0;
        Tile[][] currentRunMap = new Tile[5][7];;
        Simulator currentSimulator = new Simulator(null, null, null, currentRunMap, 0); // stop the stupid may have no been init error

        while (restartThreshold < 10000 && attempts < allowedAttempts){
            Tile[][] mapClone = new Tile[5][7];
            if (currentRunAttempts == 0){
                for (int row = 0; row < map.length; row++) {
                    for (int col = 0; col < map[row].length; col++) {
                        mapClone[row][col] = new Tile(map[row][col]); 
                    }
                }
            }else if (currentRunMap[1][1] != null){
                for (int row = 0; row < map.length; row++) {
                    for (int col = 0; col < map[row].length; col++) {
                        mapClone[row][col] = new Tile(currentRunMap[row][col]); 
                    }
                }
            }else {
                throw new IllegalArgumentException("current run map not init");
            }
            Cat bCat = new Cat(CatColor.BLUE);
            Cat rCat = new Cat(CatColor.RED);
            Cat gCat = new Cat(CatColor.GREEN);
            Simulator simulator = new Simulator(bCat, rCat, gCat, mapClone, currentScoreMoney);

            for (int i = 0; i< Math.random() * 4 + 1; i++)
                changeRandomTile(simulator);
            if (currentRunAttempts == 0){ // initial randomization
                for (int i = 0; i< 16 ; i++)
                    changeRandomTile(simulator);
            }

            Tile[][] mapClonePreSim = new Tile[5][7];
            for (int row = 0; row < mapClone.length; row++) {
                for (int col = 0; col < mapClone[row].length; col++) {
                    mapClonePreSim[row][col] = new Tile(mapClone[row][col]); 
                }
            }
            
            simulator.fullSimulation();
            // System.out.println(simulator);
            // System.out.println(score);

            int distancePenalty = 0;
            for (Cat cat : simulator.getCats()){
                if (cat.getColor() == CatColor.BLUE){
                    distancePenalty += distancePenaltyMod * (6 - cat.getX());
                    distancePenalty += distancePenaltyMod * Math.abs(3 - cat.getY());
                }
                // if (cat.getColor() == CatColor.RED)
                //     distancePenalty += distancePenaltyMod * Math.abs(1 - cat.getY());
                // if (cat.getColor() == CatColor.GREEN)
                //     distancePenalty += distancePenaltyMod * Math.abs(2 - cat.getY());
            }
            distancePenalty += (3-simulator.getCatsFinished()) * catsFinishedPenaltyMod;
            System.out.println("Distance Penalty: " + distancePenalty);

            // distancePenalty = 0; // disable distance penalty for now
            int score = simulator.getAIScore() - distancePenalty;
            if (simulator.getCatsFinished() == 3){
                score += 5000; // big bonus for finishing
            }
            // System.out.println(Math.pow(Math.E, (score - currentBestScore)/heat));
            if (score > currentBestScore || ((Math.random() < Math.pow(Math.E, (score - currentBestScore)/heat)))) {
                if (score > currentBestScore){
                    restartThreshold = 0;
                }
                currentBestScore = score;
                currentRunMap = mapClonePreSim;
                currentSimulator = simulator;
                currentScoreMoney = simulator.getMoney();
                // System.out.println("MONEYYYY" + simulator.getMoney());
            }
            restartThreshold += 1;
            currentRunAttempts += 1;
            attempts += 1;
            heat = heat * (allowedAttempts-currentRunAttempts)/ allowedAttempts + 0.1;  // HEAT COOL DOWN FORMULA // 
            // heat = initHeat *(1-(attempts+1) / allowedAttempts);  // HEAT COOL DOWN FORMULA // 
            // heat -= 0.1;  // HEAT COOL DOWN FORMULA // 
            System.out.println("Attempt: " + attempts + "/" + allowedAttempts + " Current Best Score: " + currentBestScore + "\tHeat: " + heat);
            if (bestSimulators.size() > 0){
                // System.out.println("Current ABS AI Best" + bestSimulators.getFirst().getAIScore());
                System.out.println(bestCompositeScores.toString());
            }
            // System.out.println(restartThreshold);
        }
        System.out.println("end");
        if (bestSimulators.size() == 0){
            bestSimulators.add(currentSimulator);
            bestCompositeScores.add(currentBestScore);
            bestRunMaps.add(currentRunMap);
            bestScoreMoneys.add(currentScoreMoney);
            System.out.println("added first best");
        }else{
            for (int i = 0; i<bestSimulators.size(); i++){
                if (currentBestScore > bestCompositeScores.get(i)){
                    bestSimulators.add(i, currentSimulator);
                    bestCompositeScores.add(i, currentBestScore);
                    bestRunMaps.add(i, currentRunMap);
                    bestScoreMoneys.add(i, currentScoreMoney);
                    if (bestSimulators.size() > 5){
                        bestSimulators.removeLast();
                        bestRunMaps.removeLast();
                        bestScoreMoneys.removeLast();
                        bestCompositeScores.removeLast();
                    }
                    break;
                }
            }
        }
        
        if (attempts < allowedAttempts && attempts != allowedAttempts - 1){
            // heat = initHeat;
            highHeatOptimization();
        }
    }

    private void changeRandomTile(Simulator simulator){
        Tile[][] inputMap = simulator.getMap();
        int money = simulator.getMoney();
        // System.out.println(money);
        if (money > 200 || money < 0){
            throw new IllegalArgumentException("money broke lims");
        }
        List<Tile> buildingTiles = new ArrayList<>();
        for (Tile[] tiles : inputMap){
            for (Tile tile : tiles){
                if (tile.getTileType() == TileType.BIG_BUILDING || tile.getTileType() == TileType.SMALL_BUILDING || tile.getTileType() == TileType.POWER_PLANT){
                    buildingTiles.add(tile);
                }
            }
        }

        Tile tile = buildingTiles.get((int) (Math.random() * buildingTiles.size()));
        Command randoCommand;
        if (tile.getCommandTop() == Command.NULL && tile.getCommandBottom() == Command.NULL){
            randoCommand = Command.NULL.getRandomCommandNoNULL();
        }else{
            randoCommand = Command.NULL.getRandomCommand();
        }

        if ((int) (Math.random() * 2) == 0 && tile.getTileType() != TileType.POWER_PLANT){
            // System.out.println("From (bottom): " + tile.getCommandBottom().toString());
            if (money - randoCommand.getCost() + tile.getCommandBottom().getCost() >= 0){
                // System.out.println("Into (bottom): " + randoCommand.toString() + "\tMoney change:" + (randoCommand.getCost() - tile.getCommandBottom().getCost()) + " to " + money);
                simulator.addMoney(-(randoCommand.getCost() - tile.getCommandBottom().getCost()));
                tile.setCommandBottom(randoCommand); // sorta scuffed way to get random command bc it wont let me do it normally
            }else{
                // System.out.println("Into (bottom): NULL \t Money change:" + (randoCommand.getCost() - tile.getCommandBottom().getCost()) + " to " + (money - randoCommand.getCost() + tile.getCommandTop().getCost()));
                simulator.addMoney(tile.getCommandBottom().getCost());
                tile.setCommandBottom(Command.NULL);
            }
            
        }else{
            // System.out.println("From (top): " + tile.getCommandTop().toString());
            if (money - randoCommand.getCost() + tile.getCommandTop().getCost() >= 0){
                // System.out.println("Into (top): " + randoCommand.toString() + "\tMoney change:" + (randoCommand.getCost() - tile.getCommandTop().getCost()) + "\tmoney left: from " + money + " to " + (money - randoCommand.getCost() + tile.getCommandTop().getCost()));
                simulator.addMoney(-(randoCommand.getCost() - tile.getCommandTop().getCost()));
                tile.setCommandTop(randoCommand);
            }else{
                // System.out.println("Into (top): NULL \t Money change:" + (randoCommand.getCost() - tile.getCommandTop().getCost()) + " to " + money);
                simulator.addMoney(tile.getCommandTop().getCost());
                tile.setCommandTop(Command.NULL);
            }
        }
    }
    
    public Simulator getBestSimulator(){
        if (bestSimulators.size() == 0){
            throw new IllegalArgumentException("No best simulator found, run optimization first");
        }
        return bestSimulators.getFirst();
    }
    public Tile[][] getBestRunMap(){
        if (bestRunMaps.size() == 0){
            throw new IllegalArgumentException("No best run map found, run optimization first");
        }
        return bestRunMaps.getFirst();
    }
    public ArrayList<Integer> getBestCompositeScores() {
        return bestCompositeScores;
    }


}
