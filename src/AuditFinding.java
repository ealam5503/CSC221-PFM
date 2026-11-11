/**
 * One transaction flagged by the audit, together with the reason it was flagged.
 * Instances are immutable.
 *
 * @param date     the date of the flagged transaction
 * @param category the category of the flagged transaction (e.g. "Compensation", "Food")
 * @param amount   the whole-dollar amount; income is positive, expenses are negative
 * @param auditType     whether the transaction was flagged as a duplicate or an anomaly
 * @param reason   a human-readable explanation of why it was flagged
 *
 * @author Andrew Zuniga
 */
public record AuditFinding(
        String date,
        String category,
        int amount,
        FindingType auditType,
        String reason) {
}
