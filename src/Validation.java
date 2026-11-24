import java.time.Year;
import java.util.List;

/**
 * Validates CSV data and user input.
 *
 * @author Amina Zaman
 * @author Eliott Edeki
 * @author Yugin Belbase
 */
public class Validation {


    //Private constructor to avoid javadoc generation
    private Validation(){

    }

    /**
     * Verifies date strings in the CSV file are from the same
     * year and correctly formatted.
     *
     * @param date date read from CSV file
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
    * Categories allowed in CSV file.
    * @author Eliott Edeki
    */
    public enum Categories{
        /** Income: net pay after taxes and other deductions */
        COMPENSATION,
        /** Income: contributions from others, including financial aid */
        ALLOWANCE,
        /** Income: unearned income after taxes */
        INVESTMENTS,
        /** Income: any other income */
        INCOME_OTHER,
        /** Expense: rent, mortgage, repairs, furniture, housewares, insurance, cleaning */
        HOME,
        /** Expense: gas, electric, phone, ISP for home */
        UTILITIES,
        /** Expense: groceries, non-entertainment meals */
        FOOD,
        /** Expense: clothing, grooming */
        APPEARANCE,
        /** Expense: unreimbursed business expenses, uniforms, work-related services */
        WORK,
        /** Expense: tuition, tutors, supplies */
        EDUCATION,
        /** Expense: taxis, transit fares, personal vehicle (insurance, lease, fuel, repairs) */
        TRANSPORTATION,
        /** Expense: subscriptions, dining out, admissions, gifts, vacations */
        ENTERTAINMENT,
        /** Expense: unreimbursed medical, lawyers, consultants */
        PROFESSIONAL_SERVICES,
        /** Expense: all other expenses */
        EXPENSE_OTHER
    }
    
    /**
     * Checks whether the category stored in the CSV file
     * can be found in the Categories enum.
     *
     * @param category category entered in CSV file
     * @return true if category is in Categories, false otherwise
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
     * Checks whether CSV amounts column entry is 
     * an integer.
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
     * @param headercount number of header columns in CSV file
     * @return true if format matches expected value, otherwise false
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
     * Verifies that the CSV file has the correct format
     * and contains valid data.
     *
     * @param lines row entries from the CSV file
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
     * Kinds of console input checked by inputValidate().
     *
     * @author Yugin Belbase
     */
    public enum InputType {
        /** User is navigating the menu*/
        MENU_CHOICE, 
        /** User is answering a yes/no question (y, yes, n, or no)*/
        YES_NO, 
        /** User is selecting the year of the desired CSV file (four digits, from MIN_YEAR to the current year)*/
        YEAR, 
        /** User is inputting their username*/
        USERNAME, 
        /** User is inputting their password*/
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
     * @param input the text entered by the user
     * @param type the member of InputType expected for the entry
     * @param minOption lowest valid menu number (MENU_CHOICE only; pass 0 for other types)
     * @param maxOption highest valid menu number (MENU_CHOICE only; pass 0 for other types)
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
     * Prints any CSV issues found and reports whether the file
     * has data that is formatted incorrectly.
     *
     * @param issues list of issues, with one entry per invalid CSV row
     * @return true if there is at least one issue, false if there are none
     * @author Yugin Belbase
     */
    public static boolean reportIssues(List<String> issues) {
        // Step 1: no issues means nothing is formatted incorrectly.
        if (issues == null || issues.isEmpty()) {
            return false;
        }

        // Step 2: show every issue as a numbered list.
        System.out.println();
        System.out.println("Found " + issues.size() + " problem"
                + (issues.size() == 1 ? "" : "s") + " in the file:");
        for (int i = 0; i < issues.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + issues.get(i));
        }

        // Step 3: at least one issue means something is formatted incorrectly.
        return true;
    }
}

