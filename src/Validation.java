
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
    * Empty constructor for Validation class for instantiation
    * @author Eliott Edeki
    */

    private Validation(){

    }

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
        if (dateStr == null) {
            return false;
        }

        // 1. Enforce strict MM/DD/YYYY format using regex
        String regex = "^(0[1-9]|1[0-2])/([0-2][0-9]|3[0-1])/\\d{4}$";
        if (!dateStr.matches(regex)) {
            return false;
        }

        // 2. Split into components
        String[] parts = dateStr.split("/");
        if (parts.length != 3) {
            return false;
        }

        int month, day, year;
        try {
            month = Integer.parseInt(parts[0]);
            day = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        // 3. Determine maximum days for the given month and year
        int maxDays;
        switch (month) {
            case 2:
                // Leap year check: divisible by 4, but not 100 unless also divisible by 400
                boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                maxDays = isLeapYear ? 29 : 28;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                maxDays = 30;
                break;
            default:
                maxDays = 31;
                break;
        }

        // 4. Validate that the day fits within the month's maximum days
        return day <= maxDays;
        return false;
    }

    /**
    * Categories allowed in CSV file
    * @author Eliott Edeki
    */
    public enum Categories{
        /** Income: Net pay after deductions*/
        COMPENSATION, 
        /** Income: Contributions from others*/
        ALLOWANCE, 
        /** Income: Unearned income after taxes*/
        INVESTMENTS,
        /** Income: Any other income*/
        INCOME_OTHER,
        /** Expense: Expenses related to housing*/
        HOME, 
        /** Expense: Energy and internet for home*/
        UTILITIES, 
        /** Expense: Non-entertainment spending on food*/
        FOOD,
        /** Expense: Clothing, cosmetics*/
        APPEARANCE, 
        /** Expense: Unreimbursed work-related purchases*/
        WORK, 
        /** Expense: School tuition, tutors or supplies*/
        EDUCATION, 
        /** Expense: Costs of transit fares or personal vehicle use*/
        TRANSPORTATION,
        /** Expense: Non-essential purchases for entertainment such as vacations or gifts*/
        ENTERTAINMENT, 
        /** Expense: Fees from professionals such as lawyers or doctors*/
        PROFESSIONAL_SERVICES,
        /** Expense: Other expenses not previously mentioned*/
        EXPENSE_OTHER}
    
    /**
     * Check that category in CSV file is from the specified terms.
     *
     * @param category category entered in CSV file
     * @return true if category is from approved terms, false otherwise
     * @author Eliott Edeki
     */
   
    public static boolean categoryValidate(String category) {
        if (value == null) {
            return false;
        }
        for (Categories category : Categories.values()) {
            if (category.name().equals(value)) {
                return true;
            }
        }
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
        /** User is navigating the menu*/
        MENU_CHOICE, 
        /** User is choosing if they want to display CSV file with data in incorrect format*/
        YES_NO, 
        /** Year of date in CSV file*/
        YEAR, 
        /** User is inputing their username*/
        USERNAME, 
        /** User is inputing their password*/
        PASSWORD, 
        /** All other inputs*/
        TEXT
    }

    
    /**
    * Minimum year allowed in date entry in CSV file
    * @author Yugin Belbase
    */
    public static final int MIN_YEAR = 1900;

    /**
    * Minimum length of password for account
    * @author Yugin Belbase
    */
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

