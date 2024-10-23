package wheresmymoney;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Ui {
    public Parser parser;
    public Ui(){
        parser = new Parser();
    }

    /**
     * Displays an Introductory message to the user
     */
    public static void displayIntroText() {
        System.out.println("WheresMyMoney");
        System.out.println("What can I do for you? If this is your first time " +
                "using this programme and you would like a quick guide, use " +
                "the \"guide\" command");
    }

    /**
     * Displays list expenses as requested by user
     */
    public static void displayExpenseList(ArrayList<Expense> expensesToPrint, ExpenseList expenseList) {
        for (Expense expense: expensesToPrint) {
            String index = expenseList.getIndexOf(expense) + 1 + ". ";
            String category = "CATEGORY: " + expense.category;
            String description = "   DESCRIPTION: " + expense.description;
            String price = "   PRICE: " + expense.price;
            System.out.println(index + category + description + price);
        }
    }

    /**
     * Displays list of all commands and its format and usage
     */
    public static void displayHelp() {
        System.out.println("Here are the list of commands");
        System.out.println("Take note that any words in SCREAMING_SNAKE_CASE is a parameter");
        System.out.println();

        System.out.println("Use the add command to add an expense");
        System.out.println("Format:  add [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]");
        System.out.println("Notes:");
        System.out.println("    - Price is a decimal number");
        System.out.println("    - Description and Category are Text");
        System.out.println("Examples: add /price 4.50 /description chicken rice /category food");
        System.out.println();

        System.out.println("Use the edit command to edit an expense");
        System.out.println("Format: edit INDEX [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]");
        System.out.println("Notes:");
        System.out.println("    - Price is a decimal number");
        System.out.println("    - Description and Category are Text");
        System.out.println("    - All parameters are optional and only the parameters that are" + 
                "inputted will be reflected after the edit");
        System.out.println("Examples: edit 1 /price 5.50 /description chicken rice /category food");
        System.out.println();

        System.out.println("Use the delete command to delete an expense");
        System.out.println("Format:  delete [INDEX]");
        System.out.println("Examples: delete 2");
        System.out.println();

        System.out.println("Use the list command to display expenses and gives the sum of all expenses listed");
        System.out.println("Format:  list [/category CATEGORY]");
        System.out.println("Notes:");
        System.out.println("    - Category is text");
        System.out.println("    - Lists all expenses the user has if the category is not specified");
        System.out.println("    - Lists all expenses with that category if specified");
        System.out.println("Examples: list /category food");
        System.out.println();
    }

    /**
     * Displays a quick guide for users to user the programme
     */
    public static void displayGuide() {
        System.out.println("Use the add command to add an expense");
        System.out.println("Format:  add [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]");
        System.out.println("Examples: add /price 4.50 /description chicken rice /category food");
        System.out.println();

        System.out.println("Use the edit command to edit an expense");
        System.out.println("Format: edit INDEX [/price PRICE] [/description DESCRIPTION] [/category CATEGORY]");
        System.out.println("Examples: edit 1 /price 5.50 /description chicken rice /category food");
        System.out.println();

        System.out.println("Use the delete command to delete an expense");
        System.out.println("Format:  delete [INDEX]");
        System.out.println("Examples: delete 2");
        System.out.println();

        System.out.println("Use the list command to display expenses and gives the sum of all expenses listed");
        System.out.println("Examples: list /category food");
        System.out.println();
    }

    /**
     * Runs a loop where the user can enter commands, until they exit
     *
     * @param expenseList Object representing the list of expenses
     */
    public void commandEntryLoop(ExpenseList expenseList) {
        // Command Entry
        String line;
        HashMap<String, String> argumentsList;
        Scanner scanner = new Scanner(System.in);
        boolean isAskingInput = true;
        while (isAskingInput){
            System.out.print("> ");
            line = scanner.nextLine();
            argumentsList = parser.parseCommandToArguments(line);
            try {
                isAskingInput = parser.commandMatching(argumentsList, expenseList);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }

    /**
     * Runs the entire program
     *
     * @param expenseList Object representing the list of expenses
     */
    public void run(ExpenseList expenseList){
        displayIntroText();
        commandEntryLoop(expenseList);
    }
}
