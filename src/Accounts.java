import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * takes care of user accounts for the PFM: creating an account, signing in,
 * signing out, changing a password, and deleting an account.
 *
 * Passwords and secret answers are never saved as plain text. They are
 * hashed first with hashPassword(). All accounts are saved in the file
 * users.csv so they are still there next time the program runs.
 *
 * @author Ousseynou Diop
 * @author Michael Lee
 * @author Wasif Ahmed
 */
public class Accounts 
{

    /** Name of the file where all user accounts are saved. */
    public static final String USERS_FILE_NAME = "users.csv";

    /** Where the users file is located. */
    private final String usersFilePath;

    /** All saved accounts, looked up by user name. */
    private final Map<String, UserAccount> users;

    /** The user who is signed in right now, or null if nobody is. */
    private UserAccount currentUser;

    /**
     * Creates the Accounts module with an empty account collection and 
     *
     * @param usersFilePath where the users file is located
     * @author Ousseynou Diop
     * @author Michael Lee
     * @author Wasif Ahmed
     */
    public Accounts(String usersFilePath) 
    {
        this.usersFilePath = usersFilePath;
        this.users = loadUsers(); // Fill hashmap with loaded values from a csv
    }

    /**
     * Signs a user in if the user name and password are correct.
     *
     * @param userName      the user name typed by the user
     * @param password      the password typed by the user
     * @return              true if the sign in worked, false if the user name or
     *                      password is wrong
     * @author Ousseynou Diop
     * @author Michael Lee
     * @author Wasif Ahmed
     */
    public boolean signIn(String userName, String password) {
        if (userName == null || password == null) return false;

        UserAccount account = users.get(userName);

        if (account == null || !verifyPassword(account, password)) return false;

        currentUser = account;
        return true;
    }

    /**
     * Signs out the current user.
     *
     * @author Ousseynou Diop
     * @author Michael Lee
     */
    public void signOut() {
        currentUser = null;
    }

    /**
     * Checks if someone is signed in.
     *
     * @return true if a user is signed in, false otherwise
     * @author Ousseynou Diop
     * @author Michael Lee
     */
    public boolean isSignedIn() {
        return currentUser != null;
    }

    /**
     * Gets the username of the person who is signed in.
     *
     * @return the current username, or null if nobody is signed in
     * @author Ousseynou Diop
     * @author Michael Lee
     */
    public String getCurrentUserName() {
        if (currentUser == null) {
            return null;
        }

        return currentUser.userName();
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
     * @author Michael Lee
     */
    public boolean registerUser(String userName, String password, String secretQuestion, String secretAnswer) 
    {
        if (userExists(userName)) return false;

        String salt = generateSalt(); 
        String passwordHash = hashPassword(password, salt);
        String secretHash = hashPassword(secretAnswer.trim().toLowerCase(), salt);

        UserAccount account = new UserAccount(userName, salt, passwordHash, secretQuestion, secretHash);
        users.put(userName, account);

        saveUsers();
        return true;
    }

    /**
     * Checks if a user name is already taken.
     *
     * @param userName      the user name to check
     * @return              true if an account with this user name exists, false otherwise
     * @author Ousseynou Diop
     * @author Michael Lee
     */
    public boolean userExists(String userName) {
        return users.containsKey(userName);
    }

    /**
     * Changes the password of the signed in user. The user must type their
     * current password first.
     *
     * @param currentPassword       the user's current password
     * @param newPassword           the new password
     * @return                      true if the password was changed, false if the current
     *                              password is wrong or the new password is the same as the old one
     * @author Ousseynou Diop
     * @author Michael Lee
     * @author Wasif Ahmed
     * @bug 40 New password could be identical to the old password.
     */
    public boolean changePassword(String currentPassword, String newPassword) 
    {
        if (currentUser == null || !verifyPassword(currentUser, currentPassword)) return false;
        if (verifyPassword(currentUser, newPassword)) return false; // Bug #40: new password must be different

        String newHash = hashPassword(newPassword, currentUser.salt());
        UserAccount updatedAccount = new UserAccount(currentUser.userName(), currentUser.salt(), newHash, currentUser.secretQuestion(), currentUser.secretAnswerHash());

        users.put(updatedAccount.userName(), updatedAccount);
        currentUser = updatedAccount;

        saveUsers();
        return true;
    }


    /**
     * Changes the password of a user who forgot it. The user must answer
     * their secret question correctly. The user does not need to be signed in.
     *
     * @param userName     the user name of the account
     * @param secretAnswer the answer to the secret question
     * @param newPassword  the new password
     * @return             true if the password was changed, false if the user name or
     *                     answer is wrong, or the new password is the same as the old one
     * @author Ousseynou Diop
     * @author Michael Lee
     * @author Wasif Ahmed
     * @bug 40 New password during reset could be identical to the old password.
     */
    public boolean changePassword(String userName, String secretAnswer, String newPassword) 
    {
        UserAccount unsignedAccount = users.get(userName);
        if (unsignedAccount == null) return false;
        if (currentUser != null && !currentUser.userName().equals(userName)) return false;

        if (!verifySecretAnswer(unsignedAccount, secretAnswer)) return false;
        if (verifyPassword(unsignedAccount, newPassword)) return false; // Bug #40: new password must be different

        String newHash = hashPassword(newPassword, unsignedAccount.salt());
        UserAccount updatedAccount = new UserAccount(unsignedAccount.userName(), unsignedAccount.salt(), newHash, unsignedAccount.secretQuestion(), unsignedAccount.secretAnswerHash());

        users.put(updatedAccount.userName(), updatedAccount);

        if (currentUser != null) currentUser = updatedAccount;

        saveUsers();
        return true;    
    }

    /**
     * Gets the secret question of an account, so it can be shown to a user
     * who forgot their password.
     *
     * @param userName      the user name of the account
     * @return              the secret question, or null if the user name does not exist
     * @author Ousseynou Diop
     * @author Michael Lee
     * @author Wasif Ahmed
     */
    public String getSecretQuestion(String userName) 
    {
        UserAccount account = users.get(userName);
        if (account == null) return null;

        return account.secretQuestion();
    }

    /**
     * Deletes the signed-in user's account and all of their saved budget
     * data, then signs them out. The user must type their password to
     * confirm.
     *
     * @param password      the user's password
     * @return              true if the account was deleted, false if the password is wrong
     * @author Ousseynou Diop
     * @author Wasif Ahmed
     */
    public boolean deleteUser(String password) 
    {
        if (currentUser == null || !verifyPassword(currentUser, password)) return false;

        users.remove(currentUser.userName());
        saveUsers();
        signOut();

        return true;
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
    public static String hashPassword(String plainText, String salt) 
    {
        try 
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String combined = plainText + salt;
            byte[] hashBytes = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
        
            return Base64.getEncoder().encodeToString(hashBytes);
        } 
        catch (NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("Error: SHA-256 may not be available");
        }
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
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt); //fills the salt array with random bytes due to two accounts potentially having same passwords

        return Base64.getEncoder().encodeToString(salt); //returns the bytes converted into string
    }

    /**
     * Reads all accounts from the users file.
     *
     * @return all the saved accounts, looked up by user name
     * @author Ousseynou Diop
     * @author Wasif Ahmed
     */
    private Map<String, UserAccount> loadUsers() 
    {
        Map<String, UserAccount> loaded = new HashMap<>();
        Path path = Paths.get(usersFilePath);

        if (!Files.exists(path)) return loaded;

        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.isBlank()) continue;
                UserAccount account = UserAccount.fromCsvLine(line);
                loaded.put(account.userName(), account);
            }

        } 
        catch (IOException e) 
        {
            throw new UncheckedIOException("Error: Could not read users file", e);
        }

        return loaded;
    }

    /**
     * Saves all accounts to the users file.
     *
     * @author Ousseynou Diop
     * @author Wasif Ahmed
     */
    private void saveUsers() 
    {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(usersFilePath)))
        {
            for (UserAccount account : users.values())
            {
                writer.write(account.toCsvLine());
                writer.newLine();
            }

        } 
        catch (IOException e) 
        {
            throw new UncheckedIOException("Error: Could not save users file", e);
        }
    }

    /**
     * Verifies if the inputted password is correct.
     * 
     * @param account   the account to check the input against.
     * @param password  the password to check.
     * @return          true if the input matches the password stored in the Account and false otherwise.
     * @author Wasif Ahmed
    */
    private static boolean verifyPassword(UserAccount account, String password)
    {
        return hashPassword(password, account.salt()).equals(account.passwordHash());
    }

    /**
     * Verifies if the inputted secret answer is correct.
     * 
     * @param account   the account to check the input against.
     * @param answer    the secret answer to check.
     * @return          true if the input matches the secret answer stored in the Acccount and false otherwise.
     * @author Wasif Ahmed
     */
    private static boolean verifySecretAnswer(UserAccount account, String answer)
    {
         return hashPassword(answer.trim().toLowerCase(), account.salt()).equals(account.secretAnswerHash());
    }

}
