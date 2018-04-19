
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

    private List<ActionStatePair> possibleActions;
    /**
     * north bank population
     */
    public Set<Person> northBankPopulation;
    // change these
    /**
     * south bank population
     */
    public Set<Person> southBankPopulation;
    /**
     *
     */

    /**
     * The location of the raft as defined in the {@link RiverBank} enumerated type.
     */
    public RiverBank raftLocation;

    /**
     * Create a McState object with the given initial values.
     * @param northBankPopulation hash map of people in the north bank.
     * @param southBankPopulation hash map of people in the south bank.
     * @param raft The location of the raft as defined.
     */
    public RiverCrossState(Set<Person>  northBankPopulation,  Set<Person>  southBankPopulation ,RiverBank raft) {
        this.northBankPopulation = new HashSet<>(northBankPopulation);
        this.southBankPopulation = new HashSet<>(southBankPopulation);
        this.raftLocation=raft;
        this.possibleActions = new ArrayList<>();
    } //end method

    /**
     * Converting a {@link RiverCrossState} object into a {@link String}.
     */
    public String toString() {
        String result = "\n!!!State!!!";
        if (raftLocation==RiverBank.NORTH)
            result+="\nRaft location: North";
        if (raftLocation==RiverBank.SOUTH)
            result+="\nRaft location: South";
        result+="\nNorth bank size:" + this.northBankPopulation.size() + "  "+ this.northBankPopulation.toString();
        result+="\nSouth bank size:" + this.southBankPopulation.size()+ "  "+this.southBankPopulation.toString();
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
         * Set equals ignore the ordering of the Set maps
         */
        if(stateToTest.northBankPopulation.size() != this.northBankPopulation.size() || stateToTest.southBankPopulation.size() != this.southBankPopulation.size() ||
                stateToTest.raftLocation != this.raftLocation)
            return false;

        return stateToTest.northBankPopulation.equals(this.northBankPopulation) && stateToTest.southBankPopulation.equals(this.southBankPopulation);
    } //end method

    /**
     * hash code method
     * @return an integer that is unique to the configuration of attributes values in this object
     */
    public int hashCode() {
        return this.northBankPopulation.hashCode() + this.southBankPopulation.hashCode() + ((this.raftLocation == RiverBank.NORTH)?10:1)*100;
    } //end method

    /**
     * Find the actions and next states of the current state.
     * @return A list of ActionStatePair objects.
     */
    public List<ActionStatePair> successor(){
        if(this.possibleActions.size() >= 1) return this.possibleActions;

        if (this.isInvalid())//if current state is invalid
            return this.possibleActions;

        if(this.raftLocation == RiverBank.NORTH){
            this.generateLightestDriverAction(this.northBankPopulation);
        }else{
            this.generatePossibleActions(this.southBankPopulation);
        }
        return this.possibleActions;
    }//end method

    private Set<Set<Person>> generatePossibleActions(Set<Person> originalSet) {
        Set<Set<Person>> sets = new HashSet<>();
        List<ActionStatePair> actions = new ArrayList<>();
        RiverCrossAction temp;
        if (originalSet.isEmpty() || !this.possibleActions.isEmpty()) {
            sets.add(new HashSet());
            return sets;
        }
        // temp variable to convert
        List<Person> list = new ArrayList((Set<Person>)originalSet);
        // first element in the list to be used as a head point
        Person head = (Person)list.get(0); // get first person
        // rest
        Set rest = new HashSet(list.subList(1, list.size())); // get rest of of the people

        // loop over the powerset of rest
        for (Object set : generatePossibleActions(rest)) {

            // the main recursive loop to get the powerset of originalSet
            Set<Person> setWithHead = new HashSet<>();
            setWithHead.add(head);
            setWithHead.addAll((Set<Person>)set);
            sets.add(setWithHead);
            sets.add((Set<Person>)set);

            // catch to see if a configfuration is possible wirh head and set
            if(((Set<Person>)set).isEmpty() && !((Person)head).isDriver()) continue; //

            // add newset to action state pair array if newset is valid
            if(setWithHead.size() < 1 ||
                    setWithHead.size() > RiverCrossProblem.RAFT_SIZE) continue;  // check if setWithHead set is with in range
            if(this.checkForDriverAndWeight(setWithHead))continue;
            temp = new RiverCrossAction(this.oppositeBank(this.raftLocation), setWithHead); // temp action
            this.possibleActions.add(new ActionStatePair(temp, this.applyAction(temp)));

            // add set to action state pair array if newset is valid
            if(((Set<Person>)set).size() < 1 ||
                    setWithHead.size() > RiverCrossProblem.RAFT_SIZE) continue;// check if set set is with in range
            if(this.checkForDriverAndWeight(((Set<Person>)set))) continue;
            temp=new RiverCrossAction(this.oppositeBank(this.raftLocation), ((Set<Person>)set)); // temp action
            this.possibleActions.add(new ActionStatePair(temp, this.applyAction(temp))); //
        }
        return sets;
    }

    private boolean checkForDriverAndWeight(Set c){
        Set<Person> config = new HashSet<>(c);
        boolean containsDriver = false;
        double weight = 0.0;
        for (Object p:
                config) {
            weight+= ((Person)p).getWeight();
            if(((Person)p).isDriver()) containsDriver=true;
            if(weight > RiverCrossProblem.RAFT_MAX_WEIGHT || weight <= 0) return true;
        }
        if(!containsDriver)return true;
        return false;
    }

    /**
     *
     * @param s
     */
    private void generateLightestDriverAction(Set<Person> s){
        Person lightestDriver = new Person("dummy", RiverCrossProblem.RAFT_MAX_WEIGHT, false);
        for (Person p:
             s) {
            if(p.isDriver() && p.getWeight() < lightestDriver.getWeight())lightestDriver = p;
        }
        if(lightestDriver.isDriver()){
            Set<Person> lightestPersonSet = new HashSet<>();
            lightestPersonSet.add(lightestDriver);
            RiverCrossAction action = new RiverCrossAction(this.oppositeBank(this.raftLocation), lightestPersonSet);
            this.possibleActions.add(new ActionStatePair(action, this.applyAction(action)));
        }
    }
    /**
     * Check if a state is invalid.
     * @return true if a state is invalid. Or false otherwise.
     */
    public boolean isInvalid(){
        // FIX 14/04/2018 toString: main if might need reworking
        if (((this.raftLocation == RiverBank.NORTH && this.northBankPopulation.isEmpty()) ||
                        (this.raftLocation == RiverBank.SOUTH && this.southBankPopulation.isEmpty()) ) )
            return true;
        //checks if there is a driver on the bank the raft is at.
        for (Person p:
                (this.raftLocation == RiverBank.NORTH)? this.northBankPopulation: this.southBankPopulation) {
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
        Set<Person> newNorthBank = new HashSet<>(this.northBankPopulation);
        Set<Person> newSouthBank = new HashSet<>(this.southBankPopulation);
        for(Person p : action.peopleCrossing) {
            if (action.toBank == RiverBank.NORTH) {
                newNorthBank.add(p);
                newSouthBank.remove(p);
            } else {
                newSouthBank.add(p);
                newNorthBank.remove(p);
            }
        }
        return new RiverCrossState(newNorthBank, newSouthBank, this.oppositeBank(this.raftLocation));
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