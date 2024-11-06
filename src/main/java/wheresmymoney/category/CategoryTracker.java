package wheresmymoney.category;

import java.util.HashMap;

import wheresmymoney.exception.WheresMyMoneyException;

/**
 * The {@code CategoryTracker} class tracks the expenditure for different categories.
 *
 * <p>
 * For each category, the sum of prices for all expenses in that category and that
 * category's spending limit is kept.
 *
 * <br> It:
 * <li> Provides methods to add, edit, and delete categories' expenditure. </li>
 * <li> Alerts the user if, for any category, expenditure is close to or exceeds spending limits. </li>
 * </p>
 *
 */
public class CategoryTracker {
    private HashMap<String, CategoryData> tracker;
    
    /**
     * Constructs a {@code CategoryTracker} object to manage category expenditures.
     * Initializes an empty tracker.
     */
    public CategoryTracker() {
        this.tracker = new HashMap<>();
    }
    
    public HashMap<String, CategoryData> getTracker() {
        return tracker;
    }
    public boolean isEmpty() { return tracker.isEmpty(); }
    public int size() {
        return tracker.size();
    }
    public boolean contains(String category) {
        return tracker.containsKey(category);
    }
    public void clear() { tracker.clear(); }
    /**
     * Retrieves the {@code CategoryData} object for a given category.
     *
     * @param category The name of the category to retrieve.
     * @return The CategoryData object associated with the specified category.
     * @throws WheresMyMoneyException If the category does not exist in the tracker.
     */
    public CategoryData getCategoryDataOf(String category) throws WheresMyMoneyException {
        if (!tracker.containsKey(category)) {
            throw new WheresMyMoneyException("No such category exists.");
        }
        return tracker.get(category);
    }
    /**
     * Checks if the expenditure for a given category is nearing or exceeding the spending limit.
     * If the limit is exceeded, an alert message is printed.
     * If the spending is nearing the limit, a warning message is printed.
     *
     * @param category The name of the category to check.
     * @throws WheresMyMoneyException If the category does not exist in the tracker.
     */
    public void checkLimitOf(String category) throws WheresMyMoneyException {
        CategoryData categoryData = getCategoryDataOf(category);
        float currExpenditure = categoryData.getCurrExpenditure();
        float maxExpenditure = categoryData.getMaxExpenditure();
        if (categoryData.hasExceededLimit()) {
            System.out.println("Alert! You have exceeded the spending limit of " + maxExpenditure +
                    " for the category of " + category + ", with a total expenditure of " + currExpenditure + ". ");
        } else if (categoryData.isNearingLimit()) {
            System.out.println("Warning! You are close to the spending limit of " + maxExpenditure +
                    " for the category of " + category  + ", with a total expenditure of " + currExpenditure + ". ");
        }
    }
    
    /**
     * Increases an existing category's running total by the given price.
     * or tracks a new category's expenditure details
     * <p>
     * If the category already exists, the current expenditure is increased by the given price.
     * If the category does not exist, a new one is created.
     * After updating the category, the spending limit for the category is checked.
     * </p>
     *
     * @param category The name of the category to add or update.
     * @param price    The price of the Expense to be added to the category's running total.
     * @throws WheresMyMoneyException If the category does not exist in the tracker.
     */
    public void addCategory(String category, Float price) throws WheresMyMoneyException {
        if (tracker.containsKey(category)) {
            CategoryData categoryData = getCategoryDataOf(category);
            assert categoryData != null : "Category exists.";
            categoryData.increaseCurrExpenditureBy(price);
        } else {
            CategoryData categoryData = new CategoryData(price);
            tracker.put(category, categoryData);
        }
    }

    /**
     * Decreases an existing category's running total by the given price.
     *
     * <p>
     * If the category's running total becomes zero or negative, it is removed from the tracker.
     * </p>
     *
     * @param category The name of the category to delete or update.
     * @param price    The expenditure to be subtracted from the category.
     * @throws WheresMyMoneyException If the category does not exist or another error occurs.
     */
    public void deleteCategory(String category, Float price) throws WheresMyMoneyException {
        CategoryData categoryData = getCategoryDataOf(category);
        assert categoryData != null : "Category exists.";
        categoryData.decreaseCurrExpenditureBy(price);
        if (categoryData.getCurrExpenditure() <= 0) {
            tracker.remove(category);
        }
    }

    /**
     * Edits category details if an {@code Expense}'s category attribute is changed.
     * <p>
     * The {@code Expense}'s price is removed from the old category's total
     * and added to the new category's total.
     * </p>
     *
     * @param oldCategory The current category of the {@code Expense}.
     * @param newCategory The new category of the {@code Expense}.
     * @param oldPrice    The current price of the {@code Expense}.
     * @param newPrice    The new price of the {@code Expense}.
     * @throws WheresMyMoneyException If the category does not exist in the tracker.
     */
    public void editCategory(String oldCategory, String newCategory, Float oldPrice, Float newPrice) throws WheresMyMoneyException {
        deleteCategory(oldCategory, oldPrice);
        addCategory(newCategory, newPrice);
    }
    
    /**
     * Sets the spending limit for the specified category.
     *
     * @param category The name of the category for which the spending limit is being set.
     * @param spendingLimit The spending limit to be set for the category.
     * @throws WheresMyMoneyException If the specified category does not exist in the tracker.
     */
    public void setSpendingLimitFor(String category, Float spendingLimit) throws WheresMyMoneyException {
        CategoryData categoryData = getCategoryDataOf(category);
        Float currExpenditure = categoryData.getCurrExpenditure();
        tracker.put(category, new CategoryData(currExpenditure, spendingLimit));
    }
    
}
