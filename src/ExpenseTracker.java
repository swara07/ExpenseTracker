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

        for (Transaction t : transactions) {
            String month = new SimpleDateFormat("yyyy-MM").format(t.date);
            Map<String, Double> map = t.type == TransactionType.INCOME ? incomeMap : expenseMap;
            map.put(month, map.getOrDefault(month, 0.0) + t.amount);
        }

        Set<String> allMonths = new HashSet<>();
        allMonths.addAll(incomeMap.keySet());
        allMonths.addAll(expenseMap.keySet());

        for (String month : allMonths) {
            System.out.println("Month: " + month);
            System.out.println("  Income:  " + incomeMap.getOrDefault(month, 0.0));
            System.out.println("  Expense: " + expenseMap.getOrDefault(month, 0.0));
            System.out.println("  Savings: " + (incomeMap.getOrDefault(month, 0.0) - expenseMap.getOrDefault(month, 0.0)));
            System.out.println("---------------------------------------------------");
        }
    }
}
