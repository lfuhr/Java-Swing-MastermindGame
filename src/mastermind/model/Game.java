package mastermind.model;

/**
 * An implementation to the Mastermind game logic.
 */
public final class Game implements MastermindGame {

    private boolean machineIsGuessing;
    private byte moveCount;
    private ColorCode[] moves;
    private Rating[] ratings;
    private ColorCode secret;
    private PossibilityList posslist;

    /**
     * Constructs a game where the guesser is set as specified in the argument.
     *
     * @param machineIsGuessing
     *            true if and only if the machine shall be the guesser
     */
    public Game(boolean machineIsGuessing) {
        this.machineIsGuessing = machineIsGuessing;
        initialize();
    }

    private void initialize() {
        this.moves = new ColorCode[MAX_MOVES];
        this.ratings = new Rating[MAX_MOVES];
        moveCount = 0;
        if (machineIsGuessing) {
            posslist = new PossibilityList();
            secret = null;
        } else {
            secret = ColorCode.random();
        }
    }

    @Override
    public void switchGuesser() {
        machineIsGuessing = !machineIsGuessing;
        initialize();
    }

    @Override
    public boolean isMachineGuessing() {
        return machineIsGuessing;
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }

    @Override
    public ColorCode getGameState(int moveNo) {
        if (moveNo > moveCount && moveNo > 0) {
            throw new IllegalArgumentException(
                    "The specified move number is not valid.");
        } else {
            return moves[moveNo];
        }
    }

    @Override
    public Rating getRating(int moveNo) {
        if (moveNo > moveCount) {
            throw new IllegalArgumentException(
                    "The specified move number is not valid.");
        } else {
            return ratings[lastMove()];
        }
    }

    @Override
    public ColorCode getSecret() {
        if (machineIsGuessing) {
            throw new IllegalStateException(
                    "Computer doesn't know the secret since it is guesser.");
        } else if (!gameIsOver()) {
            throw new IllegalStateException(
                    "Game is not over. So the secret is still secret.");
        } else {
            return secret;
        }
    }

    @Override
    public void humanMove(ColorCode move) {
        if (machineIsGuessing) {
            throw new IllegalStateException(
                    "Computer is guesser, so moves are made automatically.");
        } else {
            move(move);
            evaluateLastMove();
        }
    }

    @Override
    public ColorCode machineMove() {
        if (!machineIsGuessing) {
            throw new IllegalStateException(
                    "Human is guesser, so the machine doesn't make moves.");
        } else {
            ColorCode guess = null;
            for (int i = 0; i < PossibilityList.getTotalNoCombis(); i++) {
                if (posslist.combiIsPossible(i)) {
                    guess = PossibilityList.getColorCode(i);
                    if (!gameIsOver()) {
                        move(guess);
                    }
                    // else the controller is just assuring that human was not
                    // cheating
                    break;
                }
            }
            return guess;
        }
    }

    @Override
    public void processEval(ColorCode move, Rating rating) {
        if (move != moves[lastMove()]) {
            throw new IllegalArgumentException(
                    "Only last move can get evaluated.");
        }
        ratings[lastMove()] = rating;
        for (int i = 0; i < PossibilityList.getTotalNoCombis(); i++) {
            if (posslist.combiIsPossible(i)) {
                ColorCode it = PossibilityList.getColorCode(i);
                if (!it.evaluate(move).equals(rating)) {
                    posslist.remove(i);
                }
            }
        }

    }

    private boolean gameIsOver() {
        boolean full = moveCount == MAX_MOVES;
        boolean won = moveCount != 0
                && ratings[lastMove()].getBlack() == NUMBER_SLOTS;
        return full || won;
    }

    private void evaluateLastMove() {
        ratings[lastMove()] = secret.evaluate(moves[lastMove()]);
    }

    private byte lastMove() {
        return (byte) (moveCount - 1);
    }

    private void move(ColorCode move) {
        if (gameIsOver()) {
            throw new IllegalStateException("The game is over.");
        } else {
            moves[moveCount] = move;
            moveCount++;
        }
    }
}
