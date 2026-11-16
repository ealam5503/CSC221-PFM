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
        throw new UnsupportedOperationException("Annual data auditing is not implemented yet.");
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
