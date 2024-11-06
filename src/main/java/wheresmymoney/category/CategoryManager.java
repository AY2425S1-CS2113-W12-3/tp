package wheresmymoney.category;

import java.time.LocalDate;
import java.util.HashMap;

import wheresmymoney.DateUtils;
import wheresmymoney.exception.WheresMyMoneyException;

public class CategoryManager {
    private HashMap<LocalDate, CategoryTracker> trackerManager;
    
    // wrapper to Facade?
    
    public CategoryManager() {
        this.trackerManager = new HashMap<>();
    }
    public HashMap<LocalDate, CategoryTracker> getTrackerManager() {
        return trackerManager;
    }
    public int size() {
        return trackerManager.size();
    }
    public boolean isEmpty() {
        return trackerManager.isEmpty();
    }
    private boolean containsTrackerOfPeriod(LocalDate localDate) {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);
        return trackerManager.containsKey(yearMonth);
    }
    
    private CategoryTracker getTrackerOfPeriod(LocalDate localDate) {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);
        return trackerManager.get(yearMonth);
    }
    
    public void insertIntoTracker(LocalDate localDate, String category, Float price) throws WheresMyMoneyException {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);
        if (!this.containsTrackerOfPeriod(yearMonth)) {
            trackerManager.put(yearMonth, new CategoryTracker());
        }
        CategoryTracker categoryTracker = this.getTrackerOfPeriod(yearMonth);
        categoryTracker.addCategory(category, price);
    }
    public void removeFromTracker(LocalDate localDate, String category, Float price) throws WheresMyMoneyException {
        LocalDate yearMonth = DateUtils.dayMonthYearToYearMonth(localDate);
        if (!this.containsTrackerOfPeriod(yearMonth)) {
            throw new WheresMyMoneyException("No expense exists for this year and month."); // ?
        }
        CategoryTracker categoryTracker = this.getTrackerOfPeriod(yearMonth);
        categoryTracker.deleteCategory(category, price);
        if (categoryTracker.isEmpty()) {
            trackerManager.remove(localDate);
        }
    }
    public void editInTracker(LocalDate oldDate, LocalDate newDate, String oldCategory, String newCategory,
                              Float oldPrice, Float newPrice) throws WheresMyMoneyException {
        LocalDate oldYearMonth = DateUtils.dayMonthYearToYearMonth(oldDate);
        LocalDate newYearMonth = DateUtils.dayMonthYearToYearMonth(newDate);
        removeFromTracker(oldYearMonth, oldCategory, oldPrice);
        insertIntoTracker(newYearMonth, newCategory, newPrice);
    }
    
}
