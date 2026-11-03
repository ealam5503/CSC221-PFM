import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Audits one year of a user's income and expense transactions, finding
 * duplicate and anomalous entries. Class does no console I/O and only returns
 * report text for the shared menu to display.
 *
 * @author Andrew Zuniga
 * @author Minghao Dong
 * @author Bobby Lu
 * @author Genesis Vejar
 */
public class DataAudit {

	/** Categories the audit skips; empty means everything is checked. */
	private final Set<String> excludedCategories = new HashSet<>();

	/**
	 * One transaction from a loaded year. Placeholder input type until the Storage
	 * team's budget object is known.
	 *
	 * @param date     the transaction date
	 * @param category the category name
	 * @param amount   the whole-dollar amount; income positive, expenses negative
	 * @author Andrew Zuniga
	 */
	public record Entry(LocalDate date, String category, int amount) {
	}

	/**
	 * Creates an auditor with no excluded categories. DataAudit holds the state of
	 * exclusions per object instance.
	 *
	 * @author Andrew Zuniga
	 */
	public DataAudit() {
		// Nothing to do: the exclusion set is initialized at its declaration.
		// Constructor included for JavaDoc.
	}

	/**
	 * Runs both checks (duplicates and anomalies) on the given year's entries,
	 * applying the current excluded categories, and returns the result as report
	 * text ready for the shared menu to display.
	 *
	 * @param entries the transactions of the selected year; must not be null
	 * @return the formatted report; a "no issues found" message if nothing was
	 *         flagged, never null
	 * @throws IllegalArgumentException if {@code entries} is null
	 * @author Andrew Zuniga
	 */
	public String runAudit(List<Entry> entries) {
		// TODO: combine findDuplicates and findAnomalies results, formatReport and
		// return formatted string.
		return "";
	}

	/**
	 * Finds transactions that repeat another one, meaning the same date, category
	 * and amount appear more than once. Categories in the current exclusion set are
	 * skipped. Flagged transactions returned as a list of {@link AuditFinding}.
	 *
	 * @param entries the transactions to check
	 * @throws IllegalArgumentException if {@code entries} is null
	 * @return list of findings, one per duplicated entry
	 * @author Andrew Zuniga
	 */
	private List<AuditFinding> findDuplicates(List<Entry> entries) {
		// TODO: group by (date, category, amount) and flag groups larger than one
		return List.of();
	}

	/**
	 * Finds transactions whose amount is far from what is typical for their
	 * category. Categories in the current exclusion set are skipped. Income and
	 * expense entries are compared only against their own category. Flagged
	 * transactions returned as a list of {@link AuditFinding}.
	 *
	 * @param entries the transactions to check
	 * @throws IllegalArgumentException if {@code entries} is null
	 * @return list of findings, one per anomalous entry
	 * @author Andrew Zuniga
	 */
	private List<AuditFinding> findAnomalies(List<Entry> entries) {
		// TODO: compute a typical monthly amount per category and flag outliers
		return List.of();
	}

	/**
	 * Replaces the set of categories the audit will skip.
	 *
	 * @param categories the categories to exclude; an empty set means none
	 * @throws IllegalArgumentException if {@code categories} is null
	 * @author Andrew Zuniga
	 */
	public void setExcludedCategories(Set<String> categories) {
		// TODO: store a defensive copy
	}

	/**
	 * Returns the categories currently excluded from the audit.
	 *
	 * @return an unmodifiable set of excluded categories, never null
	 * @author Andrew Zuniga
	 */
	public Set<String> getExcludedCategories() {
		// TODO: return an unmodifiable view
		return Set.of();
	}

	/**
	 * Removes all excluded categories so the next audit checks everything.
	 *
	 * @author Andrew Zuniga
	 */
	public void clearExcludedCategories() {
		// TODO: empty the exclusion set
	}

	/**
	 * Turns findings into report text for the shared menu to display. Findings
	 * should be grouped sensibly (for example income and expense), and an empty
	 * list should produce a clear "no issues found" message.
	 *
	 * @param findings the combined results of {@link #findDuplicates(List)} and
	 *                 {@link #findAnomalies(List)}; must not be null
	 * @throws IllegalArgumentException if {@code findings} is null
	 * @return the formatted report, never null
	 * @author Andrew Zuniga
	 */
	private String formatReport(List<AuditFinding> findings) {
		// TODO: build the report string
		return "";
	}
}
