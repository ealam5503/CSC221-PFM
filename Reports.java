/**
 * The Reports class handles generating annual financial summaries and 
 * printing requested reports to the console terminal or to a file.
 * 
 * @author Mohammad Forazi
 * @author Shuvo Barmon
 * @author Marlin Canelo
 * @author Colin Russell
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

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
        String[] months = { "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    };
        double[] monthlyIncome = new double[12];
        double[] monthlyExpenses = new double[12]; 

        //Calculate monthly income and expenses 
        for (int i = 0; i < transactions.size(); i++) {

        Transaction t = transactions.get(i);

        int index = t.getMonth() - 1;
        double amount = t.getAmount();

        if (index < 0 || index >= 12) {
            continue;
        }
        if (amount > 0) {
            monthlyIncome[index] += amount;
        } else if (amount < 0) {
            monthlyExpenses[index] -= amount;
        } 
    } 
        //Calculate category totals using Shuvo's method
        calculateCategoryTotals();

        double totalIncome = 0;
        double totalExpenses = 0;

    try (PrintWriter writer = new PrintWriter(new FileWriter("annual_report.csv"))) {

        // Monthly summary
        writer.println("Month,Income,Expenses,Balance");

        for (int i = 0; i < 12; i++) {

            double balance =
                monthlyIncome[i] - monthlyExpenses[i];

            writer.printf(Locale.US, "%s,%.2f,%.2f,%.2f%n",
                months[i],
                monthlyIncome[i],
                monthlyExpenses[i],
                balance);

            totalIncome += monthlyIncome[i];
            totalExpenses += monthlyExpenses[i];
        }
        writer.println();
        writer.println("INCOME BY CATEGORY");
        writer.println("Category,Total");

        for (int i = 0; i < incomeCategories.length; i++) {
            writer.printf(Locale.US, "\"%s\",%.2f%n",
                incomeCategories[i].replace("\"", "\"\""),
                incomeTotals[i]);
        }

        // Expense categories
        writer.println();
        writer.println("EXPENSES BY CATEGORY");
        writer.println("Category,Total");

        for (int i = 0; i < expenseCategories.length; i++) {
            writer.printf(Locale.US, "\"%s\",%.2f%n",
                expenseCategories[i].replace("\"", "\"\""),
                expenseTotals[i]);
        }

        // Yearly summary
        double yearlyBalance = totalIncome - totalExpenses;

        writer.println();
        writer.println("YEARLY SUMMARY");
        writer.println("Metric,Value");

        writer.printf(Locale.US,
            "Total Income,%.2f%n", totalIncome);

        writer.printf(Locale.US,
            "Total Expenses,%.2f%n", totalExpenses);

        writer.printf(Locale.US,
            "Yearly Balance,%.2f%n", yearlyBalance);

        if (yearlyBalance > 0) {
            writer.println("Status,Surplus");
        } else if (yearlyBalance < 0) {
            writer.println("Status,Deficit");
        } else {
            writer.println("Status,Balanced");
        }

        System.out.println(
            "Annual report exported to annual_report.csv");
    }
    catch (IOException e) {
        System.out.println(
            "Error exporting report: " + e.getMessage());
    }
}

//  ---------------------------------------------------------------------
        
            
    /**
     * Calculates expenditure totals aggregated by financial category.
     * 
     * @author Marlin Canelo
     */
   // ------------------------------------------------------------------
    //(Shuvo's code below)
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


