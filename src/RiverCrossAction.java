import cm3038.search.*;

import java.util.HashMap;

/**
 * @author Sam McRuvie
 */
public class RiverCrossAction extends Action {




    /**
     * list of crosser keys for the north and south banks hash maps
     */
    public HashMap<String, Person> peopleCrossing;

    /**
     * We are moving the raft to this bank.
     * It must be either NORTH or SOUTH as defined in the RiverBank enumerated type.
     */
    public RiverBank toBank;

    /**
     * initialises RiverCrossAction object with people to be moved and to which bank.
     * @param peopleCrossing  hash map of people to be moved.
     * @param to the side the boat will cross to
     */
    public RiverCrossAction(RiverBank to, HashMap<String, Person> peopleCrossing) {
        this.peopleCrossing = ( HashMap<String, Person>) peopleCrossing.clone();
        this.toBank = to;
        this.cost = this.boatWeight();
    } //end method

    public double boatWeight(){
        double d = 0.0;
        for (Person p:
        peopleCrossing.values()) {
            d+=p.getWeight();
        }
        return d;
    }
    /**
     *
     */
    public String toString() {
        String result;
        if (this.toBank==RiverBank.NORTH)
            result="South->North ";
        else result="North->South ";
        result+= "("+ this.cost +")  "+this.boatWeight()+" {"+RiverCrossProblem.RAFT_MAX_WEIGHT+"}";
        result += this.peopleCrossing.keySet().toString();
        return result;
    } //end method
} //end class