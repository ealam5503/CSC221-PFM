import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
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

		if (entries == null)
			throw new IllegalArgumentException("Entries cannot be null");

		List<AuditFinding> anomalies = new ArrayList<>();

		Map<String, Map<String, Long>> monthlyTotals = new HashMap<>();
		for (Transaction transaction : entries) {
			String category = transaction.category().name();
			// Prevents excluded categories from contributing to monthly total
			if (excludedCategories.contains(category))
				continue;
			String month = transaction.date().substring(0, 2);
			boolean expense = transaction.amount() < 0;
			String groupKey = category + (expense ? "_EXPENSE" : "_INCOME");

			// If the inner hashMap doesn't have every category key then it creates one;
			if (!monthlyTotals.containsKey(groupKey)) {
				monthlyTotals.put(groupKey, new HashMap<>());
			} // end of category key check

			Map<String, Long> categoryTotals = monthlyTotals.get(groupKey);
			// check for expense and transform it into absolute values

			long amount = Math.abs((long) transaction.amount());
			categoryTotals.put(month, categoryTotals.getOrDefault(month, 0L) + amount);
		}
		// Looping through each keySet in monthly totals
		for (String groupKey : monthlyTotals.keySet()) {
			Map<String, Long> categoryTotals = monthlyTotals.get(groupKey);
			// Creates list of values without their keys
			List<Long> amounts = new ArrayList<>(categoryTotals.values());
			Collections.sort(amounts);
			// If there are less than 3 entry's for each month then the month is skipped for
			// anomaly checking
			if (amounts.size() < 3)
				continue;
			// median calculations
			double median;
			if (amounts.size() % 2 != 0) {
				median = amounts.get(amounts.size() / 2);
			} else {
				long leftMiddle = amounts.get(amounts.size() / 2 - 1);
				long rightMiddle = amounts.get(amounts.size() / 2);
				median = rightMiddle / 2.0 + leftMiddle / 2.0;
			}
			if (median == 0)
				continue;
			// 2.5 is a arbitrary number used for proof on concept
			double highThreshold = median * 2.5;
			double lowThreshold = median / 2.5;
			
			for (String month : categoryTotals.keySet()) {
				long monthlyAmount = categoryTotals.get(month);
				if (monthlyAmount >= highThreshold || monthlyAmount <= lowThreshold) {
					for (Transaction transaction : entries) {
						if (month.equals(transaction.date().substring(0, 2))) {
							boolean expense = transaction.amount() < 0;
							String transactionGroupKey = transaction.category().name() + (expense ? "_EXPENSE" : "_INCOME");
							if (groupKey.equals(transactionGroupKey)) {
								String reason = "Monthly total: $" + monthlyAmount + ", Median: $" + median;
								// Record each transaction from the anomalous month.
								AuditFinding findings = new AuditFinding(
										transaction.date(),
										transaction.category().name(),
										transaction.amount(),
										FindingType.ANOMALY,
										reason
										);
								anomalies.add(findings);
							}
						}

					}
				}
			}
		} // end of keySet loop

		return anomalies;

	
	}

	/**
	 * Replaces the set of categories the audit will skip.
	 *
	 * @param categories the categories to exclude; an empty set means none
	 * @throws IllegalArgumentException if {@code categories} is null
	 * @author Minghao Dong
	 */
	public void setExcludedCategories(Set<String> categories) {
		// TODO: store a defensive copy
	}

	/**
	 * Returns the categories currently excluded from the audit.
	 *
	 * @return an unmodifiable set of excluded categories, never null
	 * @author Minghao Dong
	 */
	public Set<String> getExcludedCategories() {
		// TODO: return an unmodifiable view
		return Set.of();
	}

	/**
	 * Removes all excluded categories so the next audit checks everything.
	 *
	 * @author Minghao Dong
	 */
	public void clearExcludedCategories() {
		// TODO: empty the exclusion set
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
