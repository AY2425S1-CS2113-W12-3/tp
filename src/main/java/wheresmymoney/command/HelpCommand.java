package wheresmymoney.command;

import wheresmymoney.category.CategoryFacade;
import wheresmymoney.ExpenseList;
import wheresmymoney.RecurringExpenseList;
import wheresmymoney.Ui;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.HashMap;

public class HelpCommand extends Command {

    public HelpCommand(HashMap<String, String> argumentsMap) {
        super(argumentsMap);
    }

    /**
     * Displays list expenses as requested by user
     */
    @Override
    public void execute(ExpenseList expenseList, CategoryFacade categoryFacade, 
            RecurringExpenseList recurringExpenseList) 
            throws WheresMyMoneyException {
        Ui.displayMessage("Here are the list of commands");
        Ui.displayMessage("Take note that any words in SCREAMING_SNAKE_CASE is a parameter");
        Ui.displayMessage("");

        Ui.displayMessage("Use the add command to add an expense");
        Ui.displayMessage("Format:  add [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - Price is a decimal number");
        Ui.displayMessage("    - Description and Category are Text");
        Ui.displayMessage("Examples: add /price 4.50 /description chicken rice /category food");
        Ui.displayMessage("");

        Ui.displayMessage("Use the edit command to edit an expense");
        Ui.displayMessage("Format: edit INDEX [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - Price is a decimal number");
        Ui.displayMessage("    - Description and Category are Text");
        Ui.displayMessage("    - All parameters are optional and only the parameters that are" +
                "inputted will be reflected after the edit");
        Ui.displayMessage("Examples: edit 1 /price 5.50 /description chicken rice /category food");
        Ui.displayMessage("");

        Ui.displayMessage("Use the delete command to delete an expense");
        Ui.displayMessage("Format:  delete [INDEX]");
        Ui.displayMessage("Examples: delete 2");
        Ui.displayMessage("");
        
        Ui.displayMessage("Use the set command to set a spending limit for a category");
        Ui.displayMessage("Format: set [/category CATEGORY] [/limit LIMIT]");
        Ui.displayMessage("Examples: set /category food /limit 100");
        Ui.displayMessage("");

        Ui.displayMessage("Use the list command to display all expenses by category");
        Ui.displayMessage("Format:  list [/category CATEGORY]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - Category is text");
        Ui.displayMessage("    - Lists all expenses the user has if the category is not specified");
        Ui.displayMessage("    - Lists all expenses with that category if specified");
        Ui.displayMessage("Examples: list /category food");
        Ui.displayMessage("");

        Ui.displayMessage("Use the stats command to display basic statistics of expenses by category");
        Ui.displayMessage("Format:  stats [/category CATEGORY]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - Category is text");
        Ui.displayMessage("    - Lists stats of all expenses the user has if the category is not specified");
        Ui.displayMessage("    - Lists stats of all expenses with that category if specified");
        Ui.displayMessage("Examples: stats /category food");
        Ui.displayMessage("");
    }
}
