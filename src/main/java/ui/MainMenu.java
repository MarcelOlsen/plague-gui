package ui;

import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

public class MainMenu extends JFrame {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;

    private final JButton newGameButton = new JButton();
    private final JButton highScoresButton = new JButton();
    private final JButton exitButton = new JButton();
    private final JPanel mainPanel;

    public MainMenu() {
        mainPanel = new JPanel(new GridBagLayout());
        initializeFrame();
        initializeButtons();
        setupLayout();
    }

    private void initializeFrame() {
        setTitle("Virus Defense Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        add(mainPanel);
    }

    private void initializeButtons() {
        configureButton(newGameButton, "New Game");
        configureButton(highScoresButton, "High Scores");
        configureButton(exitButton, "Exit");
        setupButtonListeners();
    }

    private void configureButton(JButton button, String text) {
        button.setText(text);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setFocusPainted(false);
    }

    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        mainPanel.add(newGameButton, gbc);
        mainPanel.add(highScoresButton, gbc);
        mainPanel.add(exitButton, gbc);
    }

    private void setupButtonListeners() {
        newGameButton.addActionListener(e -> {
            DifficultySelector difficultySelector = new DifficultySelector(this);
            difficultySelector.setVisible(true);
        });

        highScoresButton.addActionListener(e -> {
            HighScores highScores = new HighScores();
            highScores.setVisible(true);
            dispose();
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    public void startGame(String difficulty) {
        setVisible(false);
        GameWindow gameWindow = new GameWindow(difficulty);
        gameWindow.setVisible(true);
    }
}