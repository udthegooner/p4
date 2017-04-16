/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2016 
// PROJECT:          p4
// FILE:             Interval.java
//
// TEAM:    46 Paras
// Authors: Yuchen Bai, Matt Perry, Udhbhav Gupta
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.ArrayList;
import java.util.List;

public class IntervalTree<T extends Comparable<T>> implements IntervalTreeADT<T> {

	private IntervalNode<T> root;
	private int size;
	private int height;

	// Constructor
	public IntervalTree() {
		root = null;
		size = 0;
		height = 0;
	}

	@Override
	public IntervalNode<T> getRoot() {
		return root;
	}

	@Override
	public void insert(IntervalADT<T> interval) {
		root = insertHelper(root,interval);
	}

	private IntervalNode<T> insertHelper(IntervalNode<T> node, IntervalADT<T> interval){
		if (interval == null) throw new IllegalArgumentException();
		
		if(node == null ) return new IntervalNode<T> (interval);
		
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

	@Override
	public void delete(IntervalADT<T> interval)
			throws IntervalNotFoundException, IllegalArgumentException {

		root = deleteHelper(root, interval);
	}

	@Override
	public IntervalNode<T> deleteHelper(IntervalNode<T> node, IntervalADT<T> interval)
			throws IntervalNotFoundException, IllegalArgumentException {
		// check if exceptions are thrown
		if (interval == null)
			throw new IllegalArgumentException();

		if (!contains(interval)|| node == null)
			throw new IntervalNotFoundException(interval.toString());

		if (node.getInterval() == interval){

			if (node.getLeftNode() == null&& node.getRightNode() == null){
				return null;
			}

			else if(node.getRightNode() != null){
				node = node.getSuccessor();
				node.setRightNode(deleteHelper(node.getSuccessor(),interval));
				node.setMaxEnd(recalculateMaxEnd(node));
			}
			else {
				return node.getLeftNode();
			}
		}
		//If interval is in the left subtree
		if (interval.compareTo(node.getInterval()) < 0){
			node.setLeftNode(deleteHelper(node.getLeftNode(), interval));
			node.setMaxEnd(recalculateMaxEnd(node));
		}
		// If interval is in the right subtree
		else{
			node.setRightNode(deleteHelper(node.getRightNode(), interval));
			node.setMaxEnd(recalculateMaxEnd(node));
		}

		return node;
	}

	private T recalculateMaxEnd(IntervalNode<T> nodeToRecalculate){

		return null;
	}

	@Override
	public List<IntervalADT<T>> findOverlapping(IntervalADT<T> interval) {

		// Stores our list of intervals
		List<IntervalADT<T>> list = new ArrayList<IntervalADT<T>>();

		// Return the list generated by the helper method.
		return findOverHelper(interval, root, list);
	}

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

	@Override
	public List<IntervalADT<T>> searchPoint(T point) {

		// Stores matching intervals
		List<IntervalADT<T>> list = new ArrayList<IntervalADT<T>>();

		// Calls our helper method.
		return searchPointHelper(point, root, list);
	}

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

	@Override
	public int getSize() {
		return getSizeHelper(root);
	}

	private int getSizeHelper(IntervalNode<T> node){
		if(node == null) return 0;
		return getSizeHelper(node.getLeftNode()) + getSizeHelper(node.getRightNode()) + 1;
	}

	@Override
	public int getHeight() {
		return getHeightHelper(root) - 1;
	}

	private int getHeightHelper(IntervalNode<T> n) {
		if (n == null) return 0;
		if (n.getLeftNode() == null && n.getRightNode() == null) return 1;
		else
		return Math.max(getHeightHelper(n.getLeftNode()), getHeightHelper(n.getRightNode()) + 1);
	}

	@Override
	public boolean contains(IntervalADT<T> interval)  {
		return containsHelper(interval, root);
	}

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

	@Override
	public void printStats() {
		System.out.println("-----------------------------------------");
		System.out.println("Height: " + getHeight());
		System.out.println("Size: " + getSize());
		System.out.println("-----------------------------------------");
	}

}
