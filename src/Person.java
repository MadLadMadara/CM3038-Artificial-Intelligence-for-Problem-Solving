/**
 * this class sets out the information need for a
 * person in the river cross problem
 * @author Sam McRuvie
 */
public class Person {
    /**
     * the individual weight
     * once set this value will not need to change
     */
    final private int weight;

    /**
     * the individual ability to drive
     * once set this value will not need to change
     */
    final private boolean driver;

    /**
     * initilizes a person object with the given values
     * @param weight of individual weight
     * @param driver can the individual drive the boat
     */
    public Person(int weight, boolean driver) {
        this.weight = weight;
        this.driver = driver;
    }
    /**
     * getter for individual weight
     * @return the weight of the individual
     */
    public int getWeight() {
        return weight;
    }
    /**
     * check to see if the individual can drive
     * @return if true the individual can drive
     */
    public boolean isDriver() {
        return driver;
    }
}
