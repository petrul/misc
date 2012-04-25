package pertinence.cognibox.huffman;

import junit.framework.Assert;

/**
 * @author dadi
 */
public class HuffmanNode implements Comparable<HuffmanNode>{
	
	int weight;
	Comparable<?> label;
	
	HuffmanNode parent;
	HuffmanNode leftChild;
	HuffmanNode rightChild;

	
	public HuffmanNode(Comparable<?> label, int weight) {
		Assert.assertNotNull(label);
		
		this.weight = weight;
		this.label = label;
	}
	
	public HuffmanNode(int weight) {
		this.setWeight(weight);
	}
	
	/* accessors */
	public HuffmanNode getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(HuffmanNode leftChild) {
		this.leftChild = leftChild;
	}

	public HuffmanNode getRightChild() {
		return rightChild;
	}

	public void setRightChild(HuffmanNode rightChild) {
		this.rightChild = rightChild;
	}

	public HuffmanNode getParent() {
		return parent;
	}

	public void setParent(HuffmanNode parent) {
		this.parent = parent;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return the actual object stored in this {@link HuffmanNode}
	 */
	public Comparable<?> getLabel() {
		return label;
	}
	
	@Override
	public int compareTo(HuffmanNode node) {
		int wdiff = this.weight - node.weight;
		if (wdiff != 0) return wdiff;

		if (this.label != null) {
			if (node.label != null)
				return this.label.toString().compareTo(node.label.toString());
			else
				return 1;
		} else {
			if (node.label != null)
				return -1;
			else
				return 0;
		}
	}

	@Override
	public String toString() {
		String labelToString = label != null ? label.toString() + "|": "" ; 
		return labelToString + this.weight;
	}
}
