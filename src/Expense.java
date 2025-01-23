import java.time.LocalDate;
/**
 * The Expense class represents a single financial transaction.
 * This class provides methods to retrieve the details of an expense
 * and a custom string representation for easy display.
 *
 * @author Akshat Patel
 */
public class Expense {
    private double amount;
    private String category;
    private String description;
    private LocalDate date;

    public Expense(double amount, String category, String description, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return String.format("%s - $%.2f - %s - %s", date, amount, category, description);
    }
}