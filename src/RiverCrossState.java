
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
     * the amount of fuel used up to this state
     */
    public int currentFuelUsage;

    /**
     * Create a McState object with the given initial values.
     * @param northBankPopulation hash map of people in the north bank.
     * @param southBankPopulation hash map of people in the south bank.
     * @param raft The location of the raft as defined.
     */
    public RiverCrossState(HashMap<String, Person> northBankPopulation,  HashMap<String, Person> southBankPopulation ,RiverBank raft, int currentFuelUsage) {
        this.northBankPopulation = (HashMap<String, Person>)northBankPopulation.clone();
        this.southBankPopulation = (HashMap<String, Person>)southBankPopulation.clone();
        this.raftLocation=raft;
        this.currentFuelUsage=currentFuelUsage;
    } //end method

    /**
     * Converting a {@link RiverCrossState} object into a {@link String}.
     */
    public String toString() {
        // TODO: 13/04/2018 toString method
        String result="";
        if (raftLocation==RiverBank.NORTH)
            result+=" Raft North";

        if (raftLocation==RiverBank.SOUTH)
            result+=" Raft South";



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

        RiverCrossState stateToTest = (RiverCrossState)state;
        /**
         * hashMap equals ignore the ordering of the hash maps
         */
        if(stateToTest.northBankPopulation.size() != this.northBankPopulation.size() || stateToTest.southBankPopulation.size() != this.southBankPopulation.size() ||
                stateToTest.raftLocation != this.raftLocation || stateToTest.currentFuelUsage != this.currentFuelUsage)
            return false;

        return stateToTest.northBankPopulation.equals(this.northBankPopulation) && stateToTest.southBankPopulation.equals(this.southBankPopulation);
    } //end method

    /**
     *
     */
    public int hashCode() {
        return this.northBankPopulation.hashCode() + this.southBankPopulation.hashCode() + this.currentFuelUsage * 100 + ((this.raftLocation == RiverBank.NORTH)?10:0);
    } //end method

    /**
     * Find the actions and next states of the current state.
     * @return A list of ActionStatePair objects.
     */
    public List<ActionStatePair> successor(){
        // TODO: 13/04/2018  successor method creat an array of action state pairs for this state
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
        // TODO: 13/04/2018 isInvalid method this might need work later

        if (this.currentFuelUsage < 0 ||
                (this.northBankPopulation.size() < 1 && this.southBankPopulation.size() < 1) ||
                this.currentFuelUsage < 0 ||
                (this.raftLocation == RiverBank.NORTH ^ this.raftLocation == RiverBank.SOUTH) )
            return true;

        /**
         * checks if there is a driver on the back the raft is at.
         */
        for (Person p:
                (this.raftLocation == RiverBank.NORTH)? this.northBankPopulation.values(): this.southBankPopulation.values()) {
            if(p.isDriver()) return false; // state is valid if a driver is on the bank and the
        }

        return true;	// state is invalid if this point met
    } //end method

    /**
     *
     * @param action The action to apply.
     * @return The next state after the action is applied to the current state object.
     */
    public RiverCrossState applyAction(RiverCrossAction action){
        // TODO: 13/04/2018 applyAction method
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
