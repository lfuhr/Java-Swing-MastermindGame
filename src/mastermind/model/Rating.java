package mastermind.model;

/**
 * A Bean for the number of black and white spikes in the Mastermind Game.
 */
public final class Rating {

    private final byte black;
    private final byte white;

    /**
     * Constructs an Object with a Number of black and white spikes.
     *
     * @param black
     *            number of black spikes
     * @param white
     *            number of white spikes
     */
    public Rating(byte black, byte white) {
        if (black < 0 || white < 0
                || black + white > MastermindGame.NUMBER_SLOTS) {

            throw new IllegalArgumentException("Invalid number of spikes");
        }
        this.black = black;
        this.white = white;
    }

    /**
     * Returns the number of black spikes.
     *
     * @return number of black spikes
     */
    public byte getBlack() {
        return black;
    }

    /**
     * Returns the number of white spikes.
     *
     * @return number of white spikes.
     */
    public byte getWhite() {
        return white;
    }

    @Override
    public String toString() {
        return "black: " + black + " white: " + white;
    }

    /**
     * Compares Rating to another one whether they have the same number of black
     * and white spikes.
     *
     * @param obj
     *            object to compare to
     * @return true if param's type is Rating, and black and white are equal,
     *         otherwise false
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rating other = (Rating) obj;
        if (this.black != other.black) {
            return false;
        }
        return this.white == other.white;
    }

    /**
     * Creates a hashCode that is the same for every equal Rating.
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        final int min = 3;
        final int multiplicator = 17;
        int hash = min;
        hash = multiplicator * hash + this.black;
        hash = MastermindGame.NUMBER_SLOTS * hash + this.white;
        return hash;
    }

    /**
     * Checks if the Rating implies a correct guess.
     *
     * @return true if and only if the number of black spikes at its maximum.
     */
    public boolean isAllBlack() {
        return black == MastermindGame.NUMBER_SLOTS;
    }
}
