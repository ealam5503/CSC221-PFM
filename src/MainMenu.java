import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides the shared console menu for an authenticated user.
 * Coordinates menu actions with the other teams' modules.
 *
 * @author Sam Strugger
 */
public class MainMenu {
    private final Scanner scanner;
    private final Accounts accounts;

    private List<Integer> budgetYears = new ArrayList<>();
    private final Storage storage;

    /**
     * Creates the main menu.
     *
     * @param scanner the shared console scanner
     * @param accounts the application's Accounts module
     * @param storage the signed-in user's budget storage
     * @author Sam Strugger
     */
    public MainMenu(Scanner scanner, Accounts accounts, Storage storage){
        this.scanner = scanner;
        this.accounts = accounts;
        this.storage = storage;
    }

    /**
     * Displays the main menu options for the authenticated user.
     *
     * @author Sam Strugger
     */
    public void displayMenu() {
        System.out.println();
        System.out.println("Personal Finance Manager");
        System.out.println("-----------------------");
        System.out.println("1. Upload annual data");
        System.out.println("2. Delete annual data");
        System.out.println("3. Generate annual report");
        System.out.println("4. Audit annual data");
        System.out.println("5. Sign out");
        System.out.println();
        System.out.print("Enter your selection: ");
    }

    /**
     * Reads the user's menu selection and calls the corresponding action.
     * Invalid selections are reported and requested again.
     *
     * @author Sam Strugger
     */
    public void handleMenuSelection() {
        while (true) {
            displayMenu();

            if (!scanner.hasNextLine()) {
                accounts.signOut();
                return;
            }

            int selection;
            try {
                selection = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("Please enter a number between 1 and 5.");
                continue;
            }

            switch (selection) {
                case 1:
                    uploadAnnualData();
                    return;
                case 2:
                    deleteAnnualData();
                    return;
                case 3:
                    generateAnnualReport();
                    return;
                case 4:
                    auditAnnualData();
                    return;
                case 5:
                    signOut();
                    return;
                default:
                    System.err.println("Please enter a number between 1 and 5.");
            }
        }
    }

    /**
     * Prompts the user to select one of their stored budget years.
     * The calling action must first check that stored years are available.
     *
     * @return the selected budget year
     * @author Sam Strugger
     */
    public int selectYear() {
        if (budgetYears.isEmpty()) {
            throw new IllegalStateException("No stored budget years are available.");
        }

        System.out.println();
        System.out.println("Stored budget years");
        System.out.println("-------------------");
        for (int i = 0; i < budgetYears.size(); i++) {
            System.out.println((i + 1) + ". " + budgetYears.get(i));
        }

        while (true) {
            System.out.print("Select a year (1-" + budgetYears.size() + "): ");

            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("Console input ended before a year was selected.");
            }

            int selection;
            try {
                selection = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("Please enter a number between 1 and " + budgetYears.size() + ".");
                continue;
            }

            if (selection < 1 || selection > budgetYears.size()) {
                System.err.println("Please enter a number between 1 and " + budgetYears.size() + ".");
                continue;
            }

            return budgetYears.get(selection - 1);
        }
    }

    /**
     * Prompts for an annual CSV file and coordinates validation and storage.
     * Requests confirmation before overwriting an existing year's data and
     * lets the user skip invalid records or cancel the upload.
     *
     * @author Sam Strugger
     */
    public void uploadAnnualData(){

        System.out.print("CSV file path (leave blank to cancel): ");

        if (!scanner.hasNextLine()){
            return;
        }

        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()){
            System.out.println("Upload canceled.");
            return;
        }

        try {
            Path file = Path.of(fileName);

            if (file.getFileName() == null){
                System.err.println("Please enter a file path, not a root directory.");
                return;
            }

            // Use the project folder when only a filename is entered
            String inputDirectory = ".";
            if (file.getParent() != null){
                inputDirectory = file.getParent().toString();
            }

            Storage inputStorage = new Storage(inputDirectory);
            List<String> lines = inputStorage.readFile(file.getFileName().toString());

            if (lines.isEmpty()){
                System.err.println("The file is empty or could not be found.");
                return;
            }

            String header = lines.get(0);

            if (!Validation.headerValidate(header, header.split(",", -1).length)){
                System.err.println("Invalid header. Expected Date,Category,Amount.");
                return;
            }

            List<Transaction> transactions = new ArrayList<>();
            List<String> issues = new ArrayList<>();
            int year = -1;

            for (int i = 1; i < lines.size(); i++){
                String[] fields = lines.get(i).split(",", -1);
                int lineNumber = i + 1;

                if (fields.length != 3){
                    issues.add("Line " + lineNumber + ": Expected a date, category, and amount.");
                    continue;
                }

                for (int j = 0; j < fields.length; j++){
                    fields[j] = fields[j].trim();
                }

                if (!Validation.dateValidate(fields[0])){
                    issues.add("Line " + lineNumber + ": Invalid date. Use MM/DD/YYYY.");
                    continue;
                }

                if (!Validation.amountValidate(fields[2])){
                    issues.add("Line " + lineNumber + ": Amount must be a whole-dollar integer.");
                    continue;
                }

                int amount = Integer.parseInt(fields[2]);
                Category category = parseCategory(fields[1], amount);

                if (category == null || !Validation.categoryValidate(category.name())){
                    issues.add("Line " + lineNumber + ": Invalid category.");
                    continue;
                }

                boolean income = category == Category.COMPENSATION || category == Category.ALLOWANCE || category == Category.INVESTMENTS || category == Category.INCOME_OTHER;

                if ((income && amount <= 0) || (!income && amount >= 0)){
                    issues.add("Line " + lineNumber + ": Income must be positive and expenses must be negative.");
                    continue;
                }

                // Only a valid record can establish the budget year
                int recordYear = Integer.parseInt(fields[0].substring(6));
                if (year != -1 && recordYear != year){
                    issues.add("Line " + lineNumber + ": All records must be from " + year + ".");
                    continue;
                }

                year = recordYear;
                transactions.add(new Transaction(fields[0], category, amount));
            }

            System.out.println("Found " + transactions.size() + " valid records and " + issues.size() + " invalid records.");
            boolean hasIssues = Validation.reportIssues(issues);

            if (transactions.isEmpty()){
                System.err.println("No valid transactions were found.");
                return;
            }

            System.out.println();
            System.out.println("Budget year: " + year);

            int previewCount = Math.min(5, transactions.size());
            for (int i = 0; i < previewCount; i++){
                Transaction transaction = transactions.get(i);
                System.out.println(transaction.date() + ", " + transaction.category().name().replace('_', ' ') + ", " + transaction.amount());
            }

            if (transactions.size() > previewCount){
                System.out.println("Showing the first " + previewCount + " of " + transactions.size() + " valid records.");
            }

            if (hasIssues && !confirmAction("Skip the invalid records and continue? (y/n): ")){
                System.out.println("Upload canceled.");
                return;
            }

            List<Integer> storedYears = storage.getBudgetYears();

            if (storedYears.contains(year)){
                if (!confirmAction("A budget for " + year + " already exists. Overwrite it? (y/n): ")){
                    System.out.println("Upload canceled.");
                    return;
                }
            }

            Budget budget = new Budget(year);
            for (Transaction transaction : transactions){
                budget.addTransaction(transaction.date(), transaction.category(), transaction.amount());
            }

            storage.saveBudget(budget);

            budgetYears = new ArrayList<>(storedYears);
            if (!budgetYears.contains(year)){
                budgetYears.add(year);
            }

            System.out.println("Saved " + transactions.size() + " records for " + year + ".");

        } catch (InvalidPathException e) {
            System.err.println("Invalid file path: " + e.getMessage());

        } catch (IOException e) {
            System.err.println("Could not complete the upload: " + e.getMessage());
        }
    }

    /**
     * Converts a CSV category label to the shared category enum.
     *
     * @param categoryName the category text from the CSV
     * @param amount the amount used to distinguish income and expense Other
     * @return the matching category, or null if it is not recognized
     * @author Sam Strugger
     */
    private Category parseCategory(String categoryName, int amount){

        String categoryText = categoryName.trim();

        if (categoryText.equalsIgnoreCase("Other")){
            if (amount < 0) return Category.EXPENSE_OTHER;
            return Category.INCOME_OTHER;
        }

        for (Category category : Category.values()){
            String label = category.name().replace('_', ' ');

            if (categoryText.equalsIgnoreCase(label) || categoryText.equalsIgnoreCase(category.name())){
                return category;
            }
        }

        return null;
    }

    /**
     * Asks for a yes/no confirmation using the shared scanner.
     *
     * @param prompt the question to display
     * @return true for yes, false for no or end of input
     * @author Sam Strugger
     */
    private boolean confirmAction(String prompt){

        while (true){
            System.out.print(prompt);
            if (!scanner.hasNextLine()) return false;

            String answer = scanner.nextLine().trim();
            if (Validation.inputValidate(answer, Validation.InputType.YES_NO, 0, 0)){
                return answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes");
            }

            System.err.println("Please enter y or n.");
        }
    }

    /**
     * Prompts for a stored year and confirmation, then requests its deletion
     * through the Storage module.
     *
     * @author Sam Strugger
     * @author Asher Malik
     */
    public void deleteAnnualData() {
        if (storage == null) {
            System.err.println("Budget storage is not connected yet.");
            return;
        }

        try {
            List<Integer> storedYears = storage.getBudgetYears();
            if (storedYears == null) {
                System.err.println("Could not load the stored budget years.");
                return;
            }
            if (storedYears.isEmpty()) {
                System.out.println("No stored budget years are available to delete.");
                return;
            }

            budgetYears = storedYears;
            int year = selectYear();
            System.out.println("Are you sure you want to delete budget data for " + year + "? (y/n)");
            if( !scanner.hasNextLine()){
                return;
            }
            String confirmation = scanner.nextLine().trim();
            if(!confirmation.equalsIgnoreCase("y") && !confirmation.equalsIgnoreCase("yes")){
                System.out.println("Deletion canceled.");
                return;
            }
            if (storage.deleteBudget(year)) {
                budgetYears.remove(Integer.valueOf(year));

                System.out.println("Budget data for " + year + " has been deleted.");

            } else {
                System.err.println("Could not delete the budget data for " + year + ".");
            }

        } catch (IOException e) {
            System.err.println("Could not read the budget data: " + e.getMessage());
        }catch (IllegalStateException e){
            System.err.println(e.getMessage());
        }

    }

    /**
     * Prompts for a stored year and console or CSV output, then coordinates
     * report generation with the Reports module.
     *
     * @author Sam Strugger
     * @author Asher Malik
     */
    public void generateAnnualReport() {
        if (storage == null) {
            System.err.println("Budget storage is not connected yet.");
            return;
        }

        try {
            List<Integer> storedYears = storage.getBudgetYears();
            if (storedYears == null) {
                System.err.println("Could not load the stored budget years.");
                return;
            }
            if (storedYears.isEmpty()) {
                System.out.println("No stored budget years are available to report.");
                return;
            }

            budgetYears = storedYears;
            int year = selectYear();

            Budget budget = storage.getBudget(year);

            if (budget == null) {
                System.err.println("No budget data found for " + year + ".");
                return;
            }

            System.out.println("Select report output format:");
            System.out.println("1. Display report to console.");
            System.out.println("2. Export report to csv file");
            System.out.println("Enter your choice (1 or 2)");

            if(!scanner.hasNextLine()){
                return;
            }

            String choice = scanner.nextLine().trim();

            Reports reportGenerator = new Reports(new ArrayList<>(budget.getAllTransactions()));
            if(choice.equals("1")){
                reportGenerator.generateAnnualReport();
            }else if (choice.equals("2")){
                reportGenerator.exportReportToCSV();
            }else{
                System.err.println("Invalid Selection. Canceled generating report.");
            }

        } catch (IOException e) {
            System.err.println("Could not read the budget data: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * Runs the audit submenu for a selected year's entries.
     * Provides options to run an audit, choose excluded categories, clear
     * exclusions, and choose a different year. Displays current exclusions
     * and the formatted report supplied by the Data Audit module.
     *
     * @author Sam Strugger
     */
    public void auditAnnualData() {
        if (storage == null) {
            System.err.println("Budget storage is not connected yet.");
            return;
        }

        try {
            List<Integer> storedYears = storage.getBudgetYears();
            if (storedYears == null) {
                System.err.println("Could not load the stored budget years.");
                return;
            }
            if (storedYears.isEmpty()) {
                System.out.println("No stored budget years are available to audit.");
                return;
            }

            budgetYears = storedYears;
            int year = selectYear();
            DataAudit dataAudit = new DataAudit();
            Category[] categories = Category.values();

            while (true) {
                System.out.println();
                System.out.println("Data audit for " + year);
                System.out.println("-------------------");
                System.out.print("Excluded categories: ");
                if (dataAudit.getExcludedCategories().isEmpty()) {
                    System.out.println("None");
                } else {
                    for (Category category : categories) {
                        if (dataAudit.getExcludedCategories().contains(category)) {
                            System.out.print(category.name().replace('_', ' ') + "; ");
                        }
                    }
                    System.out.println();
                }
                System.out.println("1. Run audit");
                System.out.println("2. Exclude categories");
                System.out.println("3. Clear excluded categories");
                System.out.println("4. Choose a different year");
                System.out.println("5. Return to main menu");
                System.out.print("Enter your selection: ");

                if (!scanner.hasNextLine()) {
                    return;
                }

                int selection;
                try {
                    selection = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.err.println("Please enter a number between 1 and 5.");
                    continue;
                }

                switch (selection) {
                    case 1:
                        Budget budget = storage.getBudget(year);
                        if (budget == null) {
                            System.err.println("Could not load the budget for " + year + ".");
                            break;
                        }
                        List<Transaction> entries = budget.getAllTransactions();
                        if (entries == null) {
                            System.err.println("Could not load the transactions for " + year + ".");
                            break;
                        }
                        System.out.println(dataAudit.runAudit(entries));
                        break;
                    case 2:
                        System.out.println();
                        for (int i = 0; i < categories.length; i++) {
                            System.out.println((i + 1) + ". " + categories[i].name().replace('_', ' '));
                        }
                        System.out.println("0. Return to audit menu");

                        while (true) {
                            System.out.print("Select a category to exclude (0-" + categories.length + "): ");
                            if (!scanner.hasNextLine()) {
                                return;
                            }

                            int categorySelection;
                            try {
                                categorySelection = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.err.println("Please enter a number between 0 and " + categories.length + ".");
                                continue;
                            }

                            if (categorySelection == 0) {
                                break;
                            }
                            if (categorySelection < 0 || categorySelection > categories.length) {
                                System.err.println("Please enter a number between 0 and " + categories.length + ".");
                                continue;
                            }

                            Category category = categories[categorySelection - 1];
                            dataAudit.setExcludedCategories(category);
                            System.out.println("Excluded " + category.name().replace('_', ' ') + ".");
                        }
                        break;
                    case 3:
                        dataAudit.clearExcludedCategories();
                        System.out.println("All categories will be included in the audit.");
                        break;
                    case 4:
                        year = selectYear();
                        break;
                    case 5:
                        return;
                    default:
                        System.err.println("Please enter a number between 1 and 5.");
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read the budget data: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Requests sign-out through the Accounts module and leaves the
     * authenticated menu.
     *
     * @author Sam Strugger
     * @author Asher Malik
     */
    public void signOut() {

        if (accounts == null) {
            System.err.println("Accounts module is not connected.");
            return;
        }

        accounts.signOut();
        System.out.println("You have been signed out successfully.");
    }
}
