package ui;

import model.Country;
import javax.swing.*;
import java.awt.*;

public class CountryDetailsPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel healthyLabel;
    private JLabel sickLabel;
    private JLabel vaccinatedLabel;

    public CountryDetailsPanel() {
        setPreferredSize(new Dimension(250, 100));
        setBorder(BorderFactory.createTitledBorder("Country Details"));
        setLayout(new GridLayout(4, 1));

        nameLabel = new JLabel("Select a country to view details");
        healthyLabel = new JLabel("");
        sickLabel = new JLabel("");
        vaccinatedLabel = new JLabel("");

        add(nameLabel);
        add(healthyLabel);
        add(sickLabel);
        add(vaccinatedLabel);
    }

    public void updateDetails(Country country) {
        if (country == null) {
            nameLabel.setText("Select a country to view details");
            healthyLabel.setText("");
            sickLabel.setText("");
            vaccinatedLabel.setText("");
            return;
        }

        int infected = country.getInfected();
        int immune = country.getImmune();
        int population = country.getPopulation();
        int healthy = population - infected - immune;

        nameLabel.setText(country.getName());
        healthyLabel.setText(String.format("❤️ Healthy: %,d (%d%%)",
                healthy, (healthy * 100) / population));
        sickLabel.setText(String.format("🦠 Infected: %,d (%d%%)",
                infected, (infected * 100) / population));
        vaccinatedLabel.setText(String.format("💊 Protected: %,d (%d%%)",
                immune, (immune * 100) / population));
    }
}