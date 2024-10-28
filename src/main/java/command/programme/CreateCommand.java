package command.programme;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import command.CommandResult;
import programme.Day;
import programme.ProgrammeList;
import programme.Programme;
import history.History;

public class CreateCommand extends ProgrammeCommand {
    public static final String COMMAND_WORD = "create";
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final String name;
    private final ArrayList<Day> contents;

    public CreateCommand(String name, ArrayList<Day> contents) {
        this.name = name;
        this.contents = contents;
    }

    @Override
    public CommandResult execute(ProgrammeList programmes, History history){
        assert programmes != null : "Programme list must not be null";

        Programme created = programmes.insertProgramme(name, contents);
        assert created != null : "programme must be created";
        String result = String.format("New programme created: %n%s",created);

        logger.log(Level.INFO, "CreateCommand executed successfully.");

        return new CommandResult(result);
    }


    public String getName() {
        return name;
    }

}

