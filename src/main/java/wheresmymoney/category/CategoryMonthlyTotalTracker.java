package wheresmymoney.category;

import java.time.LocalDate;
import java.util.HashMap;

import wheresmymoney.DateUtils;
import wheresmymoney.exception.WheresMyMoneyException;

/**
 * Sorts CategoryTracker by months
 */
public class CategoryMonthlyTotalTracker {
    private HashMap<LocalDate, CategoryTotalTracker> monthlyCategoryTotals;
    
    public CategoryMonthlyTotalTracker() {
        this.monthlyCategoryTotals = new HashMap<>();
    }
    public HashMap<LocalDate, CategoryTotalTracker> getMonthlyCategoryTotals() {
        return monthlyCategoryTotals;
    }
    public int size() {
        return monthlyCategoryTotals.size();
    }
    public boolean isEmpty() {
        return monthlyCategoryTotals.isEmpty();
    }
    public boolean containsTotalsForPeriodOf(LocalDate localDate) {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);
        return monthlyCategoryTotals.containsKey(yearMonth);
    }
    
    private CategoryTotalTracker getTrackerForPeriodOf(LocalDate localDate) throws WheresMyMoneyException {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate); // convert just in case

        if (containsTotalsForPeriodOf(yearMonth)) {
            throw new WheresMyMoneyException("There is no categories tracked for the period of" +
                    yearMonth.getMonth() + " " + yearMonth.getYear());
        }
        return monthlyCategoryTotals.get(yearMonth);
    }
    
    public void increaseTotalsOf(LocalDate localDate, String category, Float price) throws WheresMyMoneyException {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);

        if (!this.containsTotalsForPeriodOf(yearMonth)) {
            monthlyCategoryTotals.put(yearMonth, new CategoryTotalTracker());
        }
        CategoryTotalTracker categoryTotalTracker = getTrackerForPeriodOf(yearMonth);
        categoryTotalTracker.increaseTotalBy(category, price);
    }
    public void decreaseTotalsOf(LocalDate localDate, String category, Float price) throws WheresMyMoneyException {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);

        CategoryTotalTracker categoryTotalTracker = getTrackerForPeriodOf(yearMonth);
        categoryTotalTracker.decreaseTotalBy(category, price);
        if (categoryTotalTracker.isEmpty()) {
            monthlyCategoryTotals.remove(yearMonth);
        }
    }
    public void editTotalsOf(LocalDate oldDate, LocalDate newDate, String oldCategory, String newCategory,
                             Float oldPrice, Float newPrice) throws WheresMyMoneyException {
        LocalDate oldYearMonth = DateUtils.dayMonthYearToYearMonth(oldDate);
        LocalDate newYearMonth = DateUtils.dayMonthYearToYearMonth(newDate);

        decreaseTotalsOf(oldYearMonth, oldCategory, oldPrice);
        increaseTotalsOf(newYearMonth, newCategory, newPrice);
    }
    
    public Float getCurrMonthTotalFor(String category) throws WheresMyMoneyException {
        LocalDate currYearMonth = DateUtils.dayMonthYearToYearMonth(DateUtils.getCurrentDate());

        CategoryTotalTracker categoryTotalTracker = getTrackerForPeriodOf(currYearMonth);
        return categoryTotalTracker.getTotalFor(category);
    }
    
}
