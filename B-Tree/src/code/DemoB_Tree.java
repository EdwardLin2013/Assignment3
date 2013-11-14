package code;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DemoB_Tree 
{
	public static void main(String[] args) throws IOException
	{
		if(args.length != 2 || args[0].equalsIgnoreCase("help"))
		{
			System.out.println("Argument is empty! Please provide the path of the input.txt");
			System.out.println("B-Tree Path/to/input.txt Path/to/output.txt");
			return;
		}
		
		FileOutputStream fos = new FileOutputStream(args[1]); 
		OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
		
		ArrayList<Integer> inputArray = new ArrayList<Integer>();
		BTree myBTree = new BTree(5);		// Parameter: t value of B-Tree 
		int i=0, index=0;
		Integer deleteKey;

		// Get an array of number from input.txt
		/*
		 *  input.txt assumption:
		 *  1) all numbers are integer
		 *  2) one line only has one number
		 *  3) number should not out of the range of int data type
		 *  4) args[0] provide the path of input.txt
		 */
		Scanner sc = new Scanner(new File(args[0]));
		while (sc.hasNextInt())
			inputArray.add(new Integer(sc.nextInt()));
		sc.close();

		// Insert the Numbers
		long startTime=0, endTime=0;
		System.out.println("Start to do the B-Tree Insert, and the Current Time is " + (startTime=System.nanoTime()));
		
		for (i = 0; i < inputArray.size(); i++)
			myBTree.insert(inputArray.get(i));
	
		System.out.println("Finish the B-Tree Insertion, and the End time is " + (endTime=System.nanoTime()));
		System.out.println("Total Execution Time is " + (endTime-startTime) + " nanoseconds");
			
		// Output the result to output.txt
		// In Ascending Order
		// Each Line shows the values of one node's keys and the type of the node {LEAF, INTERNAL, ROOT}
		myBTree.printTree(out);
		
		// Please use the following cases one by one, not at the same time!
 		/*-----------------1. Manually Find and Delete One Node (Begin)----------------------------*/
		/*
		Scanner in = new Scanner(System.in);
		System.out.println("Input a key to specify which node to delete: ");
		deleteKey = new Integer(in.nextInt());
		System.out.println("The key you want to delete is: " + deleteKey.toString());
		in.close();	

		int locatedAt[] = new int[1];
		locatedAt[0] = -1;
		BTreeNode node; = myBTree.find(deleteKey, locatedAt);		
		if(node != null)
		{
			System.out.println("The node is found!");
			System.out.println("Node is " + (node!=myBTree.root?(node.isLeaf?"LEAF":"INTERNAL"):"ROOT") + ";");
			System.out.println("deleteKey is located at the node's key's " + locatedAt[0] + "th position");
			
			myBTree.delete(deleteKey);
			node = myBTree.find(deleteKey, locatedAt);
			if(node == null)
				System.out.println("Key is deleted!");
			else
				System.out.println("Error in delete()!");
		}
		else
			System.out.println("The node is not found!");
		
		out.write ("-------------After manually delete-------------\n");
		myBTree.printTree(out);
		*/
		/*-----------------1. Manually Find and Delete One Node (END)----------------------------*/

		/*-----------------2. Delete the first half of the input numbers (Begin)----------------------------*/
		/*
		for (i = 0; i < inputArray.size()/2; i++)
		{
			System.out.println("Delete " + i + "th item: " + inputArray.get(i).intValue());
			myBTree.delete(inputArray.get(i));
		}
		out.write ("-------------After delete the first half of the input numbers-------------\n");
		myBTree.printTree(out);
		*/
		/*-----------------2. Delete the first half of the input numbers (END)----------------------------*/
		
		/*-----------------3. Random choose the input number and then delete, the chosen number is removed from the Input Array (BEGIN)--------------------*/
		i = inputArray.size()/2;
		while(i>0)
		{
			index = (int)(Math.random()*inputArray.size());
			deleteKey = new Integer(inputArray.get(index));
			inputArray.remove(index);
			
			myBTree.delete(deleteKey);
			i--;
		}
		
		out.write ("-------------After Random Delete-------------\n");
		myBTree.printTree(out);
		/*-----------------3. Random choose the input number and then delete, the chosen number is removed from the Input Array (END)-----------------------*/
		
		/*	The Smallest Number should be located at the leftmost, and the Largest Number should be located at the rightmost, 
		 *  and they should share the same height */
		int h[] = new int[1];
		myBTree.successor(myBTree.root, h);
		System.out.println("Smallest Number at depth: " + h[0]);
		
		h[0] = 0;
		myBTree.predecessor(myBTree.root, h);
		System.out.println("Largest Number at depth: " + h[0]);
		
		
		out.close();
	}

}
