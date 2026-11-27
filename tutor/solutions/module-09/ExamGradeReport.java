import java.util.Scanner;

public class ExamGradeReport {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String name = sc.next();
        int n = sc.nextInt();
        int[] grades = new int[n];
        for (int i = 0; i < n; i++) {
            grades[i] = sc.nextInt();
        }

        ExamStudent student = new ExamStudent(name, grades);
        System.out.println("Студент: " + student.getName());
        System.out.println("Оценок: " + student.count());
        if (student.count() == 0) {
            System.out.println("Оценок пока нет");
        } else {
            System.out.println("Средний балл: " + Math.round(student.average() * 100) / 100.0);
            System.out.println("Лучшая оценка: " + student.best());
        }
    }
}

class ExamStudent {
    private final String name;
    private final int[] grades;

    ExamStudent(String name, int[] grades) {
        this.name = name;
        this.grades = grades;
    }

    String getName() {
        return name;
    }

    int count() {
        return grades.length;
    }

    double average() {
        int sum = 0;
        for (int g : grades) {
            sum += g;
        }
        return (double) sum / grades.length;
    }

    int best() {
        int max = grades[0];
        for (int g : grades) {
            if (g > max) {
                max = g;
            }
        }
        return max;
    }
}
