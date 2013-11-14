/*
 *	Below Implementation is based on the information from this paper
 *
 *	"Skip lists: a probabilistic alternative to balanced trees" 
 *	by Prof. William Pugh from 	Univ. of Maryland, College Park
 *
 *	Published in Communications of the ACM Volume 33 Issue 6, June 1990 Pages 668-676
 * 	http://dl.acm.org/citation.cfm?id=78977
 */
package code;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SkipLists 
{
	int topLevel;				// 0: min level
	SkipListsNode nil;
	SkipListsNode header;
	static int MAXLevel;		// Recommended to be 16
	static double P;			// Between 0 to 1, Recommended to be 0.5
	
	public SkipLists(int setMAXLevel, double setP)
	{
		MAXLevel = setMAXLevel;
		P = setP;
		topLevel = 0;
		nil = new SkipListsNode(Integer.MAX_VALUE);
		header = new SkipListsNode(Integer.MAX_VALUE);
		header.forwards.add(nil);
	}

	public SkipListsNode find(Integer searchKey) 
	{
		int i;
		SkipListsNode x = header;
		
		for(i=topLevel; i>-1; i--)
		{
			while(x.forwards.get(i).key.compareTo(searchKey) < 0)
				x = x.forwards.get(i);
		}
		
		x = x.forwards.get(0);
		if(x.key.intValue() == searchKey.intValue())
			return x;
		else
			return nil;
	}

	public void insert(SkipListsNode searchKey) 
	{
		int i, lvl=0;
		ArrayList<SkipListsNode> update = new ArrayList<SkipListsNode>();
		SkipListsNode x = header;
		
		for(i=topLevel; i>-1; i--)
		{
			while(x.forwards.get(i).key.compareTo(searchKey.key) < 0)
				x = x.forwards.get(i);
			
			update.add(0, x);
		}
		
		x = x.forwards.get(0);
		if(x.key.intValue() == searchKey.key.intValue())
			return;
		else
		{
			// Add new levels
			lvl = randomLevel();
			if(lvl > topLevel)
			{
				for(i=topLevel+1; i<=lvl; i++)
				{
					update.add(header);
					header.forwards.add(nil);
				}

				topLevel = lvl;
			}
			
			// Add the new node and amend appropriate pointers
			for(i=0; i<=topLevel; i++)
			{
				searchKey.forwards.add(i, update.get(i).forwards.get(i));
				update.get(i).forwards.set(i, searchKey);
			}
		}
	}

	private int randomLevel()
	{
		int lvl=0;
		
		while( Math.random()<P && lvl<MAXLevel )
			lvl++;
		
		return lvl;
	}

	public void delete(Integer searchKey)
	{
		int i;
		ArrayList<SkipListsNode> update = new ArrayList<SkipListsNode>();
		SkipListsNode x = header;
		
		for(i=topLevel; i>-1; i--)
		{
			while(x.forwards.get(i).key.compareTo(searchKey) < 0)
				x = x.forwards.get(i);
			
			update.add(0, x);
		}
		
		x = x.forwards.get(0);
		if(x.key.intValue() == searchKey.intValue())
		{
			// Delete x by amending the pointers, x is not to be free as it will be destroyed by java's garbage collection
			for(i=0; i<=topLevel; i++)
			{
				if(update.get(i).forwards.get(i) != x)
					break;
				else
					update.get(i).forwards.set(i, x.forwards.get(i));
			}
			
			// Remove level with no nodes
			while(topLevel>0 && header.forwards.get(topLevel) == nil)
				topLevel--;
		}		
	}
	
    /*
     *  Print the lists from bottom level to top level
	 * 	<level> <node 0> <node 1> <node 2> <node 3>.....
	 * 	NOTE: eclipse text editor may not able to display such long number, please use Notepad++
	 */
	public void printLists(PrintWriter out) throws IOException 
	{
		int i;
		SkipListsNode x = header;
		
		for(i=0; i<=topLevel; i++, x=header)
		{
			out.print(i + " ");
			
			while(x.forwards.get(i) != nil)
			{
				out.print(x.forwards.get(i).key + " ");
				x = x.forwards.get(i);
				out.flush();								// Ensure the output is written to the file!
			}
			
			out.print("\n");
		}
	}
}
