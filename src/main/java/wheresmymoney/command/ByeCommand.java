package wheresmymoney.command;

import wheresmymoney.CategoryTracker;
import wheresmymoney.ExpenseList;
import wheresmymoney.Ui;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.HashMap;

public class ByeCommand extends Command {
    public ByeCommand(HashMap<String, String> argumentsMap) {
        super(argumentsMap);
    }

    @Override
    public void execute(ExpenseList expenseList, CategoryTracker categoryTracker) throws WheresMyMoneyException {
        Ui.displayMessage("Bye. Hope to see you again soon!");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}