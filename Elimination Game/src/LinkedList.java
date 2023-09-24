/*
 * Reid Baier
 * CS 245 Advanced Programming and Data Structures
 * 
 * Created 4/6/22
 * 
 * From the assignment instructions (here so I don't have to go back and forth between Eclipse and the instructions): 
 * “Assassin” is a real-life game in which a group of players all try individually to find and touch (“kill”) one other player. 
 * You are to use a linked list to represent this “kill ring” of players in the game. 
 * If a “kill” is made, the ring adjusts by removing that person from the list. 
 * The “kill ring” implies that the list would be a circular list (see text for a diagrammatic representation of the “kill ring.”)
 * 
 * Write a program that models a game of “Assassin.” 
 * The game reads the names of the initial “kill ring” from a text file and puts them into a linked list in random order. 
 * Then the game repeatedly prompts for the name of a person that has been “killed”. 
 * The game continues until only one player remains and is declared the winner. 
 * The program should also have methods for printing the current contents of the “kill ring” and printing a “graveyard” of all players who have been “killed”. 
 * Use a sample “player_names.txt” file (create your own) containing at least 10-15 players for the game. 
 * You are to use a node-based representation of a linked list.
 * 
 * End of the assignment's instructions.
 * 
 * 
 * The code for the node implementation and linkedlist from the LinkedList.java from the ListExample folder on Canvas.
 * There are some changes from the original LinkedList.java that have been made.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class LinkedList 
{
	
	Node head; // head of list

	// Linked list Node.
	// Node is a static nested class
	// so main() can access it
	static class Node 
	{

		String data;
		Node next;

		// Constructor
		Node(String d)
		{
			data = d;
			next = null;
		}
	}

	// Method to insert a new node
	public static LinkedList insert(LinkedList list, String data)
	{
		// Create a new node with given data
		Node new_node = new Node(data);
		new_node.next = null;

		// If the Linked List is empty,
		// then make the new node as head
		if (list.head == null) 
		{
			list.head = new_node;
		}
		else 
		{
			// Else traverse till the last node
			// and insert the new_node there
			Node last = list.head;
			while (last.next != null) 
			{
				last = last.next;
			}

			// Insert the new_node at last node
			last.next = new_node;
		}

		// Return the list by head
		return list;
	}

	// Method to print the LinkedList.
	public static void printList(LinkedList list)
	{
		Node currNode = list.head;

		// Traverse through the LinkedList
		while (currNode != null) 
		{
			// Print the data at current node
			System.out.print(currNode.data + " ");

			// Go to next node
			currNode = currNode.next;
		}
		System.out.println("\n");
	}

	// Method to delete a node in the LinkedList by KEY
	public static LinkedList deleteByKey(LinkedList list, String key)
	{
		// Store head node
		Node currNode = list.head, prev = null;

		//
		// CASE 1:
		// If head node itself holds the key to be deleted

		if (currNode != null && currNode.data == key) 
		{
			list.head = currNode.next; // Changed head

			// Display the message
			System.out.println(key + " found and deleted \n");

			// Return the updated List
			return list;
		}

		//
		// CASE 2:
		// If the key is somewhere other than at head
		//

		// Search for the key to be deleted,
		// keep track of the previous node
		// as it is needed to change currNode.next
		while (currNode != null && currNode.data != key) 
		{
			// If currNode does not hold key
			// continue to next node
			prev = currNode;
			currNode = currNode.next;
		}

		// If the key was present, it should be at currNode
		// Therefore the currNode shall not be null
		if (currNode != null) 
		{
			// Since the key is at currNode
			// Unlink currNode from linked list
			prev.next = currNode.next;

			// Display the message
			System.out.println(key + " found and deleted \n");
		}
		// return the List
		return list;
	}

	//inserts a new node at a specific index
	public static LinkedList insertIndex(LinkedList list, String data, int index)
	{
		Node n = new Node(data);
		
		//checks to see if the new node will be inserted at index 0
		if(index == 0)
		{
			//if so, then sets the head node to the newly inserted node
			n.next = list.head;
			list.head = n;
		}
		
		//otherwise iterate through the linkedlist, insert the new node at the specific index, and shift the nodes after the new node
		else if(index > 0)
		{
			Node temp = list.head;
			for(int i = 1; i < index; i++)
			{
				temp = temp.next;
			}

			n.next = temp.next;
			temp.next = n;
		}
		
		return list;
	}
	
	//allows access to a node's data
	public static String getNode(LinkedList list, int index)
	{
		//sets the current node to the head
		Node curr = list.head;
		//a counter for the node at the index
		int count = 0;
		
		while(curr != null)
		{
			//checks to see if the node is what to look at
			if(count == index)
			{
				return curr.data;
			}
			count++;
			curr = curr.next;
		}
		
		//returns null if the head node isn't null, apparently the head node will be null at some point unless some workarounds are created...
		return null;
	}

	
	public static void main(String[] args) throws FileNotFoundException
	{
		File f = new File("src\\player_names.txt");
		Scanner sc = new Scanner(f);
		//this linkedlist is for the 'kill ring' part of the game
		LinkedList ll = new LinkedList();
		//this linkedlist is for the 'graveyard' part of the game
		LinkedList list = new LinkedList();
		Random rand = new Random();
		int playerCount = 0;
		
		//inserts the names from the player_names.txt into a linkedlist randomly
		while(sc.hasNext())
		{
			int num = rand.nextInt(playerCount + 1);
			insertIndex(ll, sc.next(), num);
			playerCount++;
		}
		
		//closes the Scanner method(?) 
		//to be honest, I just have it here so I don't have too many warnings to look at...
		sc.close();
		printList(ll);
		
		//continues the game while there is more than one player remaining
		//fun fact, keeping the > 1 removes null from either linked lists, this saved a couple lines of code and a headache
		while(playerCount > 1)
		{
			start(ll, list, playerCount);
			playerCount--;
		}
		
		System.out.println("The winner is: ");
		printList(ll);
	}
	
	//this method is to simulate the 'Assassin' game
	//also note to self: list1 = 'kill ring', list2 = 'graveyard'
	public static void start(LinkedList list1, LinkedList list2, int players)
	{
		//the randomized variables is for selecting a random player to be removed from the 'kill ring' and inserted into the 'graveyard'
		//the variable name is odd because I initially had two different variables (num1 and num2) as random numbers, I don't feel like refactoring the variable to be honest...
		Random rand = new Random();
		int num2 = rand.nextInt(players + 1);

		//checks to see if the head node is null, if so then call itself recursively
		if(getNode(list1, num2) == null)
		{
			start(list1, list2, players);
		}
		//otherwise the 'victim' is removed from the 'kill ring' linkedlist and inserted into the 'graveyard' linkedlist
		else
		{
			String str = getNode(list1, num2);
			insert(list2, getNode(list1, num2));
			deleteByKey(list1, str);
			
			//prints out the active players
			System.out.println("Here are the players still in the game: ");
			printList(list1);
			
			//prints out the non-active players
			System.out.println("Here are the players that are out of the game: ");
			printList(list2);
		}
	}
}
