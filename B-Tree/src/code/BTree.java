package code;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class BTree
{
	int t;							// t-1 <= Number of keys <= 2t-1
    public BTreeNode root;
	
	BTree(int setT)
	{
		t = setT;
    	root = new BTreeNode();
	}
        
	public void insert(Integer k) 
	{
		BTreeNode r = root;
		
		// root is full, split! Note: This is the only place where we do split the tree!
		if(r.keys.size() == (2*t-1))
		{
			BTreeNode s = new BTreeNode();
			root = s;
			s.isLeaf = false;
			s.children.add(0,r);
			splitChild(s, 0);
			insertNonFull(s, k);
		}
		else			
			insertNonFull(r, k);
	}

	private void insertNonFull(BTreeNode x, Integer k)
	{
		int i = x.keys.size()-1;
		if(x.isLeaf)
		{
			while( i>-1 && k.compareTo(x.keys.get(i))<0 )
				i--;
			
			x.keys.add(i+1, k);
		}
		else
		{
			while( i>-1 && k.compareTo(x.keys.get(i))<0 )
				i--;
			i++;
			
			/* 
			 * Children is full, split the child and move child's medium to parent
			 * Note: x.children is previously considered as root!
			 */
			if( x.children.get(i).keys.size() == (2*t-1) )
			{
				splitChild(x, i);
				if(k.compareTo(x.keys.get(i))>0 )
					i++;
			}
			insertNonFull(x.children.get(i), k);
		}
	}
	
	/* Assume parent.keys.size()<2t-1, but y=parent.children.get(index) is full (parent.keys.size() == 2t-1)  */
	public void splitChild(BTreeNode parent, int index)
	{
		int j=0;
		BTreeNode z = new BTreeNode();
		BTreeNode y = parent.children.get(index);
		z.isLeaf = y.isLeaf;

		// Assign z with y's last half
		for(j=0; j<t-1; j++)
			z.keys.add(y.keys.get(j+t));
		if(!y.isLeaf)
		{
			for(j=0; j<t; j++)
				z.children.add(y.children.get(j+t));
		}
		
		// Amend the parent to contain the new child
		parent.children.add(index+1, z);
		parent.keys.add(index, y.keys.get(t-1));
		
		// Remove keys of y's last half
		while(y.keys.size() != t-1)
			y.keys.remove(y.keys.size() - 1);
		// Remove pointers of y's last half
		if(y.children.size() != 0)
		{
			while(y.children.size() != t)
				y.children.remove(y.children.size()-1);			
		}
	}

    public void delete(Integer k)
    {
    	BTreeNode r = root;
    	delete(r, k);
    	if(r.keys.size()==0 && !r.isLeaf)
    		root = r.children.get(0);
    }
    
    private void delete(BTreeNode x, Integer k)
    {
    	int i=0, j=0;
    	Integer ktemp;
		BTreeNode ci, leftSibling, rightSibling;

    	while(i<x.keys.size() && k.compareTo(x.keys.get(i))>0 )
    		i++;

    	/* Case 1: If the key k is in node x and x is a leaf, delete the key k from x. */
    	if(i<x.keys.size() && k.compareTo(x.keys.get(i))==0 && x.isLeaf)
    	{
    		x.keys.remove(i);    		
    		return;
    	}

    	/*	Case 2: If the key k is in node x and x is an internal node, ktemp = k' */
    	if(i<x.keys.size() && k.compareTo(x.keys.get(i))==0 && !x.isLeaf)
    	{
    		BTreeNode y = x.children.get(i);
    		
        	/*
        	 *  Case 2a: If the child y that precedes k in node x has at least t keys,
        	 *  		then find the predecessor k' of k in the subtree rooted at y.
        	 *  		Recursively delete k', and replace k by k' in x. 
        	 */
    		if( y.keys.size() >= t )
    		{
    			ktemp = predecessor(y);
    			delete(y, ktemp);
    			
    			x.keys.remove(i);
    			x.keys.add(i, ktemp);
    			return;
    		}

        	/*
        	 *  Case 2b: If y has fewer than t keys, then, symmetrically, examine the child z that
        	 *  		follows k in node x. If z has at least t keys, then find the successor k' of k in
        	 *  		the subtree rooted at z. Recursively delete k', and replace k by k' in x.
        	 */
    		BTreeNode z = x.children.get(i+1);
    		if( z.keys.size() >=t )
    		{
    			ktemp = successor(z);
    			delete(z,ktemp);
    			
    			x.keys.remove(i);
    			x.keys.add(i, ktemp);
    			return;
    		}

        	/*
        	 *  Case 2c:	Otherwise, if both y and z have only t - 1 keys, merge k and all of z into y,
        	 *  			so that x loses both k and the pointer to z, and y now contains 2t - 1 keys.
        	 *  			Then free z and recursively delete k from y.
        	 */
    		if(y.keys.size() == t-1 && z.keys.size() == t-1)
    		{
    			y.keys.add(x.keys.get(i));
    		
    			x.keys.remove(i);
    			x.children.remove(i+1);
    		
    			// z.keys.size() == t-1
    			for(j=0; j<z.keys.size(); j++)
    				y.keys.add(z.keys.get(j));
    			for(j=0; j<z.children.size(); j++)
    				y.children.add(z.children.get(j));
    		
    			// y.keys.size() == 2*t-1 <-- should happen at this line    		
    			delete(y,k);
    			return;	
    		}
    		else
    			System.out.println("ERROR!!! This code should not be executed at all!!!");
    	}
    	
    	/*
    	 * Case 3:
    	 * 
    	 *		If the key k is not present in internal node x, determine the root x.ci of the
    	 *		appropriate subtree that must contain k, if k is in the tree at all. If x.ci has
    	 *		only t - 1 keys, execute step 3a or 3b as necessary to guarantee that we descend
    	 *		to a node containing at least t keys. Then finish by recursing on the appropriate
    	 *		child of x.
    	*/
    	if(!x.isLeaf)
    	{
    		ci = x.children.get(i);

        	if(ci.keys.size() == t-1)
    		{
    			if(i==0)	// No leftSibling
    				leftSibling = null;
    			else
    				leftSibling = x.children.get(i-1);
    			
    			if(i==x.children.size()-1) 	// No rightSibling
    				rightSibling = null;
    			else
    				rightSibling = x.children.get(i+1);
    			
    			/* 
        		 * 	Case 3a:
        		 * 		If x.ci has only t - 1 keys but has an immediate sibling with at least t keys,
        		 * 		give x.ci an extra key by moving a key from x down into x.ci, moving a
        		 * 		key from x.ci ・s immediate left or right sibling up into x, and moving the
        		 * 		appropriate child pointer from the sibling into x.ci .
        		*/
    			if(leftSibling != null && leftSibling.keys.size() >= t)		//left sibling
        		{	
        			// give x.ci an extra key by moving a key from x down into x.ci
        			ci.keys.add(0,x.keys.get(i-1));
        			x.keys.remove(i-1);

        			// moving the appropriate child pointer from the sibling into x.ci
        			if(leftSibling.children.size() != 0)
        			{
            			ci.children.add(0, leftSibling.children.get(leftSibling.children.size()-1));
            			leftSibling.children.remove(leftSibling.children.size()-1);
        			}
        			
        			// moving a key from x.ci ・s immediate left sibling up into x
        			x.keys.add(i-1, leftSibling.keys.get(leftSibling.keys.size()-1));
        			leftSibling.keys.remove(leftSibling.keys.size()-1);    				
        		}
    			else if(rightSibling != null && rightSibling.keys.size() >= t)		//right sibling
    			{
        			// give x.ci an extra key by moving a key from x down into x.ci
        			ci.keys.add(x.keys.get(i));
        			x.keys.remove(i);

        			// moving the appropriate child pointer from the sibling into x.ci
        			if(rightSibling.children.size() != 0)
        			{
            			ci.children.add(rightSibling.children.get(0));
            			rightSibling.children.remove(0);
        			}
        			
        			// moving a key from x.ci ・s immediate right sibling up into x
        			x.keys.add(i, rightSibling.keys.get(0));
        			rightSibling.keys.remove(0);    				
    			}
        		/*
        		 * Case 3b:
        		 *		If x.ci and both of x.ci ・s immediate siblings have t - 1 keys, merge x.ci
        		 *		with one sibling, which involves moving a key from x down into the new
        		 *		merged node to become the median key for that node. 
        		 */
    			else if(leftSibling != null && leftSibling.keys.size() == t-1)
        		{
    				// moving a key from x down into the new merged node to become the median key for that node
        			ci.keys.add(0, x.keys.get(i-1));
            		x.keys.remove(i-1);
            		
            		// merge x.ci with left sibling
            		x.children.remove(i-1);
            		while(leftSibling.keys.size()>0)
            		{
            			ci.keys.add(0, leftSibling.keys.get(leftSibling.keys.size()-1));
            			leftSibling.keys.remove(leftSibling.keys.size()-1);
            		}
            		while(leftSibling.children.size()>0)
            		{
                		ci.children.add(0, leftSibling.children.get(leftSibling.children.size()-1));
                		leftSibling.children.remove(leftSibling.children.size()-1);
            		}
        		}
    			else if(rightSibling != null && rightSibling.keys.size() == t-1)
    			{
    				// moving a key from x down into the new merged node to become the median key for that node
        			ci.keys.add(x.keys.get(i));
            		x.keys.remove(i);
            		
            		// merge x.ci with right sibling
            		x.children.remove(i+1);
            		while(rightSibling.keys.size()>0)
            		{
            			ci.keys.add(rightSibling.keys.get(0));
            			rightSibling.keys.remove(0);            			
            		}
            		while(rightSibling.children.size()>0)
            		{
                		ci.children.add(rightSibling.children.get(0));
                		rightSibling.children.remove(0);
            		}
    			}
    		}
    		
    		// Then finish by recursing on the appropriate child of x.
    		delete(ci,k);

    		// If root has no key, shrink the tree! Note: This is the only place where we do shrink the tree! 
    		if(x == root && x.keys.size() == 0)
    		{
    			while(x.children.size()<0)
    				x.children.remove(0);
    			root = ci;
    		}
    	}
    }
    
    private Integer predecessor(BTreeNode z)
    {    	
    	if(z.isLeaf)
    		return z.keys.get(z.keys.size()-1);
    	else
    		return predecessor(z.children.get(z.children.size()-1));
    }
    
    private Integer successor(BTreeNode z)
    {    	
    	if(z.isLeaf)
    		return z.keys.get(0);
    	else
    		return successor(z.children.get(0));
    }

    /* For Debug only */
    public Integer predecessor(BTreeNode z, int depth[])
    {
    	depth[0]++;
    	if(z.isLeaf)
    		return z.keys.get(z.keys.size()-1);
    	else
    		return predecessor(z.children.get(z.children.size()-1), depth);
    }
    
    /* For Debug only */
    public Integer successor(BTreeNode z, int depth[])
    {	
    	depth[0]++;
    	if(z.isLeaf)
    		return z.keys.get(0);
    	else
    		return successor(z.children.get(0), depth);
    }
    
	public BTreeNode find(Integer searchKey, int returnIndex[])
    {
		if(isEmpty())
			return null;
		else
			return find(root, searchKey, returnIndex);
    }
	
	/* If found, this function returns BTreeNode and the returnIndex to locate where the key is stored */
	public BTreeNode find(BTreeNode node, Integer searchKey, int returnIndex[])
    {
		int i=0;
		while( i<node.keys.size() && searchKey.compareTo(node.keys.get(i))>0 )
			i++;	
		if( i<node.keys.size() &&  searchKey.compareTo(node.keys.get(i))==0 )
		{
			returnIndex[0] = i;
			return node;
		}
		else if (node.isLeaf)
			return null;
		else
			return find(node.children.get(i), searchKey, returnIndex);
    }
    
    public boolean isEmpty( )
    {
    	if(root.isEmpty())
    		return true;
    	else
    		return false;
    }
    
    // Print the tree contents in Ascending Order.
    public void printTree(OutputStreamWriter out) throws IOException
    {
        if( isEmpty() )
        	out.write("B-Tree is Empty");
        else
            printTree(root, out);
    }
    
    // In Ascending Order
    private void printTree(BTreeNode node, OutputStreamWriter out) throws IOException
    {
    	int i =0;
    	
    	// isLeaf - Print all its keys
    	if(node.isLeaf)
    	{
    		if(node.keys.size() == 0)
    			System.out.println("node.keys.size()= " + node.keys.size() + "DELETE GOT ERROR!!!!!!!!!!!!This code should not be executed at all!!!");
    		
    		for(i=0; i<node.keys.size(); i++)
    			out.write(node.keys.get(i).intValue() + " LEAF\n");
    	}
    	else	// !isLeaf - Print child(0)'s keys, key(0), child(1)'s keys, key(1), ... , child(n-1)'s keys, key(n-1), child(n)'s keys 
    	{
    		for(i=0; i<node.children.size(); i++)
    		{
    			printTree(node.children.get(i),out);
    			
    			if(i<node.keys.size() && node == root)
    				out.write(node.keys.get(i).intValue() + " ROOT \n");
    			else if(i<node.keys.size())
    				out.write(node.keys.get(i).intValue() + " INTERNAL \n");
    			
    		}
    	}
    }  
}
