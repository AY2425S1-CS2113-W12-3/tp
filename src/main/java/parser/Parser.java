package parser;

import command.Command;
import parser.command.factory.CommandFactory;

import java.util.logging.Logger;
import java.util.logging.Level;

import static parser.ParserUtils.splitArguments;

/*
  Parser acts as an interface layer between CommandFactory and BuffBuddy
  Takes in String from Ui and returns a Command generated by CommandFactory
 */

public class Parser {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final CommandFactory commandFactory;

    public Parser() {
        this.commandFactory = new CommandFactory();
    }

    public Command parse(String fullCommand) {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new IllegalArgumentException("Command cannot be empty. Please enter a valid command.");
        }

        String[] inputArguments = splitArguments(fullCommand);
        String commandString = inputArguments[0];
        String argumentString = inputArguments[1];

        logger.log(Level.INFO, "Parsed command: {0}, with arguments: {1}",
                new Object[]{commandString, argumentString});

        return commandFactory.createCommand(commandString, argumentString);
    }
}
