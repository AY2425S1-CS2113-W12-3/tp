package wheresmymoney;

import wheresmymoney.category.CategoryFacade;
import wheresmymoney.exception.StorageException;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * The {@code ExpenseList} class manages a collection of {@code Expense} objects.
 * It allows for the addition, editing and deletion of expenses.
 */
public class ExpenseList {
    private ArrayList<Expense> expenses;

    public ExpenseList() {
        expenses = new ArrayList<>();
    }

    public ArrayList<Expense> getExpenseList() {
        return expenses;
    }

    public int getTotal() {
        return expenses.size();
    }

    public boolean isEmpty() {
        return expenses.isEmpty();
    }

    public void clear() {
        expenses.clear();
    }

    /**
     * Retrieves the {@code Expense} at the specified index in the list.
     *
     * @param index The index of the expense to retrieve.
     * @return The {@code Expense} object at the specified index.
     * @throws WheresMyMoneyException If the index is out of bounds.
     */
    public Expense getExpenseAtIndex(int index) throws WheresMyMoneyException {
        try {
            return expenses.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new WheresMyMoneyException("Index is out of bounds.");
        }
    }
    
    /**
     * Returns the index of the specified {@code Expense} in the list.
     *
     * @param expense The {@code Expense} object to find the index of.
     * @return The index of the specified {@code Expense}.
     * @throws WheresMyMoneyException If the expense is not found in the list.
     */
    public int getIndexOf(Expense expense) throws WheresMyMoneyException {
        int index = expenses.indexOf(expense);
        if (index == -1) {
            throw new WheresMyMoneyException("Expense not in list.");
        }
        return index;
    }

    /**
     * Add an expense with an unspecified date to the end of the list
     *
     * @param price New price of expense
     * @param description New description of expense
     * @param category New category of expense
     */
    public void addExpense(Float price, String description, String category) throws WheresMyMoneyException {
        Logging.log(Level.INFO,
                String.format("Adding expense with parameters: %f, %s, %s", price, description, category));
        Expense expense = new Expense(price, description, category);
        expenses.add(expense);
        Logging.log(Level.INFO, "Successfully added expense.");
    }

    /**
     * Add an expense with a specified date to the end of the list
     *
     * @param price New price of expense
     * @param description New description of expense
     * @param category New category of expense
     */
    public void addExpense(Float price, String description, String category, String dateAdded)
            throws WheresMyMoneyException {
        Logging.log(Level.INFO,
                String.format("Adding expense with parameters: %f, %s, %s, %s",
                        price, description, category, dateAdded));
        Expense expense = new Expense(price, description, category, dateAdded);
        assert (expense != null);
        expenses.add(expense);
        Logging.log(Level.INFO, "Successfully added expense.");
    }

    /**
     * Edit the details of an expense given its position in the list
     *
     * @param index index of Expense in the list that is to be edited
     * @param price New price of expense
     * @param description New description of expense
     * @param category New category of expense
     */
    public void editExpense(int index, Float price, String description, String category, String dateAdded)
            throws WheresMyMoneyException {
        if (dateAdded != null && !DateUtils.isInDateFormat(dateAdded)){
            throw new WheresMyMoneyException("Invalid date format " + DateUtils.DATE_FORMAT);
        }
        try {
            Logging.log(Level.INFO, "Attempting to edit expense.");
            Expense expense = getExpenseAtIndex(index);
            assert (expense != null);
            if (price != null) {
                expense.setPrice(price);
            }
            if (description != null) {
                expense.setDescription(description);
            }
            if (category != null) {
                expense.setCategory(category);
            }
            if (dateAdded != null) {
                expense.setDateAdded(DateUtils.stringToDate(dateAdded));
            }
            Logging.log(Level.INFO, "Successfully edited expense.");
        } catch (WheresMyMoneyException e) {
            Logging.log(Level.INFO, "Failure when editing expense.");
            throw new WheresMyMoneyException(e.getMessage());
        }
    }

    /**
     * Deletes the expense at the specified index from the list.
     *
     * @param index The index of the expense to be deleted.
     * @throws WheresMyMoneyException If the index is out of range.
     */
    public void deleteExpense(int index) throws WheresMyMoneyException {
        if (index < 0 || index >= expenses.size()) {
            throw new WheresMyMoneyException("Index out of range!");
        }
        expenses.remove(index);
    }

    /**
     * Returns the list of all expenses from the specified category
     *
     * @param category Category of expense
     */
    public ArrayList<Expense> listByCategory(String category) {
        ArrayList<Expense> expensesFromCategory = new ArrayList<>();
        for (Expense expense: expenses) {
            if (expense.getCategory().equals(category)) {
                Logging.log(Level.INFO, "Found matching expense: " + expense.getDescription());
                expensesFromCategory.add(expense);
            }
        }
        return expensesFromCategory;
    }

    public ArrayList<Expense> listByFilter(String category, String from, String to)
            throws WheresMyMoneyException {
        return ExpenseFilter.filterExpenses(expenses, category, from, to);
    }

    /**
     * Loads from a csv file into Expense List.
     *
     * @param filePath File Path to read csv
     */
    public void loadFromCsv(CategoryFacade categoryFacade, String filePath) throws StorageException {
        clear();
        CsvUtils.readCsv(filePath, line -> {
            try {
                String category = line[0];
                String description = line[1];
                Float price = Float.parseFloat(line[2]);
                String dateAdded = line[3];
                addExpense(price, description, category, dateAdded);
                // makes it slightly less cohesive, but to do so a refactor of the program state might be better.
                if (categoryFacade != null) {
                    categoryFacade.addCategory(category, price);
                }
            } catch (Exception e) {
                throw new StorageException("An expense's price, description, category and/or date added is missing");
            }
        });
    }

    /**
     * Saves to a csv file.
     *
     * @param filePath File Path to save csv to
     */
    public void saveToCsv(String filePath) throws StorageException {
        String[] header = { "Category", "Description", "Price", "Date Added" };
        CsvUtils.writeCsv(filePath, header, (writer) -> {
            for (Expense expense: expenses) {
                String[] row = {
                        expense.getCategory(),
                        expense.getDescription(),
                        expense.getPrice().toString(),
                        DateUtils.dateFormatToString(expense.getDateAdded())
                };
                writer.writeNext(row);
            }
        });
    }
}
