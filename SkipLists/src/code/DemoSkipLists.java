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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DemoSkipLists 
{
	public static void main(String[] args) throws IOException 
	{
		if(args.length != 2 || args[0].equalsIgnoreCase("help"))
		{
			System.out.println("Argument is empty! Please provide the path of the input.txt");
			System.out.println("SkipLists Path/to/input.txt Path/to/output.txt");
			return;
		}		
		PrintWriter out = new PrintWriter(new FileWriter(args[1]));

		ArrayList<Integer> inputArray = new ArrayList<Integer>();
		// From paper, "If P=0.5, using MaxLevel=16 is appropriate for data structures containing up to 2^16 = 65536 elements
		SkipLists mySkipLists = new SkipLists(16, 0.5); 
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
		System.out.println("Start to do the Insert, and the Current Time is " + (startTime=System.nanoTime()));

		for (i = 0; i < inputArray.size(); i++)
			mySkipLists.insert(new SkipListsNode(inputArray.get(i)));
		
		System.out.println("Finish the Insertion, and the End time is " + (endTime=System.nanoTime()));
		System.out.println("Total Execution Time is " + (endTime-startTime) + " nanoseconds");
		
		// Output the result to output.txt
	    /*
	     *  Print the lists from bottom level to top level
		 * 	<level> <node 0> <node 1> <node 2> <node 3>.....
		 * 	NOTE: eclipse text editor may not able to display such long number, please use Notepad++
		 */
		mySkipLists.printLists(out);
		
		// Please use the following cases one by one, not at the same time!
 		/*-----------------1. Manually Find and Delete One Node (Begin)----------------------------*/
		/*
		Scanner in = new Scanner(System.in);
		System.out.println("Input a key to specify which node to delete: ");
		deleteKey = new Integer(in.nextInt());
		System.out.println("The key you want to delete is: " + deleteKey.toString());
		in.close();
		
		SkipListsNode node = mySkipLists.find(deleteKey);
		if(node != mySkipLists.nil)
		{
			System.out.println("The node is found! " + node.key.toString());
			mySkipLists.delete(node.key);
			System.out.println("The node is delete!");
		}
		else
			System.out.println("The node is not found!");

		out.write ("-------------After manually delete-------------\n");
		mySkipLists.printLists(out);
		*/
		/*-----------------1. Manually Find and Delete One Node (END)----------------------------*/

		/*-----------------2. Delete the first half of the input numbers (Begin)----------------------------*/
		/*
		for (i = 0; i < inputArray.size()/2; i++)
		{
			System.out.println("Delete " + i + "th item: " + inputArray.get(i).intValue());
			mySkipLists.delete(inputArray.get(i));
		}
		out.write ("-------------After delete the first half of the input numbers-------------\n");
		mySkipLists.printLists(out);
		*/
		/*-----------------2. Delete the first half of the input numbers (END)----------------------------*/
		
		/*-----------------3. Random choose the input number and then delete, the chosen number is removed from the Input Array (BEGIN)--------------------*/
		i = inputArray.size()/2;
		while(i>0)
		{
			index = (int)(Math.random()*inputArray.size());
			deleteKey = new Integer(inputArray.get(index));
			inputArray.remove(index);
			
			mySkipLists.delete(deleteKey);
			i--;
		}
		
		out.write ("-------------After Random Delete-------------\n");
		mySkipLists.printLists(out);
		/*-----------------3. Random choose the input number and then delete, the chosen number is removed from the Input Array (END)-----------------------*/
		
		System.out.println("mySkipLists has 0 to " + mySkipLists.topLevel + " level");
		
		out.close();
	}

}
