// @@author TVageesan

package command.programme.edit;

import command.CommandResult;
import programme.Day;
import programme.Programme;
import programme.ProgrammeList;

import java.util.logging.Level;

/**
 * Command to delete a specific day from a programme.
 * <p>
 * This class encapsulates the functionality to remove a day from a
 * specified programme, identified by its index.
 * </p>
 */
public class DeleteDayCommand extends EditCommand {
    public static final String SUCCESS_MESSAGE_FORMAT = "Deleted day: %s%n";

    /**
     * Constructs a DeleteDayCommand with the specified programme index and day ID.
     *
     * @param programmeIndex the index of the programme from which the day will be deleted
     * @param dayId the ID of the day to be deleted from the programme
     */
    public DeleteDayCommand(int programmeIndex, int dayId) {
        super(programmeIndex, dayId);
    }

    /**
     * Executes the command to delete the specified day from the programme.
     *
     * @param programmes the ProgrammeList containing the programmes where the day will be deleted
     * @return a CommandResult containing a success message indicating the deleted day
     */
    public CommandResult execute(ProgrammeList programmes) {
        assert programmes != null : "programmes cannot be null";

        Programme selectedProgramme = programmes.getProgramme(programmeIndex);
        Day deletedDay = selectedProgramme.deleteDay(dayIndex);

        logger.log(Level.INFO, "DeleteDayCommand executed successfully.");

        String result = String.format(SUCCESS_MESSAGE_FORMAT, deletedDay);
        return new CommandResult(result);
    }
}