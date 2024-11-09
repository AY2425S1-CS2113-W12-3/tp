package wheresmymoney.command;

import java.util.HashMap;

import wheresmymoney.category.CategoryFacade;
import wheresmymoney.ExpenseList;
import wheresmymoney.Parser;
import wheresmymoney.RecurringExpenseList;
import wheresmymoney.exception.InvalidInputException;
import wheresmymoney.exception.WheresMyMoneyException;

public class SetCommand extends Command {
    public SetCommand(HashMap<String, String> argumentsMap) {
        super(argumentsMap);
    }
    
    @Override
    public void execute(ExpenseList expenseList, CategoryFacade categoryFacade, 
            RecurringExpenseList recurringExpense) throws WheresMyMoneyException {
        try {
            String category = argumentsMap.get(Parser.ARGUMENT_CATEGORY);
            float limit = Float.parseFloat(argumentsMap.get(Parser.ARGUMENT_LIMIT));
            categoryFacade.editLimitFor(category, limit);
        } catch (NullPointerException | NumberFormatException e) {
            throw new InvalidInputException("Invalid Arguments");
        }
    }
    
}
