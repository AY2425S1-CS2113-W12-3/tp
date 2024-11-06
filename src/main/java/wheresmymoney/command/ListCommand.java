package wheresmymoney.command;

import wheresmymoney.category.CategoryFacade;
import wheresmymoney.Expense;
import wheresmymoney.ExpenseList;
import wheresmymoney.Parser;
import wheresmymoney.RecurringExpense;
import wheresmymoney.RecurringExpenseList;
import wheresmymoney.Ui;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.ArrayList;
import java.util.HashMap;

public class ListCommand extends Command {

    public ListCommand(HashMap<String, String> argumentsMap) {
        super(argumentsMap);
    }

    /**
     * Get a list of expenses based on various filter metrics
     *
     * @author shyaamald
     * @param expenseList ExpenseList to be filtered by category, a start date and an end date
     */
    private ArrayList<Expense> getExpensesToDisplay(ExpenseList expenseList) throws WheresMyMoneyException {
        String listCategory = argumentsMap.get(Parser.ARGUMENT_CATEGORY);
        String from = argumentsMap.get(Parser.ARGUMENT_FROM);
        String to = argumentsMap.get(Parser.ARGUMENT_TO);
        return expenseList.listByFilter(listCategory, from, to);
    }

    private ArrayList<RecurringExpense> getRecurringExpensesToDisplay(RecurringExpenseList recurringExpenseList) {
        String listCategory = argumentsMap.get(Parser.ARGUMENT_CATEGORY);
        if (listCategory == null) {
            return recurringExpenseList.getRecurringExpenseList();
        } else {
            return recurringExpenseList.listByCategoryForRecurring(listCategory);
        }
    }

    /**
     * Display the list of expenses passed to it
     *
     * @author shyaamald
     * @param expensesToDisplay List of expenses to be displayed
     * @param expenseList Main expense list to retrieve expense indices
     */
    private void displayExpenses(ArrayList<Expense> expensesToDisplay, ExpenseList expenseList)
            throws WheresMyMoneyException {
        if (expensesToDisplay.isEmpty()) {
            Ui.displayMessage("No matching expenses were found!");
            return;
        }
        for (Expense expense: expensesToDisplay) {
            Ui.displayExpense(expenseList, expense);
        }
    }

    private void displayRecurringExpenses(ArrayList<RecurringExpense> expensesToDisplay,
            RecurringExpenseList recurringExpenseList) throws WheresMyMoneyException {
        for (RecurringExpense recurringExpense: expensesToDisplay) {
            try {
                String index = recurringExpenseList.getIndexOf(recurringExpense) + 1 + ". ";
                String category = "CATEGORY: " + recurringExpense.getCategory();
                String description = "   DESCRIPTION: " + recurringExpense.getDescription();
                String price = "   PRICE: " + String.format("%.2f", recurringExpense.getPrice());;
                String lastAddedDate = "   LAST ADDED DATE: " + recurringExpense.getlastAddedDate();
                String frequency = "   FREQUENCY: " + recurringExpense.getFrequency();
                Ui.displayMessage(index + category + description + price + lastAddedDate + frequency);
            } catch (WheresMyMoneyException e) {
                throw new WheresMyMoneyException("displayRecurringExpenses has an error");
            }

        }
    }

    /**
     * Display list expenses as requested by user
     */
    @Override
    public void execute(ExpenseList expenseList, CategoryFacade categoryFacade,
            RecurringExpenseList recurringExpenseList) throws WheresMyMoneyException {
        if (this.isRecur()) {
            ArrayList<RecurringExpense> expensesToDisplay = getRecurringExpensesToDisplay(recurringExpenseList);
            assert (expensesToDisplay != null);
            displayRecurringExpenses(expensesToDisplay, recurringExpenseList);
        } else {
            ArrayList<Expense> expensesToDisplay = getExpensesToDisplay(expenseList);
            displayExpenses(expensesToDisplay, expenseList);
        }
    }
}
