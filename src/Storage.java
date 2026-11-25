import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        this.dataDirectory = dataDirectory;
    }

    /**
     * Reads every line of a text file in this Storage object's data folder.
     * If the file does not exist, an empty list is returned instead of an error.
     *
     * @param fileName the name of the file to read, relative to the data folder
     * @return the lines from the file, or an empty list if the file does not exist
     * @throws IOException if the file cannot be read
     * @author Richard Medina
     */
    public List<String> readFile(String fileName) throws IOException {
        Path filePath = Paths.get(dataDirectory, fileName);
        List<String> lines = new ArrayList<>();
        
        if (!Files.exists(filePath)) {
            return lines; // Return empty list if file doesn't exist yet
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Writes the given lines of data to a text file in this Storage object's
     * data folder, creating the folder if needed. An existing file with the
     * same name is replaced.
     *
     * @param fileName the name of the file to write, relative to the data folder
     * @param content the lines of data to write into the file
     * @throws IOException if the file cannot be written
     * @author Juan Balbuena
     */
    public void writeFile(String fileName, List<String> content) throws IOException {
        Path dirPath = Paths.get(dataDirectory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        Path filePath = Paths.get(dataDirectory, fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String line : content) {
                writer.write(line);
                writer.newLine();
            }
        }
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
        return loadBudgetFromFile(year);
    }

    /**
     * Lists the years that have a saved budget.
     *
     * @return a list of integers for the budget years
     * @throws IOException if the data directory cannot be read
     * @author Tawseef Jadid
     */
    public List<Integer> getBudgetYears() throws IOException {
        List<Integer> years = new ArrayList<>();
        Path dirPath = Paths.get(dataDirectory);
        
        if (!Files.exists(dirPath)) {
            return years;
        }

        try (var stream = Files.list(dirPath)) {
            stream.forEach(path -> {
                String fileName = path.getFileName().toString();
                // Assuming files are named like "budget_2026.txt"
                if (fileName.startsWith("budget_") && fileName.endsWith(".txt")) {
                    try {
                        String yearStr = fileName.substring(7, fileName.indexOf(".txt"));
                        years.add(Integer.parseInt(yearStr));
                    } catch (NumberFormatException e) {
                        // Ignore malformed filenames
                    }
                }
            });
        }
        return years;
    }

    /**
     * Saves a budget between sessions. Any budget already saved for the same
     * year is replaced. Does nothing if the budget is null.
     *
     * @param budget the Budget object to save
     * @throws IOException if writing fails
     * @author Richard Medina
     */
    public void saveBudget(Budget budget) throws IOException {
       if (budget == null) return;
        
        List<String> content = new ArrayList<>();
        content.add(String.valueOf(budget.getYear()));
        for (Transaction t : budget.getAllTransactions()) {
        content.add(t.date() + "," + t.category() + "," + t.amount());
    }
        writeFile("budget_" + budget.getYear() + ".txt", content);    
}
        
    /**
     * Deletes the saved budget for the given year.
     *
     * @param year the year of the budget to delete
     * @return true if successfully deleted, false if not
     * @author Juan Balbuena
     */
    public boolean deleteBudget(int year) {
        try {
            Path filePath = Paths.get(dataDirectory, "budget_" + year + ".txt");
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Deletes all budget data kept in this data directory, including the
     * directory itself. Files that cannot be deleted are skipped without
     * reporting an error.
     *
     * @throws IOException if the data directory cannot be read
     * @author Tawseef Jadid
     */
    public void deleteData() throws IOException {
        Path dirPath = Paths.get(dataDirectory);
        if (Files.exists(dirPath)) {
            try (var stream = Files.walk(dirPath)) {
                stream.sorted((p1, p2) -> -p1.compareTo(p2)) // Delete files before directory
                      .forEach(path -> {
                          try {
                              Files.delete(path);
                          } catch (IOException e) {
                              // Handle or log deletion error
                          }
                      });
            }
        }
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
     Path filePath = Paths.get(dataDirectory, "budget_" + year + ".txt");
        if (!Files.exists(filePath)) {
            return null;
        }
        
        List<String> lines = readFile("budget_" + year + ".txt");
        if (lines.isEmpty()) {
            return null;
        }

        Budget budget = new Budget(year);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String date = parts[0];
                Category category = Category.valueOf(parts[1]);
                int amount = Integer.parseInt(parts[2]);

                budget.addTransaction(date, category, amount);
            }
        }
        return budget;
}
}    
    
