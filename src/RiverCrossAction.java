import cm3038.search.*;
/**
 * @author Sam McRuvie
 */
public class RiverCrossAction extends Action {

    /**
     * list of crosser keys for the north and south banks hash maps
     */
    public String[] crosserKeys;

    /**
     * We are moving the raft to this bank.
     * It must be either NORTH or SOUTH as defined in the RiverBank enumerated type.
     */
    public RiverBank toBank;

    /**
     * initialises RiverCrossAction object with people to be moved and to which bank.
     * @param crosserKeys unique key for river bank hash map.
     * @param to the side the boat will cross to
     */
    public RiverCrossAction(String[] crosserKeys, RiverBank to) {
        this.crosserKeys = crosserKeys.clone();
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
        for (String s :
                this.crosserKeys) {
            result +="\r"+s;
        }
        return result;
    } //end method
} //end class