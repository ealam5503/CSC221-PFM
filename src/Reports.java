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
    
    // Official project categories from Category.java
    private final String[] incomeCategories = {
        "COMPENSATION", "ALLOWANCE", "INVESTMENTS", "INCOME_OTHER"
    };
    private final String[] expenseCategories = {
        "HOME", "UTILITIES", "FOOD", "APPEARANCE", "WORK", 
        "EDUCATION", "TRANSPORTATION", "ENTERTAINMENT", 
        "PROFESSIONAL_SERVICES", "EXPENSE_OTHER"
    };
    
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
            double amount = t.amount();

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

            Object catObj = t.category();
            String category = (catObj != null) ? catObj.toString() : "EXPENSE_OTHER";
            double amount = t.amount();

            // Check income
            if (amount > 0) {
                boolean matched = false;
                for (int j = 0; j < incomeCategories.length; j++) {
                    if (category.equalsIgnoreCase(incomeCategories[j])) {
                        incomeTotals[j] += amount;
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    incomeTotals[incomeCategories.length - 1] += amount; // Fallback to INCOME_OTHER
                }
            // Check expenses
            } else if (amount < 0) {
                boolean matched = false;
                double absAmount = Math.abs(amount);
                for (int j = 0; j < expenseCategories.length; j++) {
                    if (category.equalsIgnoreCase(expenseCategories[j])) {
                        expenseTotals[j] += absAmount;
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    expenseTotals[expenseCategories.length - 1] += absAmount; // Fallback to EXPENSE_OTHER
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

    // Get monthly income and expenses
    for (int i = 0; i < transactions.size(); i++) {

        Transaction t = transactions.get(i);

        int month = extractMonth(t.date());
        double amount = t.amount();
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

    System.out.println();
    System.out.println("================================================================================");
    System.out.println("                           ANNUAL FINANCIAL REPORT");
    System.out.println("================================================================================");

    // Monthly table heading
    System.out.printf("%-18s %19s %19s %19s%n",
            "Month", "Income", "Expenses", "Balance");

    System.out.println("--------------------------------------------------------------------------------");

    // Print all 12 months
    for (int i = 0; i < 12; i++) {

        double balance = monthlyIncome[i] - monthlyExpenses[i];

        System.out.printf("%-18s $%,18.0f $%,18.0f $%,18.0f%n",
                months[i], monthlyIncome[i],
                monthlyExpenses[i], balance);

        totalIncome += monthlyIncome[i];
        totalExpenses += monthlyExpenses[i];
    }

    double yearlyBalance = totalIncome - totalExpenses;

    System.out.println("--------------------------------------------------------------------------------");

    System.out.printf("%-18s $%,18.0f $%,18.0f $%,18.0f%n",
            "TOTAL", totalIncome, totalExpenses, yearlyBalance);

    System.out.println("================================================================================");

    // Income category report
    System.out.println();
    System.out.println("                         INCOME BY CATEGORY");
    System.out.println("------------------------------------------------------------");

    for (int i = 0; i < incomeCategories.length; i++) {

        String category = incomeCategories[i].replace("_", " ");

        System.out.printf("%-35s $%,20.0f%n",
                category, incomeTotals[i]);
    }

    System.out.println("------------------------------------------------------------");

    System.out.printf("%-35s $%,20.0f%n",
            "Total Income", totalIncome);

    // Expense category report
    System.out.println();
    System.out.println("                        EXPENSES BY CATEGORY");
    System.out.println("------------------------------------------------------------");

    for (int i = 0; i < expenseCategories.length; i++) {

        String category = expenseCategories[i].replace("_", " ");

        System.out.printf("%-35s $%,20.0f%n",
                category, expenseTotals[i]);
    }

    System.out.println("------------------------------------------------------------");

    System.out.printf("%-35s $%,20.0f%n",
            "Total Expenses", totalExpenses);

    // Annual summary
    System.out.println();
    System.out.println("                           ANNUAL SUMMARY");
    System.out.println("------------------------------------------------------------");

    System.out.printf("%-35s $%,20.0f%n",
            "Total Income", totalIncome);

    System.out.printf("%-35s $%,20.0f%n",
            "Total Expenses", totalExpenses);

    System.out.printf("%-35s $%,20.0f%n",
            "Yearly Balance", yearlyBalance);

    System.out.println("------------------------------------------------------------");

    if (yearlyBalance > 0) {
        System.out.println("Status: Surplus");
    } else if (yearlyBalance < 0) {
        System.out.println("Status: Deficit");
    } else {
        System.out.println("Status: Balanced");
    }

    System.out.println("================================================================================");
}

    /**
     * Helper method to safely parse the month integer from a Transaction date string.
     */
    private int extractMonth(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return 1;
        }
        try {
            if (dateStr.contains("-")) {
                String[] parts = dateStr.split("-");
                if (parts.length >= 2) {
                    return Integer.parseInt(parts[1]);
                }
            } else if (dateStr.contains("/")) {
                String[] parts = dateStr.split("/");
                if (parts.length >= 1) {
                    return Integer.parseInt(parts[0]);
                }
            }
            return Integer.parseInt(dateStr.trim());
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}

