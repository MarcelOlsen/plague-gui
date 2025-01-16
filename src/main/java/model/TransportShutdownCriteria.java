package model;

import java.util.Map;

public class TransportShutdownCriteria {
    private int airplaneThreshold = 10000;
    private int trainThreshold = 15000;
    private int shipThreshold = 20000;
    private int busThreshold = 25000;
    private int carThreshold = 30000;

    private double healthcareEffectiveness = 1.0;
    private double preventionRate = 1.0;
    private double researchEfficiency = 1.0;
    private double medicalCapacity = 1.0;
    private double emergencyResponse = 1.0;

    public TransportShutdownCriteria() {
    }

    public TransportShutdownCriteria(Map<TransportType, Double> thresholds) {
        if (thresholds != null) {
            airplaneThreshold = (int) (thresholds.getOrDefault(TransportType.AIRPLANE, 0.6) * 10000);
            trainThreshold = (int) (thresholds.getOrDefault(TransportType.TRAIN, 0.7) * 10000);
            shipThreshold = (int) (thresholds.getOrDefault(TransportType.SHIP, 0.65) * 10000);
            busThreshold = (int) (thresholds.getOrDefault(TransportType.BUS, 0.75) * 10000);
            carThreshold = (int) (thresholds.getOrDefault(TransportType.CAR, 0.85) * 10000);
        }
    }

    public void improveAirplaneThreshold(int amount) {
        airplaneThreshold += amount;
    }

    public void improveTrainThreshold(int amount) {
        trainThreshold += amount;
    }

    public void improveShipThreshold(int amount) {
        shipThreshold += amount;
    }

    public void improveBusThreshold(int amount) {
        busThreshold += amount;
    }

    public void improveCarThreshold(int amount) {
        carThreshold += amount;
    }

    public void improveAllThresholds(int amount) {
        airplaneThreshold += amount;
        trainThreshold += amount;
        shipThreshold += amount;
        busThreshold += amount;
        carThreshold += amount;
    }

    public void improveHealthcare(double amount) {
        healthcareEffectiveness += amount;
    }

    public void improvePreventionRate(double amount) {
        preventionRate += amount;
    }

    public void improveResearch(double amount) {
        researchEfficiency += amount;
    }

    public void improveMedicalCapacity(double amount) {
        medicalCapacity += amount;
    }

    public void improveEmergencyResponse(double amount) {
        emergencyResponse += amount;
    }

    public int getAirplaneThreshold() {
        return (int) (airplaneThreshold * emergencyResponse);
    }

    public int getTrainThreshold() {
        return (int) (trainThreshold * emergencyResponse);
    }

    public int getShipThreshold() {
        return (int) (shipThreshold * emergencyResponse);
    }

    public int getBusThreshold() {
        return (int) (busThreshold * emergencyResponse);
    }

    public int getCarThreshold() {
        return (int) (carThreshold * emergencyResponse);
    }

    public double getHealthcareMultiplier() {
        return healthcareEffectiveness * emergencyResponse;
    }

    public double getPreventionMultiplier() {
        return preventionRate * emergencyResponse;
    }

    public double getResearchMultiplier() {
        return researchEfficiency * emergencyResponse;
    }

    public double getMedicalCapacityMultiplier() {
        return medicalCapacity * emergencyResponse;
    }
}