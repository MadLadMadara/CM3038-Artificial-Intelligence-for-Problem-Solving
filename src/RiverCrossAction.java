import cm3038.search.*;

import java.util.*;

/**
 * @author Sam McRuvie
 */
public class RiverCrossAction extends Action {




    /**
     * list of crosser keys for the north and south banks hash maps
     */
    public Set<Person> peopleCrossing;

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
    public RiverCrossAction(RiverBank to, Set<Person> peopleCrossing) {
        this.peopleCrossing = new HashSet<>(peopleCrossing);
        this.toBank = to;
        this.cost = this.boatWeight();
    } //end method

    public double boatWeight(){
        double d = 0.0;
        for (Person p:
        peopleCrossing) {
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
            result="!!!Action!!!\nSouth->North ";
        else result="!!!Action!!!\nNorth->South ";

        result+= "\ncost("+ this.cost +") Max cost^"+RiverCrossProblem.RAFT_MAX_WEIGHT+"^\n"+this.peopleCrossing.toString()+"\nCrossing Size{"+this.peopleCrossing.size()+"}{"+RiverCrossProblem.RAFT_SIZE+"} \nboat weight:"+this.boatWeight();

        return result;
    } //end method
} //end class