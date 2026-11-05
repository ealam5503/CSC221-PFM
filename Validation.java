
import java.util.List;
import java.util.Scanner;

/**
 * Validates CSV data and user input.
 *
 * @author Amina Zaman
 * @author Eliott Edeki
 * @author Yugin Belbase
 */
public class Validation {

    /**
     * Check that the date in CSV file has correct format
     * and all dates are from the same year.
     *
     * @param date date entered in CSV file
     * @return true if date is valid, false if format of date
     *         is incorrect or dates have different years
     * @author Eliott Edeki
     */
    public static boolean dateValidate(int date) {
        return false;
    }

    /**
     * Check that category in CSV file is from the specified terms.
     *
     * @param category category entered in CSV file
     * @return true if category is from approved terms, false otherwise
     * @author Eliott Edeki
     */
    public static boolean categoryValidate(String category) {
        return false;
    }

    /**
     * Check amount is an integer.
     *
     * @param amount amount entered in CSV file
     * @return true if integer, otherwise false
     * @author Eliott Edeki
     */
    public static boolean amountValidate(int amount) {
        return false;
    }

    /**
     * Checks format of header in CSV file.
     *
     * @param header header of CSV file
     * @param headercount count how many headers in CSV file
     * @return true if format matches, otherwise false
     * @author Eliott Edeki
     */
    public static boolean headerValidate(String header, int headercount) {
        return false;
    }

    /**
     * Checks that the CSV file has the correct format
     * and contains valid data.
     *
     * @param lines lines from the CSV file
     * @param filename name of the CSV file
     * @return true if file is valid, false otherwise
     * @author Amina Zaman
     */
    public static boolean fileValidate(List<String> lines, String filename) {
        return false;
    }

    /**
     * Kinds of console entries checked by inputValidate().
     *
     * @author Yugin Belbase
     */
    public enum InputType {
        MENU_CHOICE, YES_NO, YEAR, USERNAME, PASSWORD, TEXT
    }

    public static final int MIN_YEAR = 1900;
    public static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Checks that a user's console entry is valid for the expected type.
     *
     * @param input the text the user typed
     * @param type the kind of entry expected
     * @param minOption lowest valid menu number (MENU_CHOICE only)
     * @param maxOption highest valid menu number (MENU_CHOICE only)
     * @return true if the entry is valid, false otherwise
     * @author Yugin Belbase
     */
    public static boolean inputValidate(String input, InputType type,
                                        int minOption, int maxOption) {
        return false;
    }

    /**
     * Checks that a user's console entry is valid for any
     * type except MENU_CHOICE.
     *
     * @param input the text the user typed
     * @param type the kind of entry expected
     * @return true if the entry is valid, false otherwise
     * @author Yugin Belbase
     */
    public static boolean inputValidate(String input, InputType type) {
        return inputValidate(input, type, 0, 0);
    }

    /**
     * Lists any CSV issues found and asks the user to ignore
     * them or cancel.
     *
     * @param issues one message per invalid line
     * @param scanner the program's shared console scanner (not closed)
     * @return true to continue loading, false to cancel
     * @author Yugin Belbase
     */
    public static boolean reportIssues(List<String> issues, Scanner scanner) {
        return false;
    }
}

