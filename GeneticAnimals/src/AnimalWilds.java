

import java.awt.Point;
import java.util.ArrayList;

public class AnimalWilds {

    private ArrayList<GeneticAnimal> animals;
    private int generations;
    private ArrayList<Point> obstacles;
    private ArrayList<Point> availableFood;
    private ArrayList<Point> eatenFood;
    private int border;
    private int divisions;
    private int numFood;
    private double seed;
    //TODO: configurable?
    private final static int NUM_ANIMALS = 10;
    private final static int NUM_WINNERS = 2;

    public enum State {

        NEW, RUNNING, PAUSED, STABLE
    };
    private State state;

    public AnimalWilds() {
        this(600);
    }

    public AnimalWilds(int border) {
        this.border = border;
        animals = new ArrayList<GeneticAnimal>();
        obstacles = new ArrayList<Point>();
        availableFood = new ArrayList<Point>();
        eatenFood = new ArrayList<Point>();
        reset(0.5);
    }

    /**
     * Randomize the animals in the current world.
     * @param seed
     */
    public void randomise() {
        animals.clear();
        int divLength = border / divisions;
        int placeAt = divLength * divisions / 2;
        for (int i = 0; i < NUM_ANIMALS; i++) {
            animals.add(PolygonAnimal.makeRandom(divLength, divLength / 5.0, divLength * 4 / 5.0, placeAt, placeAt));
        }
    }

    public int getBorder() {
        return border;
    }

    /**
     * Recreate the entire world
     * @param seed
     */
    public void reset(double seed) {
        animals.clear();
        obstacles.clear();
        availableFood.clear();
        eatenFood.clear(); 
        this.seed = seed % 1.0;
        divisions = (8 + (int) (8 * seed));
        if (divisions % 2 == 0) {
            divisions++;
        }
        numFood = divisions * divisions / 2;
        int divLength = border / divisions;
        int placeAt = divisions / 2;
        for (int x = 0; x < divisions; x++) {
            for (int y = 0; y < divisions; y++) {
                if (Math.abs(placeAt - x) > 1 || Math.abs(placeAt - y) > 1) {
                    int newObstacleX = divLength * x + (int) (Math.random() * divLength);
                    int newObstacleY = divLength * y + (int) (Math.random() * divLength);
                    obstacles.add(new Point(newObstacleX, newObstacleY));
                }
            }
        }
        int count = 0;
        while (count < numFood) {
            int x = (int) (Math.random() * divisions);
            int y = (int) (Math.random() * divisions);
            if (Math.abs(placeAt - x) > 1 || Math.abs(placeAt - y) > 1) {
                count++;
                int newObstacleX = divLength * x + (int) (Math.random() * divLength);
                int newObstacleY = divLength * y + (int) (Math.random() * divLength);
                availableFood.add(new Point(newObstacleX, newObstacleY));
            }
        }
        placeAt = placeAt * divLength;
        for (int i = 0; i < NUM_ANIMALS; i++) {
            animals.add(PolygonAnimal.makeRandom(divLength, divLength / 5.0, divLength * 3 / 5.0, placeAt, placeAt));
        }
    }

    public void run() {
        state = State.RUNNING;
    }

    public void pause() {
        state = State.PAUSED;
    }

    public int getNumAlive() {
        int numAlive = 0;
        for (GeneticAnimal animal : animals) {
            if (animal.isAlive()) {
                numAlive++;
            }
        }
        return numAlive;
    }

    public void update() {
        if (getNumAlive() > NUM_WINNERS) {
            for (GeneticAnimal animal : animals) {
                animal.update(this);
            }
            //check for collisions

            //update momentum

            //update positions (rotate as needed)
        } else {
            ArrayList<GeneticAnimal> genePool = new ArrayList<GeneticAnimal>();
            ArrayList<GeneticAnimal> nextGen = new ArrayList<GeneticAnimal>();
            for (GeneticAnimal animal : animals) {
                if (animal.isAlive()) {
                    genePool.add(animal);
                }
            }
            if(genePool.size()< NUM_WINNERS){
                genePool = animals;
            }
            for(int i = 0; i < NUM_ANIMALS; i++){
                int maleNum = (int)(Math.random()*(double)genePool.size());
                int femaleNum = (int)(Math.random()*(double)genePool.size());
                GeneticAnimal male = genePool.get(maleNum);
                GeneticAnimal female = genePool.get(femaleNum);
                nextGen.add(male.getCross(female));
            }
            animals = nextGen;
            for(Point a: eatenFood){
                availableFood.add(a);
            }
            eatenFood.clear();
        }

    }

    /**
     * Indicatse whether the game has reached a steady state.
     * @return
     */
    public boolean isStable() {
        return false;
    }

    public int getGenerations() {
        return generations;
    }

    public ArrayList<GeneticAnimal> getAnimals() {
        return animals;//TODO: ideally this would be cloned, but eh
    }

    public ArrayList<Point> getObstacles() {
        return obstacles;
    }

    public void eatFood(Point food){
        if(availableFood.remove(food)){
                eatenFood.add(food);
        }
        
    }
    
    public ArrayList<Point> getFood() {
        return availableFood;
    }
}
