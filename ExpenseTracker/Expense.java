public class Expense {
    private final String date;
    private final String description;
    private final double amount;
    private final String category;

    public Expense(String date, String description, double amount, String category) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return date + " - " + description + ": $" + amount + " (" + category + ")";
    }
}
