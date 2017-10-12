package mastermind.gui;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mastermind.gui.view.Board;

/**
 * A GUI for the Mastermind game implemented in "Aufgabe 1". It uses an
 * implementation of the interface {@code MastermindGame} as the model, the
 * Class {@code Board} as the view and the Class "Controller" as controller. In
 * this implementation the controller is completely independent from the view,
 * so the user interface (Swing) can be exchanged without changing even one line of
 * the controller. Because the developer decided against adding public methods
 * to the model (in order to implement the observer pattern of the classic MVC),
 * an association between the model and the view is no longer needed. So to
 * reduce the coupling, there is no such association.
 */
public final class MainPanel extends JPanel {

    private static final Color BG_COLOR = Color.ORANGE.darker();
    private static final long serialVersionUID = 1L;

    private final Board board;
    private final JLabel instruction = new JLabel();
    private final JButton moveButton = new JButton("Move");
    private final JButton newButton = new JButton("New");
    private final JButton swichButton = new JButton("Switch");

    /**
     * Delegates the construction of the application.
     *
     * @param args
     *            not used
     */
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mastermind");
            MainPanel mainPanel = new MainPanel();
            frame.setContentPane(mainPanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }

    private MainPanel() {

        // View on the model
        board = new Board();

        // Controller (creates model internally)
        Controller controller = new Controller(board,
                message -> instruction.setText(message));

        newButton.addActionListener(controller.getNewGameListener());
        moveButton.addActionListener(controller.getMoveListener());
        swichButton.addActionListener(controller.getSwitchListener());

        setBackground(BG_COLOR);
        addLayoutedContent();
    }

    private void addLayoutedContent() {
        GroupLayout l = new GroupLayout(this);
        setLayout(l);

        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

        l.setHorizontalGroup(l
                .createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(instruction)
                .addComponent(board)
                .addGroup(
                        l.createSequentialGroup().addComponent(newButton)
                        .addComponent(moveButton)
                        .addComponent(swichButton)));

        l.setVerticalGroup(l
                .createSequentialGroup()
                .addComponent(instruction)
                .addComponent(board)
                .addGroup(
                        l.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(newButton)
                        .addComponent(moveButton)
                        .addComponent(swichButton)));
    }
}
