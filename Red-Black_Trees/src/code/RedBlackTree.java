package code;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class RedBlackTree 
{
    static final boolean BLACK = true;
    static final boolean RED   = false;
    
    public RedBlackNode root;
    public RedBlackNode nil;
    
    RedBlackTree()
    {
    	nil = new RedBlackNode(null);
    	nil.parent = nil;
    	nil.left = nil;
    	nil.right = nil;
    	nil.key = new Integer(-1);
    	root = nil;
    }
    
    void leftRotate(RedBlackNode x)
    {
    	RedBlackNode y = x.right;		// set y
    	x.right = y.left;				// turn y's left subtree into x's right subtree
    	if(y.left != nil)
    		y.left.parent = x;
    	y.parent = x.parent;			// link x's parent to y
    	if(x.parent == nil)
    		root = y; 
    	else if (x == x.parent.left)
    		x.parent.left = y;
    	else
    		x.parent.right = y;
    	y.left = x;						// put x on y's left
    	x.parent = y;
    }
    
    void rightRotate(RedBlackNode y)
    {
    	RedBlackNode x = y.left;		// set x
    	y.left = x.right;				// turn x's right subtree into y's left subtree
    	if(x.right != nil)
    		x.right.parent = y;
    	x.parent = y.parent;			// link y's parent to x
    	if(y.parent == nil)
    		root = x; 
    	else if (y == y.parent.left)
    		y.parent.left = x;
    	else
    		y.parent.right = x;
    	x.right = y;					// put y on x's right
    	y.parent = x;
    }
    
    void insert(RedBlackNode z)
    {
    	RedBlackNode y = nil;
    	RedBlackNode x = root;
    	
    	while(x != nil)
    	{
    		y = x;
    		if(z.key.compareTo(x.key) < 0)
    			x = x.left;
    		else
    			x = x.right;
    	}
    	
    	z.parent = y;
    	if(y == nil)
    		root = z;
    	else if(z.key.compareTo(y.key) < 0)
    		y.left = z;
    	else
    		y.right = z;
    	
    	z.left = nil;
    	z.right = nil;
    	z.color = RED;
    	
    	insertFixUP(z);
    }
    
    void insertFixUP(final RedBlackNode justInsertedNode)
    {
    	RedBlackNode z = justInsertedNode;
    	while(z.parent.color == RED)
    	{
    		if(z.parent == z.parent.parent.left)
    		{
    			RedBlackNode y = z.parent.parent.right;
    			
    			// case 1
    			if(y.color == RED)
    			{
    				z.parent.color = BLACK;
    				y.color = BLACK;
    				z.parent.parent.color = RED;
    				z = z.parent.parent;
    			}
    			else
    			{
    				// case 2
    				if (z == z.parent.right)	
    				{
    					z = z.parent;
    					leftRotate(z);
    				}
    			
    				//case 3	
    				z.parent.color = BLACK;
    				z.parent.parent.color = RED;
    				rightRotate(z.parent.parent);
    			}
    		}
    		else if(z.parent == z.parent.parent.right)
    		{
    			RedBlackNode y = z.parent.parent.left;
    			
    			// case 4
    			if(y.color == RED)
    			{
    				z.parent.color = BLACK;
    				y.color = BLACK;
    				z.parent.parent.color = RED;
    				z = z.parent.parent;
    			}
    			else
    			{
    				// case 5
    				if (z == z.parent.left)	
    				{
    					z = z.parent;
    					rightRotate(z);
    				}
    			
    				//case 6	
    				z.parent.color = BLACK;
    				z.parent.parent.color = RED;
    				leftRotate(z.parent.parent);
    			}
    		}
    	}
    	
    	root.color = BLACK;
    }
    
    RedBlackNode treeMinimum(final RedBlackNode x)
    {
    	RedBlackNode ret = x;
    	while(ret.left != nil)
    		ret = ret.left;
    	return ret;
    }
    
    void transplant(RedBlackNode u, RedBlackNode v)
    {
    	if(u.parent == nil)
    		root = v;
    	else if (u == u.parent.left)
    		u.parent.left = v;
    	else
    		u.parent.right = v;
    	
    	v.parent = u.parent;
    }
    
    void delete(RedBlackNode z)
    {
    	if(z == nil)
    		return;
    	
    	RedBlackNode y = z;
    	RedBlackNode x = nil;
    	boolean yOriginalColor = y.color;
    	
    	if(z.left == nil)
    	{
    		x = z.right;
    		transplant(z, z.right);
    	}
    	else if (z.right == nil)
    	{
    		x = z.left;
    		transplant(z, z.left);    		
    	}
    	else
    	{
    		y = treeMinimum(z.right);
    		yOriginalColor = y.color;
    		x = y.right;
    		
    		if(y.parent == z)
    			x.parent = y;
    		else
    		{
    			transplant(y, y.right);
    			y.right = z.right;
    			y.right.parent = y;
    		}
    		
    		transplant(z,y);
    		y.left = z.left;
    		y.left.parent = y;
    		y.color = z.color;
    	}
    	
    	if(yOriginalColor == BLACK)
    		deleteFixUp(x);
    }
    
    void deleteFixUp(final RedBlackNode replacedNode)
    {
    	RedBlackNode x = replacedNode;
    	RedBlackNode w = nil;
    	while(x != root && x.color == BLACK)
    	{
    		if(x == x.parent.left)
    		{
    			w = x.parent.right;
    			// case 1
    			if(w.color == RED)
    			{
    				w.color = BLACK;
    				x.parent.color = RED;
    				leftRotate(x.parent);
    				w = x.parent.right;
    			}
    			// case 2
    			if(w.left.color == BLACK && w.right.color == BLACK)
    			{
    				w.color = RED;
    				x = x.parent;
    			}
    			else 
    			{
    				// case 3
    				if(w.right.color == BLACK)
    				{
        				w.left.color = BLACK;
        				w.color = RED;
        				rightRotate(w);
        				w = x.parent.right;    					
    				}
    				
    				// case 4
    				w.color = x.parent.color;
        			x.parent.color = BLACK;
        			w.right.color = BLACK;
        			leftRotate(x.parent);
        			x = root;
    			}
    		}
    		else if(x == x.parent.right)
    		{
    			w = x.parent.left;
    			// case 5
    			if(w.color == RED)
    			{
    				w.color = BLACK;
    				x.parent.color = RED;
    				rightRotate(x.parent);
    				w = x.parent.left;
    			}
    			// case 6
    			if(w.right.color == BLACK && w.left.color == BLACK)
    			{
    				w.color = RED;
    				x = x.parent;
    			}
    			else 
    			{
    				// case 7
    				if(w.left.color == BLACK)
    				{
        				w.right.color = BLACK;
        				w.color = RED;
        				leftRotate(w);
        				w = x.parent.left;    					
    				}
    				
    				// case 8
    				w.color = x.parent.color;
        			x.parent.color = BLACK;
        			w.left.color = BLACK;
        			rightRotate(x.parent);
        			x = root;
    			}  			
    		}
    	}
    	x.color = BLACK;
    }
    
    public RedBlackNode find(RedBlackNode search)
    {
    	return find(search, root);
    }
    
    public RedBlackNode find(RedBlackNode search, RedBlackNode node)
    {
    	if(node != nil)
    	{
            if( node.key.compareTo( search.key ) < 0 )
            	return find(search, node.right);
            else if( node.key.compareTo( search.key ) > 0 ) 
            	return find(search, node.left);
            else
            	return node;
    	}
    	
    	return nil;
    }
    
    public boolean isEmpty( )
    {
        return root == nil;
    }

    // Print the tree contents in sorted order.
    public void printTree(OutputStreamWriter out) throws IOException
    {
        if( isEmpty( ) )
        	out.write("Empty Red-Black Tree");
        else
            printTree(root, out);
    }

    /**
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the tree.
     * @throws IOException 
     */
    // In Ascending Order
    // <Node.key> <Node.color> <Node.Parent> <Parent Left or Right>
    private void printTree(RedBlackNode node, OutputStreamWriter out) throws IOException
    {
        if(node != nil)
        {
            printTree(node.left,out);

            out.write(node.key.toString());
            out.write(node.color? " Black ":" Red ");
            out.write(node.parent.key.toString());
            out.write(node.parent.left == node? " Left ":" Right ");
            out.write("\n");
            
            printTree(node.right,out);
        }
    }
}
