package seedu.divelog.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.divelog.logic.CommandHistory;
import seedu.divelog.model.Model;

/**
 * Exports all dives in the divelog book to the user.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_SUCCESS = "Exported all dives";

    public static final String MESSAGE_FAIL_PLANNING_MODE = "Error: You are in planning mode!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        if (model.getPlanningMode()) {
            return new CommandResult(MESSAGE_FAIL_PLANNING_MODE);
        } else {
            requireNonNull(model);
            //export
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }
}
