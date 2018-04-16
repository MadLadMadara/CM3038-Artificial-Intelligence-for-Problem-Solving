
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
        String result = "";
        int sumWeightNorth = 0;
        int sumWeightSouth = 0;
        result+="North bank population";
        for (Person p :
                this.northBankPopulation.values()) {
            result+= "\r" + p.toString();
            sumWeightNorth+=p.getWeight();
        }
        result+="South bank population";
        for (Person p :
                this.southBankPopulation.values()) {
            result+= "\r" + p.toString();
            sumWeightSouth+=p.getWeight();
        }
        if (raftLocation==RiverBank.NORTH)
            result+="\rRaft location: North";
        if (raftLocation==RiverBank.SOUTH)
            result+="\rRaft location: South";

        // FIX: 14/04/2018 toString: could add number of drivers on each bank, it would help with debugging later
        return result+"\rFuel usage:" + this.currentFuelUsage +
                "\rNorth bank size:" + this.northBankPopulation.size() +
                "\rSum North bank population weight: "+ sumWeightNorth +
                "\rAverage North bank person weight: "+ sumWeightNorth/this.northBankPopulation.size() +
                "\rSouth bank size" + this.southBankPopulation.size() +
                "\rSum South bank population weight: "+ sumWeightSouth +
                "\rAverage South bank person weight: "+ sumWeightSouth/this.southBankPopulation.size();
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
     * hash code method
     * @return an integer that is unique to the configuration of attributes values in this object
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
        // FIX 14/04/2018 toString: main if might need reworking
        if (this.currentFuelUsage < 0 ||
                ((this.raftLocation == RiverBank.NORTH && this.northBankPopulation.isEmpty()) ||
                        (this.raftLocation == RiverBank.SOUTH && this.southBankPopulation.isEmpty()) ) )
            return true;

        /**
         * checks if there is a driver on the bank the raft is at.
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
        HashMap<String, Person> newNorthBank = (HashMap<String, Person>)this.northBankPopulation.clone();
        HashMap<String, Person> newSouthBank = (HashMap<String, Person>)this.northBankPopulation.clone();
        int fuelUsed = 0;

        if (action.toBank==RiverBank.NORTH){
            for (String k:
                    action.peoplesKeysToCross) {
                Person p = newSouthBank.remove(k);
                fuelUsed += p.getWeight();
                newNorthBank.put(k, p);
            }
        }else{

            for (String k:
                    action.peoplesKeysToCross) {
                Person p = newNorthBank.remove(k);
                fuelUsed += p.getWeight();
                newSouthBank.put(k, p);
            }

        }
        RiverCrossState nextState = new RiverCrossState(newNorthBank, newSouthBank, this.oppositeBank(this.raftLocation), this.currentFuelUsage + fuelUsed);
        return nextState;

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
