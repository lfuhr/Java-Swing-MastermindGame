package mastermind.gui.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Defines the display of slots containing spikes.
 */
abstract class Slot extends JPanel {

    /**
     * Color for an empty slot.
     */
    static final Color EMPTY_COLOR = Color.ORANGE.darker().darker();

    private static final long serialVersionUID = 1L;

    /**
     * Makes the panel transparent.
     */
    Slot() {
        setOpaque(false);
    }

    /**
     * Returns a color to display.
     *
     * @return null if no spike is set, the spike color otherwise
     */
    abstract Color getColor();

    /**
     * Draws a filled circle depending on the panel size and the color defined
     * by {@code getColor()}.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int diameter = Math.min(getHeight(), getWidth());
        g2d.setColor(getColor());
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillOval(0, 0, diameter, diameter);
    }
}
