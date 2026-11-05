/**
 * One income or expense entry: its date (MM/DD/YYYY), category, and dollar amount.
 * Income is positive and expenses are negative.
 */
public record Transaction(String date, Category category, int amount) {
}