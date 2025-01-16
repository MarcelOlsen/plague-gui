package ui;

import model.Country;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class CountryButton extends JButton {
    private static final Color HEALTHY_COLOR = new Color(144, 238, 144);
    private static final Color INFECTED_COLOR = new Color(220, 20, 60);
    private static final Color IMMUNE_COLOR = new Color(100, 149, 237);
    private static final Color HEALING_COLOR = new Color(255, 182, 193);

    private final Country country;
    private Timer infectionAnimationTimer;
    private Timer healingAnimationTimer;
    private float animationPhase = 0.0f;
    private Shape countryShape;

    public CountryButton(Country country, int x, int y) {
        this.country = country;
        setBounds(x, y, 100, 60);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setupAnimationTimers();
        createCountryShape();
        updateAppearance();
        setFont(new Font("Arial", Font.BOLD, 11));
    }

    private void createCountryShape() {
        Path2D.Double path = new Path2D.Double();

        switch (country.getName()) {
            case "Russia" -> {
                path.moveTo(0, 20);
                path.curveTo(10, 15, 20, 5, 40, 10);
                path.lineTo(90, 15);
                path.curveTo(85, 30, 80, 40, 90, 45);
                path.lineTo(70, 50);
                path.curveTo(50, 45, 30, 50, 0, 40);
                path.closePath();
            }
            case "China" -> {
                path.moveTo(10, 10);
                path.lineTo(70, 10);
                path.curveTo(80, 20, 90, 30, 85, 45);
                path.curveTo(70, 50, 50, 55, 30, 50);
                path.lineTo(10, 45);
                path.closePath();
            }
            case "USA" -> {
                path.moveTo(10, 15);
                path.lineTo(80, 15);
                path.lineTo(90, 25);
                path.lineTo(85, 35);
                path.lineTo(90, 45);
                path.lineTo(70, 50);
                path.lineTo(20, 45);
                path.lineTo(10, 35);
                path.closePath();
            }
            case "India" -> {
                path.moveTo(20, 10);
                path.lineTo(80, 10);
                path.curveTo(85, 20, 80, 35, 70, 45);
                path.lineTo(45, 55);
                path.lineTo(20, 45);
                path.curveTo(15, 35, 15, 20, 20, 10);
                path.closePath();
            }
            case "Brazil" -> {
                path.moveTo(20, 10);
                path.lineTo(60, 10);
                path.curveTo(80, 20, 85, 35, 75, 50);
                path.curveTo(60, 55, 40, 55, 20, 45);
                path.closePath();
            }
            case "Japan" -> {
                path.moveTo(45, 5);
                path.curveTo(55, 10, 60, 15, 65, 25);
                path.curveTo(70, 35, 65, 45, 60, 55);
                path.curveTo(50, 58, 40, 55, 35, 50);
                path.curveTo(30, 40, 35, 30, 40, 20);
                path.curveTo(42, 10, 40, 5, 45, 5);
                path.closePath();
            }
            case "Germany" -> {
                path.moveTo(30, 10);
                path.lineTo(70, 10);
                path.lineTo(80, 20);
                path.lineTo(75, 40);
                path.lineTo(60, 50);
                path.lineTo(25, 45);
                path.lineTo(20, 30);
                path.closePath();
            }
            case "France" -> {
                path.moveTo(30, 10);
                path.lineTo(70, 15);
                path.lineTo(80, 30);
                path.lineTo(70, 45);
                path.lineTo(30, 50);
                path.lineTo(20, 30);
                path.closePath();
            }
            case "UK" -> {
                path.moveTo(40, 10);
                path.curveTo(60, 15, 70, 20, 75, 30);
                path.curveTo(70, 40, 60, 45, 45, 50);
                path.curveTo(30, 45, 25, 35, 30, 25);
                path.curveTo(35, 15, 30, 10, 40, 10);
            }
            case "Canada" -> {
                path.moveTo(10, 20);
                path.curveTo(30, 15, 50, 5, 80, 15);
                path.lineTo(90, 30);
                path.curveTo(80, 40, 60, 45, 40, 50);
                path.lineTo(10, 45);
                path.closePath();
            }
            default -> {
                path.append(new RoundRectangle2D.Double(0, 0, 90, 50, 10, 10), false);
            }
        }

        countryShape = path;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(getBackground());
        g2d.fill(countryShape);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(countryShape);

        FontMetrics fm = g2d.getFontMetrics();
        Rectangle bounds = countryShape.getBounds();
        String text = getText().replace("<html><center>", "").replace("</center></html>", "");
        String[] lines = text.split("<br>");

        int y = bounds.y + (bounds.height - (lines.length * fm.getHeight())) / 2 + fm.getAscent();
        for (String line : lines) {
            int x = bounds.x + (bounds.width - fm.stringWidth(line)) / 2;
            g2d.setColor(Color.BLACK);
            g2d.drawString(line, x, y);
            y += fm.getHeight();
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return countryShape.contains(x, y);
    }

    private void setupAnimationTimers() {
        infectionAnimationTimer = new Timer(50, e -> pulseInfection());
        healingAnimationTimer = new Timer(50, e -> pulseHealing());
    }

    public void updateAppearance() {
        int infected = country.getInfected();
        int immune = country.getImmune();
        int population = country.getPopulation();

        StringBuilder buttonText = new StringBuilder("<html><center>");
        buttonText.append(country.getName()).append("<br>");

        if (infected > 0) {
            startInfectionAnimation();
            buttonText.append(String.format("🦠 %d%%", (infected * 100) / population));
            if (immune > 0) {
                buttonText.append(String.format(" 💊 %d%%", (immune * 100) / population));
            }
        } else if (immune > 0) {
            setBackground(IMMUNE_COLOR);
            stopAnimations();
            buttonText.append(String.format("💊 %d%%", (immune * 100) / population));
        } else {
            setBackground(HEALTHY_COLOR);
            stopAnimations();
            buttonText.append("❤️ Safe");
        }

        buttonText.append("</center></html>");
        setText(buttonText.toString());
    }

    private void pulseInfection() {
        animationPhase += 0.1f;
        float intensity = (float) Math.abs(Math.sin(animationPhase));
        Color currentColor = new Color(
                220,
                (int) (20 + (intensity * 50)),
                60);
        setBackground(currentColor);
    }

    private void pulseHealing() {
        animationPhase += 0.1f;
        float intensity = (float) Math.abs(Math.sin(animationPhase));
        Color currentColor = new Color(
                255,
                (int) (182 + (intensity * 73)),
                (int) (193 + (intensity * 62)));
        setBackground(currentColor);
    }

    public void startInfectionAnimation() {
        stopAnimations();
        infectionAnimationTimer.start();
    }

    public void startHealingAnimation() {
        stopAnimations();
        healingAnimationTimer.start();
    }

    private void stopAnimations() {
        infectionAnimationTimer.stop();
        healingAnimationTimer.stop();
        animationPhase = 0.0f;
    }
}