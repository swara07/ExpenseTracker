import java.util.Arrays;
import java.util.List;

public class IncomeCategory implements CategoryFactory{

    public List<String> getCategories() {
        return Arrays.asList("Salary", "Business", "Investments");
    }
}
