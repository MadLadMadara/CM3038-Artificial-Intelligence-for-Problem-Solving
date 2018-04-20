import cm3038.search.*;				//use some classes in the uninformed search library
import cm3038.search.informed.*;	//use the informed search library

import java.util.HashMap;


/**
 * This is a informed search problem specialised for the 8-Puzzle problem.
 * @author kit
 *
 */
public class RiverCrossProblem extends BestFirstSearchProblem {

    public static int RAFT_SIZE=3;

    public static double RAFT_MAX_WEIGHT = 50;

    /**
     * Construct a RiverCrossProblem object from the initial and goal state.
     * @param initialState	The initial state.
     * @param goalState		The goal state.
     */
    public RiverCrossProblem(State initialState, State goalState)
    {
        super(initialState,goalState);
    } //end method

    /**
     * The evaluation function required by an informed search.
     * @param node	The node to be evaluated.
     * @return The score of the node. The lower the score, the more promising the node.
     */
    public double evaluation(Node node){
        //***

        return heuristic(node.state) + node.getCost();
    } //end method

    /**
     * This heuristic function estimate how far this state is from a goal.
     * @return The remaining distance/cost of the current state to a goal.
     */
    public double heuristic(State currentState) {


        RiverCrossState current = (RiverCrossState)currentState;

        double driverWeight; // drivers weight

        int numberOfReturnTrips = 0; // estimate number of times the driver the driver will travil from north to south

        double southSumWeight = 0.0; // south bank sum weight

        double maxBoatWeightWithOutDriver; // max boat weight minus driver weight

        double southBankSumWeightWithOutDriver; // sum south bank weight minus driver weight

        if(current.raftLocation == RiverBank.NORTH){
            driverWeight = RiverCrossProblem.RAFT_MAX_WEIGHT + 1; //  driver weight unknown, set to the max weight of the boat
            numberOfReturnTrips=1; // driver will always need to make a return journey
            for (Person p:
                    current.northBankPopulation) { // loop through northbank population
                if(p.isDriver() && p.getWeight() < driverWeight)
                    driverWeight = p.getWeight(); // set driverWeight if p is a driver and is lighter than driverWeight
            }
            for (Person p:
                    current.southBankPopulation) { // loop through southbank population
                southSumWeight+=p.getWeight(); // get southbank total weight
            }
        }else{
            driverWeight = 0;
            int numDriver = 0;
            for (Person p:
                    current.southBankPopulation) { // loop through southbank population
                southSumWeight+=p.getWeight();// add driver weight
                if(p.isDriver()) {
                    numDriver++;
                    driverWeight += p.getWeight();
                }
            }
            driverWeight = driverWeight/numDriver; // set to the average driver weight of south bank
        }

        maxBoatWeightWithOutDriver = RiverCrossProblem.RAFT_MAX_WEIGHT - driverWeight;
        southBankSumWeightWithOutDriver = (int) southSumWeight - driverWeight;
        numberOfReturnTrips += ((int) (southBankSumWeightWithOutDriver/maxBoatWeightWithOutDriver)); // estimated amout of return trips for driver
        // number of return trips to make times the estimated drivers weight plus the weight still to be transfered (southbankweight)
        return numberOfReturnTrips*driverWeight+southSumWeight;
    } //end method

    /**
     * This isGoal testing method defines that the a state must be
     * equal to the goal state (as an attribute in the problem object)
     * to be a goal.
     */
    @Override
    public boolean isGoal(State state) {
        return state.equals(this.goalState);
    } //end method
} //end class
