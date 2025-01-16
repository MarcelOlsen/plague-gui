package model;

public class Virus {
    private int difficultyMultiplier;
    private double travelSpreadRate;

    public Virus(int difficultyMultiplier, double travelSpreadRate) {
        this.difficultyMultiplier = difficultyMultiplier;
        this.travelSpreadRate = travelSpreadRate;
    }

    public int getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public void setDifficultyMultiplier(int value) {
        this.difficultyMultiplier = value;
    }

    public void reduceTravelSpreadRate(double amount) {
        this.travelSpreadRate = Math.max(0.1, travelSpreadRate - amount);
    }

    public int calculateSpread(Country source) {
        double populationFactor = Math.log10(source.getPopulation()) / 10.0;
        return (int) (source.getInfected() * 0.03 * travelSpreadRate * difficultyMultiplier * populationFactor);
    }
}