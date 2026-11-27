import java.util.Scanner;

public class SolarPlanetWeight {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double kg = scanner.nextDouble();
        int lighter = 0;
        for (SolarPlanet planet : SolarPlanet.values()) {
            double w = planet.weightOn(kg);
            System.out.println(planet + ": " + Math.round(w * 10) / 10.0);
            if (planet.getGravity() < 1.0) {
                lighter++;
            }
        }
        System.out.println("Легче, чем на Земле, на планетах: " + lighter);
    }
}

enum SolarPlanet {
    MERCURY(0.38), VENUS(0.91), EARTH(1.0), MARS(0.38),
    JUPITER(2.53), SATURN(1.07), URANUS(0.89), NEPTUNE(1.14);

    private final double gravity;

    SolarPlanet(double gravity) {
        this.gravity = gravity;
    }

    double getGravity() {
        return gravity;
    }

    double weightOn(double kg) {
        return kg * gravity;
    }
}
