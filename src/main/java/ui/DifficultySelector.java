package ui;

import javax.swing.*;
import java.awt.*;

public class DifficultySelector extends JDialog {
    private final String[] difficulties = { "Easy", "Medium", "Hard" };
    private JComboBox<String> difficultyBox;

    public DifficultySelector(MainMenu parent) {
        super(parent, "Select Difficulty", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel("Select Difficulty Level:", SwingConstants.CENTER);
        difficultyBox = new JComboBox<>(difficulties);
        JButton startButton = new JButton("Start Game");

        panel.add(label);
        panel.add(difficultyBox);
        panel.add(startButton);

        startButton.addActionListener(e -> {
            String difficulty = (String) difficultyBox.getSelectedItem();
            dispose();
            parent.startGame(difficulty);
        });

        add(panel);
    }
}