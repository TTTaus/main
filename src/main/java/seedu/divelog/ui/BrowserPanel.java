package seedu.divelog.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.divelog.commons.core.LogsCenter;
import seedu.divelog.commons.events.ui.DivePanelSelectionChangedEvent;
import seedu.divelog.model.dive.DiveSession;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String FORMAT_DIVE_LOCATION = "Dive @ %s";

    public static final String FORMAT_DIVE_DEPTH = "You dove to %.1fm";

    public static final String FORMAT_START_TIME = "Started at: %s";

    public static final String FORMAT_END_TIME = "Ended at: %s";

    public static final String FORMAT_SAFETY_STOP = "Safety stop at: %s";

    public static final String FORMAT_TIME_NOW = "Date and time now is: %s";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private Label diveLocation;
    @FXML
    private Label diveDepth;
    @FXML
    private Label pgStart;
    @FXML
    private Label pgEnd;
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;
    @FXML
    private Label safetyStop;
    @FXML
    private Label dateTime;

    public BrowserPanel() {
        super(FXML);
        loadMyTimeNow();
        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);
        registerAsAnEventHandler(this);
    }

    /**
     * Renders the dive specific information
     * @param dive
     */
    private void loadDivePage(DiveSession dive) {
        diveLocation.setText(String.format(FORMAT_DIVE_LOCATION, dive.getLocation().getLocationName()));
        diveDepth.setText(String.format(FORMAT_DIVE_DEPTH, dive.getDepthProfile().getDepth()));
        pgStart.setText(dive.getPressureGroupAtBeginning().getPressureGroup());
        pgEnd.setText(dive.getPressureGroupAtEnd().getPressureGroup());
        startTime.setText(String.format(FORMAT_START_TIME, dive.getStart().getTimeString()));
        endTime.setText(String.format(FORMAT_END_TIME, dive.getEnd().getTimeString()));
        safetyStop.setText(String.format(FORMAT_SAFETY_STOP, dive.getSafetyStop().getTimeString()));
        dateTime.setText(String.format(FORMAT_TIME_NOW, dateTimeSend()));
    }

    public void freeResources() {

    }
    private void loadMyTimeNow() {
        dateTime.setText(String.format(FORMAT_TIME_NOW, dateTimeSend()));
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(DivePanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadDivePage(event.getNewSelection());
    }
    /**
     * Renders the current date time information
     */
    private String dateTimeSend() {
        Date d = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY hh:mmaa");
        String dateTimeNow = dateFormat.format(d);
        return dateTimeNow;
    }
}
