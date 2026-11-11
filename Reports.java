/**
 * The Reports class handles generating annual financial summaries and 
 * printing requested reports to the console terminal or to a file.
 * 
 * @author Mohammad Forazi
 * @author Shuvo Barmon
 * @author Marlin Canelo
 * @author Colin Russell
 */
public class Reports {

    /**
     * Constructs a new Reports instance.
     * 
     * @author Mohammad Forazi
     * @author Shuvo Barmon
     * @author Marlin Canelo
     * @author Colin Russell
     */
    public Reports() {
        // Skeleton constructor
    }

    /**
     * Generates the annual report summarizing monthly income minus expenses, 
     * category totals, and overall budget performance (surplus or deficit).
     * 
     * @author Mohammad Forazi
     */
    public void generateAnnualReport() {
        // TODO: Implement report generation logic
    }

    /**
     * Exports the annual financial report to a CSV file for spreadsheet manipulation.
     * 
     * @author Colin Russell
     */
    public void exportReportToCSV() {
        // TODO: Implement file export logic
    }

    /**
     * Calculates expenditure totals aggregated by financial category.
     * 
     * @author Marlin Canelo
     */
    ------------------------------------------------------------------
    (//Shuvo's code below)
public void calculateCategoryTotals() {

    // Reset income totals
    for (int i = 0; i < incomeTotals.length; i++) {
        incomeTotals[i] = 0;
    }

    // Reset expense totals
    for (int i = 0; i < expenseTotals.length; i++) {
        expenseTotals[i] = 0;
    }

    // Go through all user transactions
    for (int i = 0; i < transactions.size(); i++) {

        Transaction t = transactions.get(i);

        String category = t.getCategory();
        double amount = t.getAmount();

        // Check income
        if (amount > 0) {

            for (int j = 0; j < incomeCategories.length; j++) {

                if (category.equals(incomeCategories[j])) {
                    incomeTotals[j] = incomeTotals[j] + amount;
                    break;
                }
            }

        // Check expenses
        } else if (amount < 0) {

            for (int j = 0; j < expenseCategories.length; j++) {

                if (category.equals(expenseCategories[j])) {
                    expenseTotals[j] = expenseTotals[j] - amount;
                    break;
                }
            }
        }
    }
}



public void printConsoleReport() {

    String[] months = {
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    };

    double[] monthlyIncome = new double[12];
    double[] monthlyExpenses = new double[12];

    // Calculate monthly income and expenses
    for (int i = 0; i < transactions.size(); i++) {

        Transaction t = transactions.get(i);

        int month = t.getMonth();
        double amount = t.getAmount();

        int index = month - 1;

        if (index < 0 || index >= 12) {
            continue;
        }

        if (amount > 0) {
            monthlyIncome[index] = monthlyIncome[index] + amount;
        } else if (amount < 0) {
            monthlyExpenses[index] = monthlyExpenses[index] - amount;
        }
    }

    // Calculate category totals
    calculateCategoryTotals();

    double totalIncome = 0;
    double totalExpenses = 0;

    System.out.println("ANNUAL FINANCIAL REPORT");
    System.out.println("-------------------------");

    // Print each month's report
    for (int i = 0; i < 12; i++) {

        double balance = monthlyIncome[i] - monthlyExpenses[i];

        System.out.println("Month: " + months[i]);
        System.out.println("Income: " + monthlyIncome[i]);
        System.out.println("Expenses: " + monthlyExpenses[i]);
        System.out.println("Balance: " + balance);
        System.out.println("-------------------------");

        totalIncome = totalIncome + monthlyIncome[i];
        totalExpenses = totalExpenses + monthlyExpenses[i];
    }

    // Print income categories
    System.out.println("INCOME BY CATEGORY");

    for (int i = 0; i < incomeCategories.length; i++) {
        System.out.println(
            incomeCategories[i] + ": " + incomeTotals[i]
        );
    }

    // Print expense categories
    System.out.println("EXPENSES BY CATEGORY");

    for (int i = 0; i < expenseCategories.length; i++) {
        System.out.println(
            expenseCategories[i] + ": " + expenseTotals[i]
        );
    }

    // Calculate yearly balance
    double yearlyBalance = totalIncome - totalExpenses;

    System.out.println("YEARLY SUMMARY");
    System.out.println("Total Income: " + totalIncome);
    System.out.println("Total Expenses: " + totalExpenses);
    System.out.println("Yearly Balance: " + yearlyBalance);

    if (yearlyBalance > 0) {
        System.out.println("Status: Surplus");
    } else if (yearlyBalance < 0) {
        System.out.println("Status: Deficit");
    } else {
        System.out.println("Status: Balanced");
    }
}


