package budget;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class BudgetManager {
    String filePath = "purchases.txt";
    File file;
    double balance;
    double sumOfPurchases;
    String[] categoriesNames = { "Food", "Clothes", "Entertainment", "Other" };
    Map<String, List<Product>> categories = new HashMap<>();
    Scanner scanner = new Scanner(System.in);


    public void start() {
        createFile();
        String action = "";
        Scanner scanner = new Scanner(System.in);
        addCategories();
        while(!action.equals("0")) {
            System.out.println("""
                    Choose your action:
                    1) Add income
                    2) Add purchase
                    3) Show list of purchases
                    4) Balance
                    5) Save
                    6) Load
                    7) Analyze (Sort)
                    0) Exit""");
            action = scanner.nextLine().trim();
            switch (action) {
                case "1" : addIncome(); break;
                case "2" : addPurchase(); break;
                case "3" : showList(); break;
                case "4" : showBalance(); break;
                case "5" : save(); break;
                case "6" : load(); break;
                case "7" : analyze(); break;
                case "0" : action = "0"; break;
                default: System.out.println("\nWrong input!\n"); break;
            }
        }
        System.out.println("\nBye!");
    }

    public void addCategories() {
        for (String name : categoriesNames) {
            categories.put(name, new ArrayList<>());
        }
    }

    public void addIncome() {
        System.out.println("\nEnter income:");
        balance += Double.parseDouble(scanner.nextLine());
        System.out.println("Income was added!\n");
    }

    public void addPurchase() {
        String action;

        while (true) {
            System.out.println("\nChoose the type of purchase");
            for (int i = 1; i <= categoriesNames.length; i++) {
                System.out.println(i + ") " + categoriesNames[i - 1]);
            }
            System.out.println(categoriesNames.length + 1 + ") Back");

            action = scanner.nextLine();
            if (Integer.parseInt(action) == categoriesNames.length + 1) { System.out.println(); return; }

            Product product = new Product();
            System.out.println("\nEnter purchase name:");
            product.setName(scanner.nextLine());
            System.out.println("Enter its price:");
            product.setCost(Double.parseDouble(scanner.nextLine()));
            balance -= product.getCost();
            sumOfPurchases += product.getCost();
            categories.get(categoriesNames[Integer.parseInt(action) - 1]).add(product);

            System.out.println("Purchase was added!");
        }
    }

    public void showList() {
        int action;
        while (true) {
            sumOfPurchases = 0;
            if (checkIfListIsEmpty("ALL")) {
                return;
            }

            System.out.println("\nChoose the type of purchases");
            for (int i = 1; i <= categoriesNames.length; i++) {
                System.out.println(i + ") " + categoriesNames[i - 1]);
            }
            System.out.println(categoriesNames.length + 1 + ") All");
            System.out.println(categoriesNames.length + 2 + ") Back");
            action = Integer.parseInt(scanner.nextLine());

            if (action == categoriesNames.length + 2) {
                System.out.println();
                return;
            } else if (action == categoriesNames.length + 1) {
                System.out.print("\nAll:");
                for (String name : categoriesNames) {
                    if (!categories.get(name).isEmpty()) {
                        categories.get(name).forEach(product -> {
                            sumOfPurchases += product.getCost();
                            System.out.printf("\n%s $%.2f", product.getName(), product.getCost());
                        });
                    }
                }
            } else {
                System.out.print("\n" + categoriesNames[action - 1] + ":");
                if (checkIfListIsEmpty(categoriesNames[action - 1])) {
                    continue;
                } else {
                    categories.get(categoriesNames[action - 1]).forEach(product -> {
                        System.out.printf("\n%s $%.2f", product.getName(), product.getCost());
                        sumOfPurchases += product.getCost();
                    });
                }
            }

            System.out.printf("\nTotal sum: $%.2f\n", sumOfPurchases);
        }
    }

    public void showBalance() {
        System.out.printf("\nBalance: $%.2f\n\n", balance);
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file, false)){
            writer.write(balance + "\n");
            for (String category : categoriesNames) {
                for (Product product : categories.get(category)) {
                    writer.write(("%s $%f $%s\n").formatted(product.getName(), product.getCost(), category));
                }
            }
            System.out.println("\nPurchases were saved!\n");
        } catch (Exception ignored) {}
    }

    public void load() {
        try (Scanner fileScanner = new Scanner(file)) {
            categories.clear();
            addCategories();
            balance = Double.parseDouble(fileScanner.nextLine().trim());
            while (fileScanner.hasNextLine()) {
                String string = fileScanner.nextLine();
                String[] data = string.split("( \\$(?=\\d+\\.))|( \\$(?=[A-Za-z]+))");
                Product product = new Product();
                product.setName(data[0]);
                product.setCost(Double.valueOf(data[1]));
                categories.get(data[2]).add(product);
            }
            System.out.println("\nPurchases were loaded!\n");
        } catch (Exception ignored) {
            System.out.println();
        }
    }

    public void createFile() {
        try {
            file = new File(filePath);
        } catch (Exception ignored) {
        }
    }

    public boolean checkIfListIsEmpty(String ALL_or_CATEGORY_NAME) {
        boolean listIsEmpty = true;
        if (ALL_or_CATEGORY_NAME.equals("ALL")) {
            for (String name : categoriesNames) {
                if (!categories.get(name).isEmpty()) {
                    listIsEmpty = false;
                    break;
                }
            }
        } else {
            if (!categories.get(ALL_or_CATEGORY_NAME).isEmpty()) {
                listIsEmpty = false;
            }
        }

        if (listIsEmpty) {
            System.out.println("\nThe purchase list is empty!");
        }
        return listIsEmpty;
    }

    public void analyze() {
        String action = "";
        while (!action.equals("4")) {
            System.out.println("""
                
                How do you want to sort?
                1) Sort all purchases
                2) Sort by type
                3) Sort certain type
                4) Back""");
            action = scanner.nextLine();
            switch (action) {
                case "1" : sort(1); break;
                case "2" : sort(2); break;
                case "3" : sort(3); break;
                case "4" : action = "4"; System.out.println(); break;
            }
        }
    }

    public void sort(int typeOfMethod) {
        /* 1 = Sort all purchases
           2 = Sort by type
           3 = Sort certain type */
        switch (typeOfMethod) {
            case 1:
                List<Product> allProducts = new ArrayList<>();
                for (String name : categoriesNames) {
                    allProducts.addAll(categories.get(name));
                }
                if (!checkIfListIsEmpty("ALL")) {
                    bubbleSort(allProducts);
                    sumOfPurchases = 0;
                    System.out.println("\nAll:");
                    for (Product product : allProducts) {
                        System.out.printf("%s $%.2f\n", product.getName(), product.getCost());
                        sumOfPurchases += product.getCost();
                    }
                    System.out.printf("Total: $%.2f\n\n", sumOfPurchases);
                }
                break;
            case 2 :
                System.out.println("\nTypes:");
                double total = 0;
                List<Product> sortedCategories = new ArrayList<>();
                for (String name : categoriesNames) {
                    Product category = new Product();
                    category.setName(name);
                    category.setCost(0.0);
                    for (Product product : categories.get(name)) {
                        category.setCost(category.getCost() + product.getCost());
                    }
                    total += category.getCost();
                    sortedCategories.add(category);
                }
                bubbleSort(sortedCategories);
                for (Product category : sortedCategories) {
                    if (category.getCost() == 0) {
                        System.out.printf("%s - $0\n", category.getName());
                    } else {
                        System.out.printf("%s - $%.2f\n", category.getName(), category.getCost());
                    }
                }
                System.out.printf("Total sum: $%.2f\n", total);
                break;
            case 3 :
                System.out.println("\nChoose the type of purchases");
                for (int i = 1; i <= categoriesNames.length; i++) {
                    System.out.println(i + ") " + categoriesNames[i - 1]);
                }
                int action = Integer.parseInt(scanner.nextLine());

                if (!checkIfListIsEmpty(categoriesNames[action - 1])) {
                    bubbleSort(categories.get(categoriesNames[action - 1]));
                    System.out.println("\n" + categoriesNames[action - 1] + ":");
                    sumOfPurchases = 0;
                    for (Product product : categories.get(categoriesNames[action - 1])) {
                        System.out.printf("%s $%.2f\n", product.getName(), product.getCost());
                        sumOfPurchases += product.getCost();
                    }
                    System.out.printf("Total sum: $%.2f\n", sumOfPurchases);
                }
        }
    }

    public void bubbleSort(List<Product> list) {
        int n = list.size();
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (list.get(i - 1).getCost() < list.get(i).getCost()) {
                    swap(list, (i - 1), i);
                    swapped = true;
                }
            }
        }
    }

    public void swap(List<Product> list, int i, int j) {
        Product temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
