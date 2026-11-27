import java.util.Scanner;

public class PayrollDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        PayrollEmployee[] staff = new PayrollEmployee[n];
        for (int i = 0; i < n; i++) {
            String kind = scanner.next();
            String name = scanner.next();
            int base = scanner.nextInt();
            if (kind.equals("manager")) {
                staff[i] = new PayrollManager(name, base, scanner.nextInt());
            } else if (kind.equals("intern")) {
                staff[i] = new PayrollIntern(name, base);
            } else {
                staff[i] = new PayrollEmployee(name, base);
            }
        }

        int fund = 0;
        PayrollEmployee top = null;
        for (int i = 0; i < staff.length; i++) {
            PayrollEmployee e = staff[i];
            System.out.println(e.getName() + " (" + e.role() + "): " + e.salary());
            fund += e.salary();
            if (top == null || e.salary() > top.salary()) {
                top = e;
            }
        }
        System.out.println("Фонд оплаты: " + fund);
        System.out.println("Больше всех: " + (top == null ? "нет" : top.getName()));
    }
}

class PayrollEmployee {
    private final String name;
    private final int baseSalary;

    PayrollEmployee(String name, int baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    String getName() {
        return name;
    }

    String role() {
        return "сотрудник";
    }

    int salary() {
        return baseSalary;
    }
}

class PayrollManager extends PayrollEmployee {
    private final int bonus;

    PayrollManager(String name, int baseSalary, int bonus) {
        super(name, baseSalary);
        this.bonus = bonus;
    }

    @Override
    String role() {
        return "руководитель";
    }

    @Override
    int salary() {
        return super.salary() + bonus;
    }
}

class PayrollIntern extends PayrollEmployee {
    PayrollIntern(String name, int baseSalary) {
        super(name, baseSalary);
    }

    @Override
    String role() {
        return "стажёр";
    }

    @Override
    int salary() {
        return super.salary() / 2;
    }
}
