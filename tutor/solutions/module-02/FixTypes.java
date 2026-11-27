// Эталон задачи m02-t08. Шесть исправлений:
// 1) 'Склад' -> "Склад" (строки в двойных кавычках)
// 2) int boxes = boxesText -> Integer.parseInt(boxesText)
// 3) int total -> double total (иначе possible lossy conversion)
// 4) 300000000000 -> 300000000000L (литерал без L — int, слишком велик)
// 5) char grade = "A" -> 'A'
// 6) boolean open = "true" -> true
public class FixTypes {
    public static void main(String[] args) {
        String title = "Склад";
        String boxesText = "12";
        int boxes = Integer.parseInt(boxesText);
        double price = 99.5;
        double total = boxes * price;
        long stars = 300000000000L;
        char grade = 'A';
        boolean open = true;

        System.out.println(title);
        System.out.println("Коробок: " + boxes);
        System.out.println("Сумма: " + total);
        System.out.println("Звёзд в галактике: " + stars);
        System.out.println("Класс склада: " + grade);
        System.out.println("Открыт: " + open);
    }
}
