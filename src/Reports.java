import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;

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

    private ArrayList<Transaction> transactions;
    private String[] incomeCategories = {"Salary", "Investments", "Freelance", "Other Income"};
    private String[] expenseCategories = {"Rent", "Groceries", "Utilities", "Entertainment", "Other Expense"};
    private double[] incomeTotals = new double[incomeCategories.length];
    private double[] expenseTotals = new double[expenseCategories.length];

    /**
     * Constructs a new Reports instance.
     * 
     * @param transactions The list of transactions from storage.
     * @author Mohammad Forazi
     * @author Shuvo Barmon
     * @author Marlin Canelo
     * @author Colin Russell
     */
    public Reports(ArrayList<Transaction> transactions) {
        this.transactions = (transactions != null) ? transactions : new ArrayList<>();
    }

    /**
     * Generates the annual report summarizing monthly income minus expenses, 
     * category totals, and overall budget performance (surplus or deficit).
     * 
     * @author Mohammad Forazi
     */
    public void generateAnnualReport() {
        printConsoleReport();
    }

    /**
     * Exports the annual financial report to a CSV file for spreadsheet manipulation.
     * 
     * @author Colin Russell
     */
    public void exportReportToCSV() {
        String[] months = { 
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        };
        double[] monthlyIncome = new double[12];
        double[] monthlyExpenses = new double[12]; 

        // Calculate monthly income and expenses using record accessors
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            int index = extractMonth(t.date()) - 1;
            double amount = t.amount(); // Fixed: using record accessor t.amount()

            if (index < 0 || index >= 12) {
                continue;
            }
            if (amount > 0) {
                monthlyIncome[index] += amount;
            } else if (amount < 0) {
                monthlyExpenses[index] -= amount; 
            } 
        } 
        
        // Calculate category totals using Marlin/Shuvo's method
        calculateCategoryTotals();

        double totalIncome = 0;
        double totalExpenses = 0;

        try (PrintWriter writer = new PrintWriter(new FileWriter("annual_report.csv"))) {
            // Monthly summary
            writer.println("Month,Income,Expenses,Balance");

            for (int i = 0; i < 12; i++) {
                double balance = monthlyIncome[i] - monthlyExpenses[i];

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

            writer.printf(Locale.US, "Total Income,%.2f%n", totalIncome);
            writer.printf(Locale.US, "Total Expenses,%.2f%n", totalExpenses);
            writer.printf(Locale.US, "Yearly Balance,%.2f%n", yearlyBalance);

            if (yearlyBalance > 0) {
                writer.println("Status,Surplus");
            } else if (yearlyBalance < 0) {
                writer.println("Status,Deficit");
            } else {
                writer.println("Status,Balanced");
            }

            System.out.println("Annual report exported to annual_report.csv");
        }
        catch (IOException e) {
            System.out.println("Error exporting report: " + e.getMessage());
        }
    }
        
    /**
     * Calculates expenditure totals aggregated by financial category.
     * 
     * @author Marlin Canelo
     */
    public void calculateCategoryTotals() {
        // Reset income totals
        for (int i = 0; i < incomeTotals.length; i++) {
            incomeTotals[i] = 0;
        }

        // Reset expense totals
        for (int i = 0; i < expenseTotals.length; i++) {
            expenseTotals[i] = 0;
        }

        // Go through all user transactions using record accessors
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            String category = t.category(); // Fixed: using record accessor t.category()
            double amount = t.amount();     // Fixed: using record accessor t.amount()

            if (category == null) {
                category = "Other";
            }

            // Check income
            if (amount > 0) {
                for (int j = 0; j < incomeCategories.length; j++) {
                    if (category.equals(incomeCategories[j])) {
                        incomeTotals[j] += amount;
                        break;
                    }
                }
            // Check expenses
            } else if (amount < 0) {
                for (int j = 0; j < expenseCategories.length; j++) {
                    if (category.equals(expenseCategories[j])) {
                        expenseTotals[j] -= amount; // matching group's negative subtraction logic
                        break;
                    }
                }
            }
        }
    }

    /**
     * Prints the console report.
     * 
     * @author Shuvo Barmon
     */
    public void printConsoleReport() {
        String[] months = {
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        };

        double[] monthlyIncome = new double[12];
        double[] monthlyExpenses = new double[12];

        // Calculate monthly income and expenses using record accessors
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            int month = extractMonth(t.date());
            double amount = t.amount(); // Fixed: using record accessor t.amount()

            int index = month - 1;

            if (index < 0 || index >= 12) {
                continue;
            }

            if (amount > 0) {
                monthlyIncome[index] += amount;
            } else if (amount < 0) {
                monthlyExpenses[index] -= amount;
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

            totalIncome += monthlyIncome[i];
            totalExpenses += monthlyExpenses[i];
        }

        // Print income categories
        System.out.println("INCOME BY CATEGORY");
        for (int i = 0; i < incomeCategories.length; i++) {
            System.out.println(incomeCategories[i] + ": " + incomeTotals[i]);
        }

        // Print expense categories
        System.out.println("EXPENSES BY CATEGORY");
        for (int i = 0; i < expenseCategories.length; i++) {
            System.out.println(expenseCategories[i] + ": " + expenseTotals[i]);
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

    /**
     * Helper method to safely parse the month integer from a Transaction date string 
     * (supports formats like "YYYY-MM-DD", "MM/DD/YYYY", or numeric strings).
     */
    private int extractMonth(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return 1; // Default fallback month if empty
        }
        try {
            // If date is stored as "YYYY-MM-DD" or "MM/DD/YYYY"
            if (dateStr.contains("-")) {
                String[] parts = dateStr.split("-");
                if (parts.length >= 2) {
                    return Integer.parseInt(parts[1]); // Assumes YYYY-MM-DD
                }
            } else if (dateStr.contains("/")) {
                String[] parts = dateStr.split("/");
                if (parts.length >= 1) {
                    return Integer.parseInt(parts[0]); // Assumes MM/DD/YYYY
                }
            }
            return Integer.parseInt(dateStr.trim());
        } catch (NumberFormatException e) {
            return 1; // Fallback
        }
    }
}
