import java.util.Scanner;

public class SoundChorus {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        ChorusVoice[] voices = new ChorusVoice[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            String word = in.next();
            ChorusVoice v = create(word);
            if (v == null) {
                System.out.println("Не знаю, кто это: " + word);
            } else {
                voices[count] = v;
                count++;
            }
        }
        System.out.println("Хор из " + count + ":");
        for (int i = 0; i < count; i++) {
            System.out.println(voices[i].name() + " — " + voices[i].sound());
        }
    }

    static ChorusVoice create(String word) {
        if (word.equals("cat")) return new ChorusCat();
        if (word.equals("dog")) return new ChorusDog();
        if (word.equals("car")) return new ChorusCar();
        return null;
    }
}

interface ChorusVoice {
    String name();
    String sound();
}

class ChorusCat implements ChorusVoice {
    public String name() { return "Кошка"; }
    public String sound() { return "Мяу"; }
}

class ChorusDog implements ChorusVoice {
    public String name() { return "Собака"; }
    public String sound() { return "Гав"; }
}

class ChorusCar implements ChorusVoice {
    public String name() { return "Машина"; }
    public String sound() { return "Би-би"; }
}
