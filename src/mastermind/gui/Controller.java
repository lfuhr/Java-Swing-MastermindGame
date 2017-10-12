package mastermind.gui;

import java.awt.event.ActionListener;
import java.util.function.Consumer;

import mastermind.gui.view.Board;
import mastermind.model.ColorCode;
import mastermind.model.Game;
import mastermind.model.MastermindGame;
import mastermind.model.Rating;

/**
 * Is the interface between model and View.
 */
public final class Controller {

    private final Board board;
    private MastermindGame game;
    private boolean gameIsOver;
    private final Consumer<String> messagePrinter;
    private final ActionListener moveListener;
    private final ActionListener newGameListener;
    private final ActionListener switchListener;

    /**
     * Constructs itself, a game and the action listener.
     *
     * @param board
     *            the view of the game
     * @param messagePrinter
     *            a function, that gives the user hints.
     */
    Controller(Board board, Consumer<String> messagePrinter) {
        this.board = board;
        this.messagePrinter = messagePrinter;

        newGame(false); // Human is guessing
        newGameListener = e -> newGame(game.isMachineGuessing());
        switchListener = e -> newGame(!game.isMachineGuessing());
        moveListener = e -> {

            if (gameIsOver) {
                message("You have to start a new game.");
            } else if (game.isMachineGuessing()) {
                board.disableSlots();
                if (game.getMoveCount() != 0) {
                    processRatingFromHuman();
                }
                if (!gameIsOver) {
                    doMachineMove();
                }
            } else {
                if (!board.isSetColorcode(game.getMoveCount())) {
                    message("Please guess a ColorCode first!");
                } else {
                    handleHumanMove();
                }
            }
        };
    }

    /**
     * Returns a listener that carries out moves on both the model and the view.
     *
     * @return listener
     */
    ActionListener getMoveListener() {
        return moveListener;
    }

    /**
     * Returns a listener that causes the game to restart.
     *
     * @return listener
     */
    ActionListener getNewGameListener() {
        return newGameListener;
    }

    /**
     * Returns a listener that causes a change of the mode.
     *
     * @return listener
     */
    ActionListener getSwitchListener() {
        return switchListener;
    }

    private void doMachineMove() {
        int moveNo = game.getMoveCount();
        ColorCode machineMove = game.machineMove();

        if (machineMove == null) {
            message("You have been cheating!");
        } else if (moveNo == MastermindGame.MAX_MOVES) {
            message("I couldn't find solution.");
        } else {
            // Give the computer another try
            board.setColorCode(moveNo, machineMove);
            board.enableRating(moveNo);
            message("Please rate my move.");
        }
    }

    private void handleHumanMove() {
        int moveNo = game.getMoveCount();

        board.disableSlots();
        game.humanMove(board.getColorcode(moveNo));

        // Get rating using the old move number
        Rating rating = game.getRating(moveNo);
        board.setRating(moveNo, rating);

        // Update move number
        moveNo = game.getMoveCount();

        if (rating.isAllBlack()) {
            message("Congratulations! You needed " + moveNo
                    + " move" + (moveNo == 1 ? "" : "s") + ".");
            gameIsOver = true;
        } else if (moveNo == MastermindGame.MAX_MOVES) {
            board.setSecret(game.getSecret());
            message("No more moves! See solution below!");
            gameIsOver = true;
        } else {
            // Give human another try
            board.enableColorCode(moveNo);
            message("It's your turn!");
        }
    }

    private void message(String message) {
        messagePrinter.accept(message);
    }

    private void newGame(boolean machineIsGuessing) {
        game = new Game(machineIsGuessing);
        board.disableSlots();
        board.resetSlots();

        if (machineIsGuessing) {
            board.enableSecret();
            message("You can now note your Code below.");
        } else {
            board.enableColorCode(0);
            message("Choose your first guess!");
        }
        gameIsOver = false;
    }

    private void processRatingFromHuman() {
        Rating rating = board.getRating(game.getMoveCount() - 1);
        game.processEval(game.getGameState(game.getMoveCount() - 1),
                rating);
        if (rating.isAllBlack()) {
            message("Wow! I did it!");
            gameIsOver = true;
        }
    }
}