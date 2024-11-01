package wheresmymoney.command;

import wheresmymoney.category.CategoryFacade;
import wheresmymoney.ExpenseList;
import wheresmymoney.Parser;
import wheresmymoney.exception.InvalidInputException;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.HashMap;

public class AddCommand extends Command {

    public AddCommand(HashMap<String, String> argumentsMap) {
        super(argumentsMap);
    }

    @Override
    public void execute(ExpenseList expenseList, CategoryFacade categoryFacade) throws WheresMyMoneyException {
        try {
            float price = Float.parseFloat(argumentsMap.get(Parser.ARGUMENT_PRICE));
            String description = argumentsMap.get(Parser.ARGUMENT_DESCRIPTION);
            String category = argumentsMap.get(Parser.ARGUMENT_CATEGORY);

            if (argumentsMap.containsKey(Parser.ARGUMENT_DATE_ADDED)) {
                String dateAdded = argumentsMap.get(Parser.ARGUMENT_DATE_ADDED);
                expenseList.addExpense(price, description, category, dateAdded);
            } else {
                expenseList.addExpense(price, description, category);
            }
            
            categoryFacade.addCategory(category, price);
        } catch (NullPointerException | NumberFormatException e) {
            throw new InvalidInputException("Invalid Arguments");
        }
    }
    
}
