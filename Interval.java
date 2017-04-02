/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2016 
// PROJECT:          p4
// FILE:             Interval.java
//
// TEAM:    46 paras
// Authors: Daniel Jones
//////////////////////////// 80 columns wide //////////////////////////////////
/**
 * This class holds 2 T objects, which represent both the start and end of an
 * interval. It also holds 1 string, which is the name of the Interval object.
 * This class allows one to access the two T objects, and the label string. In 
 * addition, it also allows the user to find if another Interval overlaps with
 * it, if a point is within the interval, or compare it to another Interval.
 *
 * <p>Bugs: None so far
 *
 * @author Daniel Jones
 */
public class Interval<T extends Comparable<T>> implements IntervalADT<T> {

    T start; // The beginning of the interval
    T end;   // The end of the interval
    String label; //String that has the categorization of Interval

    public Interval(T start, T end, String label) {
        this.start = start; //defining this.start
        this.end = end;     //defining this.end
        this.label = label; //defining this.label
    }

    /**
     * This method allows access to the T object that represents the 
     * start of the interval
     *
     *
     * @param N/A
     * @return the T object that represents the start of the interval
     */
    @Override
    public T getStart() {
        return this.start; 
    }

    /**
     * This method allows access to the T object that represents the 
     * end of the interval
     *
     *
     * @param N/A
     * @return the T object that represents the end of the interval
     */
    @Override
    public T getEnd() {
        return this.end;  
    }

    /**
     * This method allows access to the String that represents the 
     * name of the interval
     *
     *
     * @param N/A
     * @return the String that represents the name of the interval
     */    
    @Override
    public String getLabel() {
        return this.label;  
    }

    /**
     * This method allows the user to check if another Interval overlaps
     * with this one
     *
     *
     * @param other an IntervalADT<T> object to be checked for overlap
     * @return true, if there is overlap, false if not
     */
    @Override
    public boolean overlaps(IntervalADT<T> other) {
        if(other.equals(null)){ //If other does not exist
        	throw new IllegalArgumentException(); //throw appropriate Exception
        }
        if(this.end.compareTo(other.getStart()) < 0 ||
        		this.start.compareTo(other.getEnd()) > 0){
        //if end is less than other's start, or start is less than other's end
        	return false;
        }
        return true; //if other does overlaps, then return true
    }

    /**
     * This method allows the user to see if a T object is within the 
     * Interval
     *
     *
     * @param point A T object
     * @return true, if point is in the interval, else false
     */
    @Override
    public boolean contains(T point) {
        if(this.end.compareTo(point) > 0 && this.start.compareTo(point) < 1){
        	return true; //If point is between end and start, return true
        }
        return false; //If point is not between end and start, return false
    }

    /**
     * This method allows the user to compare another Interval with this
     * one to see if they have the same Interval, or if one is less than
     * the other
     *
     *
     * @param other an IntervalADT<T> object to be compared
     * @return an integer that will be negative if Interval is less than other
     * 0 if both are equal, or positive if Interval is more than other
     */
    @Override
    public int compareTo(IntervalADT<T> other) {
        if(this.start.compareTo(other.getStart()) != 0){ //If starts not equal
        	return this.start.compareTo(other.getStart()); //return comparison
        }
        return this.end.compareTo(other.getEnd()); //Return ends' comparison
    }

}
