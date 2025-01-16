package model;

import java.util.Arrays;
import java.util.List;

public class Upgrade {
        public enum UpgradeType {
                CURE_EFFECTIVENESS,
                CURE_SPREAD,
                RESEARCH_BOOST,
                VIRUS_SUPPRESSION,
                QUARANTINE_MEASURES,
                MEDICAL_RESEARCH,
                IMMUNITY_BOOST,
                TREATMENT_EFFICIENCY,
                CONTAINMENT_PROTOCOL
        }

        public static List<Upgrade> createDefaultUpgrades() {
                return Arrays.asList(
                                new Upgrade(
                                                UpgradeType.CURE_EFFECTIVENESS,
                                                "Enhanced Treatment",
                                                "Increases cure effectiveness by 30%\n" +
                                                                "Current infected people are cured faster",
                                                500),

                                new Upgrade(
                                                UpgradeType.CURE_SPREAD,
                                                "Rapid Vaccination",
                                                "Increases immunity spread rate by 25%\n" +
                                                                "Helps protect healthy people faster",
                                                750),

                                new Upgrade(
                                                UpgradeType.RESEARCH_BOOST,
                                                "Advanced Research Labs",
                                                "Improves both cure and immunity spread by 20%\n" +
                                                                "Global research effectiveness increased",
                                                1000),

                                new Upgrade(
                                                UpgradeType.VIRUS_SUPPRESSION,
                                                "Virus Containment",
                                                "Reduces virus spread rate by 25%\n" +
                                                                "Slows down infection spread between countries",
                                                850),

                                new Upgrade(
                                                UpgradeType.QUARANTINE_MEASURES,
                                                "Enhanced Quarantine",
                                                "Countries maintain transport links 20% longer\n" +
                                                                "Helps maintain crucial connections",
                                                600),

                                new Upgrade(
                                                UpgradeType.MEDICAL_RESEARCH,
                                                "Medical Breakthrough",
                                                "Increases research level by 35%\n" +
                                                                "All cure-related activities become more effective",
                                                900),

                                new Upgrade(
                                                UpgradeType.IMMUNITY_BOOST,
                                                "Immunity Enhancement",
                                                "Protected people spread immunity 30% faster\n" +
                                                                "Accelerates population protection",
                                                800),

                                new Upgrade(
                                                UpgradeType.TREATMENT_EFFICIENCY,
                                                "Efficient Treatment",
                                                "Hospitals cure 40% more people per tick\n" +
                                                                "Significantly improves recovery rate",
                                                950),

                                new Upgrade(
                                                UpgradeType.CONTAINMENT_PROTOCOL,
                                                "Global Containment",
                                                "Reduces virus travel spread by 35%\n" +
                                                                "Makes international spread less likely",
                                                1200));
        }

        private final UpgradeType type;
        private final String name;
        private final String description;
        private final int cost;
        private boolean purchased = false;

        public Upgrade(UpgradeType type, String name, String description, int cost) {
                this.type = type;
                this.name = name;
                this.description = description;
                this.cost = cost;
        }

        public UpgradeType getType() {
                return type;
        }

        public String getName() {
                return name;
        }

        public String getDescription() {
                return description;
        }

        public int getCost() {
                return cost;
        }

        public boolean isPurchased() {
                return purchased;
        }

        public void setPurchased(boolean purchased) {
                this.purchased = purchased;
        }

        public void purchase() {
                purchased = true;
        }

        public boolean affectsTransport() {
                return name.contains("Airport") || name.contains("Border");
        }

        @Override
        public String toString() {
                return name + " (" + cost + " points)";
        }
}