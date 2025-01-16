package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String filename = "highscores.dat";
    public static List<Score> scores = new ArrayList<>();

    public static void saveScores() {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scores);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error saving scores: " + e.getMessage());
        }
    }

    public static void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            scores = (List<Score>) ois.readObject();
        } catch (Exception e) {
            scores = new ArrayList<>();
        }
    }

    public static void addScore(String playerName, int points, int time) {
        Score newScore = new Score(playerName, points, time);
        scores.add(newScore);
        Collections.sort(scores);

        if (scores.size() > 10) {
            scores.remove(scores.size() - 1);
        }
        saveScores();
    }

    public static ArrayList<String> getScores() {
        ArrayList<String> scoreStrings = new ArrayList<>();
        for (Score score : scores) {
            scoreStrings.add(String.format("%s: %d points (%ds)",
                    score.getPlayerName(), score.getPoints(), score.getTime()));
        }
        return scoreStrings;
    }
}