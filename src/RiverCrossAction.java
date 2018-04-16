import cm3038.search.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Sam McRuvie
 */
public class RiverCrossAction extends Action {

    /**
     * list of crosser keys for the north and south banks hash maps
     */
    public ArrayList<String> peoplesKeysToCross;

    /**
     * We are moving the raft to this bank.
     * It must be either NORTH or SOUTH as defined in the RiverBank enumerated type.
     */
    public RiverBank toBank;

    /**
     * initialises RiverCrossAction object with people to be moved and to which bank.
     * @param peoplesKeysToCross  hash map of people to be moved.
     * @param to the side the boat will cross to
     */
    public RiverCrossAction(ArrayList<String> peoplesKeysToCross, RiverBank to) {
        this.peoplesKeysToCross = (ArrayList<String>)peoplesKeysToCross.clone();
        this.toBank = to;
    } //end method

    /**
     *
     */
    public String toString() {
        String result;
        if (this.toBank==RiverBank.NORTH)
            result="South->North ";
        else result="North->South ";
        result += this.peoplesKeysToCross.toString();
        return result;
    } //end method
} //end class