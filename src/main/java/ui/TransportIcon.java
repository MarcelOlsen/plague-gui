package ui;

import model.TransportType;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import java.net.URL;

public class TransportIcon {
    private static final Map<TransportType, ImageIcon> icons = new EnumMap<>(TransportType.class);
    private static final int ICON_SIZE = 20;

    static {
        loadIcons();
    }

    private static void loadIcons() {
        for (TransportType type : TransportType.values()) {
            String path = "/images/icons/" + type.name().toLowerCase() + ".png";
            try {
                URL resourceUrl = TransportIcon.class.getResource(path);
                if (resourceUrl == null) {
                    System.err.println("Resource not found: " + path);
                    continue;
                }
                System.out.println("Loading icon from: " + resourceUrl);
                ImageIcon icon = loadIcon(path);
                icons.put(type, icon);
            } catch (Exception e) {
                System.err.println("Failed to load icon for " + type + " from " + path);
                e.printStackTrace();
            }
        }

        if (icons.isEmpty()) {
            System.out.println("No icons loaded, creating fallback icons");
            createFallbackIcons();
        }
    }

    private static ImageIcon loadIcon(String path) {
        URL resourceUrl = TransportIcon.class.getResource(path);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }

        ImageIcon originalIcon = new ImageIcon(resourceUrl);
        if (originalIcon.getIconWidth() <= 0 || originalIcon.getIconHeight() <= 0) {
            throw new IllegalArgumentException("Invalid image loaded from: " + path);
        }

        Image scaledImage = originalIcon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private static void createFallbackIcons() {
        icons.put(TransportType.AIRPLANE, createColoredIcon(Color.RED));
        icons.put(TransportType.SHIP, createColoredIcon(Color.BLUE));
        icons.put(TransportType.TRAIN, createColoredIcon(Color.GREEN));
        icons.put(TransportType.BUS, createColoredIcon(Color.ORANGE));
        icons.put(TransportType.CAR, createColoredIcon(Color.GRAY));
    }

    private static ImageIcon createColoredIcon(Color color) {
        BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillOval(0, 0, ICON_SIZE, ICON_SIZE);
        g2d.dispose();
        return new ImageIcon(image);
    }

    public static ImageIcon getIcon(TransportType type) {
        ImageIcon icon = icons.get(type);
        if (icon == null) {
            System.err.println("No icon found for type: " + type + ", using fallback");
            icon = createColoredIcon(Color.RED);
            icons.put(type, icon);
        }
        return icon;
    }
}