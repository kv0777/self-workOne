package org.example;
import java.util.Scanner;
import java.util.*;

// Клас, що представляє товар
class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private Category category;

    public Product(int id, String name, double price, String description, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    // Гетери та сетери
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

// Клас, що представляє категорію товару
class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Гетери та сетери
}

// Клас, що представляє кошик користувача
class Cart {
    private Map<Product, Integer> items;

    public Cart() {
        items = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }

    public void removeItem(Product product, int quantity) {
        items.computeIfPresent(product, (key, value) -> value > quantity ? value - quantity : null);
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("Ваш кошик порожній");
            return;
        }
        System.out.println("У кошику: ");
        items.forEach((product, quantity) -> System.out.println(product.getName() + " - " + product.getPrice() + " x " + quantity));
    }

    // Гетери та сетери
    public Map<Product, Integer> getItems() {
        return items;
    }
}

// Клас, що представляє замовлення
class Order {
    private List<Product> items;

    public Order() {
        items = new ArrayList<>();
    }

    public void addProduct(Product product) {
        items.add(product);
    }

    public void displayOrder() {
        if (items.isEmpty()) {
            System.out.println("Замовлення порожнє");
            return;
        }
        System.out.println("Деталі замовлення: ");
        items.forEach(product -> System.out.println(product.getName() + " - " + product.getPrice()));
    }

    // Гетери та сетери
}

public class Main {
    private static final List<Product> products = new ArrayList<>();
    private static final Cart cart = new Cart();
    private static final List<Order> orderHistory = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Вітаю, друже! ;) Роби замовлення та потіш свою родину!");
        initializeProducts();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            displayMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewProducts();
                    break;
                case 2:
                    addToCart(scanner);
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    placeOrder();
                    break;
                case 5:
                    removeFromCart(scanner);
                    break;
                case 6:
                    searchProducts(scanner);
                    break;
                case 7:
                    viewOrderHistory();
                    break;
                case 8:
                    exit = true;
                    break;
                default:
                    System.out.println("Некоректний вибір");
            }
        }
        scanner.close();
    }

    private static void initializeProducts() {
        // Ініціалізація та додавання деяких товарів та категорій
        Category electronics = new Category(1, "Електроніка");
        Category clothing = new Category(2, "Одяг");

        products.add(new Product(1, "Apple MacBook (price in USD)", 1699, "Він дуже дорогий, не бери його :D", electronics));
        products.add(new Product(2, "Футболка Stone Island (price in USD)", 69, "Теж не бери, не будь школьніком :P", clothing));
    }

    private static void displayMenu() {
        System.out.println("\n1. Показати товари\n2. Додати до кошика\n3. Показати кошик\n4. Замовити <3\n5. Видалити з кошика\n6. Знайти товар\n7. Перевірити історію замовлень\n8. Вийти");
        System.out.print("Оберіть пункт: ");
    }

    private static void viewProducts() {
        System.out.println("Товари у наявності:");
        products.forEach(product -> System.out.println(product.getId() + ". " + product.getName() + " - " + product.getPrice()));
    }

    private static void addToCart(Scanner scanner) {
        viewProducts();
        System.out.print("Введіть ID товару для додавання у кошик: ");
        int productId = scanner.nextInt();
        Product selectedProduct = getProductById(productId);
        if (selectedProduct != null) {
            System.out.print("Оберіть кількість: ");
            int quantity = scanner.nextInt();
            cart.addItem(selectedProduct, quantity);
            System.out.println("Товар успішно додано до кошику");
        } else {
            System.out.println("Некоректне ID");
        }
    }

    private static void viewCart() {
        cart.displayCart();
    }

    private static void placeOrder() {
        if (cart.getItems().isEmpty()) {
            System.out.println("Ваш кошик порожній, оберіть товар, будь-ласка.");
            return;
        }
        Order order = new Order();
        cart.getItems().forEach((product, quantity) -> {
            for (int i = 0; i < quantity; i++) {
                order.addProduct(product);
            }
        });
        orderHistory.add(order);
        cart.getItems().clear();
        System.out.println("Ви успішно замовили товар");
    }

    private static void removeFromCart(Scanner scanner) {
        viewCart();
        if (cart.getItems().isEmpty()) {
            return;
        }
        System.out.print("Введіть ID товару для видалення з кошику: ");
        int productId = scanner.nextInt();
        Product selectedProduct = getProductById(productId);
        if (selectedProduct != null) {
            System.out.print("Введіть кількість: ");
            int quantity = scanner.nextInt();
            cart.removeItem(selectedProduct, quantity);
            System.out.println("Товар успішно видалено з кошику :)");
        } else {
            System.out.println("Некоректне ID товару, хмм..");
        }
    }

    private static void searchProducts(Scanner scanner) {
        System.out.print("Введіть ключові слова для пошуку товарів: ");
        String keyword = scanner.next().toLowerCase();
        List<Product> searchResults = new ArrayList<>();
        products.forEach(product -> {
            if (product.getName().toLowerCase().contains(keyword)) {
                searchResults.add(product);
            }
        });
        if (searchResults.isEmpty()) {
            System.out.println("Товарів за ключовими словами не знайдено.");
        } else {
            System.out.println("Результати пошуку:");
            searchResults.forEach(product -> System.out.println(product.getId() + ". " + product.getName() + " - " + product.getPrice()));
        }
    }

    private static void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("Історія замовлень відсутня.");
        } else {
            System.out.println("Історія замовлень:");
            orderHistory.forEach(Order::displayOrder);
        }
    }

    private static Product getProductById(int productId) {
        return products.stream().filter(product -> product.getId() == productId).findFirst().orElse(null);
    }
}
