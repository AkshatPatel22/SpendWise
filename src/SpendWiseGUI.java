import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.time.LocalDate;

/**
 * SpendWiseApp.java
 * A simplified JavaFX-based expense tracking application
 * @author Akshat Patel
 * Date: 21/01/2025
 */
public class SpendWiseGUI extends Application {

    // View Components
    private TextField amountInput;
    private ComboBox<String> categoryInput;
    private TextArea descriptionInput;
    private ListView<String> expenseList;
    private Label totalExpenseLabel;
    private Label monthlyTotalLabel;
    private Button addButton;
    private Button budgetButton;
    private Button clearButton;
    private Canvas canvas;
    private GraphicsContext gc;

    // Model
    private ExpenseManager expenseManager;

    @Override
    public void start(Stage stage) {
        // Initialize the model
        expenseManager = new ExpenseManager();

        // Create root pane and scene
        Pane root = new Pane();
        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("SpendWise");

        // Load CSS
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Initialize canvas for displaying messages
        canvas = new Canvas(500, 600);
        gc = canvas.getGraphicsContext2D();

        // Initialize GUI components
        initializeComponents();

        // Add components to root
        root.getChildren().addAll(canvas, amountInput, categoryInput, descriptionInput, expenseList, totalExpenseLabel, monthlyTotalLabel, addButton, budgetButton, clearButton);

        // Position components
        positionComponents();

        // Set up event handlers
        setupEventHandlers();

        // Show the stage
        stage.setScene(scene);
        stage.show();
    }

    private void initializeComponents() {
        // Initialize input fields
        amountInput = new TextField();
        amountInput.setPromptText("Enter amount");
        amountInput.getStyleClass().add("text-field");

        categoryInput = new ComboBox<>();
        categoryInput.getItems().addAll("Food", "Transportation", "Utilities", "Entertainment", "Rent", "Other");
        categoryInput.setPromptText("Select category");
        categoryInput.getStyleClass().add("combo-box");

        descriptionInput = new TextArea();
        descriptionInput.setPromptText("Enter description");
        descriptionInput.setPrefRowCount(2);
        descriptionInput.setPrefWidth(460);
        descriptionInput.setPrefHeight(60);
        descriptionInput.getStyleClass().add("text-area");

        // Initialize display components
        expenseList = new ListView<>();
        expenseList.setPrefHeight(250);
        expenseList.getStyleClass().add("list-view");

        totalExpenseLabel = new Label("Total Expenses: $0.00");
        monthlyTotalLabel = new Label("This Month: $0.00");
        totalExpenseLabel.getStyleClass().add("label");
        monthlyTotalLabel.getStyleClass().add("label");

        // Initialize buttons
        addButton = new Button("Add Expense");
        budgetButton = new Button("Set Budget");
        clearButton = new Button("Clear Fields");
        addButton.getStyleClass().add("button");
        budgetButton.getStyleClass().add("button");
        clearButton.getStyleClass().add("button");
    }

    private void positionComponents() {
        amountInput.relocate(20, 20);
        categoryInput.relocate(20, 63);
        descriptionInput.relocate(20, 107);

        expenseList.relocate(20, 180);
        expenseList.setPrefWidth(460);

        totalExpenseLabel.relocate(20, 450);
        monthlyTotalLabel.relocate(20, 480);

        addButton.relocate(20, 520);
        budgetButton.relocate(200, 520);
        clearButton.relocate(380, 520);
    }

    private void setupEventHandlers() {
        addButton.setOnAction(e -> handleAddExpense());
        budgetButton.setOnAction(e -> handleSetBudget());
        clearButton.setOnAction(e -> clearInputFields());
    }

    private void handleAddExpense() {
        try {
            String amountText = amountInput.getText();
            String category = categoryInput.getValue();
            String description = descriptionInput.getText();

            if (amountText.isEmpty() || category == null || description.isEmpty()) {
                drawMessage("Please fill in all fields", false);
                return;
            }

            double amount = Double.parseDouble(amountText);
            Expense expense = new Expense(amount, category, description, LocalDate.now());
            expenseManager.addExpense(expense);

            if (expenseManager.isOverBudget(category)) {
                drawMessage("Budget exceeded for " + category, false);
            } else {
                drawMessage("Expense added successfully", true);
            }

            updateDisplay();
            clearInputFields();

        } catch (NumberFormatException e) {
            drawMessage("Please enter a valid amount", false);
        }
    }

    private void handleSetBudget() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Set Budget");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));

        ComboBox<String> categorySelect = new ComboBox<>(categoryInput.getItems());
        TextField budgetField = new TextField();

        grid.add(new Label("Category:"), 0, 0);
        grid.add(categorySelect, 1, 0);
        grid.add(new Label("Budget Amount:"), 0, 1);
        grid.add(budgetField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String category = categorySelect.getValue();
                    double amount = Double.parseDouble(budgetField.getText());

                    if (category != null && amount > 0) {
                        expenseManager.setBudget(category, amount);
                        drawMessage(String.format("Budget set for %s: $%.2f", category, amount), true);
                    }
                } catch (NumberFormatException e) {
                    drawMessage("Please enter a valid amount", false);
                }
            }
        });
    }

    private void updateDisplay() {
        expenseList.getItems().clear();
        for (Expense expense : expenseManager.getExpenses()) {
            expenseList.getItems().add(0, expense.toString());
        }

        totalExpenseLabel.setText(String.format("Total Expenses: $%.2f", expenseManager.getTotalExpenses()));

        double monthlyTotal = expenseManager.getExpenses().stream()
                .filter(e -> e.getDate().getMonth() == LocalDate.now().getMonth())
                .mapToDouble(Expense::getAmount)
                .sum();
        monthlyTotalLabel.setText(String.format("This Month: $%.2f", monthlyTotal));
    }

    private void clearInputFields() {
        amountInput.clear();
        categoryInput.setValue(null);
        descriptionInput.clear();
    }

    private void drawMessage(String message, boolean isSuccess) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(isSuccess ? Color.GREEN : Color.RED);
        gc.fillText(message, 20, 160);
    }

    public static void main(String[] args) {
        launch(args);
    }
}