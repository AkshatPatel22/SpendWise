import java.util.*;
public class Budget {
    // The category for which this budget applies (e.g., "Food", "Rent").
    private String category;

    // The maximum allowable spending limit for this category.
    private double limit;

    // The total amount spent in this category so far.
    private double spent;

    /**
     * Constructor to initialize a budget with a category and spending limit.
     *
     * @param category The name of the category for this budget.
     * @param limit The spending limit for the category.
     */
    public Budget(String category, double limit) {
        this.category = category;
        this.limit = limit;
        this.spent = 0; // Initialize spent amount to zero.
    }

    /**
     * Adds an expense amount to the total spent for this budget.
     *
     * @param amount The amount to add to the spent total.
     */
    public void addExpense(double amount) {
        this.spent += amount;
    }

    /**
     * Checks if the spending has exceeded the set limit for this category.
     *
     * @return True if the total spent exceeds the limit, otherwise false.
     */
    public boolean isOverBudget() {
        return spent > limit;
    }

    /**
     * Calculates the remaining budget by subtracting the spent amount from the limit.
     *
     * @return The remaining amount available to spend in this category.
     */
    public double getRemaining() {
        return limit - spent;
    }

    /**
     * Gets the name of the budget category.
     *
     * @return The category name.
     */
    public String getCategory() { return category; }

    /**
     * Gets the spending limit for this category.
     *
     * @return The spending limit.
     */
    public double getLimit() { return limit; }

    /**
     * Gets the total amount spent so far in this category.
     *
     * @return The total spent amount.
     */
    public double getSpent() { return spent; }
}
