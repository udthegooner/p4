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

	/**
	 * Delete the node containing the specified interval in the tree.
	 * Delete operations must also update the maxEnd of interval nodes
	 * that change as a result of deletion.  
	 *  
	 * <p>Tip: call <code>deleteHelper(root)</code> with the root node.</p>
	 * 
	 * @throws IllegalArgumentException if interval is null
	 * @throws IntervalNotFoundException if the interval does not exist.
	 */
	@Override
	public void delete(IntervalADT<T> interval)
			throws IntervalNotFoundException, IllegalArgumentException {

		if (interval == null) {
			throw new IllegalArgumentException();
		}

		root = deleteHelper(root, interval); 

	}


	/** 
	 * Recursive helper method for the delete operation.  
	 * 
	 * <p>Note: the maxEnd of some interval nodes may also need to change
	 * as a result of an interval's deletion.</p>
	 * 
	 * <p>Note: the key for comparison here is the start of the interval
	 * stored at each IntervalNode.</p>
	 * 
	 * <p>Tip: write a non-recursive helper method that recalculates maxEnd for 
	 * any node based on the maxEnd of its child nodes</p>
	 * 
	 * <pre>      private T recalculateMaxEnd(IntervalNode&lt;T&gt; nodeToRecalculate)</pre>
	 * 
	 * <h3>Pseudo-code for this deleteHelper method:</h3>
	 *
	 * <ul>
	 * <li>If node is null, throw IntervalNotFoundException</li>
	 * <li>If interval is found in this node, delete it and replace it 
	 * with leftMost in right subtree.  There are two cases:
	 * 
	 * <ol><li>If right child exists
	 *     <ol><li>Replace the node's interval with the in-order successor interval. 
	 *     <br />Tip: Be sure to code the and use the <code>getSuccessor</code> method for <code>IntervalNode</code> class.</li>
	 *         <li>Call deleteHelper() on the in-order successor node of the right subtree.</li>
	 *         <li>Update the new maxEnd.</li>
	 *         <li>Return the node.</li>
	 *     </ol>
	 *     </li>
	 *     
	 *     <li>If right child doesn't exist, return the left child</li>
	 * </ol>
	 * 
	 * <li>If interval is in the right subtree,
	 *      <ol>
	 *	    <li>Set right child to result of calling deleteHelper on right child.</li>
	 *	    <li>Update the maxEnd if necessary. </li>
	 *      <li>Return the node.</li>
	 *      </ol>
	 *      </li>
	 *
	 * <li>If interval is in the left subtree.
	 *      <ol>
	 *	    <li>Set left child to result of calling deleteHelper on left child.</li>
	 *	    <li>Update the maxEnd if necessary. </li>
	 *      <li>Return the node.</li>
	 *      </ol>
	 *      </li>
	 *  </ul>
	 *
	 * @param node the interval node that is currently being checked.
	 * 
	 * @param interval the interval to delete.
	 * 
	 * @throws IllegalArgumentException if the interval is null.
	 * 
	 * @throws IntervalNotFoundException
	 *             if the interval is not null, but is not found in the tree.
	 * 
	 * @return Root of the tree after deleting the specified interval.
	 */
	@Override
	public IntervalNode<T> deleteHelper(IntervalNode<T> node, IntervalADT<T> interval)
			throws IntervalNotFoundException, IllegalArgumentException {

		// if the node is null
		if (node == null) {
			throw new IntervalNotFoundException(interval.toString());
		}

		// locate the node (containing the interval) that you want to delete

		//key for comparison is the start of interval stored at each IntervalNode
		if (node.getInterval() == interval) {
			//node is the node to be removed

			//node is a leaf; replace with null
			if (node.getLeftNode() == null && node.getRightNode() == null) {
				node = null;
			}

			//right child exists
			if (node.getRightNode() != null) {
				//Replace the node's interval with the in-order successor interval
				node.setInterval(node.getSuccessor().getInterval());

				//Call deleteHelper() on the in-order successor node of the right subtree
				node = deleteHelper(node.getSuccessor(), interval);

				//Update the new maxEnd
				node.setMaxEnd(recalculateMaxEnd(node));
			}
			//If right child doesn't exist, return the left child
			else {
				return node.getLeftNode();
			}


			//Case 1: interval is in the left subtree
			if (interval.compareTo(node.getInterval()) < 0){
				node.setLeftNode(deleteHelper(node.getLeftNode(), interval));
				node.setMaxEnd(recalculateMaxEnd(node));
			}
			// If interval is in the right subtree
			else{
				node.setRightNode(deleteHelper(node.getRightNode(), interval));
				node.setMaxEnd(recalculateMaxEnd(node));
			}
		}
		return node;
	}

	private T recalculateMaxEnd(IntervalNode<T> nodeToRecalculate){
		T newMaxEnd = nodeToRecalculate.getInterval().getEnd();
		IntervalNode<T> traceNode;
		
		if(nodeToRecalculate.getLeftNode() != null){
			if(nodeToRecalculate.getLeftNode().getInterval().getEnd().compareTo( 
				nodeToRecalculate.getInterval().getEnd()) > 0) {
				newMaxEnd = nodeToRecalculate.getLeftNode().getInterval().getEnd();
			}
		}
			
		if(nodeToRecalculate.getRightNode() != null) {	
			if(nodeToRecalculate.getRightNode().getInterval().getEnd().compareTo( 
				nodeToRecalculate.getInterval().getEnd()) > 0) {
				newMaxEnd = nodeToRecalculate.getRightNode().getInterval().getEnd();
				}
			}
		nodeToRecalculate.setMaxEnd(newMaxEnd);
		return newMaxEnd;

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
				list = searchPointHelper(point, n.getLeftNode(), list);
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
