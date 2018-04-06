/**
 * this class sets out the information need for a
 * person in the river cross problem
 * @author Sam McRuvie
 */
public class Person {
    /**
     * individuals name
     */
    final private String name;
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
     * initializes a person object with the given values
     * @param name of individual
     * @param weight of individual weight
     * @param driver can the individual drive the boat
     */
    public Person(String name, int weight, boolean driver) {
        this.name = name;
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

    /**
     * simple to string method of person
     * @return formated string
     */
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", driver=" + driver +
                '}';
    }
}