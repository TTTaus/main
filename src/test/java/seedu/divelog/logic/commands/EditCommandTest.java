package seedu.divelog.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.divelog.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.divelog.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.divelog.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.divelog.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.divelog.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.divelog.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.divelog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.divelog.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.divelog.testutil.TypicalIndexes.INDEX_FIRST_DIVE;
import static seedu.divelog.testutil.TypicalIndexes.INDEX_SECOND_DIVE;
import static seedu.divelog.testutil.TypicalDiveSessions.getTypicalAddressBook;

import org.junit.Test;

import seedu.divelog.commons.core.Messages;
import seedu.divelog.commons.core.index.Index;
import seedu.divelog.logic.CommandHistory;
import seedu.divelog.logic.commands.EditCommand.EditDiveDescriptor;
import seedu.divelog.model.DiveLog;
import seedu.divelog.model.Model;
import seedu.divelog.model.ModelManager;
import seedu.divelog.model.UserPrefs;
import seedu.divelog.model.person.Person;
import seedu.divelog.testutil.EditDiveDescriptorBuilder;
import seedu.divelog.testutil.DiveSessionBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new DiveSessionBuilder().build();
        EditDiveDescriptor descriptor = new EditDiveDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_DIVE, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new DiveLog(model.getDiveLog()), new UserPrefs());
        try {
            expectedModel.updateDiveSession(model.getFilteredDiveList().get(0), editedPerson);
        } catch (seedu.divelog.model.dive.exceptions.DiveNotFoundException e) {
            e.printStackTrace();
        }
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredDiveList().size());
        Person lastPerson = model.getFilteredDiveList().get(indexLastPerson.getZeroBased());

        DiveSessionBuilder personInList = new DiveSessionBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditDiveDescriptor descriptor = new EditDiveDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new DiveLog(model.getDiveLog()), new UserPrefs());
        try {
            expectedModel.updateDiveSession(lastPerson, editedPerson);
        } catch (seedu.divelog.model.dive.exceptions.DiveNotFoundException e) {
            e.printStackTrace();
        }
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_DIVE, new EditDiveDescriptor());
        Person editedPerson = model.getFilteredDiveList().get(INDEX_FIRST_DIVE.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new DiveLog(model.getDiveLog()), new UserPrefs());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_DIVE);

        Person personInFilteredList = model.getFilteredDiveList().get(INDEX_FIRST_DIVE.getZeroBased());
        Person editedPerson = new DiveSessionBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_DIVE,
                new EditDiveDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new DiveLog(model.getDiveLog()), new UserPrefs());
        try {
            expectedModel.updateDiveSession(model.getFilteredDiveList().get(0), editedPerson);
        } catch (seedu.divelog.model.dive.exceptions.DiveNotFoundException e) {
            e.printStackTrace();
        }
        expectedModel.commitAddressBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredDiveList().get(INDEX_FIRST_DIVE.getZeroBased());
        EditDiveDescriptor descriptor = new EditDiveDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_DIVE, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_DIVE);

        // edit person in filtered list into a duplicate in divelog book
        Person personInList = model.getDiveLog().getPersonList().get(INDEX_SECOND_DIVE.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_DIVE,
                new EditDiveDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredDiveList().size() + 1);
        EditDiveDescriptor descriptor = new EditDiveDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of divelog book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_DIVE);
        Index outOfBoundIndex = INDEX_SECOND_DIVE;
        // ensures that outOfBoundIndex is still in bounds of divelog book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getDiveLog().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditDiveDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Person editedPerson = new DiveSessionBuilder().build();
        Person personToEdit = model.getFilteredDiveList().get(INDEX_FIRST_DIVE.getZeroBased());
        EditDiveDescriptor descriptor = new EditDiveDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_DIVE, descriptor);
        Model expectedModel = new ModelManager(new DiveLog(model.getDiveLog()), new UserPrefs());
        expectedModel.updateDiveSession(personToEdit, editedPerson);
        expectedModel.commitAddressBook();

        // edit -> first person edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first person edited again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredDiveList().size() + 1);
        EditDiveDescriptor descriptor = new EditDiveDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> divelog book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single divelog book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Person} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited person in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the person object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        Person editedPerson = new DiveSessionBuilder().build();
        EditDiveDescriptor descriptor = new EditDiveDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_DIVE, descriptor);
        Model expectedModel = new ModelManager(new DiveLog(model.getDiveLog()), new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_DIVE);
        Person personToEdit = model.getFilteredDiveList().get(INDEX_FIRST_DIVE.getZeroBased());
        expectedModel.updateDiveSession(personToEdit, editedPerson);
        expectedModel.commitAddressBook();

        // edit -> edits second person in unfiltered person list / first person in filtered person list
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredDiveList().get(INDEX_FIRST_DIVE.getZeroBased()), personToEdit);
        // redo -> edits same second person in unfiltered person list
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_DIVE, DESC_AMY);

        // same values -> returns true
        EditDiveDescriptor copyDescriptor = new EditDiveDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_DIVE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_DIVE, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_DIVE, DESC_BOB)));
    }

}
