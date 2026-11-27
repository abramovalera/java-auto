import java.util.Scanner;

public class ShelterDogInfo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShelterPet pet = new ShelterPet(scanner.next(), scanner.nextInt());
        ShelterDog dog = new ShelterDog(scanner.next(), scanner.nextInt(), scanner.next());
        System.out.println("Питомец: " + pet);
        System.out.println("Собака: " + dog);
        System.out.println(pet.getName() + " — малыш: " + pet.isBaby());
        System.out.println(dog.getName() + " — малыш: " + dog.isBaby());
    }
}

class ShelterPet {
    private final String name;
    private final int age;

    ShelterPet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    String getName() {
        return name;
    }

    boolean isBaby() {
        return age < 1;
    }

    @Override
    public String toString() {
        return name + ", возраст " + age;
    }
}

class ShelterDog extends ShelterPet {
    private final String breed;

    ShelterDog(String name, int age, String breed) {
        super(name, age);
        this.breed = breed;
    }

    @Override
    public String toString() {
        return super.toString() + ", порода " + breed;
    }
}
