/**
 * One income or expense entry: its date (MM/DD/YYYY), category, and dollar amount.
 * Income is positive and expenses are negative
 *
 * @param date     the date of the transaction in MM/DD/YYYY format
 * @param category the category of the transaction
 * @param amount   the dollar amount
 */
public record Transaction(String date, Category category, int amount) {
}
