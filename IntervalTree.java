/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2016 
// PROJECT:          p4
// FILE:             IntervalTree.java
//
// TEAM:    46 paras
// Authors: Daniel Jones
//////////////////////////// 80 columns wide //////////////////////////////////

/**
 * This class holds 3 objects. An IntervalNode called root, which is the source
 * node for the tree. Two ints, size, the number of nodes in the tree, 
 * and height, or how long the longest path of nodes is from root.
 * 
 *  This class allows one to access root, size, and height. It also allows one to
 *  delete a node in the tree, insert one, find if the interval of two nodes
 *  overlap, find if the tree already contains an interval, and print the stats
 *  of the tree.
 *  @author Daniel Jones
 */
import java.util.List;

public class IntervalTree<T extends Comparable<T>> implements IntervalTreeADT<T> {
	
	IntervalNode<T> root;
	int size;
	int height;

	@Override
	public IntervalNode<T> getRoot() {
		if(root == null){
			return null;
		}
		return root;
	}

	@Override
	public void insert(IntervalADT<T> interval)
					throws IllegalArgumentException {
		

	}

	@Override
	public void delete(IntervalADT<T> interval)
					throws IntervalNotFoundException, IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public IntervalNode<T> deleteHelper(IntervalNode<T> node,
					IntervalADT<T> interval)
					throws IntervalNotFoundException, IllegalArgumentException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<IntervalADT<T>> findOverlapping(
					IntervalADT<T> interval) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<IntervalADT<T>> searchPoint(T point) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean contains(IntervalADT<T> interval) {
		if(interval.compareTo(root.getInterval()) == 0){
			return true;
		}
		if(interval.compareTo(root.getInterval()) < 0){
			if(root.getLeftNode() != null){
				return contains(interval, root.getLeftNode());
			}
			return false;
		}
		if(interval.compareTo(root.getInterval()) > 0){
			if(root.getRightNode() != null){
				return contains(interval, root.getRightNode());
			}
			return false;
		}
		return false;
	}
	
	private boolean contains(IntervalADT<T> interval, IntervalNode<T> curr){
		if(interval.compareTo(curr.getInterval()) == 0){
			return true;
		}
		if(interval.compareTo(curr.getInterval()) < 0){
			if(curr.getLeftNode() != null){
				return contains(interval, curr.getLeftNode());
			}
			return false;
		}
		if(interval.compareTo(root.getInterval()) > 0){
			if(curr.getRightNode() != null){
				return contains(interval, curr.getRightNode());
			}
			return false;
		}
		return false;
	}

	@Override
	public void printStats() {
		// TODO Auto-generated method stub

	}

}
