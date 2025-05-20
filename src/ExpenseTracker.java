import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpenseTracker {

    private static ExpenseTracker instance;
    private List<Transaction> transactions = new ArrayList<>();

    private ExpenseTracker() {}

    public static ExpenseTracker getInstance() {
        if (instance == null) {
            instance = new ExpenseTracker();
        }
        return instance;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void loadFromFile(String filePath) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            TransactionType type = TransactionType.valueOf(parts[0].toUpperCase());
            String category = parts[1];
            double amount = Double.parseDouble(parts[2]);
            Date date = sdf.parse(parts[3]);
            addTransaction(new Transaction(type, category, amount, date));
        }
        reader.close();
    }

    public void generateMonthlySummary() {
        Map<String, Double> incomeMap = new HashMap<>();
        Map<String, Double> expenseMap = new HashMap<>();
        Map<String, Map<String, Double>> incomeCategories = new HashMap<>();
        Map<String, Map<String, Double>> expenseCategories = new HashMap<>();

        for (Transaction t : transactions) {
            String month = new SimpleDateFormat("yyyy-MM").format(t.date);
            Map<String, Double> totalMap = t.type == TransactionType.INCOME ? incomeMap : expenseMap;
            Map<String, Map<String, Double>> categoryMap = t.type == TransactionType.INCOME ? incomeCategories : expenseCategories;

            totalMap.put(month, totalMap.getOrDefault(month, 0.0) + t.amount);
            categoryMap.putIfAbsent(month, new HashMap<>());
            Map<String, Double> innerMap = categoryMap.get(month);
            innerMap.put(t.category, innerMap.getOrDefault(t.category, 0.0) + t.amount);
        }

        Set<String> allMonths = new HashSet<>();
        allMonths.addAll(incomeMap.keySet());
        allMonths.addAll(expenseMap.keySet());

        for (String month : allMonths) {
            System.out.println("Month: " + month);
            System.out.println("  Income:  " + incomeMap.getOrDefault(month, 0.0));
            if (incomeCategories.containsKey(month)) {
                for (Map.Entry<String, Double> entry : incomeCategories.get(month).entrySet()) {
                    System.out.println("    - " + entry.getKey() + ": " + entry.getValue());
                }
            }
            System.out.println("  Expense: " + expenseMap.getOrDefault(month, 0.0));
            if (expenseCategories.containsKey(month)) {
                for (Map.Entry<String, Double> entry : expenseCategories.get(month).entrySet()) {
                    System.out.println("    - " + entry.getKey() + ": " + entry.getValue());
                }
            }
            System.out.println("  Savings: " + (incomeMap.getOrDefault(month, 0.0) - expenseMap.getOrDefault(month, 0.0)));
            System.out.println("---------------------------------------------------");
        }
    }
}
