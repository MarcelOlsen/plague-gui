package ui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UpgradePanel extends JPanel {
    private ArrayList<Upgrade> upgrades;
    private GameState gameState;
    private List<Upgrade> activeUpgrades = new ArrayList<>();

    public UpgradePanel(GameState gameState) {
        this.gameState = gameState;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Upgrades"));

        initializeUpgrades();
        createUpgradeButtons();
    }

    private void initializeUpgrades() {
        upgrades = new ArrayList<>();
        upgrades.addAll(Upgrade.createDefaultUpgrades());
    }

    private void createUpgradeButtons() {
        for (Upgrade upgrade : upgrades) {
            JButton button = createUpgradeButton(upgrade);
            add(button);
            add(Box.createVerticalStrut(5));
        }
    }

    private JButton createUpgradeButton(Upgrade upgrade) {
        JButton button = new JButton();
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateButtonText(button, upgrade);

        String tooltipText = String.format("<html>" +
                "<div style='padding: 5px;'>" +
                "<b style='color: #2C3E50; font-size: 12px;'>%s</b><br>" +
                "<div style='color: #34495E; margin: 5px 0;'>%s</div>" +
                "<div style='color: #E74C3C;'><b>Cost:</b> %d points</div>" +
                "</div></html>",
                upgrade.getName(),
                upgrade.getDescription().replace("\n", "<br>"),
                upgrade.getCost());

        button.setToolTipText(tooltipText);

        button.addActionListener(e -> purchaseUpgrade(upgrade, button));
        return button;
    }

    private void updateButtonText(JButton button, Upgrade upgrade) {
        String buttonText = String.format("<html><div style='text-align: left;'>" +
                "<b>%s</b><br>" +
                "<font color='#7F8C8D'>Cost: %d points</font></div></html>",
                upgrade.getName(),
                upgrade.getCost());
        button.setText(buttonText);
    }

    private void purchaseUpgrade(Upgrade upgrade, JButton button) {
        if (upgrade.isPurchased()) {
            JOptionPane.showMessageDialog(this,
                    "This upgrade has already been purchased!",
                    "Already Purchased",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (gameState.getPoints() >= upgrade.getCost()) {
            upgrade.setPurchased(true);
            gameState.addPoints(-upgrade.getCost());
            activeUpgrades.add(upgrade);
            gameState.applyUpgrade(upgrade);

            JOptionPane.showMessageDialog(this,
                    String.format("<html><div style='padding: 5px;'>" +
                            "<b>Upgrade Purchased Successfully!</b><br><br>" +
                            "%s<br><br>" +
                            "<font color='green'>Points remaining: %d</font></div></html>",
                            upgrade.getDescription(),
                            gameState.getPoints()),
                    "Upgrade Purchased",
                    JOptionPane.INFORMATION_MESSAGE);

            button.setEnabled(false);
            updateButtonText(button, upgrade);
        } else {
            JOptionPane.showMessageDialog(this,
                    String.format("<html><div style='padding: 5px;'>" +
                            "<b>Not Enough Points!</b><br><br>" +
                            "Required: %d points<br>" +
                            "Current: %d points<br>" +
                            "Missing: %d points</div></html>",
                            upgrade.getCost(),
                            gameState.getPoints(),
                            upgrade.getCost() - gameState.getPoints()),
                    "Insufficient Points",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public List<Upgrade> getActiveUpgrades() {
        return activeUpgrades;
    }
}