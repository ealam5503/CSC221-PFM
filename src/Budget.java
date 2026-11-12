import java.util.List;

/**
 * Holds the income and expense transactions for a single year.
 * @author Tawseef Jadid
 * @author Richard Medina
 * @author Juan Balbuena
 */
public class Budget {

    /**
     * Creates an empty budget for the given year.
     *
     * @param year the year this budget covers
     * @author Tawseef Jadid
     */
    public Budget(int year) {
    }

    /**
     * Gets the year this budget covers.
     *
     * @return the year
     * @author Juan Balbuena
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
     * @author Richard Medina
     */
    public void addTransaction(String date, Category category, int amount) {
    }

    /**
     * Gets every transaction in this budget.
     *
     * @return a copy of the list of all transactions
     * @author Tawseef Jadid
     */
    public List<Transaction> getAllTransactions() {
        return null;
    }

    /**
     * Gets only the transactions in one category.
     *
     * @param category the category to look for
     * @return a copy of the list of matching transactions (empty if none)
     * @author Richard Medina
     */
    public List<Transaction> getTransactions(Category category) {
        return null;
    }
}
