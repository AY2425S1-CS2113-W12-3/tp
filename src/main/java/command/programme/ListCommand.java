package command.programme;

import command.CommandResult;
import programme.ProgrammeList;
import history.History;

import java.util.logging.Level;

public class ListCommand extends ProgrammeCommand {
    public static final String COMMAND_WORD = "list";
    public static final String SUCCESS_MESSAGE_FORMAT = "Listing programmes: %n%s";

    @Override
    public CommandResult execute(ProgrammeList programmes, History history){
        assert programmes != null : "Programme list must not be null";

        String result = String.format(SUCCESS_MESSAGE_FORMAT, programmes);

        logger.log(Level.INFO, "ListCommand executed successfully.");

        return new CommandResult(result);
    }
}