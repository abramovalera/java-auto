import java.util.Scanner;

public class FarmChorus {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        FarmAnimal[] farm = new FarmAnimal[n];
        for (int i = 0; i < n; i++) {
            String kind = scanner.next();
            String name = scanner.next();
            if (kind.equals("cow")) {
                farm[i] = new FarmCow(name);
            } else if (kind.equals("dog")) {
                farm[i] = new FarmDog(name);
            } else {
                farm[i] = new FarmRooster(name);
            }
        }
        for (FarmAnimal animal : farm) {
            System.out.println(animal.speak());
        }
        System.out.println("Голосов в хоре: " + n);
    }
}

abstract class FarmAnimal {
    private final String name;

    FarmAnimal(String name) {
        this.name = name;
    }

    abstract String sound();

    String speak() {
        return name + ": " + sound();
    }
}

class FarmCow extends FarmAnimal {
    FarmCow(String name) {
        super(name);
    }

    @Override
    String sound() {
        return "Му-у";
    }
}

class FarmDog extends FarmAnimal {
    FarmDog(String name) {
        super(name);
    }

    @Override
    String sound() {
        return "Гав!";
    }
}

class FarmRooster extends FarmAnimal {
    FarmRooster(String name) {
        super(name);
    }

    @Override
    String sound() {
        return "Ку-ка-ре-ку!";
    }
}
