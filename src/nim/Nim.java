/*
 * NIM game.
 * Version 1.
 * Copyright: Khoa Pham
 */



package nim;
import java.util.*;
import java.io.*;


public class Nim {
	public Node currentNode;
	
	public int setDepth = 0;
	
	/*
	 * Construction Nim.
	 */
	public Nim() { }
	
	/*
	 * Initialize Nim by console input.
	 */
	public Node initNim() {
		Node root = new Node();
		int piles;
		Scanner scan1 = new Scanner(System.in);
		Scanner scan2 = new Scanner(System.in);
		
		System.out.print("Input the number of piles: ");
		piles = scan1.nextInt();
		root.nPiles = new int[piles];
		for(int i = 0; i < piles; i++) {
			System.out.print("Input the value for pile " + i + " : ");
			root.nPiles[i] = scan2.nextInt();
		}
		root.parent = null;
		root.child = null;
		root.atDepth = 0;
		root.heuristicValue = evaluateHeuristicValue(root);
		root.player = "Max";
		return new Node(root.nPiles, root.parent,root.child, root.heuristicValue,root.atDepth, root.player);
	}
	
	
	/*
	 * Initialize Nim from file input.
	 */
	public Node initNim(int[] nPiles) {
		Node root = new Node();
		int piles;

		piles = nPiles.length;
		root.nPiles = new int[piles];
		for(int i = 0; i < nPiles.length; i ++) {
			root.nPiles[i] = nPiles[i];
		}
		root.parent = null;
		root.child = null;
		root.atDepth = 0;
		root.heuristicValue = evaluateHeuristicValue(root);
		root.player = "Max";
		return new Node(root.nPiles, root.parent,root.child, root.heuristicValue,root.atDepth, root.player);
	}
	
	
	/*
	 * Heuristic function: using to calculate the heuristic value of a specific configuration
	 * The Heuristic value is calculate as following:
	 * - Every node is either 1 or -1. 
	 * 		+ If hValue = 1, it means the current player will win, and next player will lose.
	 * 		+ If hValue = -1, vice versa.
	 * - At every node, the variable xor will be used to scan all array to determine its hValue. 
	 * Example: at node [1 3 5]
	 * We will have:
	 * 		0 xor 1 xor 3 xor 5 = 7	|| 0 -> 000
	 * 								|| 1 -> 001
	 * 								|| 3 -> 011
	 * 								|| 5 -> 101
	 * 								|| --------
	 * 								||		111
	 * 								|| -> xor != 0. Then: hValue = -1.
	 * 
	 */
	public int evaluateHeuristicValue(Node current) {
		int hValue;
		int xor = 0;
		for(int i = 0; i < current.nPiles.length; i ++) {
			xor ^= current.nPiles[i];
		}
		if(xor == 0)
			hValue = 1;	// Win for the current player, next player will lose.
		else
			hValue = -1;	//Lose for the current player, next player will win.
		return hValue;
	}
	
	/*
	 * Print configuration of a NIM
	 */
	public void printConfiguration(Node current) {
		
		for(int i = 0; i < current.nPiles.length; i ++) {
			System.out.print(current.nPiles[i] + "   ");
		}
		System.out.print(" || hValue:  " + evaluateHeuristicValue(current) +" || At depth: " 
		+ current.atDepth +" || Label as: " + current.player + "\n");
		
		System.out.println();
	}
	
	/*
	 * Print an array of NIM only.
	 */
	public void printArrayConfiguration(Node current) {
		
		for(int i = 0; i < current.nPiles.length; i ++) {
			System.out.print(current.nPiles[i] + "   ");
		}
	}
	
	/*
	 * Print all possible moves (configuration) from one configuration
	 */
	public void printAllConfiguration(Node current) {
		Vector<Node> vectorNode = new Vector<Node>();
		vectorNode = getAllPossibleSuccessorsFromNode(current);
		if(vectorNode == null) {
			System.out.println("No more configuration from this current board");
			return;
		}
		for(int i = 0;i < vectorNode.size(); i ++) {
			printConfiguration(vectorNode.elementAt(i));
		}
	}
	
	
	/*
	 * HumanMove function.
	 */
	public void HumanMove(Node current) {
		int pile;
		int pieces;
		
		Scanner scan1 = new Scanner(System.in);
		Scanner scan2 = new Scanner(System.in);
		
		System.out.print("Enter which pile you want to remove: ");
		pile = scan1.nextInt();
		System.out.print("Please enter number of pieces in pile that you want to remove: ");
		pieces = scan2.nextInt();
		if(legalMove(current, pile,pieces) == true) 
			updateNim(current, pile,pieces);
		else
			System.err.print("Cannot move");
	}
	
	/*
	 * We use this function to update the NIM when playing game.
	 */
	public void updateNim(Node current, int pile, int pieces) {
		current.nPiles[pile-1] = current.nPiles[pile-1] - pieces;
	}
	
	
	/*
	 * Check if the next move is valid or not.
	 * Return true if valid, else return false.
	 */
	public boolean legalMove(Node current, int pile, int pieces) {
		boolean result = true;
		if(pieces < 0 || pieces > current.nPiles[pile])
			result = false;
		return result;
		
	}
	
	/*
	 * ----------- Generate NIM Tree ------------
	 */
	
	/*
	 * Get a successor from a current node.
	 * Return null if there is no more successor.
	 */
	
	public Node getSuccessor(Node current,int pile, int pieces) {
		int[] nPiles;
		int hValue;
		int depth;
		String player;
		if(this.legalMove(current, pile, pieces) == false)
			return null;
		if(this.isEnd(current) == true)
			return null;
		nPiles = new int[current.nPiles.length];
		System.arraycopy(current.nPiles, 0, nPiles, 0, nPiles.length);
		nPiles[pile] = nPiles[pile] - pieces;
		hValue = this.evaluateHeuristicValue(current);
		depth = current.atDepth + 1;
		if(current.player == "Max") 
			player = "Min";
		else
			player = "Max";
		return new Node(nPiles, current,null,hValue,depth, player);
	}
	
	
	/*
	 * Get all successors from a current Node
	 * Return a vector of node.
	 */
	public Vector<Node> getAllPossibleSuccessorsFromNode(Node current) {
		int pieces;
		
		Vector<Node> vec = new Vector<Node>();
		if(current == null) {
			return null;
		}
		else {
		for(int i = 0; i < current.nPiles.length; i ++) {
			pieces = 0;
			while(current.nPiles[i] > 0) {
				pieces++;
				Node successor = getSuccessor(current,i,1);
				if(successor != null){
					vec.addElement(successor);
				}
				current.nPiles[i]--;
			}
			current.nPiles[i] = current.nPiles[i] + pieces++;
		}
		return vec;	
		}
	}
	
	/*
	 * We use the global variable to keep track all result of all Nodes
	 * which is generated from NIM Tree.
	 */
	public Vector<Node> NimTreeResult = new Vector<Node>();
	public Vector<Node> checkNodeGenerated = new Vector<Node>();
	Queue queue = new Queue();
	
	/*
	 * Generate NIM Tree at particular node;
	 */
	public Vector<Node> generateNimTree(Node current) {
		
		Vector<Node> vec = new Vector<Node>();
		Node temp = new Node();

		vec = getAllPossibleSuccessorsFromNode(current);
		for(int i = 0; i < vec.size(); i ++) {
			NimTreeResult.add(vec.elementAt(i));
			queue.equeue(vec.elementAt(i));
		}
		
		temp = (Node) queue.dequeue();	
		while(isEnd(temp) == true) {
			temp = (Node)queue.dequeue();
			if(queue.isEmpty() == true)
				return NimTreeResult;
		}
//		System.out.print("Node in result: " + result.size()+"   ");
//		System.out.print("queue size: " + st1.size() + "   Generated nodes: " + checkGenerated.size() + "\n");
//		
		while(isGenerated(temp) == false && checkNodeGenerated.size() < 7000) {
			checkNodeGenerated.add(temp);
			generateNimTree(temp);
		}
		queue.removeAll();
//		copyVector(result,vResult);
//		result.removeAllElements();
		return NimTreeResult;
	}
	
	/*
	 * Check if a Node is generated.
	 * We look in the vector of checkNodeGenerated to see if a particular node is generated.
	 */
	
	public boolean isGenerated(Node node) {
		boolean result = false;
		if(checkNodeGenerated.contains(node) == true) {
			int index = checkNodeGenerated.indexOf(node);
			if(checkNodeGenerated.elementAt(index).atDepth == node.atDepth)
				result = true;
		}
		return result;
	}
	
	/*
	 * Generate NIM tree at depth.
	 */
	
	public Vector<Node> sameDepthNode = new Vector<Node>();
	
	public Vector<Node> NimTreeAtDepth(Vector<Node> result,int depth) {
		int count = 0;
		for(int i = 0; i < result.size(); i++) {
			if(result.elementAt(i).atDepth == depth) {
				sameDepthNode.add(result.elementAt(i));
				this.printConfiguration(result.elementAt(i));
				count++;
			}
		}
		System.out.println("Number of leaves at depth " + depth + " : " + count);
		return sameDepthNode;	
	}
		
	/*
	 * This function is used to copy the Vector from source to destination. 
	 */
	
	public Vector<Node> copyVector(Vector<Node> source, Vector<Node> dest) {
		for(int i = 0; i < source.size(); i ++) {
			dest.add(source.elementAt(i));
		}
		return dest;
	}
	

	/*
	 * *********** Minimax function *************
	 * This function will scan the tree from bottom - up.
	 * Until it reach the top one.
	 * Then it will return the top node.
	 */
	public Vector<Node> minimaxResult = new Vector<Node>();
	public void getMinimaxInitialConfiguration(Vector<Node> v) {
		Vector<Node> tempVector = new Vector<Node>();
		
		// If size of the input vector is null, then we just simply return. No need to do anything further.
		if(v.size() == 0) {
			return;
		}
		
		//Since we know there is only one initial NIM. 
		// When the size of minimaxResult = 1, then we just return the result.
		if(minimaxResult.size() == 1) {
			return;
		}
		else {
		copyVector(v, tempVector);
		minimaxResult.removeAllElements();
		
		int iOuterLoop = 0;
		int count = 1;
		int increase = 0;
		Node nTemp = new Node();
		while(iOuterLoop < tempVector.size()) {
				nTemp = tempVector.elementAt(iOuterLoop);
				increase = increase + 1;
				count = increase;
			while(count < tempVector.size()) {
				if(nTemp.parent == tempVector.elementAt(count).parent) {
					if(nTemp.player == "Max") {
						if(nTemp.heuristicValue >= tempVector.elementAt(count).heuristicValue) {
							nTemp = tempVector.elementAt(count);
							nTemp.parent.heuristicValue = nTemp.heuristicValue;
						}
					}
					else {
						if(nTemp.heuristicValue <= tempVector.elementAt(count).heuristicValue) {
							nTemp = tempVector.elementAt(count);
							nTemp.parent.heuristicValue = nTemp.heuristicValue;
						}
					}
					increase++;
				}
				count ++;
			}
			minimaxResult.add(nTemp.parent);
			iOuterLoop =  increase;		
		}
		tempVector.removeAllElements();
		getMinimaxInitialConfiguration(minimaxResult);
		}
	}
	
	/*
	 * Return the minimax value at initial node.
	 */
	public int getMinimaxInitialNode(Vector<Node> v) {
		if(v.size() == 0)
			return 0;
		if(v.elementAt(0).parent == null) {
			return  v.elementAt(0).heuristicValue;
		}
		
		return 0;
	}
	
	/*
	 * Check if the current NIM configuration is ended
	 */
	public boolean isEnd(Node currentNode) {
		
		boolean result = true;
		for(int i = 0; i < currentNode.nPiles.length; i++) {
			if(currentNode.nPiles[i] != 0)
				result = false;
		}
		return result;
			
	} 
	
	/*
	 * Set and get current NIM configuration
	 */
	protected void setCurrentNimNode(Node currentNode) {
		this.currentNode = currentNode;
	}
	protected Node getCurrentNimNode() {
		return currentNode;
	}
	
	/*
	 * Set and get current depth of NIM configuration
	 */
	protected void setDepth(int depth) {
		this.setDepth = depth;
	}
	protected int getDepth() {
		return setDepth;
	}
	
	/*
	 * Set and get current NIMTree of NIM configuration
	 */
	protected void setResultNimTree(Vector<Node> result) {
		this.NimTreeResult = result;
	}
	protected Vector<Node> getResultNimTree() {
		return NimTreeResult;
	}
	
	
	/*
	 * ***************** Main function ******************
	 */
	public static void main(String args[]) throws IOException {
		
		int inputIndex = 0;
		String inputString = null;
		
		Nim nim = new Nim();
		/*
		 * ----------------------------------
		 * This is used to read number of input files which want to test
		 * Change the inputIndex if want to.
		 * The inputFile will scan from the path, and read all the file as name convention
		 */
		
		for(inputIndex = 1; inputIndex <= 8; inputIndex++) {
			
			int NumberOfLineInput = 0;
			int[] nimInput;
			inputString = String.valueOf(inputIndex);
			/*
			 * *********** Please change the pathname here ***********
			 */
			File inputFile = new File("/Users/khoapham/Testcase/Nim/TestcaseHW3/test"+inputString);
			BufferedReader buffReader1 = new BufferedReader(new FileReader(inputFile));
			String inputLine = null;
			
			
			while(buffReader1.readLine() != null) {
				NumberOfLineInput++;
			}
			
			BufferedReader buffReader2 = new BufferedReader(new FileReader(inputFile));
			
			nimInput = new int[NumberOfLineInput];
			for(int i = 0; i < NumberOfLineInput; i ++) {
				inputLine = buffReader2.readLine();
				StringTokenizer stk = new StringTokenizer(inputLine, "\n");
				while(stk.hasMoreElements())
					nimInput[i] = Integer.parseInt(stk.nextToken());
			}
		
			
		Node current = new Node();
		current = nim.initNim(nimInput);

		/*
		 * ************* Please change the pathname here **************
		 */
		PrintStream pst = new PrintStream(new FileOutputStream( 
				"/Users/khoapham/Testcase/Nim/TestcaseHW3/testOut"+inputString ));
		
		System.setOut( pst);
		
		System.out.println("------- Initial NIM input -------");
		nim.printConfiguration(current);
		System.out.println("------ All possible configuration from the initial NIM -------");
		nim.printAllConfiguration(current);
		//setResultNimTree(nim.generateNimTree(current));
		nim.generateNimTree(current);
		
		/*
		 * set Depth of NIM Tree we want to derive.
		 */
		nim.setDepth(3);				
		int depth = nim.getDepth();
		
		System.out.println("------- Generate NIM tree at depth: " + depth + "  --------");
		nim.NimTreeAtDepth(nim.NimTreeResult,depth);	// You can change the depth of tree if want.
		nim.getMinimaxInitialConfiguration(nim.sameDepthNode);
		System.out.println();
		System.out.print("------ Minimax value at initial NIM: ");
		nim.printArrayConfiguration(nim.minimaxResult.elementAt(0));
		System.out.print(" is: " + nim.getMinimaxInitialNode(nim.minimaxResult) + " ------- \n");
		 
		nim.NimTreeResult.removeAllElements();
		nim.checkNodeGenerated.removeAllElements();
		nim.sameDepthNode.removeAllElements();
		nim.minimaxResult.removeAllElements();

		pst.close();
		
		}	
	}	
}
