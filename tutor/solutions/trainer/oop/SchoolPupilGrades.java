import java.util.Scanner;

public class SchoolPupilGrades {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SchoolPupil pupil = new SchoolPupil(scanner.next());
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            int grade = scanner.nextInt();
            if (!pupil.addGrade(grade)) {
                System.out.println("Не принята оценка: " + grade);
            }
        }
        if (pupil.getCount() == 0) {
            System.out.println(pupil.getName() + ": оценок нет");
        } else {
            double avg = Math.round(pupil.average() * 100) / 100.0;
            System.out.println(pupil.getName() + ": средний балл " + avg + ", лучшая оценка " + pupil.getBest());
        }
    }
}

class SchoolPupil {
    private final String name;
    private int sum;
    private int count;
    private int best;

    SchoolPupil(String name) {
        this.name = name;
    }

    boolean addGrade(int grade) {
        if (grade < 1 || grade > 5) {
            return false;
        }
        sum += grade;
        count++;
        if (grade > best) {
            best = grade;
        }
        return true;
    }

    double average() {
        if (count == 0) {
            return 0;
        }
        return (double) sum / count;
    }

    String getName() {
        return name;
    }

    int getCount() {
        return count;
    }

    int getBest() {
        return best;
    }
}
