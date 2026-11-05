import java.util.Map;

/**
 * Handles user accounts for the PFM: creating an account, signing in,
 * signing out, changing a password, and deleting an account.
 *
 * Passwords and secret answers are never saved as plain text. They are
 * hashed first with hashPassword(). All accounts are saved in the file
 * users.csv so they are still there next time the program runs.
 *
 * @author Ousseynou Diop
 */
public class Accounts {

    /** Name of the file where all user accounts are saved. */
    public static final String USERS_FILE_NAME = "users.csv";

    /** Where the users file is located. */
    private final String usersFilePath;

    /** All saved accounts, looked up by user name. */
    private final Map<String, UserAccount> users;

    /** The user who is signed in right now, or null if nobody is. */
    private UserAccount currentUser;

    /**
     * Creates the Accounts module and loads all saved accounts from the
     * users file. If the file does not exist yet, it starts with no accounts.
     *
     * @param usersFilePath where the users file is located
     * @author Ousseynou Diop
     */
    public Accounts(String usersFilePath) {
        // implementation for loading the accounts from the file.
        this.usersFilePath = usersFilePath;
        this.users = null;
    }

    /**
     * Signs a user in if the user name and password are correct.
     *
     * @param userName      the user name typed by the user
     * @param password      the password typed by the user
     * @return              true if the sign in worked, false if the user name or
     *                      password is wrong
     * @author Ousseynou Diop
     */
    public boolean signIn(String userName, String password) {
        // TODO: Implement signIn.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Signs out the current user.
     *
     * @author Ousseynou Diop
     */
    public void signOut() {
        // TODO: Implement signOut.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Checks if someone is signed in.
     *
     * @return true if a user is signed in, false otherwise
     * @author Ousseynou Diop
     */
    public boolean isSignedIn() {
        // TODO: Implement isSignedIn.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the username of the person who is signed in.
     *
     * @return the current username, or null if nobody is signed in
     * @author Ousseynou Diop
     */
    public String getCurrentUserName() {
        // TODO: Implement getCurrentUserName.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Creates a new account and saves it. The password and secret answer
     * are hashed before saving.
     *
     * @param userName       the new user name
     * @param password       the new password
     * @param secretQuestion the secret question the user picked
     * @param secretAnswer   the answer to the secret question
     * @return true if the account was created, false if the user name is already taken
     * @author Ousseynou Diop
     * @author Wasif Ahmed
     */
    public boolean registerUser(String userName, String password, String secretQuestion, String secretAnswer) {
        // TODO: Implement registerUser.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Checks if a user name is already taken.
     *
     * @param userName      the user name to check
     * @return              true if an account with this user name exists, false otherwise
     * @author Ousseynou Diop
     */
    public boolean userExists(String userName) {
        // TODO: Implement userExists.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Changes the password of the signed in user. The user must type their
     * current password first.
     *
     * @param currentPassword       the user's current password
     * @param newPassword           the new password
     * @return                      true if the password was changed, false if the current
*                                   password is wrong
     * @author Ousseynou Diop
     */
    public boolean changePassword(String currentPassword, String newPassword) {
        // TODO: Implement changePassword.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Changes the password of a user who forgot it. The user must answer
     * their secret question correctly. The user does not need to be signed in.
     *
     * @param userName     the user name of the account
     * @param secretAnswer the answer to the secret question
     * @param newPassword  the new password
     * @return             true if the password was changed, false if the user name or
     *                     answer is wrong
     * @author Ousseynou Diop
     */
    public boolean changePassword(String userName, String secretAnswer, String newPassword) {
        // TODO: Implement changePassword (using secretAnswer).
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the secret question of an account, so it can be shown to a user
     * who forgot their password.
     *
     * @param userName      the user name of the account
     * @return              the secret question, or null if the user name does not exist
     * @author Ousseynou Diop
     */
    public String getSecretQuestion(String userName) {
        // TODO: Implement getSecretQuestion.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Deletes the signed-in user's account and all of their saved budget
     * data, then signs them out. The user must type their password to
     * confirm.
     *
     * @param password      the user's password
     * @return              true if the account was deleted, false if the password is wrong
     * @author Ousseynou Diop
     */
    public boolean deleteUser(String password) {
        // TODO: Implement deleteUser.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Hashes a password or secret answer so it is not saved as plain text.
     * The same text and salt always give the same result.
     *
     * @param plainText     the password or answer to hash
     * @param salt          a random value saved with the user's account
     * @return              the hashed text
     * @author Ousseynou Diop
     * @author Wasif Ahmed
     */
    public static String hashPassword(String plainText, String salt) {
        // TODO: Implement hashPassword.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * This creates a new random salt for a new account. The salt is a random value that's added before hashing  
     * to prevent two people with same password having the same hash based on.
     *
     * @return a random salt
     * @author Ousseynou Diop
     * @author Wasif Ahmed
     */
    private static String generateSalt() {
        // TODO: Implement generateSalt.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Reads all accounts from the users file.
     *
     * @return all the saved accounts, looked up by user name
     * @author Ousseynou Diop
     */
    private Map<String, UserAccount> loadUsers() {
        // TODO: Implement loadUsers.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Saves all accounts to the users file.
     *
     * @author Ousseynou Diop
     */
    private void saveUsers() {
        // TODO: Implement saveUsers.
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
