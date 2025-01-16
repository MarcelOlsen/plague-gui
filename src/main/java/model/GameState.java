package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameState {
    private static final int POINTS_PER_CURE = 1;
    private static final int POINTS_PER_IMMUNITY = 1;

    private final List<Country> countries;
    private volatile int points;
    private volatile int timeElapsed;
    private volatile boolean isGameOver;
    private final List<Transport> transports;
    private final Virus virus;
    private final Cure cure;
    private Country cureOrigin;

    private final ExecutorService calculationThread;
    private final ExecutorService simulationThread;
    private final AtomicBoolean isPaused;

    public GameState() {
        this.countries = new ArrayList<>();
        this.points = 0;
        this.timeElapsed = 0;
        this.isGameOver = false;
        this.transports = new ArrayList<>();
        this.virus = new Virus(2, 3);
        this.cure = new Cure();
        this.calculationThread = Executors.newSingleThreadExecutor();
        this.simulationThread = Executors.newSingleThreadExecutor();
        this.isPaused = new AtomicBoolean(false);

        startSimulation();
    }

    private void startSimulation() {
        simulationThread.submit(() -> {
            while (!isGameOver && !Thread.currentThread().isInterrupted()) {
                if (!isPaused.get()) {
                    spreadInfection();
                    spreadCure();
                    incrementTime();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public void pause() {
        isPaused.set(true);
    }

    public void resume() {
        isPaused.set(false);
    }

    public void shutdown() {
        calculationThread.shutdown();
        simulationThread.shutdown();
    }

    public void initializeCureOrigin() {
        this.cureOrigin = findCountryByName("USA");
        if (cureOrigin != null) {
            cureOrigin.applyImmunity(5000);
            System.out.println(cure.getResearchLevel());
        }
    }

    public void addCountry(Country country) {
        countries.add(country);
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void addPoints(int amount) {
        this.points += amount;
    }

    public int getPoints() {
        return points;
    }

    public void incrementTime() {
        this.timeElapsed++;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public void addTransport(String fromCountry, String toCountry, TransportType type) {
        Country source = findCountryByName(fromCountry);
        Country destination = findCountryByName(toCountry);
        Transport transport = new Transport(source, destination, type);
        transports.add(transport);

        source.addTransport(transport);
        destination.addTransport(transport);
    }

    public void spreadInfection() {
        calculationThread.submit(() -> {
            for (Transport transport : transports) {
                Country source = transport.getSource();
                Country destination = transport.getDestination();

                if (source.isInfected() && transport.isActive()) {
                    double spreadChance = switch (transport.getType()) {
                        case AIRPLANE -> 0.45;
                        case SHIP -> 0.35;
                        case TRAIN -> 0.25;
                        case BUS -> 0.15;
                        case CAR -> 0.10;
                    };

                    double sourceInfectionRate = (double) source.getInfected() / source.getPopulation();
                    spreadChance *= sourceInfectionRate * 2;

                    if (Math.random() < spreadChance) {
                        int spreadAmount = virus.calculateSpread(source);
                        destination.setInfected(destination.getInfected() + spreadAmount);
                    }
                }
            }

            for (Country country : countries) {
                if (country.isInfected()) {
                    country.updateInfection();
                }
            }
        });
    }

    public void spreadCure() {
        calculationThread.submit(() -> {
            for (Transport transport : transports) {
                Country source = transport.getSource();
                Country destination = transport.getDestination();

                if (source.getImmune() > 0 && transport.isActive()) {
                    double spreadChance = switch (transport.getType()) {
                        case AIRPLANE -> 0.75;
                        case SHIP -> 0.65;
                        case TRAIN -> 0.55;
                        case BUS -> 0.45;
                        case CAR -> 0.35;
                    };

                    double sourceImmunityRate = (double) source.getImmune() / source.getPopulation();
                    spreadChance *= sourceImmunityRate * 3;

                    if (Math.random() < spreadChance) {
                        int spreadAmount = (int) (source.getImmune() * 0.08 * cure.getSpreadRate()
                                * cure.getResearchLevel());
                        cure.applyCure(destination, spreadAmount);
                    }
                }
            }

            for (Country country : countries) {
                if (country.getImmune() > 0) {
                    cure.applyCure(country, country.getImmune());
                }
            }
        });
    }

    private Country findCountryByName(String name) {
        return countries.stream()
                .filter(country -> country.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Time: " + timeElapsed + "s, Points: " + points + ", Game Over: " + isGameOver;
    }

    public void curePeople(Country country, int amount) {
        int previousInfected = country.getInfected();
        country.setInfected(Math.max(0, previousInfected - amount));
        int cured = previousInfected - country.getInfected();

        if (cured > 0) {
            addPoints(cured * POINTS_PER_CURE);
        }
    }

    public void preventInfection(Country country, int amount) {
        addPoints(amount * POINTS_PER_IMMUNITY);
    }

    public int getTotalCured() {
        return 0;
    }

    public int getTotalPrevented() {
        return 0;
    }

    public List<Transport> getTransports() {
        return transports;
    }

    public void applyUpgrade(Upgrade upgrade) {
        switch (upgrade.getType()) {
            case CURE_EFFECTIVENESS -> cure.improveEffectiveness(0.02);
            case CURE_SPREAD -> cure.improveSpreadRate(0.01);
            case RESEARCH_BOOST -> {
                cure.improveEffectiveness(0.01);
                cure.improveSpreadRate(0.01);
                cure.improveResearch(0.1);
            }
            case VIRUS_SUPPRESSION -> virus.setDifficultyMultiplier(
                    Math.max(1, virus.getDifficultyMultiplier() - 1));
            case QUARANTINE_MEASURES -> countries.forEach(c -> c.getShutdownCriteria().improveAllThresholds(2000));
            case MEDICAL_RESEARCH -> cure.improveResearch(0.15);
            case IMMUNITY_BOOST -> cure.improveSpreadRate(0.02);
            case TREATMENT_EFFICIENCY -> cure.improveEffectiveness(0.03);
            case CONTAINMENT_PROTOCOL -> virus.reduceTravelSpreadRate(1);
        }
    }
}