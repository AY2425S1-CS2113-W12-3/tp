package wheresmymoney.command;

import wheresmymoney.ExpenseList;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.HashMap;

public abstract class Command {
    protected HashMap<String, String> argumentsMap;

    public Command(HashMap<String, String> argumentsMap) {
        this.argumentsMap = argumentsMap;
    }
    public abstract void execute(ExpenseList expenseList) throws WheresMyMoneyException;
    public boolean isExit(){
        return false;
    }
}
