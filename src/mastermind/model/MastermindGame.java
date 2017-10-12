package mastermind.model;

/**
 * Interface of the Mastermind game logic.
 */
public interface MastermindGame {

    /**
     * Maximum number of moves.
     */
    byte MAX_MOVES = 7;

    /**
     * Number of colors.
     */
    byte NUMBER_COLORS = 6;

    /**
     * Number of slots for the colors.
     */
    byte NUMBER_SLOTS = 4;

    /**
     * Switches between human and machine guessing. This state should be saved
     * over multiple games. The constructor must deliver an appropriate game
     * object. Default: human plays.
     */
    void switchGuesser();

    /**
     * Determines who is guessing at the moment.
     *
     * @return {@code true}, if machine is guessing, {@code false} otherwise.
     */
    boolean isMachineGuessing();

    /**
     * Returns move counter. Attention with the admissible range! Is incremented
     * by {@code machineMove()} or {@code humanMove()}, respectively.
     *
     * @return Value of the move counter.
     */
    int getMoveCount();

    /**
     * Returns the color choice for move {@code moveNo}. Valid, irrespective of
     * human or machine guessing.
     *
     * @param moveNo
     *            A valid move number.
     * @return The color choice for move {@code moveNo}.
     */
    ColorCode getGameState(int moveNo);

    /**
     * Returns the black and white rating for move {@code moveNo}. Only if human
     * is guessing! Prior, {@code humanMove()} must be called. {@code Rating} is
     * a bean which has the black and white spike count as properties.
     *
     * @param moveNo
     *            A valid move number.
     * @return The rating in the number of black and white spikes.
     */
    Rating getRating(int moveNo);

    /**
     * Queries on the secret machine color code. Only if a human is guessing!
     * Should only be displayed at the end of futile guessing.
     *
     * @return The secret machine color code.
     */
    ColorCode getSecret();

    /**
     * Updates the game state according to guessed color code. Only if a human
     * is guessing. Computes the black/white rating, internally.
     *
     * @param move
     *            Guessed color code.
     */
    void humanMove(ColorCode move);

    /**
     * Computes the next machine move.
     *
     * @return The next machine move, {@code null}, if there is no admissible
     *         color code left (human has cheated by declaring the black/white
     *         rating).
     */
    ColorCode machineMove();

    /**
     * Updates internal game state according to human rating. Only if machine is
     * guessing!
     *
     * @param move
     *            The last color code guessed by the machine.
     * @param rating
     *            The rating in black and white spikes declared by human.
     */
    void processEval(ColorCode move, Rating rating);

}
