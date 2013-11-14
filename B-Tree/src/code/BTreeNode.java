package code;

import java.util.ArrayList;

public class BTreeNode 
{
	boolean					isLeaf;			// isLeaf
	ArrayList<Integer>		keys;			// Stored Keys
	ArrayList<BTreeNode>	children;		// Pointer to its children

	public BTreeNode()
	{
		isLeaf = true;
		keys = new ArrayList<Integer>();
		children = new ArrayList<BTreeNode>();
	}
	
    public boolean hasNoChildren()
    {
    	if(children.size() == 0)
    		return true;
    	else
    		return false;
    }
    
    public boolean hasNoKeys()
    {
    	if(keys.size() == 0)
    		return true;
    	else
    		return false;
    }
    
    public boolean isEmpty()
    {
    	if(hasNoKeys())
    		return true;
    	else
    		return false;
    }
}
