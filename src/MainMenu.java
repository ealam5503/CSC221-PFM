/**
 * Provides the shared console menu for an authenticated user.
 * Coordinates menu actions with the other teams' modules.
 *
 * @author Sam Strugger
 */
public class MainMenu {
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
        throw new UnsupportedOperationException("Menu display is not implemented yet.");
    }

    /**
     * Reads the user's menu selection and calls the corresponding action.
     * Invalid selections are reported and requested again.
     *
     * @author Sam Strugger
     */
    public void handleMenuSelection() {
        throw new UnsupportedOperationException("Menu selection is not implemented yet.");
    }

    /**
     * Prompts the user to select one of their stored budget years.
     * The calling action must first check that stored years are available.
     *
     * @return the selected budget year
     * @author Sam Strugger
     */
    public int selectYear() {
        throw new UnsupportedOperationException("Year selection is not implemented yet.");
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
