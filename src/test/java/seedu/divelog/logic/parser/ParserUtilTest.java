package seedu.divelog.logic.parser;

import static org.junit.Assert.assertEquals;

import static seedu.divelog.logic.parser.ParserUtil.MESSAGE_INVALID_DEPTH;
import static seedu.divelog.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.divelog.testutil.TypicalIndexes.INDEX_FIRST_DIVE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.divelog.logic.parser.exceptions.ParseException;
import seedu.divelog.model.dive.DepthProfile;


public class ParserUtilTest {
    private static final String WHITESPACE = " \t\r\n";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseIndex_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseIndex("10 a");
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_DIVE, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_DIVE, ParserUtil.parseIndex("  1  "));
    }

    //@@author arjo129
    @Test
    public void parseDepth_validInput_success() throws Exception {
        //No white spaces
        assertEquals(new DepthProfile(1.0f), ParserUtil.parseDepth("1.0"));
        //Trailing whitespaces
        assertEquals(new DepthProfile(1.0f), ParserUtil.parseDepth(" 1.0  "));
    }

    //@@author arjo129
    @Test
    public void parseDepth_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_INVALID_DEPTH);
        ParserUtil.parseDepth("otototo MAMAMAMA");
    }

    //@@author arjo129
    @Test
    public void parseDepth_negativeInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_INVALID_DEPTH);
        ParserUtil.parseDepth("-1");
    }

    //@@author arjo129
    @Test
    public void parseDepth_zeroInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_INVALID_DEPTH);
        ParserUtil.parseDepth("0");
    }

    //    @Test
    //    public void checkTimeformat_Test() {
    //
    //    }

}
