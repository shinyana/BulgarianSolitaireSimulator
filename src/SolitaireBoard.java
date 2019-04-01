import java.util.ArrayList;
import java.util.Random;

/**
 class SolitaireBoard
 The board for Bulgarian Solitaire.  You can change what the total number of cards is for the game
 by changing NUM_FINAL_PILES, below.  Don't change CARD_TOTAL directly, because there are only some values
 for CARD_TOTAL that result in a game that terminates.
 (See comments below next to named constant declarations for more details on this.)
 */

public class SolitaireBoard {

    public static final int NUM_FINAL_PILES = 9;
    // number of piles in a final configuration
    // (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)

    public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
    // bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
    // see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
    // the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES

    // Note to students: you may not use an ArrayList -- see assgt description for details.

    public static final int NO_TARGET_NUMBER = -1;
    // Return negative constant if there is no target value in the array.

    /**
     <put rep. invar. comment here>

     #1: 0 <= arrayNumCards[i] <= CARD_TOTAL (Note: 0 <= i < CARD_TOTAL - 1)
     #2: arrayNUmCards[0] + arrayNUmCards[1] + ... arrayNUmCards[i] = CARD_TOTAL
     #3: -1 <= lastLoc <= CARD_TOTAL - 1

     #4: arrayNumCards.length = CARD_TOTAL
     */

    ////* <instance variables> *////
    private int[] arrayNumCards;
    private int lastLoc;

    /**
     Creates a solitaire board with the configuration specified in piles, which has been created by users.
     piles has the number of cards in the first pile, then the number of cards in the second pile, etc.
     PRE: piles contains a sequence of positive numbers that sum to SolitaireBoard.CARD_TOTAL
     @param piles
     */

    public SolitaireBoard(ArrayList<Integer> piles) {

        arrayNumCards = new int [CARD_TOTAL];

        for (int i = 0; i < piles.size(); i++) {
            arrayNumCards[i] = piles.get(i);
            lastLoc = i;
        }
        assert isValidSolitaireBoard();
    }

    /**
     Creates a solitaire board with a random initial configuration.
     This constructor has no argument.
     */

    public SolitaireBoard() {

        Random generator = new Random();
        arrayNumCards = new int [CARD_TOTAL];

        //*set values in the array*//
        int sumCardsNum = 0;    //sum of the created piles
        for (int i = 0; sumCardsNum != CARD_TOTAL; i++) {
            if (i == 0) {
                arrayNumCards[i] = 1 + generator.nextInt(CARD_TOTAL);   //yield value from 1 to CARD_TOTAL
            }
            else {
                arrayNumCards[i] = 1 + generator.nextInt(CARD_TOTAL - sumCardsNum);  //yield value from 1 to (CARD_TOTAL - sum of generated values)
            }
            sumCardsNum += arrayNumCards[i];    //add new piles
            lastLoc = i;                        //keep the index of the end of the pile in the array
        }
        assert isValidSolitaireBoard();
    }

    /**
     Plays one round of Bulgarian solitaire.  Updates the configuration according to the rules
     of Bulgarian solitaire: Takes one card from each pile, and puts them all together in a new pile.
     The old piles that are left will be in the same relative order as before,
     and the new pile will be at the end.
     */

    public void playRound() {

        //* subtract 1 from each pile in the array *//
        for (int i = 0; i < lastLoc + 1; i++) {
            arrayNumCards[i] -= 1;
        }

        int tempAddNum = lastLoc + 1;    //preserve the new value in the end of the pile in the array temporarily

        //* lookup the element which holds zero in the array until lastLoc *//
        boolean searchCompFlag = false;  //initialization
        while (!searchCompFlag) {

            int arrayIndex = lookupLoc(0);     //call private static method whether the pile is zero or not

            if (arrayIndex >= 0) {  //if found, remove zero-pile and reduce lastLoc
                for (int i = arrayIndex; i < lastLoc; i++) {
                    arrayNumCards[i] = arrayNumCards[i + 1];
                }
                arrayNumCards[lastLoc] = 0;   //set zero to avoid accessing exceptional area (case:there are 45 pieces of 1)
                lastLoc--;
            }
            else {                  //not found
                searchCompFlag = true;
            }
        }
        //* create a new pile in the end of the array *//
        lastLoc++;
        arrayNumCards[lastLoc] = tempAddNum;
        assert isValidSolitaireBoard();
    }

    /**
     Returns true iff the current board is at the end of the game.  That is, there are NUM_FINAL_PILES
     piles that are of sizes 1, 2, 3, . . . , NUM_FINAL_PILES, in any order.
     */

    public boolean isDone() {

        if (lastLoc == NUM_FINAL_PILES - 1) {   //check only if lastLoc point to NUM_FINAL_PILES - 1

            //* check how many cards each pile has *//
            for (int i = 1; i < NUM_FINAL_PILES + 1; i++) {
                //* if number 1 through NUM_FINAL_PILES were not found in the first NUM_FINAL_PILES indices
                // of the array, lookupLoc would return negative value, otherwise, index of the array *//
                if (lookupLoc(i) < 0) {
                    assert isValidSolitaireBoard();
                    return false;
                }
            }
            assert isValidSolitaireBoard();
            return true;
        }
        assert isValidSolitaireBoard();
        return false;
    }

    /**
     Returns current board configuration as a string with the format of
     a space-separated list of numbers with no leading or trailing spaces.
     The numbers represent the number of cards in each non-empty pile.
     */
    public String configString() {

        String configMsg = "";    //initialize to concatenate strings

        for (int i = 0; i < lastLoc + 1; i++){    //concatenate elements from index 0 to index lastLoc
            configMsg = configMsg + (" " + arrayNumCards[i]);
        }

        assert isValidSolitaireBoard();
        return configMsg;
    }

    /**
     Returns true iff the solitaire board data is in a valid state
     (See representation invariant comment for more details.)
     */
    private boolean isValidSolitaireBoard() {
        int sumOfCards = 0;
        for (int i = 0; i < lastLoc + 1; i++){

            sumOfCards += arrayNumCards[i];

            if ( (arrayNumCards[i] < 0) || (arrayNumCards[i] > CARD_TOTAL) ){     //rep. invar. #1
                return false;
            }
        }
        if (sumOfCards != CARD_TOTAL){    //rep. invar. #2
            return false;
        }
        if ( (lastLoc < -1) || (lastLoc > CARD_TOTAL - 1) ){    //rep. invar. #3
            return false;
        }

        return true;

    }

    /**
     * If the input value matches an element in the array, return the index of the array.
     * @param targetValue
     * @return  (found): int the index of the array (0 ~ lastLoc)
     *          (not found): int -1
     */
    private int lookupLoc(int targetValue) {

        for (int i = 0; i < lastLoc + 1; i++) {

            if (arrayNumCards[i] == targetValue) {
                return i;
            }
        }
        return NO_TARGET_NUMBER;
    }
}