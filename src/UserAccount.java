/**
 * Holds the saved information for one user account. The password and
 * secret answer are stored only as hashes, never as plain text.
 *
 * @param userName         the user's name
 * @param salt             the random value used when hashing
 * @param passwordHash     the hashed password
 * @param secretQuestion   the user's secret question that's going to be used for some identity verification
 * @param secretAnswerHash the hashed secret answer
 *
 * @author Ousseynou Diop
 */
public record UserAccount(String userName, String salt, String passwordHash, String secretQuestion, String secretAnswerHash) 
{
    /**
     * Turns this account into one line of text for the users file so it can be saved in users.csv file.
     *
     * @return the account as CSV (one comma-separated) line
     * @author Ousseynou Diop
     */
    public String toCsvLine() {
        // implementition part
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Turns one line from the users file back into an account.
     *
     * @param line this is going to be one line from the users file
     * @return the account
     * @author Ousseynou Diop
     */
    public static UserAccount fromCsvLine(String line) {
        // implement part
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
