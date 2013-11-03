package code;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DemoRed_Black_Trees 
{
	public static void main(String[] args) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(args[1]); 
		OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
		
		ArrayList<Integer> inputArray = new ArrayList<Integer>();
		RedBlackTree RBTree = new RedBlackTree(); 
		int i=0;
		
		if(args.length != 2 || args[0].equalsIgnoreCase("help"))
		{
			System.out.println("Argument is empty! Please provide the path of the input.txt");
			System.out.println("Red_Black_Trees Path/to/input.txt Path/to/output.txt");
			out.close();
			return;
		}
		
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
			RBTree.insert(new RedBlackNode(inputArray.get(i)));
		
		System.out.println("Finish the Insertion, and the End time is " + (endTime=System.nanoTime()));
		System.out.println("Total Execution Time is " + (endTime-startTime) + " nanoseconds");
		
		// Output the result to output.txt
	    // In Ascending Order
	    // <Node.key> <Node.color> <Node.Parent> <Parent Left or Right>
		RBTree.printTree(out);
		
/*
 		// Manually Delete One Node
		Scanner in = new Scanner(System.in);
		System.out.println("Input a key to specify which node to delete: ");
		Integer deleteKey = new Integer(in.nextInt());
		System.out.println("The key you want to delete is: " + deleteKey);
		
		RedBlackNode node = RBTree.find(new RedBlackNode(deleteKey));
		if(node != RBTree.nil)
			System.out.println("The node is found! " + node.key.toString());
		else
			System.out.println("The node is not found!");
			
		RBTree.delete(node);
		in.close();
*/
		
		/* Delete the first half of the input numbers */
		for (i = 0; i < inputArray.size()/2; i++)
		{
			RBTree.delete(RBTree.find(new RedBlackNode(inputArray.get(i))));
		}
		
		out.write ("After delete\n");
	    // In Ascending Order
	    // <Node.key> <Node.color> <Node.Parent> <Parent Left or Right>
		RBTree.printTree(out);
		
		out.close();
	}

}
