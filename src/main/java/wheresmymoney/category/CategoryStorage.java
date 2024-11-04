package wheresmymoney.category;

import java.util.ArrayList;
import java.util.HashMap;

import wheresmymoney.CsvUtils;
import wheresmymoney.Expense;
import wheresmymoney.exception.StorageException;
import wheresmymoney.exception.WheresMyMoneyException;

/**
 * The {@code CategoryStorage} class handles conversion between CSV and CategoryTracker.
 */
public class CategoryStorage {
    private CategoryFacade categoryFacade;
    
    public void setCategoryFacade(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }
    
    /**
     * Creates a {@code CategoryTracker} object based on the given {@code ExpenseList}.
     *
     * @param expenseList The list of expenses to be tracked, each with a category and a price.
     * @return A CategoryTracker containing the total spending for each category based on the provided expense list.
     * @throws WheresMyMoneyException If an error occurs while adding a category.
     */
    public CategoryTracker trackCategoriesOf(ArrayList<Expense> expenseList) throws WheresMyMoneyException {
        CategoryTracker categoryTracker = new CategoryTracker();
        for (Expense expense : expenseList) {
            String categoryName = expense.getCategory();
            Float price = expense.getPrice();
            categoryTracker.addCategory(categoryName, price);
        }
        return categoryTracker;
    }
    
    /**
     * Loads from CSV file and updates spending limits for found categories.
     *
     * @param filePath File Path to read CSV from
     */
    public void loadFromCsv(String filePath, CategoryTracker categoryTracker) throws StorageException {
        categoryTracker.clear();
        CsvUtils.readCsv(filePath, line -> {
            if (line.length != 2) {
                return;
            }

            String categoryName = line[0];
            Float spendingLimit = Float.parseFloat(line[1]);
            if (categoryTracker.contains(categoryName)) {
                CategoryData categoryData = categoryTracker.getCategoryDataOf(categoryName);
                categoryData.setMaxExpenditure(spendingLimit);
            }
        });
    }

    /**
     * Saves a Category Tracker to a csv file.
     *
     * @param filePath File Path to save csv to
     */
    public void saveToCsv(String filePath, HashMap<String, CategoryData> tracker) throws StorageException {
        String[] header = { "Category", "SpendingLimit" };
        CsvUtils.writeCsv(filePath, header, (writer) -> {
            for (String categoryName : tracker.keySet()) {
                Float spendingLimit = tracker.get(categoryName).getMaxExpenditure();
                String[] row = { categoryName, spendingLimit.toString() };
                writer.writeNext(row);
            }
        });
    }
}
