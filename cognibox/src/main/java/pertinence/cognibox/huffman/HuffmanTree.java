package pertinence.cognibox.huffman;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * 
 */
public class HuffmanTree {

	HuffmanNode 			root;
	TreeSet<HuffmanNode> 	leaves;
	
	HashMap<Object, HuffmanNode> data;

	/**
	 * @param data
	 *            maps objects to their frequencies; the huffman algorithm will
	 *            find a shorter code for more frequent objects.
	 */
	public HuffmanTree(Map<? extends Comparable<?>, Integer> data) {
		this(new Data(data));
	}
	
	
	public HuffmanTree(Data data) {
		leaves = new TreeSet<HuffmanNode>();
		this.data = new HashMap<Object, HuffmanNode>();
		
		for (Comparable<?> o : data.getMap().keySet()) {
			Integer weight = data.getMap().get(o);
			HuffmanNode leaf = new HuffmanNode(o, weight);
			leaves.add(leaf);
			this.data.put(o, leaf);
		}
		
		buildInternalTree();
	}

	public HuffmanNode getRoot() {
		return root;
	}

	public void setRoot(HuffmanNode root) {
		this.root = root;
	}

	public Set<HuffmanNode> getLeaves() {
		return leaves;
	}

	public void setLeaves(Set<HuffmanNode> leaves) {
		this.leaves = new TreeSet<HuffmanNode>();
	}

	/* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */
	private void buildInternalTree() {

		TreeSet<Object> leavesCopy = new TreeSet<Object>();
		
		for (HuffmanNode leaf : this.leaves)
			leavesCopy.add(leaf);
		
		if (leavesCopy.size() == 1) { 
			// special bizarre tree with one leaf only
			buildOneLeafOnlyTree(leavesCopy);
			return;
		}
		
		
		HuffmanNode newNode = null;
		
		int internalNodeCounter = 1;
		while (leavesCopy.size() > 1) {
			HuffmanNode minElem1 = (HuffmanNode) leavesCopy.first();
			leavesCopy.remove(minElem1);
			HuffmanNode minElem2 = (HuffmanNode) leavesCopy.first();
			leavesCopy.remove(minElem2);
			
			newNode = new HuffmanNode("_" + internalNodeCounter++, minElem1.getWeight() + minElem2.getWeight());
			newNode.setLeftChild(minElem1);
			newNode.setRightChild(minElem2);
			minElem1.setParent(newNode);
			minElem2.setParent(newNode);
			
			leavesCopy.add(newNode);
		}
		
		
		this.root = newNode;
	}

	private void buildOneLeafOnlyTree(TreeSet<Object> leavesCopy) {
		HuffmanNode leaf = (HuffmanNode) leavesCopy.first();
		HuffmanNode root = new HuffmanNode("_" + 1, leaf.getWeight());
		root.setLeftChild(leaf);
		leaf.setParent(root);
		this.root = root;
	}


	public String exportGraphViz() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph g3 {\n");
		//sb.append("node [style = filled] \n");
		
		for (HuffmanNode node: this.getAllNodes()) {
			HuffmanNode left = node.getLeftChild();
			if (left != null) 
				sb.append("\t" + quote(node.toString()) + " -> " + quote(left.toString()) + " [label=0]" + "\n");
			
			HuffmanNode right = node.getRightChild();
			if (right != null)
				sb.append("\t" + quote(node.toString()) + " -> " + quote(right.toString()) + " [label=1]" + "\n");
			
			if (left == null && right == null) {
				sb.append("\t" + quote(node.toString()) + " [style=filled, fillcolor = gray]\n");
			}
		}
		
		sb.append("}");
		
		return sb.toString();
	}

	protected List<HuffmanNode> getAllNodes() {
		return this.getAllNodesInternal(root);
	}
	
	protected List<HuffmanNode> getAllNodesInternal(HuffmanNode root) {
		
		ArrayList<HuffmanNode> allNodes = new ArrayList<HuffmanNode>();
		
		allNodes.add(root);
		
		HuffmanNode child1 = root.getLeftChild();
		HuffmanNode child2 = root.getRightChild();
		
		if (child1 != null) {
			List<HuffmanNode> childrenOfLeft = this.getAllNodesInternal(child1);
			allNodes.addAll(childrenOfLeft);
		}
		
		if (child2 != null) {
			List<HuffmanNode> childrenOfRight = this.getAllNodesInternal(child2);
			allNodes.addAll(childrenOfRight);
		}
		
		return allNodes;
	}
	
	/**
	 * find code (path) for a leaf, a program for a piece of data, a concept for an observation
	 */
	public Code encode(Object o) {
		if (!this.data.containsKey(o))
			throw new IllegalArgumentException("tree contains no information about object " + o);
		
		Stack<HuffmanNode> stack = new Stack<HuffmanNode>();
		HuffmanNode node = this.data.get(o);
		while (node != null) {
			stack.push(node);
			node = node.getParent();
		}
		
		BitSet bitset = new BitSet();
		int i = 0;
		HuffmanNode upNode = stack.pop();
		HuffmanNode downNode;
		while (! stack.isEmpty()) {
			downNode = stack.pop();
			boolean crtBit = upNode.getRightChild() == downNode ; 
			bitset.set(i, crtBit);
			
			i++;
			upNode = downNode;
		}
		
		return new Code(bitset, i);
	}
	
	/**
	 * is the given object in this tree?
	 */
	boolean contains(Object o) {
		return this.data.containsKey(o);
	}
	
	/**
	 * given a binary code, find the label of the node that corresponds
	 */
	public Object decode(Code c) {
		HuffmanNode node = this.root;
		for (int i = 0; i < c.getLength(); i++) {
			int bit = c.nextBit();
			try {
				node = ( bit == 1 ?  node.getRightChild() : node.getLeftChild());
				if (node.getLeftChild() == null && node.getRightChild() == null) // leaf node
					break;
			} catch (NullPointerException npe) {
				throw new IllegalStateException("code [" + c + "] is not legal in this huffman tree, does not lead to a leaf" );
			}
		}
		return node.label;
	}
	
	
	/**
	 * this is the opposite of portability, it only work on one of my machines, sometimes
	 */
	public void totallyUnportableSeeGraphvizRepresentation() throws IOException, InterruptedException {
		String gv = this.exportGraphViz();
		String tmpBasename = "/tmp/totallyUnportableSeeGraphvizRepresentation-" + RandomStringUtils.randomAlphabetic(20);
		
		File gvFile = new File(tmpBasename + ".gv");
		
		FileWriter fileWriter = new FileWriter(gvFile);
		IOUtils.write(gv, fileWriter);
		fileWriter.close();
		String cmdLine = "dot -Tpng " + tmpBasename + ".gv";
		Process exec = Runtime.getRuntime().exec(cmdLine );
		
		InputStream dotOutStream = exec.getInputStream();
		String pngPathname = tmpBasename + ".png";
		FileOutputStream pngOutstream = new FileOutputStream(pngPathname);
		IOUtils.copy(dotOutStream, pngOutstream);
		pngOutstream.close();
		
		exec.waitFor();
		LOG.info(cmdLine + " finished with code : " + exec.exitValue());
		
		//IOUtils.copy(dotOutStream, new FileInputStream(new File(tmpBasename + ".png")));
		Runtime.getRuntime().exec("eog " + pngPathname).waitFor();
		
		FileUtils.deleteQuietly(gvFile);
		FileUtils.deleteQuietly(new File(pngPathname));
 	}

	public HashMap<Object, HuffmanNode> getData() {
		return data;
	}

	
	private String quote(String s) { 
		return new StringBuilder("\"").append(s).append("\"").toString(); 
	}
	
	Logger LOG = Logger.getLogger(HuffmanTree.class);
}
