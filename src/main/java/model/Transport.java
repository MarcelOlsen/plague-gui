package model;

public class Transport {
    private final Country source;
    private final Country destination;
    private final TransportType type;
    private boolean isActive = true;

    public Transport(Country source, Country destination, TransportType type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public Country getSource() {
        return source;
    }

    public Country getDestination() {
        return destination;
    }

    public TransportType getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}