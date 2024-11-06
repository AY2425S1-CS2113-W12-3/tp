package wheresmymoney.command;

import wheresmymoney.category.CategoryFacade;
import wheresmymoney.ExpenseList;
import wheresmymoney.Parser;
import wheresmymoney.RecurringExpenseList;
import wheresmymoney.exception.InvalidInputException;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.HashMap;

public class EditCommand extends Command {

    public EditCommand(HashMap<String, String> argumentsMap) {
        super(argumentsMap);
    }

    /**
     * Edits an expense or recurring expense
     * 
     * @param expenseList List of expenses
     * @param recurringExpenseList List of recurring expenses
     */
    @Override
    public void execute(ExpenseList expenseList, CategoryFacade categoryFacade, 
            RecurringExpenseList recurringExpenseList) throws WheresMyMoneyException {
        try {
            int index = Integer.parseInt(argumentsMap.get(Parser.ARGUMENT_MAIN)) - 1;
            String oldCategory;
            float oldPrice;
            if (!this.isRecur()) {
                oldCategory = expenseList.getExpenseAtIndex(index).getCategory();
                oldPrice = expenseList.getExpenseAtIndex(index).getPrice();
            } else {
                oldCategory = recurringExpenseList.getRecurringExpenseAtIndex(index).getCategory();
                oldPrice = recurringExpenseList.getRecurringExpenseAtIndex(index).getPrice();
            }
            
            String newCategory = argumentsMap.get(Parser.ARGUMENT_CATEGORY);
            float newPrice = Float.parseFloat(argumentsMap.get(Parser.ARGUMENT_PRICE));
            if (newCategory == null) {
                newCategory = oldCategory;
            }
            if (argumentsMap.containsKey(Parser.ARGUMENT_PRICE)) {
                oldPrice = Float.parseFloat(argumentsMap.get(Parser.ARGUMENT_PRICE));
                if (oldPrice <= 0) {
                    throw new InvalidInputException("Price cannot be less than or equal to 0.");
                }
            }
            String description = argumentsMap.get(Parser.ARGUMENT_DESCRIPTION);
            String dateAdded = argumentsMap.get(Parser.ARGUMENT_DATE);
            if (this.isRecur()) {
                String frequency = argumentsMap.get(Parser.ARGUMENT_FREQUENCY);
                recurringExpenseList.editRecurringExpense(
                        index, oldPrice, description, newCategory, dateAdded, frequency);
            } else {
                expenseList.editExpense(index, oldPrice, description, newCategory, dateAdded);
                categoryFacade.editCategory(oldCategory, newCategory, oldPrice, newPrice);
            }
        } catch (NullPointerException | NumberFormatException e) {
            throw new InvalidInputException("Invalid Arguments.");
        }
    }
    
}
