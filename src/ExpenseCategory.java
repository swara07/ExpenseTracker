import java.util.Arrays;
import java.util.List;

public class ExpenseCategory implements CategoryFactory{

    public List<String> getCategories() {
        return Arrays.asList("Food", "Rent", "Travel", "Utilities");
    }
}
