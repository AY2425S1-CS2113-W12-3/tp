package wheresmymoney;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import wheresmymoney.exception.StorageException;
import wheresmymoney.exception.WheresMyMoneyException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.time.LocalDate;

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
    public void addExpense(Float price, String description, String category, LocalDate dateAdded)
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
        if (!DateUtils.isInDateFormat(dateAdded)){
            throw new WheresMyMoneyException("Invalid date format " + DateUtils.DATE_FORMAT);
        }
        try {
            Logging.log(Level.INFO, "Attempting to edit expense.");
            Expense expense = getExpenseAtIndex(index);
            assert (expense != null);
            expense.setPrice(price);
            expense.setDescription(description);
            expense.setCategory(category);
            expense.setDateAdded(DateUtils.stringToDate(dateAdded));
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
    public void loadFromCsv(String filePath) throws StorageException {
        try {
            File file = new File(filePath);
            FileReader reader = new FileReader(file);
            CSVReader csvReader = new CSVReader(reader);
            
            csvReader.readNext(); // Skip the header
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                addExpense(Float.parseFloat(line[2]), line[1], line[0], line[3]);
            }
            
            // closing writer connection
            reader.close();
            csvReader.close();
        } catch (WheresMyMoneyException exc) {
            throw new StorageException("An expense's price, description, category and/or date added is missing");
        } catch (IOException ex) {
            throw new StorageException("Unable to read file!");
        } catch (CsvValidationException e){
            throw new StorageException("File not in the correct format!");
        }
    }

    /**
     * Saves to a csv file.
     *
     * @param filePath File Path to save csv to
     */
    public void saveToCsv(String filePath) throws StorageException {
        File file = new File(filePath);

        // create FileWriter object with file as parameter
        FileWriter outFile;
        try{
            outFile = new FileWriter(file);
        } catch (IOException e) {
            throw new StorageException("Unable to save to file!");
        }


        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outFile);

        // adding header to csv
        String[] header = { "Category", "Description", "Price", "Date Added" };
        writer.writeNext(header);

        for (Expense expense: expenses) {
            String[] row = {
                    expense.getCategory(),
                    expense.getDescription(),
                    expense.getPrice().toString(),
                    DateUtils.dateFormatToString(expense.getDateAdded())
            };
            writer.writeNext(row);
        }

        // closing writer connection
        try {
            writer.close();
        } catch (IOException e) {
            throw new StorageException("Unable to save to file!");
        }
    }
}
