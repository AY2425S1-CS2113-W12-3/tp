package wheresmymoney;
import java.util.HashMap;

public class Parser{
    public static final String ARGUMENT_COMMAND = "command";
    public static final String ARGUMENT_MAIN = "main";
    public static final String ARGUMENT_CATEGORY = "category";
    public static final String ARGUMENT_PRICE = "price";
    public static final String ARGUMENT_DESCRIPTION = "description";

    /**
     * Gets command from words.
     * @param words String list of arguments
     * @return command word
     */
    private String getCommandFromWords(String[] words){
        // Command
        if (words.length == 0) {
            return "";
        }
        return(words[0]);
    }

    /**
     * Packs command from words into an existing argument map.
     * @param argumentsMap Arguments Mapping
     * @param words String list of arguments
     */
    private void packCommandToExistingArgumentsMap(HashMap<String, String> argumentsMap, String[] words) {
        argumentsMap.put(Parser.ARGUMENT_COMMAND,getCommandFromWords(words));
    }

    /**
     * Packs following arguments from words into an existing argument map.
     * @param argumentsMap Arguments Mapping
     * @param words String list of arguments
     */
    private void packFollowingArgumentsToExistingArgumentsMap(HashMap<String, String> argumentsMap, String[] words) {
        // Arguments
        String currArgumentName = Parser.ARGUMENT_MAIN;
        StringBuilder currArgument = new StringBuilder();
        for (int i = 1; i < words.length; i++) {
            if (words[i].isEmpty()) { // Should be redundant but just in case
                continue;
            }
            if (words[i].charAt(0) == '/') {
                // New argument
                if (!currArgument.toString().isEmpty()){
                    argumentsMap.put(currArgumentName, currArgument.toString().strip());
                }
                currArgumentName = words[i];
                currArgument.setLength(0);
            } else {
                // Add on to existing argument
                currArgument.append(" ").append(words[i]);
            }
        }

        // Add last command
        if (!currArgument.toString().isEmpty()) {
            argumentsMap.put(currArgumentName, currArgument.toString().strip());
        }
    }

    /**
     * Packs words into a new argument map.
     * @param words String list of arguments/words
     */
    private HashMap<String, String> packWordsToArgumentsMap(String[] words) {
        HashMap<String, String> argumentsList = new HashMap<>();
        packCommandToExistingArgumentsMap(argumentsList, words);
        packFollowingArgumentsToExistingArgumentsMap(argumentsList, words);
        return argumentsList;
    }

    /**
     * Parses the given user input into command arguments.
     * @param line Line that a user inputs
     * @return HashMap of Arguments, mapping the argument to its value given
     */
    public HashMap<String, String> parseCommandToArgumentsMap(String line) {
        String[] words = line.split(" ");
        return packWordsToArgumentsMap(words);
    }

    /**
     * Matches the argument list to a related command and runs said command
     * @param argumentsMap List of arguments
     * @param expenseList List of expenses
     * @return Whether to continue running the program
     * @throws Exception If command fails to run
     */
    public boolean commandMatching(HashMap<String, String> argumentsMap, ExpenseList expenseList)
            throws Exception {
        int index;
        float price;
        String description;
        String category;
        switch(argumentsMap.get(Parser.ARGUMENT_COMMAND)){
        case "bye":
            System.out.println("Bye. Hope to see you again soon!");
            return false;
        case "add":
            price = Float.parseFloat(argumentsMap.get(Parser.ARGUMENT_PRICE));
            description = argumentsMap.get(Parser.ARGUMENT_DESCRIPTION);
            category = argumentsMap.get(Parser.ARGUMENT_CATEGORY);
            expenseList.addExpense(price, description, category);
            break;
        case "edit":
            index = Integer.parseInt(argumentsMap.get(Parser.ARGUMENT_MAIN)) - 1;
            category = argumentsMap.get(Parser.ARGUMENT_CATEGORY);
            price = Float.parseFloat(argumentsMap.get(Parser.ARGUMENT_PRICE));
            description = argumentsMap.get(Parser.ARGUMENT_DESCRIPTION);
            expenseList.editExpense(index, category, price, description);
            break;
        case "delete":
            index = Integer.parseInt(argumentsMap.get(Parser.ARGUMENT_MAIN)) - 1;
            expenseList.deleteExpense(index);
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
