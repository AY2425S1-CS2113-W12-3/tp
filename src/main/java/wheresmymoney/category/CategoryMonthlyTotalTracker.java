package wheresmymoney.category;

import java.time.LocalDate;
import java.util.HashMap;

import wheresmymoney.DateUtils;
import wheresmymoney.exception.WheresMyMoneyException;

/**
 * Sorts CategoryTracker by months
 */
public class CategoryManager {
    private HashMap<LocalDate, CategoryTotalTracker> monthlyCategoryTotals;
    
    // wrapper to Facade?
    
    public CategoryManager() {
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
    private boolean containsCategoryTotalsInPeriod(LocalDate localDate) {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);
        return monthlyCategoryTotals.containsKey(yearMonth);
    }
    public void checkLimitOf(String category) throws WheresMyMoneyException {
        LocalDate currYearMonth = DateUtils.dayMonthYearToYearMonth(DateUtils.getCurrentDate());
        CategoryTotalTracker categoryTotalTracker = getTrackerOfPeriod(currYearMonth);
        categoryTotalTracker.checkLimitOf(category);
    }
    
    private CategoryTotalTracker getTrackerOfPeriod(LocalDate localDate) throws WheresMyMoneyException {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);
        if (containsCategoryTotalsInPeriod(yearMonth)) {
            throw new WheresMyMoneyException("There is no categories tracked for the period of" +
                    yearMonth.getMonth() + " " + yearMonth.getYear());
        }
        return monthlyCategoryTotals.get(yearMonth);
    }
    
    public void insertIntoTracker(LocalDate localDate, String category, Float price) throws WheresMyMoneyException {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);
        if (!this.containsCategoryTotalsInPeriod(yearMonth)) {
            monthlyCategoryTotals.put(yearMonth, new CategoryTotalTracker());
        }
        CategoryTotalTracker categoryTotalTracker = this.getTrackerOfPeriod(yearMonth);
        categoryTotalTracker.addCategory(category, price);
    }
    public void removeFromTracker(LocalDate localDate, String category, Float price) throws WheresMyMoneyException {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);
        CategoryTotalTracker categoryTotalTracker = this.getTrackerOfPeriod(yearMonth);
        categoryTotalTracker.deleteCategory(category, price);
        if (categoryTotalTracker.isEmpty()) {
            monthlyCategoryTotals.remove(localDate);
        }
    }
    public void editInTracker(LocalDate oldDate, LocalDate newDate, String oldCategory, String newCategory,
                              Float oldPrice, Float newPrice) throws WheresMyMoneyException {
        LocalDate oldYearMonth = DateUtils.dayMonthYearToYearMonth(oldDate);
        LocalDate newYearMonth = DateUtils.dayMonthYearToYearMonth(newDate);
        removeFromTracker(oldYearMonth, oldCategory, oldPrice);
        insertIntoTracker(newYearMonth, newCategory, newPrice);
    }
    
    public void setSpendingLimitFor(String category, Float spendingLimit) {
        LocalDate currYearMonth = DateUtils.dayMonthYearToYearMonth(DateUtils.getCurrentDate());
        CategoryTotalTracker categoryTotalTracker = getTrackerOfPeriod(currYearMonth);
        categoryTotalTracker.setSpendingLimitFor(category, spendingLimit);
    }
    
}
