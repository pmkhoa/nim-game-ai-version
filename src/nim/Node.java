package nim;
import java.util.*;

public class Node {
	int[] nPiles;
	Vector<Node> child ;
	Node parent;
	int heuristicValue;
	int atDepth;
	String player;
	

	public Node() {}
	
	
	public Node(int[] nPiles) {
		this(nPiles,null,null,0,0,null);
	}
	
	public Node(int[] nPiles, Node parent, Vector<Node> child, int hValue, int atDepth, String player) {
		this.nPiles = nPiles;
		this.parent = parent;
		this.child = child;
		heuristicValue = hValue;
		this.atDepth = atDepth;
		this.player = player;
	}
	
	
}

