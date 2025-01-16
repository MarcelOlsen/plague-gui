package model;

public class Cure {
    private double effectiveness = 0.04;
    private double spreadRate = 0.15;
    private double researchLevel = 1.0;

    public double getResearchLevel() {
        return researchLevel;
    }

    public double getSpreadRate() {
        return spreadRate;
    }

    public void improveEffectiveness(double amount) {
        effectiveness += amount;
    }

    public void improveSpreadRate(double amount) {
        spreadRate += amount;
    }

    public void improveResearch(double amount) {
        researchLevel += amount;
    }

    public int calculateHealing(int infectedPopulation) {
        return (int) (infectedPopulation * effectiveness * researchLevel * 0.6);
    }

    public void applyCure(Country country, int spreadAmount) {
        if (country.getInfected() > 0) {
            int healingAmount = calculateHealing(country.getInfected());
            int healed = Math.min(healingAmount, country.getInfected());
            country.setInfected(country.getInfected() - healed);

            country.applyImmunity(healed);
        }

        int healthyPeople = country.getPopulation() - country.getInfected() - country.getImmune();
        if (healthyPeople > 0) {
            int immunityAmount = (int) (spreadAmount * spreadRate * researchLevel * 1.5);
            country.applyImmunity(immunityAmount);
        }
    }
}