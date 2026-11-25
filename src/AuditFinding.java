/**
 * One transaction flagged by the audit, together with the reason it was flagged.
 * Instances are immutable.
 *
 * @param date     the date of the flagged transaction
 * @param category the Validation.Categories enum member of the flagged transaction (e.g. COMPENSATION, FOOD)
 * @param amount   the whole-dollar amount; income is positive, expenses are negative
 * @param auditType     the FindingType enum member assigned to the transaction (DUPLICATE or ANOMALY)
 * @param reason   a human-readable explanation of why it was flagged
 *
 * @author Andrew Zuniga
 */
public record AuditFinding(
        String date,
        Category category,
        int amount,
        FindingType auditType,
        String reason) {
}
