package code;

public class RedBlackNode 
{
	boolean			color;      // Color
	Integer			key;		// The data in the node
	RedBlackNode	left;       // Left child
	RedBlackNode	right;      // Right child
	RedBlackNode	parent;		// Parent
	
    // Constructors
	RedBlackNode(Integer theKey)
	{
		this(theKey, null, null, null);
	}

	RedBlackNode(Integer theKey, RedBlackNode leftNode, RedBlackNode rightNode, RedBlackNode parentNode)
	{
		color	= RedBlackTree.BLACK;
		key		= theKey;
		left	= leftNode;
		right	= rightNode;
		parent	= parentNode;
	}
}
