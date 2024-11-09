package wheresmymoney.category;

import java.time.LocalDate;

import wheresmymoney.ExpenseList;
import wheresmymoney.exception.StorageException;
import wheresmymoney.exception.WheresMyMoneyException;

/**
 * The {@code CategoryFacade} class provides a simplified interface for category management.
 *
 * <p>
 * It acts as a facade to the underlying CategoryTracker and CategoryFilter components,
 * allowing commands to perform operations on categories such as:
 * <li> CRUD operations </li>
 * <li> filtering operations </li>
 * <li> file operations </li>
 * </p>
 */
public class CategoryFacade {
    private CategoryMonthlyTotalTracker monthlyTotalTracker;
    private CategoryLimitTracker limitTracker;
    private CategoryFilter categoryFilter;
    private CategoryStorage categoryStorage;
    
    public CategoryFacade() {
        this.monthlyTotalTracker = new CategoryMonthlyTotalTracker();
        this.limitTracker = new CategoryLimitTracker();
        this.categoryFilter = new CategoryFilter();
        this.categoryStorage = new CategoryStorage();
        this.categoryFilter.setCategoryFacade(this);
        this.categoryStorage.setCategoryFacade(this);
    }
    public CategoryMonthlyTotalTracker getMonthlyTotalTracker() {
        return monthlyTotalTracker;
    }
    public CategoryLimitTracker getCategoryLimitTracker() {
        return limitTracker;
    }
    
    /**
     * The interface for {@code AddCommand} when the user adds a new Expense.
     *
     * @param category the category of the newly added Expense
     * @param price the price of the newly added Expense
     * @throws WheresMyMoneyException if there is an error while adding the category
     */
    public void addCategory(LocalDate localDate, String category, float price) throws WheresMyMoneyException {
        monthlyTotalTracker.increaseTotalsOf(localDate, category, price);
        categoryFilter.checkLimitFor(category);
    }
    /**
     * The interface for {@code DeleteCommand} when the user deletes an Expense.
     *
     * @param category the category of the Expense to be deleted
     * @param price the price of the Expense to be deleted
     * @throws WheresMyMoneyException if there is an error while deleting the category
     */
    public void deleteCategory(LocalDate localDate, String category, Float price) throws WheresMyMoneyException {
        monthlyTotalTracker.decreaseTotalsOf(localDate, category, price);
    }
    /**
     * The interface for {@code EditCommand} when the user edits an Expense.
     *
     * @param oldCategory the current name of the category to be edited
     * @param newCategory the new name for the category
     * @param oldPrice    The current price of the {@code Expense}.
     * @param newPrice    The new price of the {@code Expense}.
     * @throws WheresMyMoneyException if there is an error while editing the category
     */
    public void editCategory(LocalDate oldDate, LocalDate newDate, String oldCategory, String newCategory,
                             Float oldPrice, Float newPrice) throws WheresMyMoneyException {
        monthlyTotalTracker.editTotalsOf(oldDate, newDate, oldCategory, newCategory, oldPrice, newPrice);
        categoryFilter.checkLimitFor(newCategory);
    }
    
    /**
     * The interface for {@code LoadCommand} to load category information from a CSV file.
     *
     * @param expenseList the list of expenses to track category information
     * @throws WheresMyMoneyException if there is an error while loading category info
     */
    public void loadCategoryLimits(ExpenseList expenseList, String filePath) throws WheresMyMoneyException {
        categoryTracker = categoryStorage.loadFromCsv(
                filePath, categoryStorage.trackCategoriesOf(expenseList.getExpenseList()));
    }


    /**
     * The interface for {@code LoadCommand} to show filtered categories
     * based on spending limits.
     */
    public void displayExceededAndNearingCategories() {
        categoryFilter.getCategoriesFiltered();
        categoryFilter.displayExceededCategories();
        categoryFilter.displayNearingCategories();
    }
    /**
     * The interface for {@code SaveCommand} to save current category information to a CSV file.
     *
     * @throws StorageException if there is an error while saving category info
     */
    public void saveCategoryLimits(String filePath) throws StorageException {
        categoryStorage.saveToCsv(filePath, limitTracker.getLimits());
    }
    /**
     * The interface for {@code SetCommand} to set a spending limit for a specified category.
     *
     * @param category the name of the category for which to set the spending limit for
     * @param limit the spending limit to set for the category
     * @throws WheresMyMoneyException if there is an error while setting the spending limit
     */
    public void editLimitFor(String category, float limit) throws WheresMyMoneyException {
        limitTracker.setCustomLimitFor(category, limit);
    }
}
