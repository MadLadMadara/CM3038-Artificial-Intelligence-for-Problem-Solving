
import java.util.*;
import java.util.stream.Collectors;

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
    public double southBankPopulationTotalWeight;
    /**
     *
     */
    public double northBankPopulationTotalWeight;
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
        northBankPopulationTotalWeight = 0.0;
        for (Person p:
                this.northBankPopulation) {
            this.northBankPopulationTotalWeight+=p.getWeight();
        }
        this.southBankPopulationTotalWeight = 0.0;
        for (Person p:
             this.southBankPopulation) {
            this.southBankPopulationTotalWeight+=p.getWeight();
        }
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

        result+="\nNorth Bank Population:"+this.northBankPopulation.toString();
        result+="\nSorth Bank Population:"+this.southBankPopulation.toString();
        // can be useful for debugging.
//        result+="\nNorth Bank Population detail:"+this.northBankPopulation.toString();
//        result+="\nSouth Bank Population detail:"+this.southBankPopulation.toString();

        // FIX: 14/04/2018 toString: could add number of drivers on each bank, it would help with debugging later
        return result+"\nNorth bank size:" + this.northBankPopulation.size() + "\nSouth bank size:" + this.southBankPopulation.size();

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
        //I chose to use an ArrayList object as the list will be short.
        if (this.isInvalid())//if current state is invalid
            return this.possibleActions;
        this.generatePossibleActions((this.raftLocation == RiverBank.NORTH)?this.northBankPopulation:this.southBankPopulation);

        return this.possibleActions;
    }//end method

    private Set<Set> generatePossibleActions(Set originalSet) {

        List<ActionStatePair> actions = new ArrayList<>();
        RiverCrossAction temp;
        Set<Set> sets = new HashSet<>();
        if (originalSet.isEmpty() || !this.possibleActions.isEmpty()) {
            sets.add(new HashSet());
            return sets;
        }
        List list = new ArrayList(originalSet);
        Object head = (Object)list.get(0);
        Set rest = new HashSet(list.subList(1, list.size()));

        Set newSet = new HashSet();
        for (Set set : generatePossibleActions(rest)) {
            if(set.isEmpty() && !((Person)head).isDriver()) continue;
            System.out.println(head.toString()+" "+set.toString());
            newSet.clear();
            newSet.add(head);
            newSet.addAll(set);
            if(this.isInvalidateBoatCongig(newSet))continue;
            sets.add(newSet);
            temp=new RiverCrossAction(this.oppositeBank(this.raftLocation), newSet);
            this.possibleActions.add(new ActionStatePair(temp, this.applyAction(temp)));
        }

        return sets;
    }

    private boolean isInvalidateBoatCongig(Set c){
        System.out.println(c);
        Set<Person> config = new HashSet<>(c);
        boolean containsDriver = false;
        if(config.size() < 1 ||
                config.size() > RiverCrossProblem.RAFT_SIZE){
            return true;
        }
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
     * Check if a state is invalid.
     * @return true if a state is invalid. Or false otherwise.
     */
    public boolean isInvalid(){
        // FIX 14/04/2018 toString: main if might need reworking
        if (((this.raftLocation == RiverBank.NORTH && this.northBankPopulation.isEmpty()) ||
                        (this.raftLocation == RiverBank.SOUTH && this.southBankPopulation.isEmpty()) ) )
            return true;

        /**
         * checks if there is a driver on the bank the raft is at.
         */
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
