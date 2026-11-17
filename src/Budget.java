import java.util.List;
import java.util.ArrayList;

/**
 * Holds the income and expense transactions for a single year.
 * @author Tawseef Jadid
 * @author Juan Balbuena
 */
public class Budget {
	
	private final int year;
	private final List<Transaction> transactions;
	
    /**
     * Creates an empty budget for the given year.
     *
     * @param year the year this budget covers
     * @author Tawseef Jadid
     */
    public Budget(int year) {
    	this.year = year;
    	this.transactions = new ArrayList<>();
    }

    /**
     * Gets the year this budget covers.
     *
     * @return the year
     * @author Tawseef Jadid
     */
    public int getYear() {
        return year;
    }

    /**
     * Adds one transaction. Income amounts are positive and expenses are negative.
     *
     * @param date the date (MM/DD/YYYY) of the transaction
     * @param category the category of the transaction
     * @param amount the dollar amount of the transaction
     * @author Tawseef Jadid
     */
    public void addTransaction(String date, Category category, int amount) {
    	transactions.add(new Transaction(date, category, amount));
    }

    /**
     * Gets every transaction in this budget.
     *
     * @return a copy of the list of all transactions
     * @author Juan Balbuena 
     */
    public List<Transaction> getAllTransactions() {
        return null;
    }

    /**
     * Gets only the transactions in one category.
     *
     * @param category the category to look for
     * @return a copy of the list of matching transactions (empty if none)
     * @author Juan Balbuena
     */
    public List<Transaction> getTransactions(Category category) {
        return null;
    }
}
