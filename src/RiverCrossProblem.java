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
        // TODO: 16/04/2018 heuristic method needs tinkering

        RiverCrossState state = (RiverCrossState)currentState;

        double result = 0.0;



        return result*2;
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
