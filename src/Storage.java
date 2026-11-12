import java.io.IOException;
import java.util.List;

/**
 * Handles all reading from and writing to files for the program, including
 * saving, loading and deleting Budget objects between sessions.
 *
 * @author Tawseef Jadid
 * @author Richard Medina
 * @author Juan Balbuena
 */
public class Storage {

    /** The folder where this Storage object keeps its files. */
    private String dataDirectory;

    /**
     * Creates a Storage object that keeps its files in the given folder.
     *
     * @param dataDirectory the folder where the files are kept
     * @author Tawseef Jadid
     */
    public Storage(String dataDirectory) {
    }

    /**
     * Reads every line of a text file.
     *
     * @param fileName the name or path of the file to read
     * @return the lines from the file
     * @throws IOException if the file cannot be read
     * @author Richard Medina
     */
    public List<String> readFile(String fileName) throws IOException {
        return null;
    }

    /**
     * Writes the given lines of data to a text file.
     *
     * @param fileName the name or path of the file to write
     * @param content the lines of data to write into the file
     * @throws IOException if the file cannot be written
     * @author Juan Balbuena
     */
    public void writeFile(String fileName, List<String> content) throws IOException {
    }

    /**
     * Returns the saved budget for the given year.
     *
     * @param year the year of the budget to return
     * @return the budget object for that year or null if there is none
     * @throws IOException if the saved data exists but cannot be read
     * @author Juan Balbuena
     */
    public Budget getBudget(int year) throws IOException {
        return null;
    }

    /**
     * Lists the years that have a saved budget.
     *
     * @return a list of integers for the budget years
     * @throws IOException if the data directory cannot be read
     * @author Tawseef Jadid
     */
    public List<Integer> getBudgetYears() throws IOException {
        return null;
    }

    /**
     * Saves a budget between sessions.
     *
     * @param budget the Budget object to save
     * @throws IOException if writing fails
     * @author Richard Medina
     */
    public void saveBudget(Budget budget) throws IOException {
    }

    /**
     * Deletes the saved budget for the given year.
     *
     * @param year the year of the budget to delete
     * @return true if successfully deleted, false if not
     * @author Juan Balbuena
     */
    public boolean deleteBudget(int year) {
        return false;
    }

    /**
     * Deletes all budget data kept in this data directory.
     *
     * @throws IOException if any of the data cannot be deleted
     * @author Tawseef Jadid
     */
    public void deleteData() throws IOException {
    }

    /**
     * Reads the saved file for the given year and builds a Budget object from it.
     *
     * @param year the year to load
     * @return the Budget object built from the file, or null if there is none
     * @throws IOException if the file cannot be read
     * @author Richard Medina
     */
    private Budget loadBudgetFromFile(int year) throws IOException {
        return null;
    }
}
