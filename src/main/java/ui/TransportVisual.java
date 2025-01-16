package ui;

import model.TransportType;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class TransportVisual extends JComponent {
    private final Point2D.Double originalFrom;
    private final Point2D.Double originalTo;
    private Point2D.Double currentFrom;
    private Point2D.Double currentTo;
    private final TransportType type;
    private double animationProgress = 0.0;

    public TransportVisual(Point2D.Double from, Point2D.Double to, TransportType type) {
        this.originalFrom = new Point2D.Double(from.x, from.y);
        this.originalTo = new Point2D.Double(to.x, to.y);
        this.currentFrom = from;
        this.currentTo = to;
        this.type = type;
        setOpaque(false);
    }

    public void updateEndpoints(double scaleX, double scaleY) {
        currentFrom = new Point2D.Double(originalFrom.x * scaleX, originalFrom.y * scaleY);
        currentTo = new Point2D.Double(originalTo.x * scaleX, originalTo.y * scaleY);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double currentX = currentFrom.x + (currentTo.x - currentFrom.x) * animationProgress;
        double currentY = currentFrom.y + (currentTo.y - currentFrom.y) * animationProgress;

        ImageIcon icon = TransportIcon.getIcon(type);
        if (icon != null) {
            double dx = currentTo.x - currentFrom.x;
            double dy = currentTo.y - currentFrom.y;
            double angle = Math.atan2(dy, dx) + Math.PI / 2;

            AffineTransform oldTransform = g2d.getTransform();

            g2d.translate(currentX, currentY);
            g2d.rotate(angle);

            g2d.drawImage(icon.getImage(),
                    -icon.getIconWidth() / 2,
                    -icon.getIconHeight() / 2,
                    null);

            g2d.setTransform(oldTransform);
        }
    }

    public void updateAnimation() {
        animationProgress += 0.02;
        if (animationProgress >= 1.0) {
            animationProgress = 0.0;
        }
        repaint();
    }
}