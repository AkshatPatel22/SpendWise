import java.util.*;

/**
 * The ExpenseManager class handles the management of expenses and budgets.
 * It allows tracking of individual expenses, setting budgets, and checking budget status.
 * @author Akshat Patel
 * */
public class ExpenseManager {

    // List to store all recorded expenses.
    private List<Expense> expenses;

    // Map to store budgets by category.
    private Map<String, Budget> budgets;

    // The total amount of all recorded expenses.
    private double totalExpenses;

    /**
     * Constructor to initialize the ExpenseManager with empty expense and budget lists.
     */
    public ExpenseManager() {
        expenses = new ArrayList<>();
        budgets = new HashMap<>();
        totalExpenses = 0; // Initialize total expenses to zero.
    }

    /**
     * Adds a new expense to the list and updates the total expenses.
     * If a budget exists for the category of the expense, the budget is updated.
     *
     * @param expense The expense to add.
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
        totalExpenses += expense.getAmount();

        // Update the budget for the category if it exists.
        Budget budget = budgets.get(expense.getCategory());
        if (budget != null) {
            budget.addExpense(expense.getAmount());
        }
    }

    /**
     * Sets a budget for a specific category.
     * If a budget already exists for the category, it is replaced with the new one.
     *
     * @param category The category for which the budget is set.
     * @param limit The spending limit for the budget.
     */
    public void setBudget(String category, double limit) {
        budgets.put(category, new Budget(category, limit));
    }

    /**
     * Checks if the spending in a specific category has exceeded the budget limit.
     *
     * @param category The category to check.
     * @return True if the budget for the category is exceeded, otherwise false.
     */
    public boolean isOverBudget(String category) {
        Budget budget = budgets.get(category);
        return budget != null && budget.isOverBudget();
    }

    /**
     * Gets the total amount of all recorded expenses.
     *
     * @return The total expenses.
     */
    public double getTotalExpenses() { return totalExpenses; }

    /**
     * Retrieves a copy of the list of all recorded expenses.
     *
     * @return A new list containing all recorded expenses.
     */
    public List<Expense> getExpenses() { return new ArrayList<>(expenses); }

    /**
     * Retrieves the budget for a specific category.
     *
     * @param category The category whose budget is to be retrieved.
     * @return The budget for the category, or null if no budget exists.
     */
    public Budget getBudget(String category) { return budgets.get(category); }
}
