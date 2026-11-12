import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
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
	 * Creates an auditor with no excluded categories. DataAudit holds the state of
	 * exclusions per object instance.
	 *
	 * @author Andrew Zuniga
	 * @author Minghao Dong
	 * @author Bobby Lu
	 * @author Genesis Vejar
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
	public String runAudit(List<Transaction> entries) {
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
	 * @author Genesis Vejar
	 */
	private List<AuditFinding> findDuplicates(List<Transaction> entries) {

		if (entries == null) { // If a null list is passed, throw an exception.
			throw new IllegalArgumentException("Entries can't be null");

		}
		
		//Holds an AuditFinding for each duplicate.
		List<AuditFinding> resultsAuditFindings = new ArrayList<>();

		
		for (int i = 0; i < entries.size(); i++) {

			Transaction currentTransaction = entries.get(i);
			
			//Convert Category enum to a String, as excludedCategories and AuditFinding
			//use Strings.
			String categoryString = currentTransaction.category().name();
			
			//If a category is excluded from the audit, skip it.
			if (excludedCategories.contains(categoryString)) {
				continue;
			}

			for (int j = 0; j < i; j++) {
				
				//Check if two transactions are equal (same date, category and amount).
				if (entries.get(j).equals(currentTransaction)) {
					
					//Build a finding that describes this duplicate.
					AuditFinding findings = new AuditFinding(
							currentTransaction.date(), 
							categoryString,
							currentTransaction.amount(), 
							FindingType.DUPLICATE,
							"Same date, category and amount as a previous entry");
					
					//Add the finding to the results list
					//Stop looking back, so the transaction is flagged once.
					resultsAuditFindings.add(findings);
					break;
				}

			}
		}
		
		//Return every duplicate found.
		return resultsAuditFindings;
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
	 * @author Bobby Lu
	 */
	private List<AuditFinding> findAnomalies(List<Transaction> entries) {
		// TODO: compute a typical monthly amount per category and flag outliers
		return List.of();
	}

	/**
	 * Replaces the set of categories the audit will skip.
	 *
	 * @param categories the categories to exclude; an empty set means none
	 * @throws IllegalArgumentException if {@code categories} is null
	 * @author Minghao Dong
	 */
	public void setExcludedCategories(Set<String> categories) {
		if (categories == null) {
		    throw new IllegalArgumentException("categories cannot be null");
		}
		
		Set<String> copy = new HashSet<>(categories);
		
		excludedCategories.clear();

	    excludedCategories.addAll(copy);
	}

	/**
	 * Returns the categories currently excluded from the audit.
	 *
	 * @return an unmodifiable set of excluded categories, never null
	 * @author Minghao Dong
	 */
	public Set<String> getExcludedCategories() {
		return Set.copyOf(excludedCategories);
	}

	/**
	 * Removes all excluded categories so the next audit checks everything.
	 *
	 * @author Minghao Dong
	 */
	public void clearExcludedCategories() {
		excludedCategories.clear();
	}

	/**
	 * Turns findings into a formatted report text ready for the shared menu to
	 * display. Findings should be grouped sensibly (for example income and
	 * expense), and an empty list should produce a clear "no issues found" message.
	 *
	 * @param findings the combined results of {@link #findDuplicates(List)} and
	 *                 {@link #findAnomalies(List)}; must not be null
	 * @throws IllegalArgumentException if {@code findings} is null
	 * @return the formatted report, never null
	 * @author Andrew Zuniga
	 */
	private String formatReport(List<AuditFinding> findings) {
		StringBuilder report = new StringBuilder();
		report.append(
				String.format("%-16s | %-21s | %-18s | %-9s | %s%n", "Date", "Category", "Amount", "Type", "Reason")); // Header
																														// for
																														// report

		report.append("-".repeat(120)).append(System.lineSeparator()); // Divider for report.
		List<AuditFinding> sorted = new ArrayList<>(findings); // Sort all findings.
		sorted.sort(Comparator.comparing(AuditFinding::category) // First by category.
				.thenComparing(AuditFinding::auditType) // Second by the audit finding type.
				.thenComparing(AuditFinding::date)); // Third by the date.
		for (AuditFinding entry : sorted) {
			report.append(String.format("%-16s | %-21s | $ %16d | %9s | %s%n", entry.date(), entry.category(),
					entry.amount(), entry.auditType(), entry.reason()));
		}
		return report.toString();
	}
}
