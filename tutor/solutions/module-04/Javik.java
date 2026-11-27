public class Javik {
    public String decide(Radar radar) {
        // <= , а не == : на скорости 2 расстояние уменьшается через одну клетку (5, 3, 1…)
        if (radar.distance() <= 2) {
            if (radar.isLow()) {
                return "JUMP";
            }
            return "DUCK";
        }
        return "RUN";
    }
}
