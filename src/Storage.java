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
     * Reads every line of a text file.
     *
     * @param fileName the name or path of the file to read
     * @return the lines from the file
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
     * Writes the given lines of data to a text file.
     *
     * @param fileName the name or path of the file to write
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
        // Implementation will depend on how your Budget class is structured 
        // (e.g., reading a file named "budget_" + year + ".txt")
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
     * Saves a budget between sessions.
     *
     * @param budget the Budget object to save
     * @throws IOException if writing fails
     * @author Richard Medina
     */
    public void saveBudget(Budget budget) throws IOException {
        // Example implementation hook:
        // List<String> data = budget.serialize();
        // writeFile("budget_" + budget.getYear() + ".txt", data);
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
     * Deletes all budget data kept in this data directory.
     *
     * @throws IOException if any of the data cannot be deleted
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
        // Logic to construct and return a Budget object from lines goes here
        return null; 
    }
}
