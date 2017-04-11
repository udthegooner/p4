/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2016 
// PROJECT:          p4
// FILE:             Interval.java
//
// TEAM:    46 paras
// Authors: Daniel Jones Collin Lacy
//////////////////////////// 80 columns wide //////////////////////////////////

/**
 * This class holds 2 T objects, which represent both the start and end of an
 * interval. It also holds 1 string, which is the name of the Interval object.
 * 
 * This class allows one to access the two T objects, and the label string. In 
 * addition, it also allows the user to find if another Interval overlaps with
 * it, if a point is within the interval, or compare it to another Interval.
 *
 * @author Daniel Jones Collin Lacy
 */
public class Interval<T extends Comparable<T>> implements IntervalADT<T> {

	//Start of interval
	T start;
	//End of interval
	T end;
	//Holds category of interval
	String label;

	/**
	 * Constructor for Interval class
	 * 
	 * No setter methods for the data members of Interval type so 
	 * they must be passed in as arguments to the constructor and 
	 * saved as data members accordingly.
	 */
	public Interval(T start, T end, String label) {
		this.start = start;
		this.end = end;
		this.label = label;
	}

	/**
	 * This method allows access to the T object that represents the 
	 * start of the interval
	 *
	 * @return the start value (must be Comparable<T>) of the interval
	 */
	@Override
	public T getStart() {
		return this.start;
	}

	/**
	 * This method allows access to the T object that represents the 
	 * end of the interval
	 *
	 * @return Returns the end value (must be Comparable<T>) of the interval
	 */
	@Override
	public T getEnd() {
		return this.end;
	}

	/**
	 * This method allows access to the String that represents the 
	 * name of the interval
	 *
	 * @return Returns the label for the interval
	 */
	@Override
	public String getLabel() {
		return this.label; 
	}

	/**
	 * This method allows the user to check if another Interval overlaps
	 * with this one
	 *
	 * @param other target interval to compare for overlap
	 * @return true if it overlaps, false otherwise.
	 * @throws IllegalArgumentException
	 *         if the other interval is null.
	 */
	@Override
	public boolean overlaps(IntervalADT<T> other) throws IllegalArgumentException {
		//If null, throw exception
		if (other.equals(null)) {
			throw new IllegalArgumentException();
		}
		//If (b < c) or (a > d), no overlap, return false
		if (end.compareTo(other.getStart()) < 0 
				|| start.compareTo(other.getEnd()) > 0) {
			return false;
		}
		return true;
	}

	/**
	 * This method allows the user to see if a T object is within the 
	 * Interval
	 *
	 * @param point A T object
	 * @return true, if point is in the interval, else false
	 */
	@Override
	public boolean contains(T point) {
		if (end.compareTo(point) > 0 && start.compareTo(point) < 1) {
			return true;
		}
		return false;
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
		if (start.compareTo(other.getStart()) != 0) { 
			return start.compareTo(other.getStart());
		}
		return end.compareTo(other.getEnd()); 
	}
}