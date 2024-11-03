import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class ExpenseManager {
    private final List<Expense> expenses;
    private double dailyBudget;
    private double monthlyBudget;
    private double yearlyBudget;

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadExpensesFromCSV("expenses.csv");
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        saveExpensesToCSV("expenses.csv");
        checkBudgets();
    }

    public void setBudgets(double daily, double monthly, double yearly) {
        this.dailyBudget = daily;
        this.monthlyBudget = monthly;
        this.yearlyBudget = yearly;
    }

    public void saveToCSV(String filePath) {
        saveExpensesToCSV(filePath);
    }

    public void loadFromCSV(String filePath) {
        loadExpensesFromCSV(filePath);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public double calculateTotalSpending() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public Map<String, Double> categorizeExpenses() {
        return expenses.stream().collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));
    }

    public List<Expense> getExpensesByTimeframe(String timeframe) {
        // Implement filtering logic based on timeframe (daily, weekly, etc.)
        // Here you could filter based on the expense date
        return expenses; // Return all expenses for now; implement filtering as needed.
    }

    private void checkBudgets() {
        double dailySpending = calculateTotalSpending(); // You might want to calculate this based on the actual date
        if (dailySpending > dailyBudget) {
            JOptionPane.showMessageDialog(null, "Warning: Daily spending has exceeded your budget!");
        }
        // Implement similar checks for monthly and yearly budgets as needed
    }

    private void saveExpensesToCSV(String filePath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (Expense expense : expenses) {
                writer.write(expense.getDate() + "," + expense.getDescription() + "," + expense.getAmount() + "," + expense.getCategory());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadExpensesFromCSV(String filePath) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            expenses.clear(); // Clear existing expenses before loading new ones
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Expense expense = new Expense(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]);
                    expenses.add(expense);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load expenses: " + e.getMessage());
        }
    }
}
