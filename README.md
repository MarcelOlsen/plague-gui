# Virus Defense Game

A Java Swing-based game where players defend the world against a spreading virus. Unlike traditional pandemic games, players work to cure and prevent infections across multiple countries.

## Features

- Interactive world map with 10 major countries
- Multiple transport routes (air, sea, land) between countries
- Real-time virus spread simulation
- Various upgrades to help combat the virus
- High score system
- Multiple difficulty levels
- Dynamic transport shutdown system based on infection rates

## Getting Started

### Prerequisites

- Java 17 or higher
- [Gradle build tool](https://gradle.org/install/)

### Installation

1. Clone the repository

```bash
git clone https://github.com/marcelolsen/plague-gui.git
```

2. Navigate to the project directory

```bash
cd plague-gui
```

3. Build the project

```bash
gradle build
```

4. Run the project

```bash
gradle run
```

## Gameplay

### Main Menu

- New Game: Start a new game session
- High Scores: View the top 10 highest scores
- Exit: Close the game

### Game Controls

- Click on countries to view detailed statistics
- Purchase upgrades using points earned from curing and preventing infections
- Use Ctrl+Shift+Q to return to the main menu at any time

### Scoring System

- Points are earned by:
  - Curing infected people
  - Preventing new infections
  - Successfully protecting populations
- Bonus points awarded for complete virus elimination

### Transport System

Different transport types have varying infection rates:

- Airplanes: Highest spread rate but crucial for long-distance connections
- Ships: Medium-high spread rate
- Trains: Medium spread rate
- Buses: Medium-low spread rate
- Cars: Lowest spread rate

### Upgrades

Nine different upgrades available:

- Enhanced Treatment
- Rapid Vaccination
- Advanced Research Labs
- Virus Containment
- Enhanced Quarantine
- Medical Breakthrough
- Immunity Enhancement
- Efficient Treatment
- Global Containment

## Game Features

### Country Management

Each country has:

- Population statistics
- Infection rates
- Transport connections
- Unique shutdown thresholds

### Virus Simulation

The virus spreads through:

- Population growth within countries
- Transport connections between countries
- Different rates based on population density

### Save System

High scores are automatically saved using Java serialization and persist between game sessions.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Inspired by Plague Inc. (but in reverse)
- Built as a university project
