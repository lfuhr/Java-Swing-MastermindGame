package mastermind.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mastermind.model.ColorCode;
import mastermind.model.Game;
import mastermind.model.MastermindGame;
import mastermind.model.Rating;

/**
 * Provides an interface for the user to use the Trie.
 */
public final class Shell {

    /**
     * Boolean that indicates that a game is over. Wrapped in an Object the the
     * boolean is passed by reference. (In lecture we were told to not declare a
     * game as a global Variable therefore the boolean can't be one either.
     */
    private static class Boolean {
        private boolean value = false;
    }

    private enum ErrCode {
        INVALID_COMMAND, NOT_A_NUMBER, NOT_ENOUGH_NUMBERS, WRONG_MODE,
        GAME_OVER, INVALID_RATING
    }

    private enum MessCode {
        HUMAN_WON, HUMAN_LOST, RATING, MOVE, CHEAT, MACHINE_LOST, MACHINE_WON
    }

    private Shell() {
    }

    /**
     * Launches a Shell to interact with the user.
     *
     * @param args
     *            not used
     */
    public static void main(final String[] args) {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(isr);
        MastermindGame game = new Game(false); // Human is guesser by default
        Boolean isGameOver = new Boolean();

        while (game != null) {
            String line;
            System.out.print("mastermind> ");
            try {
                line = reader.readLine();
            } catch (IOException ex) {
                continue; // print prompt again
            }
            game = execute(game, line, isGameOver); // null if user quits app
        }
    }

    private static MastermindGame execute(
            MastermindGame game, String line, Boolean isGameOver) {

        line = line.trim();
        if (line.isEmpty()) {
            return game; // Output prompt again
        }
        String[] tokens = line.split("\\s+");
        String command = tokens[0].toLowerCase();
        char firstLetterOfCommand = command.charAt(0);
        boolean success;

        switch (firstLetterOfCommand) {
        case 'h': // help
            printHelp();
            break;
        case 'q': // quit
            return null;
        case 's': // switch
            game = newGame(!game.isMachineGuessing(), isGameOver);
            break;
        case 'n': // new
            game = newGame(game.isMachineGuessing(), isGameOver);
            break;
        case 'm': // move
            if (isGameOver.value) {
                errorMessage(ErrCode.GAME_OVER, null);
                return game;
            }
            success = humanMove(getArgs(tokens), game);
            if (success) {
                Rating lastRating = game.getRating(game.getMoveCount());
                if (lastRating.getBlack() == MastermindGame.NUMBER_SLOTS) {
                    String moveCount;
                    moveCount = (String.valueOf(game.getMoveCount()));
                    message(MessCode.HUMAN_WON, moveCount);
                    isGameOver.value = true;
                } else if (boardFull(game)) {
                    String secret = game.getSecret().toString();
                    message(MessCode.HUMAN_LOST, secret);
                    isGameOver.value = true;
                } else {
                    message(MessCode.RATING, lastRating.toString());
                }
            }
            break;
        case 'e': // eval
            if (isGameOver.value) {
                errorMessage(ErrCode.GAME_OVER, null);
                return game;
            }
            Rating rating = processEval(getArgs(tokens), game);
            // if processEval printed error
            if (rating == null) {
                return game;
            } else if (rating.getBlack() == MastermindGame.NUMBER_SLOTS) {
                message(MessCode.MACHINE_WON, null);
                isGameOver.value = true;
            } else if (boardFull(game)) {
                message(MessCode.MACHINE_LOST, null);
                isGameOver.value = true;
            } else {
                ColorCode move = game.machineMove();
                if (move == null) {
                    message(MessCode.CHEAT, null);
                    isGameOver.value = true;
                } else {
                    message(MessCode.MOVE, String.valueOf(move));
                }
            }
            break;
        default:
            errorMessage(ErrCode.INVALID_COMMAND, command);
        }
        return game;
    }

    private static MastermindGame newGame(boolean machineIsGuessing,
            Boolean isGameOver) {
        isGameOver.value = false;
        MastermindGame game = new Game(machineIsGuessing);
        if (machineIsGuessing) {
            ColorCode move = game.machineMove();
            message(MessCode.MOVE, move.toString());
        }
        return game;
    }

    private static boolean boardFull(MastermindGame game) {
        return game.getMoveCount() == MastermindGame.MAX_MOVES;
    }

    private static Rating processEval(String[] args, MastermindGame game) {
        if (!game.isMachineGuessing()) {
            errorMessage(ErrCode.WRONG_MODE, null);
            return null;
        } else if (args.length < 2) {
            errorMessage(ErrCode.NOT_ENOUGH_NUMBERS, "2");
            return null;
        }
        byte[] blackwhite = stringToByte(args);
        // If arguments ar not numbers
        if (blackwhite == null) {
            return null;
        }
        Rating rating = new Rating(blackwhite[0], blackwhite[1]);
        int numberOfRatingPegs = rating.getBlack() + rating.getWhite();
        if (numberOfRatingPegs > MastermindGame.NUMBER_SLOTS) {
            errorMessage(ErrCode.INVALID_RATING, rating.toString());
            return null;
        }
        int lastMoveNo = game.getMoveCount() - 1;
        ColorCode lastMove = game.getGameState(lastMoveNo);
        game.processEval(lastMove, rating);
        return rating;
    }

    /**
     * Converts an array of Strings to an array of bytes if possilbe.
     * 
     * @param strings
     *            array of numbers
     * @return array of bytes or null if at least one string could not be
     *         converted
     */
    private static byte[] stringToByte(String[] strings) {
        byte[] bytes = new byte[strings.length];
        for (int i = 0; i < strings.length; i++) {
            try {
                bytes[i] = Byte.valueOf(strings[i]);
            } catch (NumberFormatException ex) {
                errorMessage(ErrCode.NOT_A_NUMBER, strings[i]);
                return null;
            }
        }
        return bytes;
    }

    private static String[] getArgs(String[] tokens) {
        String[] arguments = new String[tokens.length - 1];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = tokens[i + 1];
        }
        return arguments;
    }

    private static boolean humanMove(String[] args, MastermindGame game) {
        if (game.isMachineGuessing()) {
            errorMessage(ErrCode.WRONG_MODE, "");
            return false;
        }
        byte[] pegs = stringToByte(args);
        if (pegs == null) {
            return false;
        } else if (MastermindGame.NUMBER_SLOTS > args.length) {
            errorMessage(ErrCode.NOT_ENOUGH_NUMBERS, "4");
            return false;
        } else {
            game.humanMove(new ColorCode(pegs));
            return true;
        }
    }

    private static void message(MessCode mess, String adj) {
        String output;
        switch (mess) {
        case HUMAN_WON:
            output = "Congratulations! You needed " + adj + " moves.";
            break;
        case HUMAN_LOST:
            output = "No more moves - solution: " + adj;
            break;
        case MACHINE_WON:
            output = "Wow! I did it!";
            break;
        case MACHINE_LOST:
            output = "No more moves - I couldn't find solution.";
            break;
        case RATING:
            output = adj;
            break;
        case MOVE:
            output = "machine guess: " + adj;
            break;
        case CHEAT:
            output = "No possibilities left - you have been cheating!";
            break;
        default:
            throw new IllegalArgumentException("Unknown Output!");
        }
        System.out.println(output);
    }

    private static void errorMessage(ErrCode err, String adj) {
        String output = "Error! ";
        switch (err) {
        case INVALID_COMMAND:
            output += "Invalid command " + adj;
            break;
        case NOT_A_NUMBER:
            output += adj + " is not a number from 0 to "
                    + (MastermindGame.NUMBER_COLORS - 1) + ".";
            break;
        case NOT_ENOUGH_NUMBERS:
            output += "This commad needs " + adj
                    + " arguments.";
            break;
        case WRONG_MODE:
            output += "Cannot perform the Command in this Mode.\n"
                    + "Use the switch command.";
            break;
        case GAME_OVER:
            output += "Game is over. Please start a new one with \"new\"";
            break;
        case INVALID_RATING:
            output += "This is not a valid Rating. " + adj;
            break;
        default:
            throw new IllegalArgumentException("Unknown Error!");
        }
        System.out.println(output);
    }

    private static void printHelp() {
        String help = "\n"
                + "Everybody knows the rules of Mastermind. If you don't,\n"
                + "please search search the web. Just to remind you: One\n"
                + "black peg means that one coloured peg of the right color\n"
                + "is in the right place.\n"
                + "\n"
                + "quit\n"
                + "Quits the Application.\n"
                + "\n"

                + "switch\n"
                + "Switches the roles of the player and the Computer.\n"
                + "Initially The Comuter is the coder and the player is the\n"
                + "guesser\n"
                + "\n"

                + "new\n"
                + "Creates a new game whitout chaning the Roles.\n"
                + "\n"

                + "move\n"
                + "(only if the player is guessing)\n"
                + "Sets the " + MastermindGame.NUMBER_SLOTS + " coloured pegs "
                + "as a guess.\n"
                + "The command musst be followed by "
                + MastermindGame.NUMBER_SLOTS + " numbers from 0 to "
                + (MastermindGame.NUMBER_COLORS - 1) + ".\n"
                + "\n"

                + "eval\n"
                + "(only if the computer is guessing)\n"
                + "Defines a Rating for the move the Computer has taken.\n"
                + "The command must be followed by 2 numbers from 0 to "
                + MastermindGame.NUMBER_SLOTS + ",\n"
                + "where the first one indicates the number of black and the\n"
                + "second one number of white pegs.\n";
        System.out.println(help);
    }

}