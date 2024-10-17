package wheresmymoney;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser{
    public static final String ARGUMENT_COMMAND = "command";
    public static final String ARGUMENT_MAIN = "main";
    public static final String ARGUMENT_CATEGORY = "category";
    public static final String ARGUMENT_PRICE = "price";
    public static final String ARGUMENT_DESCRIPTION = "description";

    private static Logger logger = Logger.getLogger("Foo");
    /**
     * Parses the given user input into command arguments
     *
     * @param line Line that a user inputs
     * @return HashMap of Arguments, mapping the argument to its value given
     */
    public HashMap<String, String> parseCommandToArguments(String line) {
        logger.log(Level.INFO, "Parsing command: " + line);
        HashMap<String, String> argumentsList = new HashMap<>();
        String[] lineArgs = line.split(" ");

        // Command
        if (lineArgs.length == 0) {
            argumentsList.put(Parser.ARGUMENT_COMMAND,"");
            return argumentsList;
        }
        assert lineArgs.length > 0;
        argumentsList.put(Parser.ARGUMENT_COMMAND,lineArgs[0]);

        // Arguments
        String currArgumentName = Parser.ARGUMENT_MAIN;
        StringBuilder currArgument = new StringBuilder();

        for (int i=1; i<lineArgs.length; i++) {
            if (lineArgs[i].isEmpty()) { // Should be redundant but just in case
                continue;
            }
            if (lineArgs[i].charAt(0) == '/') {
                // New argument
                if (!currArgument.toString().isEmpty()){
                    argumentsList.put(currArgumentName, currArgument.toString().strip());
                }
                currArgumentName = lineArgs[i].replace("/", "");
                currArgument.setLength(0);
            } else {
                // Add on to existing argument
                currArgument.append(" ").append(lineArgs[i]);
            }
        }
        // Add last command
        if (!currArgument.toString().isEmpty()) {
            argumentsList.put(currArgumentName, currArgument.toString().strip());
        }
        logger.log(Level.INFO, "Finish parsing command: " + line);
        return argumentsList;
    }

    /**
     * Matches the argument list to a related command and runs said command
     *
     * @param argumentsList List of arguments
     * @param expenseList List of expenses
     * @return Whether to continue running the program
     * @throws Exception If command fails to run
     */
    public boolean commandMatching(HashMap<String, String> argumentsList, ExpenseList expenseList) 
            throws Exception {
        int index;
        float price;
        String description;
        String category;
        switch(argumentsList.get(Parser.ARGUMENT_COMMAND)) {
        case "bye":
            System.out.println("Bye. Hope to see you again soon!");
            return false;
        case "add":
            price = Float.parseFloat(argumentsList.get(Parser.ARGUMENT_PRICE));
            description = argumentsList.get(Parser.ARGUMENT_DESCRIPTION);
            category = argumentsList.get(Parser.ARGUMENT_CATEGORY);
            expenseList.addExpense(price, description, category);
            break;
        case "edit":
            index = Integer.parseInt(argumentsList.get(Parser.ARGUMENT_MAIN)) - 1;
            category = argumentsList.get(Parser.ARGUMENT_CATEGORY);
            price = Float.parseFloat(argumentsList.get(Parser.ARGUMENT_PRICE));
            description = argumentsList.get(Parser.ARGUMENT_DESCRIPTION);
            expenseList.editExpense(index, price, description, category);
            break;
        case "delete":
            index = Integer.parseInt(argumentsList.get(Parser.ARGUMENT_MAIN)) - 1;
            expenseList.deleteExpense(index);
            break;
        case "list":
            category = argumentsList.get(Parser.ARGUMENT_CATEGORY);
            ArrayList<Expense> expensesToDisplay;
            if (category == null) {
                expensesToDisplay = expenseList.getList();
            } else {
                expensesToDisplay = expenseList.listByCategory(category);
            }
            Ui.displayExpenseList(expensesToDisplay, expenseList);
            break;
        case "load":
            expenseList.loadFromCsv("./data.csv");
            break;
        case "save":
            expenseList.saveToCsv("./data.csv");
            break;
        case "help":
            Ui.displayHelp();
            break;
        default:
            System.out.println("No valid command given!");
            break;
        }
        return true;
    }
}
