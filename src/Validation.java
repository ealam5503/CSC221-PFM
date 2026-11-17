
import java.time.Year;
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
    
    public static boolean dateValidate(String dateStr) {
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
        if (category == null) {
        return false;
    }
    for (Categories cat : Categories.values()) {
        if (cat.name().equals(category)) {
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
    public static boolean amountValidate(String amount) {
        if(amount == null || amount.isEmpty()){
            return false;
        }
        try {
            Integer.parseInt(amount);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks format of header in CSV file.
     *
     * @param header header of CSV file
     * @param headercount count how many headers in CSV file
     * @return true if format matches, otherwise false
     * @author Amina Zaman
     */
public static boolean headerValidate(String header, int headercount) {
    if (header == null) {
        return false;
    }

    if (headercount != 3) {
        return false;
    }

    return header.equals("Date,Category,Amount");
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
    if (lines == null || lines.isEmpty()) {
        return false;
    }

    if (filename == null || !filename.endsWith(".csv")) {
        return false;
    }

    String header = lines.get(0);

    if (header == null || !headerValidate(header, header.split(",", -1).length)) {
        return false;
    }
    String year = null;
    for (int i = 1; i < lines.size(); i++) {
    String line = lines.get(i);

    if (line == null) {
        return false;
    }

    String[] parts = line.split(",", -1);

    if (parts.length != 3) {
        return false;
    }
        if (!dateValidate(parts[0])) {
    return false;
    }
    String currentYear = parts[0].substring(6);

    if (year == null) {
        year = currentYear;
    } else if (!year.equals(currentYear)) {
        return false;
    }
    if (!categoryValidate(parts[1])) {
        return false;
    }

    if (!amountValidate(parts[2])) {
        return false;
    }
    }
        return true;
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
    * Minimum length of username for account
    * @author Yugin Belbase
    */
    private static final int MIN_USERNAME_LENGTH = 3;

    /**
    * Maximum length of username for account
    * @author Yugin Belbase
    */
    private static final int MAX_USERNAME_LENGTH = 20;

    /**
    * Maximum length of any other text entry (such as a secret question or answer)
    * @author Yugin Belbase
    */
    private static final int MAX_TEXT_LENGTH = 100;

    /**
     * Checks that a user's console entry is valid for the expected type.
     *
     * Rules by type:
     * MENU_CHOICE - a whole number from minOption to maxOption (inclusive).
     * YES_NO      - y, yes, n, or no (any capitalization).
     * YEAR        - exactly four digits, from MIN_YEAR to the current year.
     * USERNAME    - 3 to 20 characters: letters, digits, or underscores,
     *               starting with a letter.
     * PASSWORD    - at least MIN_PASSWORD_LENGTH characters with no spaces
     *               (passwords are checked exactly as typed, not trimmed).
     * TEXT        - 1 to 100 characters with no commas, double quotes,
     *               or line breaks, so it can be stored safely in a file.
     *
     * Leading and trailing spaces are ignored for every type except PASSWORD.
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
        // Step 1: reject missing input or a missing type.
        if (input == null || type == null) {
            return false;
        }

        // Step 2: passwords are checked exactly as typed (no trimming).
        if (type == InputType.PASSWORD) {
            return input.length() >= MIN_PASSWORD_LENGTH
                    && !input.matches("(?s).*\\s.*");
        }

        // Step 3: every other type ignores surrounding spaces; blank is invalid.
        String value = input.trim();
        if (value.isEmpty()) {
            return false;
        }

        // Step 4: apply the rule for the requested type.
        switch (type) {
            case MENU_CHOICE:
                if (minOption > maxOption || !value.matches("-?\\d{1,9}")) {
                    return false;
                }
                int choice = Integer.parseInt(value);
                return choice >= minOption && choice <= maxOption;

            case YES_NO:
                String answer = value.toLowerCase();
                return answer.equals("y") || answer.equals("yes")
                        || answer.equals("n") || answer.equals("no");

            case YEAR:
                if (!value.matches("\\d{4}")) {
                    return false;
                }
                int year = Integer.parseInt(value);
                return year >= MIN_YEAR && year <= Year.now().getValue();

            case USERNAME:
                return value.length() >= MIN_USERNAME_LENGTH
                        && value.length() <= MAX_USERNAME_LENGTH
                        && value.matches("[A-Za-z][A-Za-z0-9_]*");

            case TEXT:
                return value.length() <= MAX_TEXT_LENGTH
                        && !value.contains(",")
                        && !value.contains("\"")
                        && !value.contains("\n")
                        && !value.contains("\r");

            default:
                return false;
        }
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
        // Step 1: nothing wrong means keep loading.
        if (issues == null || issues.isEmpty()) {
            return true;
        }

        // Step 2: show every issue as a numbered list.
        System.out.println();
        System.out.println("Found " + issues.size() + " problem"
                + (issues.size() == 1 ? "" : "s") + " in the file:");
        for (int i = 0; i < issues.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + issues.get(i));
        }
        System.out.println("Invalid lines will be skipped if you continue.");

        // Step 3: without a scanner we can't ask, so cancel safely.
        if (scanner == null) {
            return false;
        }

        // Step 4: ask until the user gives a valid yes/no answer.
        while (true) {
            System.out.print("Ignore these lines and continue loading? (y/n): ");
            if (!scanner.hasNextLine()) {
                System.out.println();
                return false;   // input ended: cancel instead of crashing
            }
            String answer = scanner.nextLine();
            if (inputValidate(answer, InputType.YES_NO)) {
                return answer.trim().toLowerCase().startsWith("y");
            }
            System.out.println("Please enter y or n.");
        }
    }
}

