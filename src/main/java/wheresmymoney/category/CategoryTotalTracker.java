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
public class CategoryTotalTracker {
    private HashMap<String, Float> categoryTotals;
    
    /**
     * Constructs a {@code CategoryTracker} object to manage category expenditures.
     * Initializes an empty tracker.
     */
    public CategoryTotalTracker() {
        this.categoryTotals = new HashMap<>();
    }
    public HashMap<String, Float> getCategoryTotals() {
        return categoryTotals;
    }
    public boolean isEmpty() { return categoryTotals.isEmpty(); }
    public int size() {
        return categoryTotals.size();
    }
    public boolean contains(String category) {
        return categoryTotals.containsKey(category);
    }
    
    /**
     * Retrieves the {@code CategoryData} object for a given category.
     *
     * @param category The name of the category to retrieve.
     * @return The CategoryData object associated with the specified category.
     * @throws WheresMyMoneyException If the category does not exist in the tracker.
     */
    public Float getTotalFor(String category) throws WheresMyMoneyException {
        if (!categoryTotals.containsKey(category)) {
            throw new WheresMyMoneyException("No such category exists.");
        }
        return categoryTotals.get(category);
    }

    
    /**
     * Increases an existing category's running total by the given price
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
    public void increaseTotalBy(String category, Float price) throws WheresMyMoneyException {
        if (!categoryTotals.containsKey(category)) {
            categoryTotals.put(category, price);
        } else {
            Float oldTotal = getTotalFor(category);
            categoryTotals.put(category, oldTotal + price);
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
    public void decreaseTotalBy(String category, Float price) throws WheresMyMoneyException {
        Float oldTotal = getTotalFor(category);
        categoryTotals.put(category, oldTotal - price);
        if (getTotalFor(category) <= 0) { // need Float.compare() ?
            categoryTotals.remove(category);
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
    public void editTotalsBy(String oldCategory, String newCategory, Float oldPrice, Float newPrice) throws WheresMyMoneyException {
        decreaseTotalBy(oldCategory, oldPrice);
        increaseTotalBy(newCategory, newPrice);
    }
    
}
