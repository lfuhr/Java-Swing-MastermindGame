package mastermind.model;

import java.util.Random;

/**
 * Implementation of one combination of colored spikes in the Mastermind Game.
 */
public final class ColorCode {

    private final byte[] spikes = new byte[MastermindGame.NUMBER_SLOTS];

    /**
     * Returns the ith spike.
     * 
     * @param i
     *            specified spike to return
     * @return integral representation of color.
     */
    public byte get(int i) {
        if (i < 0 || i >= spikes.length) {
            throw new IllegalArgumentException("Number must be between 0 and "
                    + (MastermindGame.NUMBER_SLOTS - 1));
        }
        return spikes[i];
    }

    /**
     * Defines the color of the ith spike.
     * 
     * @param i
     *            specified spike to set
     * @param value
     *            integral representation of color.
     */
    public void set(int i, byte value) {
        if (i < 0 || i >= spikes.length) {
            throw new IllegalArgumentException("Number must be between 0 and "
                    + (MastermindGame.NUMBER_SLOTS - 1));
        }
        spikes[i] = value;
    }

    /**
     * Evaluates {@code other}. It takes {@code this} as the secret. (Given the
     * rules of Mastermind, it also works vice-versa)
     *
     * @param other
     *            Colorcode to evaluate
     * @return calculated Rating
     */
    public Rating evaluate(ColorCode other) {

        byte absolutecounter = 0;
        byte blackcounter = 0;

        final byte[] numberOfOccurrInThis =
                new byte[MastermindGame.NUMBER_COLORS];
        final byte[] numberOfOccurrInOther =
                new byte[MastermindGame.NUMBER_COLORS];

        // Count occurrences of every Color
        for (final byte spike : this.spikes) {
            numberOfOccurrInThis[spike]++;
        }
        for (final byte spike : other.spikes) {
            numberOfOccurrInOther[spike]++;
        }

        // Calculate absolute number of black/white spikes
        for (int i = 0; i < numberOfOccurrInOther.length; i++) {
            absolutecounter += Math.min(numberOfOccurrInThis[i],
                    numberOfOccurrInOther[i]);
        }

        // Calculate Black spikes
        for (int i = 0; i < spikes.length; i++) {

            if (this.spikes[i] == other.spikes[i]) {
                blackcounter++;
            }
        }

        // Calculate black spikes
        final byte white = (byte) (absolutecounter - blackcounter);

        return new Rating(blackcounter, white);
    }

    /**
     * Generates a random ColorCode. It uses {@code java.util.Random}.
     *
     * @return random color code
     */
    public static ColorCode random() {
        final Random r = new Random();
        final ColorCode random = new ColorCode();

        for (int i = 0; i < random.spikes.length; i++) {
            random.spikes[i] = (byte) (r
                    .nextInt(MastermindGame.NUMBER_COLORS));
        }
        return random;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spikes.length - 1; i++) {
            sb.append(spikes[i]);
            sb.append(" ");
        }
        sb.append(spikes[spikes.length - 1]);
        return sb.toString();
    }
}
