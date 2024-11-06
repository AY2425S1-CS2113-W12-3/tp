package wheresmymoney;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;

import wheresmymoney.exception.WheresMyMoneyException;

public class ExpenseFilter {

    public static boolean isBeforeEndDate(Expense expense, LocalDate endDate){
        return (!expense.getDateAdded().isAfter(endDate));
    }

    public static boolean isAfterStartDate(Expense expense, LocalDate startDate){
        return (!expense.getDateAdded().isBefore(startDate));
    }

    public static boolean isInCategory(Expense expense, String category) {
        return (expense.getCategory().equals(category));
    }

    /**
     * Check if an expense is a match with the given filter criteria
     * If a criteria is null, that field is considered a match
     *
     * @author andrewnguyen4
     * @param expense the expense under check
     * @param category The filter category
     * @param startDate Expense's dateAdded must not be before this date
     * @param endDate Expense's dateAdded must not be after this date
     *
     * @return true if all criteria are either null or matched
     */
    public static boolean isFiltered(Expense expense, String category, String startDate, String endDate)
            throws WheresMyMoneyException {
        if (category != null && !isInCategory(expense, category)) {
            return false;
        }
        if (startDate != null && !isAfterStartDate(expense, DateUtils.stringToDate(startDate))) {
            return false;
        }
        if (endDate != null && !isBeforeEndDate(expense, DateUtils.stringToDate(endDate))) {
            return false;
        }
        return true;
    }

    public static ArrayList<Expense> filterExpenses(ArrayList<Expense> expenses, String category,
                                                    String from, String to) throws WheresMyMoneyException {
        ArrayList<Expense> expenseByFilter = new ArrayList<>();
        for (Expense expense : expenses) {
            if (isFiltered(expense, category, from, to)) {
                Logging.log(Level.INFO, "Found matching expense: " + expense.getDescription());
                expenseByFilter.add(expense);
            }
        }
        return expenseByFilter;
    }

}
