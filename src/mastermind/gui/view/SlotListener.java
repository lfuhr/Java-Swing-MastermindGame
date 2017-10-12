package mastermind.gui.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

/**
 * A {@code MouseListener} that triggers a popup menu depending on the current
 * look and feel.
 */
class SlotListener extends MouseAdapter {

    private final JPopupMenu menu;

    /**
     * Constructs a {@code MouseListener} that triggers the given
     * {@code JPopupMenu}.
     *
     * @param menu
     *            menu to trigger
     */
    SlotListener(JPopupMenu menu) {
        this.menu = menu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (e.getComponent().getParent().isEnabled()) {
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}