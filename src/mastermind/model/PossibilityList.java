package mastermind.model;

import static mastermind.model.MastermindGame.NUMBER_COLORS;
import static mastermind.model.MastermindGame.NUMBER_SLOTS;

/**
 * A simple Class to make a boolean array abstract. For each ColorCode it
 * contains one boolean that indicates whether it can match the secret.
 */
final class PossibilityList {

    private static int totalNumberCombis = (int) Math.pow(NUMBER_COLORS,
            NUMBER_SLOTS);

    private final boolean[] combis;

    /**
     * Constructs a list where every Combination is possible.
     */
    public PossibilityList() {
        combis = new boolean[totalNumberCombis];
        for (int i = 0; i < combis.length; i++) {
            combis[i] = true;
        }
    }

    /**
     * Returns the static saved number of all ColorCode Combinations.
     *
     * @return total number of combinations
     */
    public static int getTotalNoCombis() {
        return totalNumberCombis;
    }

    /**
     * Every index {@code i} represents a ColorCode.
     *
     * @param i
     *            index
     * @return corresponding ColorCode
     */
    public static ColorCode getColorCode(int i) {
        ColorCode result = new ColorCode();
        int remainder = i;

        for (byte j = NUMBER_SLOTS - 1; j >= 0; j--) {
            result.set(j, (byte) (remainder % NUMBER_COLORS));
            remainder /= NUMBER_COLORS;
        }
        return result;
    }

    /**
     * Checks whether a color code is still possible.
     *
     * @param i
     *            index representing color code
     * @return true if ColorCode is possible, otherwise false
     */
    public boolean combiIsPossible(int i) {
        return combis[i];
    }

    /**
     * Memorizes that the specified ColorCode is no longer possible.
     *
     * @param i
     *            Index representing color code
     */
    public void remove(int i) {
        combis[i] = false;
    }
}
