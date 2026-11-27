// Задача 8: этот склад писали, путая типы данных. Найди и исправь ошибки.
// Не переписывай всё заново — чини по одной ошибке и проверяй на сайте.
public class FixTypes {
    public static void main(String[] args) {
        String title = 'Склад';
        String boxesText = "12";
        int boxes = boxesText;
        double price = 99.5;
        int total = boxes * price;
        long stars = 300000000000;
        char grade = "A";
        boolean open = "true";

        System.out.println(title);
        System.out.println("Коробок: " + boxes);
        System.out.println("Сумма: " + total);
        System.out.println("Звёзд в галактике: " + stars);
        System.out.println("Класс склада: " + grade);
        System.out.println("Открыт: " + open);
    }
}
