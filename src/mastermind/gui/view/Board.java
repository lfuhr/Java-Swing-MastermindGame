package mastermind.gui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import mastermind.model.ColorCode;
import mastermind.model.MastermindGame;
import mastermind.model.Rating;

/**
 * View for Mastermind.
 */
public final class Board extends JPanel {

    /**
     * This constant is designed to be set to personal preferences. Rating Slots
     * should adapt automatically
     */
    static final int SIZE_COLOR_SLOT = 40;

    /**
     * Margin around color codes and ratings.
     */
    private static final int MARGIN = 5;

    private static final long serialVersionUID = 1L;

    private final ColorCodePanel[] colorCodes =
            new ColorCodePanel[MastermindGame.MAX_MOVES];
    private final RatingPanel[] ratings =
            new RatingPanel[MastermindGame.MAX_MOVES];

    private final ColorCodePanel secret;

    /**
     * Constructs a game board with {@value MastermindGame.MAX_MOVES} rows plus
     * a field for the secret code.
     */
    public Board() {
        super(new GridBagLayout());
        setOpaque(false);

        // There only exist two instances of MousListener for a board
        SlotListener ratingSlotListener =
                new SlotListener(new RatingPanel.RatingPopup());
        SlotListener colorSlotListener =
                new SlotListener(new ColorCodePanel.ColorPopup());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);

        for (c.gridy = 0; c.gridy < MastermindGame.MAX_MOVES; c.gridy++) {

            // Add ColorCodePanel
            c.gridx = 0;
            colorCodes[c.gridy] =
                    new ColorCodePanel(colorSlotListener);
            add(colorCodes[c.gridy], c);

            // Add RatingPanel
            c.gridx++;
            ratings[c.gridy] = new RatingPanel(ratingSlotListener);
            add(ratings[c.gridy], c);
        }

        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;

        // Add Separator
        c.gridy++;
        add(new JSeparator(), c);

        // Add secret panel
        c.weightx = 1;
        c.gridy++;
        secret = new ColorCodePanel(colorSlotListener);
        add(secret, c);

    }

    /**
     * Disables all slots so that their value can't be changed by the user.
     */
    public void disableSlots() {

        for (int i = 0; i < colorCodes.length; i++) {
            colorCodes[i].setEnabled(false);
            ratings[i].setEnabled(false);
        }
        secret.setEnabled(false);
    }

    /**
     * Enables the specified color code so that its value can be changed by the
     * user.
     *
     * @param moveCount
     *            specified row
     */
    public void enableColorCode(int moveCount) {
        checkMoveCount(moveCount);
        colorCodes[moveCount].setEnabled(true);
    }

    /**
     * Enables the specified rating so that its value can be changed by the
     * user.
     *
     * @param moveCount
     *            specified row
     */
    public void enableRating(int moveCount) {
        checkMoveCount(moveCount);
        ratings[moveCount].setEnabled(true);
    }

    /**
     * Enables the secret color code to help the user remembering it.
     */
    public void enableSecret() {
        secret.setEnabled(true);
    }

    /**
     * Returns the color code that is currently displayed in the specified row.
     *
     * @param moveCount
     *            specified row
     * @return color code in specified row
     */
    public ColorCode getColorcode(int moveCount) {
        checkMoveCount(moveCount);
        return colorCodes[moveCount].getColorCode();
    }

    /**
     * Returns the rating that is currently displayed in the specified row.
     *
     * @param moveCount
     *            row to specify
     * @return rating in specified field
     */
    public Rating getRating(int moveCount) {
        checkMoveCount(moveCount);
        return ratings[moveCount].getRating();
    }

    /**
     * Checks if the displayed color code is valid, which means that every slot
     * has a spike.
     *
     * @param moveCount
     *            specified row
     * @return true if and only if color code is valid
     */
    public boolean isSetColorcode(int moveCount) {
        checkMoveCount(moveCount);
        return colorCodes[moveCount].isSetColorCode();
    }

    /**
     * Deletes all values of all slots.
     */
    public void resetSlots() {
        for (int i = 0; i < colorCodes.length; i++) {
            colorCodes[i].reset();
            ratings[i].reset();
        }
        secret.reset();
    }

    /**
     * Displays a given color code.
     *
     * @param moveCount
     *            row to display
     * @param colorCode
     *            color code to display
     */
    public void setColorCode(int moveCount, ColorCode colorCode) {
        checkMoveCount(moveCount);
        colorCodes[moveCount].setColorCode(colorCode);
    }

    /**
     * Displays a given rating.
     *
     * @param moveCount
     *            row to display
     * @param rating
     *            rating to display
     */
    public void setRating(int moveCount, Rating rating) {
        checkMoveCount(moveCount);
        ratings[moveCount].setRating(rating);
    }

    /**
     * Displays the secret if the user did not guess it.
     *
     * @param secret
     *            color code to display
     */
    public void setSecret(ColorCode secret) {
        this.secret.setColorCode(secret);
    }

    private void checkMoveCount(int moveCount) {
        if (moveCount < 0 || moveCount >= MastermindGame.MAX_MOVES) {
            throw new IllegalArgumentException("moveCount must be from 0 to "
                    + MastermindGame.MAX_MOVES + ". Is " + moveCount);
        }
    }
}
