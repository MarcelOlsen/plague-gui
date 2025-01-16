import ui.MainMenu;
import model.HighScoreManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Could not set look and feel");
        }

        SwingUtilities.invokeLater(() -> {
            HighScoreManager.loadScores();
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}