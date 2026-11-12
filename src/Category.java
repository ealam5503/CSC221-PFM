/**
 * Income and expense categories.
 *
 * @author Tawseef Jadid
 */
public enum Category {
    /** Income: net pay after taxes and other deductions. */
    COMPENSATION,
    /** Income: contributions from others, including financial aid. */
    ALLOWANCE,
    /** Income: unearned income after taxes. */
    INVESTMENTS,
    /** Income: any other income. */
    INCOME_OTHER,
    /** Expense: rent, mortgage, repairs, furniture, housewares, insurance, cleaning. */
    HOME,
    /** Expense: gas, electric, phone, ISP for home. */
    UTILITIES,
    /** Expense: groceries, non-entertainment meals. */
    FOOD,
    /** Expense: clothing, grooming. */
    APPEARANCE,
    /** Expense: unreimbursed business expenses, uniforms, work-related services. */
    WORK,
    /** Expense: tuition, tutors, supplies. */
    EDUCATION,
    /** Expense: taxis, transit fares, personal vehicle (insurance, lease, fuel, repairs). */
    TRANSPORTATION,
    /** Expense: subscriptions, dining out, admissions, gifts, vacations. */
    ENTERTAINMENT,
    /** Expense: unreimbursed medical, lawyers, consultants. */
    PROFESSIONAL_SERVICES,
    /** Expense: all other expenses. */
    EXPENSE_OTHER
}
