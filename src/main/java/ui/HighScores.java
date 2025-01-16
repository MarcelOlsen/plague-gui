package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import model.HighScoreManager;

public class HighScores extends JFrame {
    private JList<String> scoresList;
    private DefaultListModel<String> listModel;

    public HighScores() {
        setTitle("High Scores");
        setSize(400, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        scoresList = new JList<>(listModel);

        scoresList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(scoresList);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            dispose();
            new MainMenu().setVisible(true);
        });
        add(backButton, BorderLayout.SOUTH);

        loadHighScores();

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                e.getWindow().dispose();
                new MainMenu().setVisible(true);
            }
        });
    }

    private void loadHighScores() {
        ArrayList<String> scores = HighScoreManager.getScores();
        for (String score : scores) {
            listModel.addElement(score);
        }
    }
}
