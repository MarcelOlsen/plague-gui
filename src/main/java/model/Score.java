package model;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {
    private final String playerName;
    private final int points;
    private final int time;

    public Score(String playerName, int points, int time) {
        this.playerName = playerName;
        this.points = points;
        this.time = time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPoints() {
        return points;
    }

    public int getTime() {
        return time;
    }

    @Override
    public int compareTo(Score other) {
        return Integer.compare(other.points, this.points);
    }

    @Override
    public String toString() {
        return playerName + " - Points: " + points + " (Time: " + time + "s)";
    }
}