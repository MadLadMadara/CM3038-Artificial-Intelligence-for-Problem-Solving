import cm3038.search.*;
import cm3038.search.informed.*;

import java.lang.reflect.Array;
import java.util.*;

/**
 * River Cross Problem
 * @author Sam McRuvie
 */
public class RiverCrossRun {

    public static HashMap<String, Person> northBank, southBank;


    public static void main(String[] args){
        RiverCrossProblem.RAFT_MAX_WEIGHT = 50;

        southBank = new HashMap<String, Person>();
        southBank.put("Sam", new Person("Sam", 10, true));
        southBank.put("Max", new Person("Max", 20, false));
        southBank.put("George", new Person("George", 30, false));
        southBank.put("Chrise", new Person("Chrise", 40, true));

        southBank.put("john", new Person("john", 10, true));
        southBank.put("jeff", new Person("jeff", 20, false));
        southBank.put("joe", new Person("joe", 30, false));
        southBank.put("bob", new Person("bob", 40, true));


        northBank = new HashMap<String, Person>();

        RiverCrossState initialState = new RiverCrossState(northBank, southBank, RiverBank.SOUTH);
        RiverCrossState goalState = new RiverCrossState(southBank, northBank, RiverBank.NORTH);

        RiverCrossProblem problem=new RiverCrossProblem(initialState,goalState);

        System.out.println("Searching...");		//print some message
        Path path=problem.search();				//perform search, get result
        System.out.println("Done!");			//print some message
        if (path==null)							//if it is null, no solution
            System.out.println("No solution");
        else	{
            path.print();							//otherwise print path
            System.out.println("Nodes visited: "+problem.nodeVisited);
            System.out.println("Cost: "+path.cost+"\n");

        }
    }

}