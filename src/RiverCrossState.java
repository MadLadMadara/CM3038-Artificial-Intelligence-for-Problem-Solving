
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
         * hashMap equals ignore the ordering of the hash maps
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

        List<ActionStatePair> result=new ArrayList<ActionStatePair>();		//I chose to use an ArrayList object as the list will be short.

        if (this.isInvalid())				//if current state is invalid
            return result;

        // temp temp RiverCrossAction object
        RiverCrossAction tempAction;
        // the populating hashmap of which ever bank the raft is located
        Set<Person> bankPopulationWithRaft = (this.raftLocation == RiverBank.NORTH)? new HashSet<>(this.northBankPopulation): new HashSet<>(this.southBankPopulation);

        // the driver populating hashmap of which ever bank the raft is located
        Set<Person> bankDriverPopulationWithRaft = this.driversOnBank(bankPopulationWithRaft);

        // all drivers can travel across
        for (Person driver:
                bankDriverPopulationWithRaft) {
           // gets the weight of the driver
            /**
             * if raft is south work out the configuration of prople that can be added to the beat
             */
            if(this.raftLocation == RiverBank.SOUTH){
                HashSet<Person> tempPopulation = new HashSet<>(bankPopulationWithRaft);
                Set<Set> bankPopulationPowerset = this.safeBoatConfiguration(populationPowerSet(tempPopulation), driver); // get the powerset of populationBank with out driver
                for (Set s:
                    bankPopulationPowerset) {
                    tempAction = new RiverCrossAction(this.oppositeBank(this.raftLocation), new HashSet<>(s));
                    result.add(new ActionStatePair(tempAction, this.applyAction(tempAction)));
                }
            }else{
                Set<Person> d = new HashSet<>();
                d.add(driver);
                tempAction = new RiverCrossAction(this.oppositeBank(this.raftLocation), new HashSet<>(d));
                result.add(new ActionStatePair(tempAction, this.applyAction(tempAction)));
            }

        }


        return result;
    }//end method
    public Set<Set> safeBoatConfiguration(Set<Set> s, Person driver){
        Set<Set> returns = new HashSet<>();
        for (Set config:
             s) {
            boolean pass = true;
            if (!config.contains(driver)) continue;
            if(config.size() < 1 ||
                    config.size() > RiverCrossProblem.RAFT_SIZE){
                // System.out.println("T S"+s.size());
                continue;
            }
            double weight = 0.0;
            for (Object p:
                    config) {
                weight+= ((Person)p).getWeight();
                if(weight > RiverCrossProblem.RAFT_MAX_WEIGHT || weight < 0.0){
                    //  System.out.println("F W"+weight+"S"+s.size());
                    pass=false;
                    break;
                }
            }
            // System.out.println("T W"+weight+"S"+s.size());
            if(pass)returns.add(config);
        }
        return returns;
    }

    private Set<Person> driversOnBank(Set<Person> bank){
        return bank.stream().filter(p -> p.isDriver()).collect(Collectors.toSet());
    }

    private Set<Set> populationPowerSet(Set originalSet) {
        Set<Set> sets = new HashSet<Set>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet());
            return sets;
        }
        List list = new ArrayList(originalSet);
        Object head = (Object)list.get(0);
        Set rest = new HashSet(list.subList(1, list.size()));
        for (Set set : populationPowerSet(rest)) {
            Set newSet = new HashSet();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
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
            // do what you have to do here

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
