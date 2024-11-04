package wheresmymoney;

import wheresmymoney.category.CategoryFacade;
import wheresmymoney.exception.StorageException;
import wheresmymoney.exception.WheresMyMoneyException;

public class Storage {
    private static String EXPENSES_FILE_PATH = "./expenses_data.csv";
    private static String RECURRING_EXPENSES_FILE_PATH = "./recurring_expenses_data.csv";
    private static String CATEGORIES_FILE_PATH = "./category_spending_limit.csv";

    public static void save(
            ExpenseList expenseList, CategoryFacade categoryFacade, RecurringExpenseList recurringExpenseList,
            String expenseFilePath, String categoryFilePath, String recurrExpenseFilePath) throws StorageException {
        if (expenseFilePath == null && recurrExpenseFilePath == null && categoryFilePath == null){
            expenseList.saveToCsv(EXPENSES_FILE_PATH);
            categoryFacade.saveCategoryInfo(CATEGORIES_FILE_PATH);
            recurringExpenseList.saveToCsv(RECURRING_EXPENSES_FILE_PATH);
            Ui.displayMessage("Saved to default file paths");
            return;
        }

        if (expenseFilePath != null) {
            expenseList.saveToCsv(expenseFilePath);
            Ui.displayMessage("Saved Expense List");
        }

        if (categoryFilePath != null) {
            categoryFacade.saveCategoryInfo(categoryFilePath);
            Ui.displayMessage("Saved Category Info");
        }

        if (recurrExpenseFilePath != null) {
            recurringExpenseList.saveToCsv(recurrExpenseFilePath);
            Ui.displayMessage("Saved Recurring Expense List");
        }
    }


    public static void load(
            ExpenseList expenseList, CategoryFacade categoryFacade, RecurringExpenseList recurringExpenseList,
            String expenseFilePath, String categoryFilePath, String recurrExpenseFilePath)
            throws WheresMyMoneyException {
        if (expenseFilePath == null && recurrExpenseFilePath == null && categoryFilePath == null){
            expenseList.loadFromCsv(categoryFacade, EXPENSES_FILE_PATH);
            categoryFacade.loadCategoryInfo(expenseList, CATEGORIES_FILE_PATH);
            recurringExpenseList.loadFromCsv(RECURRING_EXPENSES_FILE_PATH);
            Ui.displayMessage("Loaded from default file paths");
            return;
        }

        if (expenseFilePath != null) {
            expenseList.loadFromCsv(categoryFacade, expenseFilePath);
            Ui.displayMessage("Loaded Expense List");
        }

        if (categoryFilePath != null) {
            categoryFacade.loadCategoryInfo(expenseList, categoryFilePath);
            Ui.displayMessage("Loaded Category Info");
        }

        if (recurrExpenseFilePath != null) {
            recurringExpenseList.loadFromCsv(recurrExpenseFilePath);
            Ui.displayMessage("Loaded Recurring Expense List");
        }
    }
}
