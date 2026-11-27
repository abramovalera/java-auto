public class PetClinicFix {
    public static void main(String[] args) {
        ClinicPet cat = new ClinicPet("Барсик", 3);
        ClinicPet dog = new ClinicPet("Шарик", 7);
        cat.describe();
        dog.describe();
        System.out.println("Пациентов: " + ClinicPet.total);
    }
}

class ClinicPet {
    static int total = 0;
    String name;
    static int age;

    void ClinicPet(String name, int age) {
        name = name;
        this.age = age;
        total++;
    }

    void describe() {
        System.out.println(name + ", возраст: " + age);
    }
}
