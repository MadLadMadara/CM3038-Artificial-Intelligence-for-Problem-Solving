
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


    /**
     * Create a McState object with the given initial values.
     * @param northBankPopulation hash map of people in the north bank.
     * @param southBankPopulation hash map of people in the south bank.
     * @param raft The location of the raft as defined.
     * @param currentFuelUsage current fuel usage up to this point
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
        String result = "";
        if (raftLocation==RiverBank.NORTH)
            result+="\nRaft location: North";
        if (raftLocation==RiverBank.SOUTH)
            result+="\nRaft location: South";

        result+="\nNorth Bank Population:"+this.northBankPopulation.keySet().toString();
        result+="\nNorth Bank Population detail:"+this.northBankPopulation.toString();
        result+="\nSorth Bank Population:"+this.southBankPopulation.keySet().toString();
        result+="\nSouth Bank Population detail:"+this.southBankPopulation.toString();

        // FIX: 14/04/2018 toString: could add number of drivers on each bank, it would help with debugging later
        return result+"\nFuel usage:" + "\nNorth bank size:" + this.northBankPopulation.size() + "\nSouth bank size" + this.southBankPopulation.size();

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
        // temp hashmap for people keys crossing
        HashMap<String, Person> peopleCrossing = new HashMap<String, Person>();
        // the populating hashmap of which ever bank the raft is located
        HashMap<String, Person> bankPopulationWithRaft = (this.raftLocation == RiverBank.NORTH)? (HashMap<String, Person>)this.northBankPopulation.clone():(HashMap<String, Person>)this.southBankPopulation.clone();
        // the driver populating hashmap of which ever bank the raft is located
        HashMap<String, Person> bankDriverPopulationWithRaft = this.driversOnBank(bankPopulationWithRaft);

        // all drivers can travel across
        for (Map.Entry<String, Person> driver:
                bankDriverPopulationWithRaft.entrySet()) {

            peopleCrossing.put(driver.getKey(), driver.getValue());
            tempAction = new RiverCrossAction(this.oppositeBank(this.raftLocation), peopleCrossing);
            result.add(new ActionStatePair(tempAction, this.applyAction(tempAction)));

           // gets the weight of the driver
            /**
             * if raft is south work out the configuration of prople that can be added to the beat
             */
            if(this.raftLocation == RiverBank.SOUTH){

                HashMap<String, Person> tempPopulation =  (HashMap<String, Person>) bankPopulationWithRaft.clone();
                tempPopulation.remove(driver.getKey());
                Set<Set> bankPopulationPowerset = populationPowerSet(tempPopulation.keySet()); // get the powerset of hashmap keys


                // loop over all possible boat configura
                for (Set boatConfiguration:
                        bankPopulationPowerset) {

                    if (!boatConfiguration.isEmpty()){
                        double boatWeight = driver.getValue().getWeight();
                        System.out.println("driver"+boatWeight);
                        // loop over all people in boat configuration
                        peopleCrossing.clear();

                        for (Object stringPersonKey:
                                boatConfiguration) {

                            boatWeight += tempPopulation.get(stringPersonKey).getWeight();

                            if((boatWeight <= RiverCrossProblem.RAFT_MAX_WEIGHT && boatWeight > 0) ||
                                    (boatConfiguration.size() < RiverCrossProblem.RAFT_SIZE && boatConfiguration.size() > 0)) {

                                peopleCrossing.put((String) stringPersonKey, bankPopulationWithRaft.get((String) stringPersonKey));
                            }else{
                                boatWeight = driver.getValue().getWeight();
                                peopleCrossing.clear();
                                break;
                            }
                            peopleCrossing.put((String) stringPersonKey, bankPopulationWithRaft.get((String) stringPersonKey));
                        }

                        peopleCrossing.put(driver.getKey(), bankDriverPopulationWithRaft.get(driver.getKey()) );
                        System.out.println(boatWeight);
                        tempAction = new RiverCrossAction(this.oppositeBank(this.raftLocation), peopleCrossing);
                        result.add(new ActionStatePair(tempAction, this.applyAction(tempAction)));

                    }
                }
            }

        }


        return result;
    }//end method

    private HashMap<String, Person> driversOnBank(HashMap<String, Person> bank){
        HashMap<String, Person> driversOnBank = new HashMap<>();
        for(Map.Entry<String, Person> entry : bank.entrySet()) {
            if (entry.getValue().isDriver()) driversOnBank.put(entry.getKey(), entry.getValue());
        }
        return driversOnBank;
    }

    private Set<Set> populationPowerSet(Set originalSet) {
        Set<Set> sets = new HashSet<Set>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet());
            return sets;
        }
        List list = new ArrayList(originalSet);
        String head = (String)list.get(0);
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
        HashMap<String, Person> newSouthBank = (HashMap<String, Person>)this.southBankPopulation.clone();

        for(Map.Entry<String, Person> person : action.peopleCrossing.entrySet()) {
            if (action.toBank == RiverBank.NORTH) {
                newNorthBank.put(person.getKey(), person.getValue());
                newSouthBank.remove(person.getKey());
            } else {
                newSouthBank.put(person.getKey(), person.getValue());
                newNorthBank.remove(person.getKey());
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
