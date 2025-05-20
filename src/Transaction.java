import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    TransactionType type;
    String category;
    double amount;
    Date date;

    public Transaction(TransactionType type, String category, double amount, Date date) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String toString() {
        return type + "," + category + "," + amount + "," + new SimpleDateFormat("yyyy-MM").format(date);
    }
}
