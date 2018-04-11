
import java.util.*;
import cm3038.search.*;

/**
 * This class implements the State to model the state of the Missionares & Cannibals problem.
 * The state of the M&C world is captured by 5 object-level attributes.
 *
 * @author kit
 *
 */
public class RiverCrossState implements State {
    /**
     * north bank population
     */
    public HashMap<String, Person> northBankPopulation;
    /**
     * south bank population
     */
    public HashMap<String, Person> southBankPopulation;

    /**
     * The location of the raft as defined in the {@link RiverBank} enumerated type.
     */
    public RiverBank raftLocation;

    /**
     * Create a McState object with the given initial values.
     *
     * @param raft The location of the raft as defined.
     */
    public RiverCrossState(HashMap<String, Person> northBankPopulation,  HashMap<String, Person> southBankPopulation ,RiverBank raft) {
        this.northBankPopulation = (HashMap<String, Person>)northBankPopulation.clone();
        this.southBankPopulation = (HashMap<String, Person>)southBankPopulation.clone();
        this.raftLocation=raft;
    } //end method

    /**
     * Converting a {@link RiverCrossState} object into a {@link String}.
     */
    public String toString() {
        String result="";
        if (raftLocation==RiverBank.NORTH)
            result+=" Raft";

        if (raftLocation==RiverBank.SOUTH)
            result+=" Raft";



        return result;
    } //end method

    /**
     *
     * @param state The other state to test.
     * @return true if the parameter state is the same as the current state object.
     */
    public boolean equals(Object state) {
        if (!(state instanceof RiverCrossState))	//if the given parameter is not a McState
            return false;				//it must be false

        RiverCrossState mcState=(RiverCrossState)state;

        return true;
    } //end method

    /**
     *
     */
    public int hashCode() {
        return 1;
    } //end method

    /**
     * Find the actions and next states of the current state.
     * @return A list of ActionStatePair objects.
     */
    public List<ActionStatePair> successor(){
        List<ActionStatePair> result=new ArrayList<ActionStatePair>();		//I chose to use an ArrayList object as the list will be short.

        //
        //This should not happen but just to be safe.
        //
        if (this.isInvalid())				//if current state is invalid
            return result;					//return an empty set


        return result;
    }//end method

    /**
     * Check if a state is invalid.
     * @return true if a state is invalid. Or false otherwise.
     */
    public boolean isInvalid(){



        return false;	//if none of the above, then state is valid
    } //end method

    /**
     *
     * @param action The action to apply.
     * @return The next state after the action is applied to the current state object.
     */
    public RiverCrossState applyAction(RiverCrossAction action){
        if (action.toBank==RiverBank.NORTH){
//            river bank north

        }else{
//            river bank south

        }

    } //end method

    /**
     * A handy method to find the opposite bank of the river.
     * @param current
     * @return
     */
    private RiverBank oppositeBank(RiverBank current) {
        if (current==RiverBank.NORTH)
            return RiverBank.SOUTH;
        return RiverBank.NORTH;
    } //end method
} //end class
