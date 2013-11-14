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

import java.util.ArrayList;

public class SkipListsNode 
{
	Integer key;
	ArrayList<SkipListsNode>	forwards;				// 0 index:level 0 
	
	public SkipListsNode(Integer setKey)
	{
		key = setKey;									// key and value are the same, as the focus is on the implementation of data structure
		forwards = new ArrayList<SkipListsNode>();
	}
	
	public SkipListsNode(int setKey)
	{
		key = new Integer(setKey);
		forwards = new ArrayList<SkipListsNode>();
	}
}
