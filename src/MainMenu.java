import java.io.IOException;
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
    private final Scanner scanner = new Scanner(System.in);
    private List<Integer> budgetYears = new ArrayList<>();
    private Storage storage;

    /**
     * Creates the main menu.
     *
     * @author Sam Strugger
     */
    public MainMenu() {
        // Dependencies will be connected when the shared module contracts are available.
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
    public void uploadAnnualData() {
        throw new UnsupportedOperationException("Annual data upload is not implemented yet.");
    }

    /**
     * Prompts for a stored year and confirmation, then requests its deletion
     * through the Storage module.
     *
     * @author Sam Strugger
     */
    public void deleteAnnualData() {
        throw new UnsupportedOperationException("Annual data deletion is not implemented yet.");
    }

    /**
     * Prompts for a stored year and console or CSV output, then coordinates
     * report generation with the Reports module.
     *
     * @author Sam Strugger
     */
    public void generateAnnualReport() {
        throw new UnsupportedOperationException("Annual reporting is not implemented yet.");
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
     */
    public void signOut() {
        throw new UnsupportedOperationException("Sign-out is not implemented yet.");
    }
}
