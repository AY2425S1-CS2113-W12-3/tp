package wheresmymoney.command;

import wheresmymoney.Parser;
import wheresmymoney.Storage;
import wheresmymoney.category.CategoryFacade;
import wheresmymoney.ExpenseList;
import wheresmymoney.RecurringExpenseList;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.HashMap;

public class SaveCommand extends Command {
    public SaveCommand(HashMap<String, String> argumentsMap) {
        super(argumentsMap);
    }

    /**
     * Saves data of expenses and recurring expenses to csv
     * 
     * @param expenseList
     * @param recurringExpenseList
     * @throws WheresMyMoneyException
     */
    public void execute(ExpenseList expenseList,  CategoryFacade categoryFacade, 
            RecurringExpenseList recurringExpenseList) throws WheresMyMoneyException {
        Storage.save(expenseList, categoryFacade, recurringExpenseList,
                argumentsMap.get(Parser.ARGUMENT_EXPENSE_LIST),
                argumentsMap.get(Parser.ARGUMENT_CATEGORY_INFO),
                argumentsMap.get(Parser.ARGUMENT_RECURRING_EXPENSE_LIST));
    }
    
}
