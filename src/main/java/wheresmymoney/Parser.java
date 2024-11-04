package wheresmymoney;


import wheresmymoney.command.AddCommand;
import wheresmymoney.command.ByeCommand;
import wheresmymoney.command.Command;
import wheresmymoney.command.DeleteCommand;
import wheresmymoney.command.EditCommand;
import wheresmymoney.command.HelpCommand;
import wheresmymoney.command.ListCommand;
import wheresmymoney.command.StatsCommand;
import wheresmymoney.command.LoadCommand;
import wheresmymoney.command.SaveCommand;
import wheresmymoney.command.SetCommand;
import wheresmymoney.exception.InvalidInputException;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.HashMap;
import java.util.logging.Level;

public class Parser {
    public static final String ARGUMENT_COMMAND = "command";
    public static final String ARGUMENT_MAIN = "main";
    public static final String ARGUMENT_CATEGORY = "category";
    public static final String ARGUMENT_PRICE = "price";
    public static final String ARGUMENT_DESCRIPTION = "description";
    public static final String ARGUMENT_RECUR = "recur";
    public static final String ARGUMENT_DATE = "date";
    public static final String ARGUMENT_FREQUENCY = "frequency";
    public static final String ARGUMENT_FROM = "from";
    public static final String ARGUMENT_TO = "to";
    public static final String ARGUMENT_LIMIT = "limit";

    public static final String ARGUMENT_EXPENSE_LIST = "expenseList";
    public static final String ARGUMENT_CATEGORY_INFO = "categoryInfo";
    public static final String ARGUMENT_RECURRING_EXPENSE_LIST = "recurringExpenseList";

    /**
     * Gets command from words.
     *
     * @param words String list of arguments
     * @return command word
     */
    private static String getCommandFromWords(String[] words){
        // Command
        if (words.length == 0) {
            return "";
        }
        assert words.length > 0;
        return(words[0]);
    }

    /**
     * Packs command from words into an existing argument map.
     *
     * @param argumentsMap Arguments Mapping
     * @param words String list of arguments
     */
    private static void packCommandToExistingArgumentsMap(HashMap<String, String> argumentsMap, String[] words) {
        argumentsMap.put(Parser.ARGUMENT_COMMAND,getCommandFromWords(words));
    }

    private static void packArgumentToExistingArgumentsMap(HashMap<String, String> argumentsMap,
            String currArgumentName, String currArgument) throws InvalidInputException {
        if (argumentsMap.containsKey(currArgumentName)) {
            throw new InvalidInputException("Duplicate arguments or Invalid Arguments (eg. /command, /main");
        }
        argumentsMap.put(currArgumentName, currArgument.replace("\\/", "/").strip());
    }
    /**
     * Packs following arguments from words into an existing argument map.
     *
     * @param argumentsMap Arguments Mapping
     * @param words String list of arguments
     */
    private static void packFollowingArgumentsToExistingArgumentsMap(HashMap<String, String> argumentsMap,
                                                                     String[] words) throws InvalidInputException {
        // Arguments
        String currArgumentName = Parser.ARGUMENT_MAIN;
        StringBuilder currArgument = new StringBuilder();
        for (int i = 1; i < words.length; i++) {
            if (words[i].isEmpty()) { // Skip empty values/ duplicate spaces
                currArgument.append(" ");
                continue;
            }
            if (words[i].charAt(0) == '/') {
                // New argument
                packArgumentToExistingArgumentsMap(argumentsMap, currArgumentName, currArgument.toString());
                currArgumentName = words[i].replaceFirst("/", "");
                currArgument.setLength(0);
            } else {
                // Add on to existing argument
                currArgument.append(" ").append(words[i]);
            }
        }

        // Add last command
        packArgumentToExistingArgumentsMap(argumentsMap, currArgumentName, currArgument.toString());
    }

    /**
     * Packs words into a new argument map.
     *
     * @param words String list of arguments/words
     */
    private static HashMap<String, String> packWordsToArgumentsMap(String[] words) throws WheresMyMoneyException {
        HashMap<String, String> argumentsList = new HashMap<>();
        packFollowingArgumentsToExistingArgumentsMap(argumentsList, words);
        packCommandToExistingArgumentsMap(argumentsList, words);
        return argumentsList;
    }

    /**
     * Parses the given user input into command arguments.
     *
     * @param line Line that a user inputs
     * @return HashMap of Arguments, mapping the argument to its value given
     */
    public static HashMap<String, String> parseLineToArgumentsMap(String line) throws WheresMyMoneyException {
        Logging.log(Level.INFO, "Parsing Line: " + line);
        String[] words = line.trim().split(" ");
        return packWordsToArgumentsMap(words);
    }

    /**
     * Matches the argument list to a related command
     *
     * @param argumentsMap List of arguments
     * @return Command to run with the respective configurations
     * @throws WheresMyMoneyException If no valid command can be matched
     */
    public static Command commandMatching(HashMap<String, String> argumentsMap)
            throws WheresMyMoneyException {
        switch(argumentsMap.get(Parser.ARGUMENT_COMMAND)) {
        case "bye":
            return new ByeCommand(argumentsMap);
        case "add":
            return new AddCommand(argumentsMap);
        case "edit":
            return new EditCommand(argumentsMap);
        case "delete":
            return new DeleteCommand(argumentsMap);
        case "list":
            return new ListCommand(argumentsMap);
        case "stats":
            return new StatsCommand(argumentsMap);
        case "load":
            return new LoadCommand(argumentsMap);
        case "save":
            return new SaveCommand(argumentsMap);
        case "help":
            return new HelpCommand(argumentsMap);
        case "set":
            return new SetCommand(argumentsMap);
        default:
            throw new InvalidInputException("No valid command given!");
        }
    }

    /**
     * Parses a line to a related command
     *
     * @param line Line that a user inputs
     * @return Command to run with the respective configurations
     * @throws WheresMyMoneyException If no valid command can be matched
     */
    public static Command parseInputToCommand(String line) throws WheresMyMoneyException {
        return commandMatching(parseLineToArgumentsMap(line));
    }
}
