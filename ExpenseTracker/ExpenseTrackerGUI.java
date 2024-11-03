import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.List;

public class ExpenseTrackerGUI {
    private final ExpenseManager expenseManager;
    private JTextArea displayArea;
    private JTextField dateField, descriptionField, amountField, categoryField, dailyBudgetField, monthlyBudgetField, yearlyBudgetField;

    public ExpenseTrackerGUI() {
        expenseManager = new ExpenseManager();
        createGUI();
    }

    private void createGUI() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Input panel for adding expenses
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Date (yyyy-mm-dd):"));
        dateField = new JTextField();
        inputPanel.add(dateField);
        
        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);
        
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);
        
        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);
        
        inputPanel.add(new JLabel("Daily Budget:"));
        dailyBudgetField = new JTextField();
        inputPanel.add(dailyBudgetField);
        
        inputPanel.add(new JLabel("Monthly Budget:"));
        monthlyBudgetField = new JTextField();
        inputPanel.add(monthlyBudgetField);
        
        inputPanel.add(new JLabel("Yearly Budget:"));
        yearlyBudgetField = new JTextField();
        inputPanel.add(yearlyBudgetField);

        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(e -> addExpense());
        inputPanel.add(addButton);

        JButton setBudgetButton = new JButton("Set Budgets");
        setBudgetButton.addActionListener(e -> setBudgets());
        inputPanel.add(setBudgetButton);

        JButton saveButton = new JButton("Save to CSV");
        saveButton.addActionListener(e -> saveExpenses());
        inputPanel.add(saveButton);

        JButton loadButton = new JButton("Load from CSV");
        loadButton.addActionListener(e -> loadExpenses());
        inputPanel.add(loadButton);

        // Display area for showing expenses
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Button panel for analytics
        JPanel analyticsPanel = new JPanel();
        JButton totalSpendingButton = new JButton("Show Total Spending");
        totalSpendingButton.addActionListener(e -> showTotalSpending());
        analyticsPanel.add(totalSpendingButton);

        JButton categoryBreakdownButton = new JButton("Show Category Breakdown");
        categoryBreakdownButton.addActionListener(e -> showCategoryBreakdown());
        analyticsPanel.add(categoryBreakdownButton);

        JButton viewDailyExpensesButton = new JButton("View Daily Expenses");
        viewDailyExpensesButton.addActionListener(e -> viewExpenses("daily"));
        analyticsPanel.add(viewDailyExpensesButton);

        JButton viewWeeklyExpensesButton = new JButton("View Weekly Expenses");
        viewWeeklyExpensesButton.addActionListener(e -> viewExpenses("weekly"));
        analyticsPanel.add(viewWeeklyExpensesButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(analyticsPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void addExpense() {
        String date = dateField.getText();
        String description = descriptionField.getText();
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a numeric value.");
            return;
        }
        String category = categoryField.getText();

        Expense expense = new Expense(date, description, amount, category);
        expenseManager.addExpense(expense);
        displayArea.append("Added: " + expense + "\n");
        clearInputFields();
    }

    private void setBudgets() {
        double daily, monthly, yearly;
        try {
            daily = Double.parseDouble(dailyBudgetField.getText());
            monthly = Double.parseDouble(monthlyBudgetField.getText());
            yearly = Double.parseDouble(yearlyBudgetField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid budget value. Please enter numeric values.");
            return;
        }
        expenseManager.setBudgets(daily, monthly, yearly);
        JOptionPane.showMessageDialog(null, "Budgets set!");
    }

    private void saveExpenses() {
        expenseManager.saveToCSV("expenses.csv");
        JOptionPane.showMessageDialog(null, "Expenses saved to expenses.csv!");
    }

    private void loadExpenses() {
        expenseManager.loadFromCSV("expenses.csv");
        displayArea.setText("Expenses Loaded:\n");
        for (Expense expense : expenseManager.getExpenses()) {
            displayArea.append(expense.toString() + "\n");
        }
    }

    private void showTotalSpending() {
        double totalSpending = expenseManager.calculateTotalSpending();
        JOptionPane.showMessageDialog(null, "Total Spending: $" + totalSpending);
    }

    private void showCategoryBreakdown() {
        Map<String, Double> categoryTotals = expenseManager.categorizeExpenses();
        StringBuilder breakdown = new StringBuilder("Category Breakdown:\n");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            breakdown.append(entry.getKey()).append(": $").append(entry.getValue()).append("\n");
        }
        JOptionPane.showMessageDialog(null, breakdown.toString());
    }

    private void viewExpenses(String timeframe) {
        List<Expense> filteredExpenses = expenseManager.getExpensesByTimeframe(timeframe);
        displayArea.setText("Expenses for " + timeframe + ":\n");
        for (Expense expense : filteredExpenses) {
            displayArea.append(expense.toString() + "\n");
        }
    }

    private void clearInputFields() {
        dateField.setText("");
        descriptionField.setText("");
        amountField.setText("");
        categoryField.setText("");
        dailyBudgetField.setText("");
        monthlyBudgetField.setText("");
        yearlyBudgetField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerGUI::new);
    }
}
