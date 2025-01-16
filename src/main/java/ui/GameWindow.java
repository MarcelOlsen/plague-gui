package ui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.awt.geom.Point2D;
import java.net.URL;

public class GameWindow extends JFrame {
    private GameState gameState;
    private HashMap<String, CountryButton> countryButtons;
    private JLabel pointsLabel;
    private JLabel timeLabel;
    private Timer gameTimer;
    private JLayeredPane mapPanel;
    private UpgradePanel upgradePanel;
    private List<TransportVisual> transportVisuals = new ArrayList<>();
    private Timer animationTimer;
    private CountryDetailsPanel countryDetailsPanel;
    private static final int BASE_MAP_WIDTH = 800;
    private static final int BASE_MAP_HEIGHT = 600;

    public GameWindow(String difficulty) {
        setTitle("Virus Defense Game");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeGame(difficulty);
        createGameInterface();
        setupKeyBindings();

        pack();
        setLocationRelativeTo(null);

        gameTimer = new Timer(1000, e -> updateGame());
        gameTimer.start();
    }

    private void initializeGame(String difficulty) {
        gameState = new GameState();
        countryButtons = new HashMap<>();

        int difficultyMultiplier = "Hard".equals(difficulty) ? 3 : "Medium".equals(difficulty) ? 2 : 1;

        setupCountries();
        setupTransports();
        gameState.initializeCureOrigin();

        Country china = findCountryByName("China");
        if (china != null) {
            china.setInfected(1000);
        }

        startVisualUpdates();
    }

    private void startVisualUpdates() {
        Timer visualUpdateTimer = new Timer(100, e -> {
            SwingUtilities.invokeLater(() -> {
                updateLabels();
                for (Country country : gameState.getCountries()) {
                    updateCountryButton(country);
                }

                if (checkGameOver()) {
                    ((Timer) e.getSource()).stop();
                    gameTimer.stop();
                    gameState.shutdown();
                    showGameOverDialog("Game Over!");
                }
            });
        });
        visualUpdateTimer.start();
    }

    @Override
    public void dispose() {
        super.dispose();
        gameState.shutdown();
    }

    private void createGameInterface() {
        mapPanel = new JLayeredPane();
        mapPanel.setPreferredSize(new Dimension(800, 600));
        mapPanel.setBackground(Color.WHITE);
        mapPanel.setOpaque(true);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pointsLabel = new JLabel("Points: 0");
        timeLabel = new JLabel("Time: 0s");
        statusPanel.add(pointsLabel);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(timeLabel);

        upgradePanel = new UpgradePanel(gameState);

        countryDetailsPanel = new CountryDetailsPanel();

        JPanel transportLayer = new JPanel() {
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        transportLayer.setLayout(null);
        transportLayer.setOpaque(false);
        transportLayer.setBounds(0, 0, 800, 600);

        for (Transport transport : gameState.getTransports()) {
            CountryButton fromButton = countryButtons.get(transport.getSource().getName());
            CountryButton toButton = countryButtons.get(transport.getDestination().getName());

            if (fromButton != null && toButton != null) {
                Point fromCenter = new Point(
                        fromButton.getX() + fromButton.getWidth() / 2,
                        fromButton.getY() + fromButton.getHeight() / 2);
                Point toCenter = new Point(
                        toButton.getX() + toButton.getWidth() / 2,
                        toButton.getY() + toButton.getHeight() / 2);

                TransportVisual visual = new TransportVisual(
                        new Point2D.Double(fromCenter.x, fromCenter.y),
                        new Point2D.Double(toCenter.x, toCenter.y),
                        transport.getType());
                visual.setBounds(0, 0, 800, 600);
                transportVisuals.add(visual);
                transportLayer.add(visual);
            }
        }

        mapPanel.add(transportLayer, JLayeredPane.DEFAULT_LAYER);

        for (Country country : gameState.getCountries()) {
            CountryButton button = countryButtons.get(country.getName());
            if (button != null) {
                button.addActionListener(e -> countryDetailsPanel.updateDetails(country));
                mapPanel.add(button, JLayeredPane.PALETTE_LAYER);
            }
        }

        animationTimer = new Timer(50, e -> {
            for (TransportVisual visual : transportVisuals) {
                visual.updateAnimation();
            }
        });
        animationTimer.start();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = mapPanel.getSize();
                for (Component comp : mapPanel.getComponents()) {
                    if (comp instanceof JPanel) {
                        comp.setBounds(0, 0, size.width, size.height);
                    }
                }
                updateCountryButtonPositions();
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(statusPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(upgradePanel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(countryDetailsPanel, BorderLayout.EAST);

        add(mapPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupCountries() {
        addCountry("China", 600, 200, 1439323776);
        addCountry("Japan", 700, 150, 126476461);
        addCountry("India", 550, 300, 1380004385);
        addCountry("Russia", 450, 100, 145912025);
        addCountry("Germany", 350, 150, 83783942);
        addCountry("France", 300, 200, 65273511);
        addCountry("UK", 250, 100, 67886011);
        addCountry("USA", 150, 200, 331002651);
        addCountry("Canada", 150, 100, 37742154);
        addCountry("Brazil", 200, 450, 212559417);
    }

    private void setupTransports() {
        setupTransport("China", "Japan", TransportType.AIRPLANE);
        setupTransport("China", "Russia", TransportType.TRAIN);
        setupTransport("China", "India", TransportType.BUS);
        setupTransport("China", "USA", TransportType.SHIP);

        setupTransport("India", "China", TransportType.BUS);
        setupTransport("India", "Russia", TransportType.AIRPLANE);
        setupTransport("India", "UK", TransportType.SHIP);
        setupTransport("India", "Japan", TransportType.AIRPLANE);

        setupTransport("USA", "UK", TransportType.AIRPLANE);
        setupTransport("USA", "Canada", TransportType.CAR);
        setupTransport("USA", "Japan", TransportType.SHIP);
        setupTransport("USA", "Brazil", TransportType.AIRPLANE);
        setupTransport("USA", "France", TransportType.SHIP);

        setupTransport("Russia", "Germany", TransportType.TRAIN);
        setupTransport("Russia", "China", TransportType.BUS);
        setupTransport("Russia", "Japan", TransportType.SHIP);
        setupTransport("Russia", "France", TransportType.AIRPLANE);

        setupTransport("Japan", "USA", TransportType.SHIP);
        setupTransport("Japan", "China", TransportType.AIRPLANE);
        setupTransport("Japan", "Russia", TransportType.SHIP);

        setupTransport("Germany", "France", TransportType.TRAIN);
        setupTransport("Germany", "UK", TransportType.BUS);
        setupTransport("Germany", "Russia", TransportType.TRAIN);
        setupTransport("Germany", "Brazil", TransportType.AIRPLANE);

        setupTransport("France", "Germany", TransportType.CAR);
        setupTransport("France", "UK", TransportType.TRAIN);
        setupTransport("France", "Brazil", TransportType.AIRPLANE);
        setupTransport("France", "Canada", TransportType.AIRPLANE);

        setupTransport("UK", "France", TransportType.TRAIN);
        setupTransport("UK", "Germany", TransportType.BUS);
        setupTransport("UK", "USA", TransportType.AIRPLANE);

        setupTransport("Brazil", "USA", TransportType.AIRPLANE);
        setupTransport("Brazil", "France", TransportType.AIRPLANE);
        setupTransport("Brazil", "UK", TransportType.SHIP);

        setupTransport("Canada", "USA", TransportType.CAR);
        setupTransport("Canada", "UK", TransportType.AIRPLANE);
        setupTransport("Canada", "France", TransportType.SHIP);
    }

    private void setupTransport(String from, String to, TransportType type) {
        gameState.addTransport(from, to, type);
    }

    private void addCountry(String name, int x, int y, int population) {
        Country country = new Country(name, population);
        gameState.addCountry(country);
        CountryButton button = new CountryButton(country, x, y);
        button.setName(x + "," + y);
        countryButtons.put(name, button);
    }

    private Country findCountryByName(String name) {
        return gameState.getCountries().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void updateGame() {
        gameState.incrementTime();
        updateLabels();
        updateInfections();
    }

    private void updateLabels() {
        timeLabel.setText("Time: " + gameState.getTimeElapsed() + "s");
        pointsLabel.setText("Points: " + gameState.getPoints());
    }

    private void updateInfections() {
        gameState.spreadInfection();

        for (Country country : gameState.getCountries()) {
            if (country.getInfected() > 0) {
                int preventedInfections = calculatePreventedInfections(country);
                if (preventedInfections > 0) {
                    gameState.preventInfection(country, preventedInfections);
                }

                int cureRate = calculateCureRate(country);
                if (cureRate > 0) {
                    gameState.curePeople(country, cureRate);
                }
            }
            updateCountryButton(country);
        }
    }

    private int calculatePreventedInfections(Country country) {
        int basePreventionRate = 50;
        return (int) (basePreventionRate * getPreventionMultiplier());
    }

    private int calculateCureRate(Country country) {
        int baseCureRate = 25;
        return (int) (baseCureRate * getHealthcareMultiplier());
    }

    private double getPreventionMultiplier() {
        return upgradePanel.getActiveUpgrades().stream()
                .filter(u -> u.getName().contains("Prevention"))
                .map(u -> 0.2)
                .reduce(1.0, Double::sum);
    }

    private double getHealthcareMultiplier() {
        return upgradePanel.getActiveUpgrades().stream()
                .filter(u -> u.getName().contains("Healthcare"))
                .map(u -> 0.3)
                .reduce(1.0, Double::sum);
    }

    private int calculateSpreadAmount(Country source, Country destination) {
        int baseSpread = (int) (source.getInfected() * 0.01);
        return (int) (baseSpread * getTransportEffectiveness(source, destination));
    }

    private double getTransportEffectiveness(Country source, Country destination) {
        return Math.max(0.1, 1.0 - (upgradePanel.getActiveUpgrades().stream()
                .filter(u -> u.affectsTransport())
                .count() * 0.2));
    }

    private void updateCountryButton(Country country) {
        CountryButton button = countryButtons.get(country.getName());
        if (button != null) {
            int previousInfected = country.getInfected();
            button.updateAppearance();

            if (country.getInfected() < previousInfected) {
                button.startHealingAnimation();
            }
        }
    }

    private boolean checkGameOver() {
        if (gameState.isGameOver()) {
            return false;
        }

        boolean allInfected = gameState.getCountries().stream()
                .allMatch(Country::isFullyInfected);

        boolean noInfections = gameState.getCountries().stream()
                .allMatch(country -> country.getInfected() == 0);

        if (allInfected) {
            gameState.setGameOver(true);
            return true;
        } else if (noInfections && gameState.getTimeElapsed() > 10) {
            gameState.setGameOver(true);
            int bonusPoints = 5000;
            gameState.addPoints(bonusPoints);
            return true;
        }

        return false;
    }

    private void showGameOverDialog(String message) {
        if (!gameState.isGameOver()) {
            return;
        }

        String finalMessage = gameState.getCountries().stream()
                .allMatch(Country::isFullyInfected) ? "Game Over - Everyone is infected!"
                        : "Victory! You've eliminated the virus!\nBonus Points: 5000";

        String playerName = JOptionPane.showInputDialog(this,
                finalMessage + "\nEnter your name for the high score:");

        if (playerName != null && !playerName.trim().isEmpty()) {
            HighScoreManager.addScore(playerName, gameState.getPoints(),
                    gameState.getTimeElapsed());

            int option = JOptionPane.showConfirmDialog(this,
                    "Would you like to view the high scores?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                new HighScores().setVisible(true);
            }
        }
        dispose();
        new MainMenu().setVisible(true);
    }

    private void setupKeyBindings() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                "exitGame");

        getRootPane().getActionMap().put("exitGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainMenu().setVisible(true);
            }
        });
    }

    private void updateCountryButtonPositions() {
        Dimension currentSize = mapPanel.getSize();
        double scaleX = (double) currentSize.width / BASE_MAP_WIDTH;
        double scaleY = (double) currentSize.height / BASE_MAP_HEIGHT;

        for (Map.Entry<String, CountryButton> entry : countryButtons.entrySet()) {
            CountryButton button = entry.getValue();
            String[] originalPos = button.getName().split(",");
            int originalX = Integer.parseInt(originalPos[0]);
            int originalY = Integer.parseInt(originalPos[1]);

            int newX = (int) (originalX * scaleX);
            int newY = (int) (originalY * scaleY);

            button.setBounds(newX, newY, button.getWidth(), button.getHeight());
        }

        for (TransportVisual visual : transportVisuals) {
            visual.updateEndpoints(scaleX, scaleY);
        }
    }
}