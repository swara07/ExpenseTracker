import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseTracker tracker = ExpenseTracker.getInstance();

        System.out.println("Welcome to Expense Tracker!");
        System.out.println("Do you want to load data from a file? (yes/no)");
        String load = scanner.nextLine();

        if (load.equalsIgnoreCase("yes")) {
            System.out.print("Enter file path: ");
            String path = scanner.nextLine();
            try {
                tracker.loadFromFile(path);
                System.out.println("Data loaded successfully.");
            } catch (Exception e) {
                System.err.println("Error loading file: " + e.getMessage());
            }
        }

        String choice;
        do {
            try {
                System.out.print("Enter transaction type (income/expense): ");
                TransactionType type = TransactionType.valueOf(scanner.nextLine().toUpperCase());
                CategoryFactory categoryFactory = (type == TransactionType.INCOME) ? new IncomeCategory() : new ExpenseCategory();
                System.out.println("Choose category: " + categoryFactory.getCategories());
                String category = scanner.nextLine();
                System.out.print("Enter amount: ");
                double amount = Double.parseDouble(scanner.nextLine());
                System.out.print("Enter date (yyyy-MM-dd): ");
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
                tracker.addTransaction(new Transaction(type, category, amount, date));
                System.out.println("Transaction added successfully.\n");
            } catch (Exception e) {
                System.err.println("Invalid input: " + e.getMessage());
            }

            System.out.print("Do you want to add another transaction? (yes/no): ");
            choice = scanner.nextLine();
        } while (choice.equalsIgnoreCase("yes"));

        System.out.println("\nMonthly Summary:");
        tracker.generateMonthlySummary();
    }
}


enum TransactionType {
    INCOME, EXPENSE
}