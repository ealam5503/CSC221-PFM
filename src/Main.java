import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Entry point for the Personal Finance Manager application.
 * Coordinates authentication and entry into the shared main menu.
 *
 * @author Sam Strugger
 */
public class Main {
    /**
     * Prevents creating instances of the application entry point.
     *
     * @author Sam Strugger
     */
    private Main() {
    }

    /**
     * Starts the application. No command-line arguments are required.
     *
     * @param args command-line arguments; unused
     * @author Sam Strugger
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Accounts accounts;

            try {
                accounts = new Accounts("users.csv");
            } catch (UnsupportedOperationException e) {
                System.err.println("Could not load saved accounts: " + e.getMessage());
                return;
            }

            while (true) {
                System.out.println();
                System.out.println("Welcome to Personal Finance Manager");
                System.out.println("-----------------------------------");
                System.out.println("1. Sign in");
                System.out.println("2. Create an account");
                System.out.println("3. Reset password");
                System.out.println("4. Exit");
                System.out.print("Enter your selection: ");

                if (!scanner.hasNextLine()) {
                    return;
                }

                String input = scanner.nextLine().trim();
                if (!Validation.inputValidate(input, Validation.InputType.MENU_CHOICE, 1, 4)) {
                    System.err.println("Please enter a number between 1 and 4.");
                    continue;
                }

                int selection = Integer.parseInt(input);

                switch (selection) {
                    case 1: {
                        String userName = readInput(scanner, "Username: ", Validation.InputType.USERNAME, "Use 3 to 20 letters, digits, or underscores, starting with a letter.");
                        if (userName == null) {
                            return;
                        }

                        String password = readInput(scanner, "Password: ", Validation.InputType.PASSWORD, "Use at least " + Validation.MIN_PASSWORD_LENGTH + " characters with no spaces.");
                        if (password == null) {
                            return;
                        }

                        if (accounts.signIn(userName, password)) {
                            System.out.println("Signed in as " + accounts.getCurrentUserName() + ".");
                        } else {
                            System.err.println("Incorrect username or password.");
                        }
                        break;
                    }

                    case 2: {
                        String userName = readInput(scanner, "Choose a username: ", Validation.InputType.USERNAME, "Use 3 to 20 letters, digits, or underscores, starting with a letter.");
                        if (userName == null) {
                            return;
                        }

                        if (accounts.userExists(userName)) {
                            System.err.println("That username is already taken.");
                            break;
                        }

                        String password = readInput(scanner, "Choose a password: ", Validation.InputType.PASSWORD, "Use at least " + Validation.MIN_PASSWORD_LENGTH + " characters with no spaces.");
                        if (password == null) {
                            return;
                        }

                        String secretQuestion = readInput(scanner, "Secret question: ", Validation.InputType.TEXT, "Enter 1 to 100 characters without commas or double quotes.");
                        if (secretQuestion == null) {
                            return;
                        }

                        String secretAnswer = readInput(scanner, "Secret answer: ", Validation.InputType.TEXT, "Enter 1 to 100 characters without commas or double quotes.");
                        if (secretAnswer == null) {
                            return;
                        }

                        if (accounts.registerUser(userName, password, secretQuestion, secretAnswer)) {
                            System.out.println("Account created. You can now sign in.");
                        } else {
                            System.err.println("That username is already taken.");
                        }
                        break;
                    }

                    case 3: {
                        String userName = readInput(scanner, "Username: ", Validation.InputType.USERNAME, "Use 3 to 20 letters, digits, or underscores, starting with a letter.");
                        if (userName == null) {
                            return;
                        }

                        String secretQuestion = accounts.getSecretQuestion(userName);
                        if (secretQuestion == null) {
                            System.err.println("No account was found with that username.");
                            break;
                        }

                        System.out.println("Secret question: " + secretQuestion);

                        String secretAnswer = readInput(scanner, "Secret answer: ", Validation.InputType.TEXT, "Enter 1 to 100 characters without commas or double quotes.");
                        if (secretAnswer == null) {
                            return;
                        }

                        String newPassword = readInput(scanner, "New password: ", Validation.InputType.PASSWORD, "Use at least " + Validation.MIN_PASSWORD_LENGTH + " characters with no spaces.");
                        if (newPassword == null) {
                            return;
                        }

                        if (accounts.changePassword(userName, secretAnswer, newPassword)) {
                            System.out.println("Password reset. You can now sign in.");
                        } else {
                            System.err.println("The secret answer was incorrect.");
                        }
                        break;
                    }

                    case 4:
                        System.out.println("Goodbye.");
                        return;
                }

                if (accounts.isSignedIn()) {
                    String dataDirectory = Path.of("data", accounts.getCurrentUserName()).toString();
                    Storage storage = new Storage(dataDirectory);

                    MainMenu menu = new MainMenu(scanner, accounts, storage);

                    while (accounts.isSignedIn()) {
                        menu.handleMenuSelection();
                    }
                }
            }
        } catch (UncheckedIOException e) {
            System.err.println("Could not access the accounts file: " + e.getMessage());
        }
    }

    /**
     * Prompts until the user enters valid text or console input ends.
     * Passwords are kept exactly as entered; other entries are trimmed.
     *
     * @param scanner the shared console scanner
     * @param prompt the prompt to display
     * @param type the kind of input to validate
     * @param errorMessage the message to display for invalid input
     * @return the validated input, or null if console input ends
     * @author Sam Strugger
     */
    private static String readInput(Scanner scanner, String prompt, Validation.InputType type, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return null;
            }

            String input = scanner.nextLine();

            if (Validation.inputValidate(input, type, 0, 0)) {
                if (type == Validation.InputType.PASSWORD) {
                    return input;
                }
                return input.trim();
            }
            System.err.println(errorMessage);
        }
    }
}
