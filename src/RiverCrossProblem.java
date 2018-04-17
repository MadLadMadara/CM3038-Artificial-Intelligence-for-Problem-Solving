import cm3038.search.*;				//use some classes in the uninformed search library
import cm3038.search.informed.*;	//use the informed search library

import java.util.HashMap;


/**
 * This is a informed search problem specialised for the 8-Puzzle problem.
 * @author kit
 *
 */
public class RiverCrossProblem extends BestFirstSearchProblem {

    public static int RAFT_SIZE=2;

    public static double RAFT_MAX_WEIGHT = 180;

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
    public double evaluation(Node node)
    {

        //
        //*** Update this evaluation function.
        //*** Currently it is doing Greedy best-first by using the heuristic alone.
        //*** i.e. estimate how far the given "node" is from a goal.
        //*** It does not take into consideration the cost from the root to "node" so far.
        //***
        return heuristic(node.state) + node.getCost();
    } //end method

    /**
     * This heuristic function estimate how far this state is from a goal.
     * @return The remaining distance/cost of the current state to a goal.
     */
    public double heuristic(State currentState) {
        // TODO: 16/04/2018 heuristic method

        RiverCrossState state = (RiverCrossState)currentState;

        double result = 0.0;
        for (Person p:
        state.southBankPopulation.values()) {
            result+=p.getWeight()*2000;
        }


        return result;
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
