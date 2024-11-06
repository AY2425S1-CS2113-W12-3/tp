package wheresmymoney.command;

import wheresmymoney.Parser;
import wheresmymoney.category.CategoryFacade;
import wheresmymoney.ExpenseList;
import wheresmymoney.RecurringExpenseList;
import wheresmymoney.Ui;
import wheresmymoney.exception.InvalidInputException;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.HashMap;

public class HelpCommand extends Command {

    public HelpCommand(HashMap<String, String> argumentsMap) {
        super(argumentsMap);
    }

    private static void introHelp() {
        Ui.displayMessage("Below is the complete list of all valid commands.");
        Ui.displayMessage("Take note that any word in SCREAMING_SNAKE_CASE is a parameter.");
        Ui.displayMessage("");
    }

    private static void addHelp() {
        Ui.displayMessage("Use the add command to add an expense.");
        Ui.displayMessage("Format:  add [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - PRICE is a decimal number.");
        Ui.displayMessage("    - DESCRIPTION and CATEGORY are text.");
        Ui.displayMessage("Examples: add /price 4.50 /description chicken rice /category food");
        Ui.displayMessage("");
    }

    private static void editHelp() {
        Ui.displayMessage("Use the edit command to edit an expense.");
        Ui.displayMessage("Format: edit INDEX [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - PRICE is a decimal number.");
        Ui.displayMessage("    - DESCRIPTION and CATEGORY are text.");
        Ui.displayMessage("    - All parameters are optional and only the parameters that are" +
                "inputted will be reflected after the edit.");
        Ui.displayMessage("Examples: edit 1 /price 5.50 /description chicken rice /category food");
        Ui.displayMessage("");
    }

    private static void deleteHelp() {
        Ui.displayMessage("Use the delete command to delete an expense.");
        Ui.displayMessage("Format:  delete [INDEX]");
        Ui.displayMessage("Examples: delete 2");
        Ui.displayMessage("");
    }

    private static void setHelp() {
        Ui.displayMessage("Use the set command to set a spending limit for a category");
        Ui.displayMessage("Format: set [/category CATEGORY] [/limit LIMIT]");
        Ui.displayMessage("Examples: set /category food /limit 100");
        Ui.displayMessage("");
    }

    private static void listHelp() {
        Ui.displayMessage("Use the list command to display all expenses by category.");
        Ui.displayMessage("Format:  list [/category CATEGORY] [/from FROM_DATE] [/to TO_DATE]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - CATEGORY is text.");
        Ui.displayMessage("    - FROM_DATE and TO_DATE are dates in DD-MM-YYYY format.");
        Ui.displayMessage("    - Lists all expenses the user has if filters are not specified");
        Ui.displayMessage("    - Lists all expenses that pass through the filters if specified.");
        Ui.displayMessage("Example: list /category food /from 02-11-2024 /to 04-11-2024");
        Ui.displayMessage("");
    }

    private static void statsHelp() {
        Ui.displayMessage("Use the stats command to display statistics for expenses according to specified filters.");
        Ui.displayMessage("Format:  stats [/category CATEGORY] [/from FROM_DATE] [/to TO_DATE]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - CATEGORY is text");
        Ui.displayMessage("    - FROM_DATE and TO_DATE are dates in DD-MM-YYYY format.");
        Ui.displayMessage("    - Lists statistics of all expenses the user has if filters are not specified.");
        Ui.displayMessage("    - Lists statistics of all expenses that pass through the filters if specified.");
        Ui.displayMessage("Example: stats /category food /from 02-11-2024 /to 04-11-2024");
        Ui.displayMessage("");
    }

    private static void helpHelp() {
        Ui.displayMessage("Use the help command to list the command formats that the app recognises.");
        Ui.displayMessage("Format:  help [/method METHOD]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - METHOD is text");
        Ui.displayMessage("    - METHOD exists in our app.");
        Ui.displayMessage("    - Lists statistics of all expenses the user has if filters are not specified.");
        Ui.displayMessage("    - Lists statistics of all expenses that pass through the filters if specified.");
        Ui.displayMessage("Example: help /method add");
        Ui.displayMessage("");
    }

    private static void saveHelp() {
        Ui.displayMessage("Use the save command to save data to files.");
        Ui.displayMessage("Format:  save [/expenseList EXPENSE_FILE_PATH] [/categoryInfo CATEGORY_FILE_PATH]" +
                "[/recurringExpenseList RECUR_FILE_PATH]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - If nothing at all is specified, it loads from the default paths:");
        Ui.displayMessage("    - EXPENSE_FILE_PATH == \"expenses_data.csv\"");
        Ui.displayMessage("    - CATEGORY_FILE_PATH == \"category_spending_limit.csv\"");
        Ui.displayMessage("    - RECUR_FILE_PATH == \"recurring_expenses_data.csv\"");
        Ui.displayMessage("Examples:");
        Ui.displayMessage("    - \"save\" saves data to the default paths.");
        Ui.displayMessage("    - \"save /expenseList ./data.csv\" saves only the expenseList to \"./data.csv\".");
        Ui.displayMessage("");
    }

    private static void loadHelp() {
        Ui.displayMessage("Use the load command to load data from files.");
        Ui.displayMessage("Format:  load [/expenseList EXPENSE_FILE_PATH] [/categoryInfo CATEGORY_FILE_PATH]" +
                "[/recurringExpenseList RECUR_FILE_PATH]");
        Ui.displayMessage("Notes:");
        Ui.displayMessage("    - If nothing at all is specified, it loads from the default paths:");
        Ui.displayMessage("    - EXPENSE_FILE_PATH == \"expenses_data.csv\"");
        Ui.displayMessage("    - CATEGORY_FILE_PATH == \"category_spending_limit.csv\"");
        Ui.displayMessage("    - RECUR_FILE_PATH == \"recurring_expenses_data.csv\"");
        Ui.displayMessage("    - It clears existing data on read for ease of usage.");
        Ui.displayMessage("    - On read failure, it loads whatever it could read from the corrupted files.");
        Ui.displayMessage("Examples:");
        Ui.displayMessage("    - \"load\" loads data from the default paths.");
        Ui.displayMessage("    - \"load /expenseList ./data.csv\" loads only the expenseList from \"./data.csv\".");
        Ui.displayMessage("");
    }

    private static void allHelp() {
        introHelp();
        addHelp();
        editHelp();
        deleteHelp();
        setHelp();
        listHelp();
        statsHelp();
        helpHelp();
        saveHelp();
        loadHelp();
    }

    private static void commandSwitch(String command) {
        switch(command) {
        case "add":
            addHelp();
            break;
        case "edit":
            editHelp();
            break;
        case "delete":
            deleteHelp();
            break;
        case "set":
            setHelp();
            break;
        case "list":
            listHelp();
            break;
        case "stats":
            statsHelp();
            break;
        case "help":
            helpHelp();
            break;
        case "save":
            saveHelp();
            break;
        case "load":
            loadHelp();
            break;
        default:
            throw new InvalidInputException("No valid command given!");
        }
    }

    /**
     * Displays list expenses as requested by user
     */
    @Override
    public void execute(ExpenseList expenseList, CategoryFacade categoryFacade,
                        RecurringExpenseList recurringExpenseList) throws WheresMyMoneyException {
        String command = argumentsMap.get(Parser.ARGUMENT_METHOD);
        if (command == null) {
            allHelp();
        } else {
            commandSwitch(command);
        }
    }
}
