import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Character;

/**
 <add main program comment here>
 check which mode is available, then simulation starts
 */

public class BulgarianSolitaireSimulator {

    public static void main(String[] args) {

        boolean singleStep = false;
        boolean userConfig = false;

        //check if args contains "-u" or "-s"
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-u")) {
                userConfig = true;
            } else if (args[i].equals("-s")) {
                singleStep = true;
            }
        }

	Scanner in = new Scanner(System.in);

        if (userConfig) {     //input values by user

            //* Print out instruction *//
            System.out.println("Number of total card is " + SolitaireBoard.CARD_TOTAL);
            System.out.println("You will be entering the initial configuration of the cards" +
                    "(i.e., how many in each pile).");
            
            ArrayList<Integer> numCards = new ArrayList<Integer>();

            //* Check whether input are valid or not*//
            while ( numCards.size() == 0 ) {
                System.out.println("Please enter a space-separated list of positive integer followed by newline:");
                String line = in.nextLine();
                boolean errorChar = checkChar(line);   //call private static method to check character
                if(errorChar == true) {
                    numCards = checkNum(line);         //call private static method to check valid integer
                }
            }

            SolitaireBoard pileCards = new SolitaireBoard(numCards);   //create constructor with an argument
            simStart(pileCards, singleStep, in);   //call private static method to run simulation
        }

        else {     //input values created randomly
            SolitaireBoard pileCards = new SolitaireBoard();   //create constructor without an argument
            simStart(pileCards, singleStep, in);   //call private static method to run simulation
        }
    }

    /**
     * @method Check whether the input includes character or not (include negative sign)
     * @param line
     * @return true (input is correct) or false (contains character)
     */

    private static boolean checkChar(String line){

        int i = 0;   //initialize to pick up the first character

        while ( i != line.length() ) {

            //* check whether the String line is 0, 1, 2... , space, newline or tub *//
            if ( !(Character.isDigit(line.charAt(i)) || Character.isWhitespace(line.charAt(i))) ) {
                System.out.println("ERROR: Each pile must have at least one card and the total number of cards" +
                        " must be " + SolitaireBoard.CARD_TOTAL);
                return false;
            }
            i++;
        }
        return true;
    }


    /**
     * @method: check sum of the elements and elements are not zero
     * @param line
     * @return initArray with input value (valid integers) or initArray with size zero
     */

    private static ArrayList<Integer> checkNum(String line) {

        ArrayList<Integer> initArray = new ArrayList<Integer>();
        Scanner lineScanner = new Scanner(line);
        int tempSum = 0;  //initialize to sum each element up

        while ( lineScanner.hasNextInt() ) {
            int temp = lineScanner.nextInt();

            //* check whether the element is zero or not *//
            if (temp == 0) {
                System.out.println("ERROR: Each pile must have at least one card and the total number of cards" +
                        " must be " + SolitaireBoard.CARD_TOTAL);
                initArray.clear();    //the size of arraylist becomes zero
                return initArray;
            }
            initArray.add(temp);
            tempSum += temp;
        }

        if (tempSum != SolitaireBoard.CARD_TOTAL){
            System.out.println("ERROR: Each pile must have at least one card and the total number of cards" +
                    " must be " + SolitaireBoard.CARD_TOTAL);
            initArray.clear();    //the size of arraylist becomes zero
        }
        return initArray;
    }

    /**
     * @method: run simulation and print out the result
     * @param pileCards
     * @param singleStep
     * @param in
     * @return void
     */
    private static void simStart(SolitaireBoard pileCards, boolean singleStep, Scanner in){

        //* print initial configuration*//
        System.out.println("Initial configuration:" + pileCards.configString());

        //* simulation start *//
        int countSim = 1;   //initialization to count implementation of simulation

        while ( !pileCards.isDone() ){     //check condition to terminate

            //////* Play cards *//////
            pileCards.playRound();

            //////* Show a current configuration *//////
            System.out.println("[" + countSim + "] current configuration:" + pileCards.configString());

            if (singleStep) {   //true:single step function is valid
                System.out.print("<Type return to continue>");
                in.nextLine();     //read newline
            }
            countSim++;     //increment the number of simulation
        }
        System.out.println("Done!");
    }
}
