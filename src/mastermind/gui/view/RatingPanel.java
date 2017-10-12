package mastermind.gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import mastermind.model.MastermindGame;
import mastermind.model.Rating;

/**
 * Implementation of a rating in the View.
 */
class RatingPanel extends JPanel {

    /**
     * Popup menu which includes the functionality to change the value
     * graphically.
     */
    static class RatingPopup extends JPopupMenu {

        private static final long serialVersionUID = 1L;

        /**
         * Constructs a Popup (since one object is sufficient, this constructor
         * must only be called once.
         */
        RatingPopup() {

            for (RatingSlot.Value value : RatingSlot.Value.values()) {
                JMenuItem item = new JMenuItem();

                RatingSlot.Value constantValue = value;
                item.addActionListener(e -> {
                    JPopupMenu popupMenu = (JPopupMenu) item.getParent();
                    RatingSlot slot = (RatingSlot) popupMenu.getInvoker();
                    slot.setValue(constantValue);
                });

                item.setBackground(value.color);
                add(item);
            }
        }
    }

    /**
     * Implementation of a single rating slot.
     */
    private static final class RatingSlot extends Slot {

        /**
         * Representation of states a slot can have.
         */
        private static enum Value {

            BLACK(Color.BLACK), EMPTY(Slot.EMPTY_COLOR), WHITE(Color.WHITE);

            private final Color color;

            private Value(Color color) {
                this.color = color;
            }
        }

        private static final long serialVersionUID = 1L;

        private Value value = Value.EMPTY;

        private RatingSlot(int diameter) {
            setPreferredSize(new Dimension(diameter, diameter));
        }

        private RatingSlot(int diameter, MouseListener listener) {
            this(diameter);
            addMouseListener(listener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        Color getColor() {
            return value.color;
        }

        private void setValue(Value value) {
            this.value = value;
            repaint();
        }
    }

    private static final int MARGIN = 1;
    private static final int NO_ROWS = 2;
    private static final int DIAMETER = Board.SIZE_COLOR_SLOT / NO_ROWS
            - MARGIN;

    private static final long serialVersionUID = 1L;

    private final RatingSlot[] slots =
            new RatingSlot[MastermindGame.NUMBER_SLOTS];

    /**
     * Constructs an object containing slots.
     *
     * @param listener
     *            a global listener for all rating slots
     */
    RatingPanel(MouseListener listener) {
        setOpaque(false);

        GridBagLayout l = new GridBagLayout();
        setLayout(l);

        for (int i = 0; i < MastermindGame.NUMBER_SLOTS; i++) {

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = i / NO_ROWS;
            c.gridy = i % NO_ROWS;
            c.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);

            RatingSlot slot = new RatingSlot(DIAMETER, listener);

            add(slot, c);

            slots[i] = slot;
        }
    }

    /**
     * Returns a {@code Rating} object equivalent to its graphical presentation.
     *
     * @return current rating
     */
    Rating getRating() {
        byte black = 0;
        byte white = 0;

        for (RatingSlot slot : slots) {
            if (slot.value == RatingSlot.Value.BLACK) {
                black++;
            } else if (slot.value == RatingSlot.Value.WHITE) {
                white++;
            }
        }
        return new Rating(black, white);
    }

    /**
     * Removes the spike from the slot.
     */
    void reset() {
        for (RatingSlot slot : slots) {
            slot.setValue(RatingSlot.Value.EMPTY);
        }
    }

    /**
     * Sets displays a representation of a rating.
     *
     * @param rating
     *            the rating to display
     */
    void setRating(Rating rating) {
        byte black = rating.getBlack();
        byte white = rating.getWhite();

        for (int i = 0; i < black; i++) {
            slots[i].setValue(RatingSlot.Value.BLACK);
        }
        for (int i = black; i < black + white; i++) {
            slots[i].setValue(RatingSlot.Value.WHITE);
        }
    }

}
