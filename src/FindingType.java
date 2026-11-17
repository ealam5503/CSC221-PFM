/**
 * The kind of problem the audit found with a transaction.
 *
 * @author Andrew Zuniga
 */
public enum FindingType {

    /** The transaction repeats another one (same date, category and amount). */
    DUPLICATE,

    /** The transaction's amount is far from what is typical for its category. */
    ANOMALY
}
