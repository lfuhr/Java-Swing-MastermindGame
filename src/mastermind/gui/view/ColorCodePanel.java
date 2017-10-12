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

import mastermind.model.ColorCode;
import mastermind.model.MastermindGame;

/**
 * This class is the representation of a color code in the GUI.
 */
final class ColorCodePanel extends JPanel {

    /**
     * Popup menu which includes the functionality to change the value
     * graphically.
     */
    static final class ColorPopup extends JPopupMenu {

        private static final long serialVersionUID = 1L;

        /**
         * Constructs a popup (since one object is sufficient, this constructor
         * must only be called once.
         */
        ColorPopup() {
            for (int i = 0; i < MastermindGame.NUMBER_COLORS; i++) {
                JMenuItem item = new JMenuItem();

                final int value = i;
                item.addActionListener(e -> {
                    ColorPopup popupMenu = (ColorPopup) item.getParent();
                    ColorSlot slot = (ColorSlot) popupMenu.getInvoker();

                    slot.setValue(value);
                });

                item.setBackground(ColorSlot.AVAILIBLE_COLORS[i]);
                add(item);
            }
        }
    }

    /**
     * Implementation of a single color code slot.
     */
    private static final class ColorSlot extends Slot {

        private static final Color[] AVAILIBLE_COLORS = assignColors();
        private static final int EMPTY = -1;
        private static final long serialVersionUID = 1L;

        private int value = EMPTY;

        /**
         * Assigns Colors. Place for a possible color-scheme-algorithm.
         *
         * @return possible colors
         */
        private static Color[] assignColors() {
            Color[] colors = new Color[] {
                    Color.BLUE,
                    Color.YELLOW,
                    new Color(205, 20, 20), // red
                    Color.green.darker(),
                    new Color(255, 150, 30), // orange
                    Color.magenta.darker(), };
            if (colors.length < MastermindGame.NUMBER_COLORS) {
                throw new RuntimeException("Not enough colors defined");
            }
            return colors;
        }

        private ColorSlot(int diameter) {
            setPreferredSize(new Dimension(diameter, diameter));
        }

        private ColorSlot(int diameter, MouseListener listener) {
            this(diameter);
            addMouseListener(listener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        Color getColor() {
            return value == EMPTY ? Slot.EMPTY_COLOR
                    : AVAILIBLE_COLORS[value];
        }

        private void setValue(int value) {
            this.value = value;
            repaint();
        }
    }

    private static final int MARGIN = 2;

    private static final long serialVersionUID = 1L;

    private final ColorSlot[] slots =
            new ColorSlot[MastermindGame.NUMBER_SLOTS];

    /**
     * Constructs an object containing slots.
     *
     * @param listener
     *            a global listener for all color slots
     */
    ColorCodePanel(MouseListener listener) {
        super(new GridBagLayout());
        setOpaque(false);

        for (int column = 0; column < MastermindGame.NUMBER_SLOTS; column++) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = column;
            c.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
            ColorSlot slot =
                    new ColorSlot(Board.SIZE_COLOR_SLOT, listener);
            slots[column] = slot;
            add(slot, c);
        }
    }

    /**
     * Returns a {@code ColorCode} object equivalent to its graphical
     * presentation.
     *
     * @return current color code
     */
    ColorCode getColorCode() {
        ColorCode colorCode = new ColorCode();
        for (int i = 0; i < slots.length; i++) {
            colorCode.set(i, (byte) slots[i].value);
        }
        return colorCode;
    }

    /**
     * Checks if all slots contain a spike.
     *
     * @return true if and only if all slots contain a spike.
     */
    boolean isSetColorCode() {
        boolean result = true;
        for (ColorSlot slot : slots) {
            result = result && slot.value != ColorSlot.EMPTY;
        }
        return result;
    }

    /**
     * Removes the spike from the slot.
     */
    void reset() {
        for (ColorSlot slot : slots) {
            slot.setValue(ColorSlot.EMPTY);
        }
    }

    /**
     * Sets displays a representation of a color code.
     *
     * @param colorCode
     *            the color code to display
     */
    void setColorCode(ColorCode colorCode) {
        for (int i = 0; i < slots.length; i++) {
            slots[i].setValue(colorCode.get(i));
        }
    }
}