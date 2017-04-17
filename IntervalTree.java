/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2016 
// PROJECT:          p4
// FILE:             IntervalTree.java
//
// TEAM:    46 Paras
// Authors: Udhbhav Gupta Collin Lacy Daniel Jones Matthew Perry Yuchen Bai
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for holding the root, size and height of the interval
 * tree.  This class also inserts and deletes nodes, as well as finding 
 * overlapping nodes.
 *
 * @author Udhbhav Gupta Collin Lacy Daniel Jones Matthew Perry Yuchen Bai
 */
public class IntervalTree<T extends Comparable<T>> implements IntervalTreeADT<T> {

	private IntervalNode<T> root; //root of the tree
	private int size; //number of nodes in the tree
	private int height; //number of nodes in the longest path from the root

	/**
	 * This is the constructor for the IntervalTree, it sets root to null and
	 * size and height to 0.
	 *
	 */
	public IntervalTree() {
		root = null;
		size = 0;
		height = 0;
	}

	/**
	 * This method returns the root of the tree
	 *
	 * @return the root of the tree
	 */
	public IntervalNode<T> getRoot() {
		return root;
	}

	/**
	 * This method inserts a new node into the correct location in the interval
	 * tree.
	 *
	 * @param interval to insert
	 */
	public void insert(IntervalADT<T> interval) {
		root = insertHelper(root,interval);
	}

	/**
	 * This recursive method inserts a new node into the correct location in 
	 * the interval tree.
	 *
	 * @param node to compare to
	 * @param interval to compare against
	 * @return the node that was inserted.
	 */
	private IntervalNode<T> insertHelper(IntervalNode<T> node, IntervalADT<T> interval){
		if (interval == null) throw new IllegalArgumentException();
		
		if(node == null ) return new IntervalNode<T> (interval);
		
		//if interval already exists
		if(node.getInterval().compareTo(interval) == 0) throw new IllegalArgumentException();
		
		//change the maxend
		if(node.getMaxEnd().compareTo(interval.getEnd())<0)
			node.setMaxEnd(interval.getEnd());
			
		if(interval.compareTo(node.getInterval())<0){
			//add interval to the left subtree
			node.setLeftNode(insertHelper(node.getLeftNode(),interval));
		}
		else{
			//add interval to the right subtree
			node.setRightNode(insertHelper(node.getRightNode(),interval));
		}
		return node;
	}

	/**
	 * This method deletes a node then reorganizes the interval tree.
	 *
	 * @param An interval to delete
	 * @throws IntervalNotFoundException
	 * @throws IllegalArgumentException
	 */
	public void delete(IntervalADT<T> interval)
			throws IntervalNotFoundException, IllegalArgumentException {

		if (interval == null) throw new IllegalArgumentException();
		root = deleteHelper(root, interval);
	}

	/**
	 * This method recursively deletes then reorganizes the interval tree.
	 *
	 * @param a node to check
	 * @param an interval to delete.
	 * @return the node to be deleted.
	 * @throws IllegalArgumentException
	 */
	public IntervalNode<T> deleteHelper(IntervalNode<T> node, IntervalADT<T> interval)
			throws IntervalNotFoundException, IllegalArgumentException {

		// if the node is null
		if (node == null) throw new IntervalNotFoundException(interval.toString());
		
		//if interval is found
		if (node.getInterval().compareTo(interval) == 0) {
			
			//node with no right child
			if (node.getRightNode() == null)
				node = node.getLeftNode();
			
			//node with right child
			else{
				IntervalNode<T> successor = node.getSuccessor();
				
				//replace the node interval with successor interval
				node.setInterval(successor.getInterval());
				
				//delete successor from right subtree
				node.setRightNode(deleteHelper(node.getRightNode(), successor.getInterval()));
				
				//recalculate max end of node
				node.setMaxEnd(recalculateMaxEnd(node));
			}
			return node;
		}
		
		//if node interval is greater than interval, move left
		else if (node.getInterval().compareTo(interval) > 0){
			if (node.getLeftNode() == null) 
				throw new IntervalNotFoundException(interval.toString());
			
			node.setLeftNode(deleteHelper(node.getLeftNode(), interval));
			node.setMaxEnd(recalculateMaxEnd(node));
			
			return node;
		}
		
		//if node interval is less than interval,move right
		else{
			if (node.getRightNode() == null) 
				throw new IntervalNotFoundException(interval.toString());
			
			node.setRightNode(deleteHelper(node.getRightNode(),interval));
			node.setMaxEnd(recalculateMaxEnd(node));
			
			return node;
		}
	}

	/**
	 * This method recalculates the maximum end of a branch of the interval
	 * tree.
	 *
	 * @param a node to calculate the maximum end of.
	 * @return the maximum end of the branch of the interval tree.
	 */
	private T recalculateMaxEnd(IntervalNode<T> node){
		//node has no children
		if (node.getLeftNode() == null && node.getRightNode() == null) 
			return node.getInterval().getEnd();
		
		T higherMaxEnd; //higher value of max ends of children
		
		//node has only right child
		if (node.getLeftNode() == null)
			higherMaxEnd = node.getRightNode().getMaxEnd();
		
		//node has only left child
		else if (node.getRightNode() == null)
			higherMaxEnd = node.getLeftNode().getMaxEnd();
		
		//node has both children
		else {
			T lMaxEnd = node.getLeftNode().getMaxEnd();
			T rMaxEnd = node.getRightNode().getMaxEnd();
			
			if (lMaxEnd.compareTo(rMaxEnd) > 0)
				higherMaxEnd = lMaxEnd;
			else
				higherMaxEnd = rMaxEnd;
		}
		
		//if higher of max end of children is greater than end of interval of node
		if (node.getInterval().getEnd().compareTo(higherMaxEnd) < 0)
			return higherMaxEnd;
		else
			return node.getInterval().getEnd();
	}
	

	/**
	 * This method returns a list of overlapping nodes given an interval.
	 *
	 * @param an interval to check overlaps against.
	 * @return a list of overlapping intervals.
	 */
	public List<IntervalADT<T>> findOverlapping(IntervalADT<T> interval) {

		// Stores our list of intervals
		List<IntervalADT<T>> list = new ArrayList<IntervalADT<T>>();

		// Return the list generated by the helper method.
		return findOverHelper(interval, root, list);
	}

	/**
	 * This method returns a list of overlapping nodes given an interval.
	 *
	 * @param interval to compare against.
	 * @param a node to compare.
	 * @param a list to add overlapping intervals to.
	 * @return a list of overlapping intervals.
	 */
	private List<IntervalADT<T>> findOverHelper(IntervalADT<T> interval, IntervalNode<T> n, List<IntervalADT<T>> list) {

		if (n == null) return list; //base case
		
		// If the interval is contained in the nodes interval, add it to the list.
		if (interval.overlaps(n.getInterval()))
			list.add(n.getInterval());
		
		if (n.getLeftNode() != null)
			if (n.getLeftNode().getMaxEnd().compareTo(interval.getStart()) >= 0)
				list = findOverHelper(interval, n.getLeftNode(), list);
		if (n.getRightNode() != null)
			if (n.getRightNode().getMaxEnd().compareTo(interval.getStart()) >= 0)
				list = findOverHelper(interval, n.getRightNode(), list);
		return list;
	}

	/**
	 * This method returns a list of intervals that contain a given point.
	 *
	 * @param a point to compare against
	 * @return a list of intervals containing the given point.
	 */
	public List<IntervalADT<T>> searchPoint(T point) {

		// Stores matching intervals
		List<IntervalADT<T>> list = new ArrayList<IntervalADT<T>>();

		// Calls our helper method.
		return searchPointHelper(point, root, list);
	}

	/**
	 * This method returns a list of intervals that contain a given point
	 *
	 * @param A point to check within the interval.
	 * @param A node to check
	 * @param a list of intervals that ovrlap
	 * @return a list of intervals containing the given point.
	 */
	private List<IntervalADT<T>> searchPointHelper(T point, IntervalNode<T> n, List<IntervalADT<T>> list) {

		if (n == null) return list; //base case
		
		if (n.getInterval().contains(point))
			list.add(n.getInterval());
		
		if (n.getLeftNode() != null)
			if (n.getLeftNode().getMaxEnd().compareTo(point) >= 0)
				list = searchPointHelper(point, n.getLeftNode(), list);;
		if (n.getRightNode() != null)
			if (n.getRightNode().getMaxEnd().compareTo(point) >= 0)
				list = searchPointHelper(point, n.getRightNode(), list);
		return list;
	}

	/**
	 * Returns size of the tree.
	 *
	 * @return size of the tree.
	 */
	public int getSize() {
		return getSizeHelper(root);
	}

	/**
	 * Returns the size of the tree.
	 * 
	 * @param a node that you want the size of
	 * @return size of the tree
	 *
	 */
	private int getSizeHelper(IntervalNode<T> node){
		if(node == null) return 0;
		return getSizeHelper(node.getLeftNode()) + getSizeHelper(node.getRightNode()) + 1;
	}

	/**
	 * Returns height of the tree
	 *
	 * @return height of the tree.
	 */
	public int getHeight() {
		return getHeightHelper(root);
	}

	/**
	 * Returns height of the tree
	 *
	 * @param a node that you want the height of
	 * @return height of the tree.
	 */
	private int getHeightHelper(IntervalNode<T> n) {
		if (n == null) return 0;
		return Math.max(getHeightHelper(n.getLeftNode()), getHeightHelper(n.getRightNode())) + 1;
	}

	/**
	 * Returns true if the given interval is contained in the tree.
	 *
	 * @param the interval to check
	 * @return true is the interval is in the tree.
	 * @throws IllegalArgumentException
	 */
	public boolean contains(IntervalADT<T> interval)  {
		return containsHelper(interval, root);
	}

	/**
	 * Returns true if the given interval is contained in the tree.
	 *
	 * @param the interval to check
	 * @param the node to check
	 * @return true is the interval is in the tree.
	 * @throws IllegalArgumentException
	 */
	private boolean containsHelper(IntervalADT<T> interval, IntervalNode<T> n) throws IllegalArgumentException {

		if (interval == null) throw new IllegalArgumentException();

		if(n == null) return false;

		if (interval.compareTo(n.getInterval()) == 0)
			return true;
		
		if (interval.compareTo(n.getInterval()) < 0) 
			return containsHelper(interval, n.getLeftNode());
		
		if (interval.compareTo(n.getInterval()) > 0)
			return containsHelper(interval, n.getRightNode());

		return false;
	}

	/**
	 * Prints out the height and size of the tree.
	 *
	 */
	public void printStats() {
		System.out.println("-----------------------------------------");
		System.out.println("Height: " + getHeight());
		System.out.println("Size: " + getSize());
		System.out.println("-----------------------------------------");
	}

}
