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
     * @author Wasif Ahmed
     */
    public String toCsvLine() 
    {
        return String.join(",", toCorrectCsvField(userName), toCorrectCsvField(salt), toCorrectCsvField(passwordHash), toCorrectCsvField(secretQuestion), toCorrectCsvField(secretAnswerHash));
    }

    /**
     * Turns one line from the users file back into an account.
     *
     * @param line the line from the users file
     * @return the account
     * @throws IllegalArgumentException if the line is null, has an unclosed quote, or does not contain the correct amount of fields
     * @author Ousseynou Diop
     * @author Wasif Ahmed
     */
    public static UserAccount fromCsvLine(String line) 
    {
        if (line == null) throw new IllegalArgumentException("Error: Line Cannot be null");

        // Use a Regex to split on commas that are followed by an even number of quotes.
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (parts.length != 5) throw new IllegalArgumentException("Error: Expected 5 fields but found " + parts.length);

        for (int i = 0; i < parts.length; i++)
        {
            //Determine if the field has outer quotes, then remove them. 
            boolean hasOuterQuotes = parts[i].length() >= 2 && parts[i].startsWith("\"") && parts[i].endsWith("\"");            
            
            if (hasOuterQuotes) 
            {
                parts[i] = parts[i].substring(1, parts[i].length() - 1).replace("\"\"", "\"");
            }
        }

        return new UserAccount(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    /**
     * Prepares a field so it can safely be placed in a CSV line. If the value
     * contains a comma, double quote, or line break, it is wrapped in double quotes
     * and any double quotes inside it are doubled. Otherwise the value is returned unchanged.
     *
     * @param field the input field
     * @return the "safe" version of the field with the proper amount of quotes, or an empty string if the field is null
     * @author Wasif Ahmed
     */
    private static String toCorrectCsvField(String field)
    {
        if (field == null) return "";

        // Check if the field contains any commas, quotes, or linebreaks
        boolean needsQuotes = field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r");

        if (needsQuotes) return "\"" + field.replace("\"", "\"\"") + "\"";

        return field;
    }
}
