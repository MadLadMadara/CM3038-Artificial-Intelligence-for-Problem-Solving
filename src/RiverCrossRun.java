import cm3038.search.*;
import java.util.*;

/**
 * River Cross Problem
 * @author Sam McRuvie
 */
public class RiverCrossRun {

    public static Set<Person> northBank, southBank;


    public static void main(String[] args){
        northBank = new HashSet<>();
        southBank = new HashSet<Person>();

        // set south bank
        southBank.add(new Person("Adam", 10, true));
        southBank.add(new Person("Betty", 20, false));
        southBank.add(new Person("Claire", 30, true));
        southBank.add(new Person("Dave", 40, false));

        // initalize inital and goal state, goal state is just north and south bank swapped
        RiverCrossState initialState = new RiverCrossState(northBank, southBank, RiverBank.SOUTH);
        RiverCrossState goalState = new RiverCrossState(southBank, northBank, RiverBank.NORTH);

        RiverCrossProblem.RAFT_SIZE = 2;
        RiverCrossProblem.RAFT_MAX_WEIGHT = 50;
        RiverCrossProblem problem=new RiverCrossProblem(initialState,goalState);
        System.out.println("Searching...");		//print some message
        Path path=problem.search();				//perform search, get result
        System.out.println("Done!");			//print some message
        System.out.println("Starting array problem "+southBank.toString());
        if (path==null)							//if it is null, no solution
            System.out.println("No solution");
        else	{
            path.print();							//otherwise print path
            System.out.println("Nodes visited: "+problem.nodeVisited);
            System.out.println("Cost: "+path.cost+"\n");

        }
    }

}