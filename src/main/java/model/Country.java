package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Country {
    private final String name;
    private final int population;
    private int infected;
    private int immune;
    private final Map<TransportType, Double> shutdownThresholds;
    private List<Transport> activeTransports;

    public Country(String name, int population) {
        this.name = name;
        this.population = population;
        this.infected = 0;
        this.immune = 0;
        this.activeTransports = new ArrayList<>();
        this.shutdownThresholds = initializeThresholds();
    }

    private Map<TransportType, Double> initializeThresholds() {
        Map<TransportType, Double> thresholds = new HashMap<>();
        switch (name) {
            case "USA" -> {
                thresholds.put(TransportType.AIRPLANE, 0.5);
                thresholds.put(TransportType.TRAIN, 0.6);
                thresholds.put(TransportType.SHIP, 0.55);
                thresholds.put(TransportType.BUS, 0.7);
                thresholds.put(TransportType.CAR, 0.8);
            }
            default -> {
                thresholds.put(TransportType.AIRPLANE, 0.6);
                thresholds.put(TransportType.TRAIN, 0.7);
                thresholds.put(TransportType.SHIP, 0.65);
                thresholds.put(TransportType.BUS, 0.75);
                thresholds.put(TransportType.CAR, 0.85);
            }
        }
        return thresholds;
    }

    public void addTransport(Transport transport) {
        activeTransports.add(transport);
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public int getInfected() {
        return infected;
    }

    public int getImmune() {
        return immune;
    }

    public void setInfected(int infected) {
        if (infected > population) {
            this.infected = population;
        } else {
            this.infected = infected;
        }
    }

    public boolean isFullyInfected() {
        return infected == population;
    }

    public List<Transport> getActiveTransports() {
        return activeTransports;
    }

    public boolean isInfected() {
        return infected > 0;
    }

    public void updateInfection() {
        if (isInfected() && !isFullyInfected()) {
            double growthRate = 1.02;
            int newInfected = (int) (infected * growthRate);
            setInfected(Math.min(newInfected, population));
        }
    }

    public void checkTransportShutdown() {
        double infectionRate = (double) infected / population;
        for (Transport transport : activeTransports) {
            if (infectionRate >= shutdownThresholds.get(transport.getType())) {
                transport.setActive(false);
            }
        }
    }

    public void applyImmunity(int amount) {
        int healthyNonImmune = population - infected - immune;
        int actualImmune = Math.min(amount, healthyNonImmune);
        immune += actualImmune;
    }

    public TransportShutdownCriteria getShutdownCriteria() {
        return new TransportShutdownCriteria(shutdownThresholds);
    }

    @Override
    public String toString() {
        return name + " (Population: " + population + ", Infected: " + infected + ")";
    }
}