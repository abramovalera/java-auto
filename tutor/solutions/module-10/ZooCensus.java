import java.util.Scanner;

public class ZooCensus {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        CensusAnimal[] animals = new CensusAnimal[n];
        for (int i = 0; i < n; i++) {
            String kind = scanner.next();
            String name = scanner.next();
            if (kind.equals("cat")) {
                animals[i] = new CensusCat(name);
            } else if (kind.equals("kitten")) {
                animals[i] = new CensusKitten(name);
            } else if (kind.equals("dog")) {
                animals[i] = new CensusDog(name, scanner.next().equals("guard"));
            } else {
                animals[i] = new CensusParrot(name);
            }
        }
        census(animals);
    }

    static void census(CensusAnimal[] animals) {
        int cats = 0;
        int dogs = 0;
        int guards = 0;
        int others = 0;
        String guardNames = "";
        for (int i = 0; i < animals.length; i++) {
            CensusAnimal a = animals[i];
            if (a instanceof CensusCat) {
                cats++;
            } else if (a instanceof CensusDog dog) {
                dogs++;
                if (dog.isGuard()) {
                    guards++;
                    guardNames += guardNames.isEmpty() ? dog.getName() : ", " + dog.getName();
                }
            } else {
                others++;
            }
        }
        System.out.println("Всего: " + animals.length);
        System.out.println("Кошачьих: " + cats);
        System.out.println("Собак: " + dogs);
        System.out.println("Сторожевых собак: " + guards);
        System.out.println("Прочих: " + others);
        System.out.println("Сторожа: " + (guardNames.isEmpty() ? "нет" : guardNames));
    }
}

class CensusAnimal {
    private final String name;

    CensusAnimal(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class CensusCat extends CensusAnimal {
    CensusCat(String name) {
        super(name);
    }
}

class CensusKitten extends CensusCat {
    CensusKitten(String name) {
        super(name);
    }
}

class CensusDog extends CensusAnimal {
    private final boolean guard;

    CensusDog(String name, boolean guard) {
        super(name);
        this.guard = guard;
    }

    boolean isGuard() {
        return guard;
    }
}

class CensusParrot extends CensusAnimal {
    CensusParrot(String name) {
        super(name);
    }
}
