package wheresmymoney.command;

import wheresmymoney.Parser;
import wheresmymoney.Storage;
import wheresmymoney.category.CategoryFacade;
import wheresmymoney.ExpenseList;
import wheresmymoney.RecurringExpenseList;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.HashMap;

public class LoadCommand extends Command {


    public LoadCommand(HashMap<String, String> argumentsMap) {
        super(argumentsMap);
    }

    /**
     * Loads expenses and recurring expenses from csv
     * 
     * @param expenseList
     * @param recurringExpenseList
     * @throws WheresMyMoneyException
     */
    @Override
    public void execute(ExpenseList expenseList, CategoryFacade categoryFacade, 
            RecurringExpenseList recurringExpenseList) throws WheresMyMoneyException {
        Storage.load(expenseList, categoryFacade, recurringExpenseList,
                argumentsMap.get(Parser.ARGUMENT_EXPENSE_LIST),
                argumentsMap.get(Parser.ARGUMENT_CATEGORY_INFO),
                argumentsMap.get(Parser.ARGUMENT_RECURRING_EXPENSE_LIST));
    }
}
