import java.util.List;

/**
 * Holds the income and expense transactions for a single year.
 */
public class Budget {

    /**
     * Creates an empty budget for the given year.
     *
     * @param year the year this budget covers
     */
    public Budget(int year) {
    }

    /**
     * Gets the year this budget covers.
     *
     * @return the year
     */
    public int getYear() {
        return 0;
    }

    /**
     * Adds one transaction. Income amounts are positive and expenses are negative.
     *
     * @param date the date (MM/DD/YYYY) of the transaction
     * @param category the category of the transaction
     * @param amount the dollar amount of the transaction
     */
    public void addTransaction(String date, Category category, int amount) {
    }

    /**
     * Gets every transaction in this budget.
     *
     * @return a copy of the list of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return null;
    }

    /**
     * Gets only the transactions in one category.
     *
     * @param category the category to look for
     * @return a copy of the list of matching transactions (empty if none)
     */
    public List<Transaction> getTransactions(Category category) {
        return null;
    }
}