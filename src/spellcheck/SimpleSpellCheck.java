package spellcheck;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SimpleSpellCheck {
	public static void main (String[] args){
		String s1 = "baget";
		String s2 = "badger";
		
		System.out.println("min dist: "+minDist(s1,s2));
	
		System.out.println();
		System.out.println();

		System.out.println("kitten = " + spellCheck("kitten"));
		System.out.println("Kitten = " + spellCheck("Kitten"));
		System.out.println("kittin = " + spellCheck("kittin"));
		System.out.println("kitin = " + spellCheck("kitin"));
		System.out.println("kitteng = " + spellCheck("kitteng"));
	
	}

	public static ArrayList<String> spellCheck(String input) {
		
		/****check input edit Distance vs all words in dictionary, add all mins to list (temp for now)****/
		
		ArrayList<Integer> minDistance = new ArrayList<Integer>(); //stores the minEditDist of each word in dict
		ArrayList<String> Dwords = new ArrayList<String>(); //stores each word in the dic
		ArrayList<String> mins = new ArrayList<String>(); //stores the list of words with smallest minEditDist

		/****
			Reads in the Dictionary file for now
			can be replaced later to only accept another Dwords arrayList!?

		****/

		try {

			BufferedReader in = new BufferedReader(new FileReader("dict.txt"));
			for (String i = in.readLine(); i != null; i = in.readLine()) {
				Dwords.add(i);
				minDistance.add(minDist(input, i)); 
			}
			in.close();

			/***
				for (int i = 0; i < Dwords.size(); i++) {
					minDistance.add(minDist(input, i));
				}
			***/
			for (int i = 0; i < minDistance.size(); i++) {
				if ( minDistance.get(i) == findMinEditDist(minDistance)) {
					mins.add(Dwords.get(i)); 
				}
			}
				 
			return mins;

		} catch (IOException e) {
			System.out.println("Error: IOException in spellCheck() method! (unable to read dict.txt?)");
			return null;
		}
		 
		
	}
	
	private static int findMinEditDist(ArrayList<Integer> arr) {
		int min = 10000;
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i) < min ) {
				min = arr.get(i);
			}
		}

		return min;			
	}

	private static int minimum(int a, int b, int c) {                            
	    return Math.min(Math.min(a, b), c);                                      
	}
	
	public static int minDist(String target, String source){	
		//int m = target.length();
		//int n = source.length();
		//System.out.println("target length: "+target.length()+" source length: "+source.length());
		
		int[][] dist = new int[target.length()+1][source.length()+1];  
		dist[0][0] = 0;

		for (int i=0; i<target.length()+1;i++){
			dist[i][0] = i;
		}
		
		for (int j=0;j<source.length()+1;j++){
			dist[0][j]= j;
		}
		
		/***********
		for(int i=0; i<target.length()+1; i++){  
	        for(int j=0; j<source.length()+1; j++){  
	           
	                System.out.print(" [ " + dist[i][j] + " ] ");
	        }
	        System.out.print("\n");
	    }
		***********/
		//System.out.println();
		//System.out.println(dist[0][0]);
		for (int i=1; i<=target.length();i++){
			for (int j=1; j<=source.length(); j++){
				
				dist[i][j]= minimum(
						dist[i-1][j]+1,
						dist[i][j-1]+1,
						dist[i-1][j-1]+((target.charAt(i-1)== source.charAt(j-1)) ? 0 : 1)); // boolean to int here
			}
		}
		/***********
		for(int i=0; i<target.length()+1; i++){  
	        for(int j=0; j<source.length()+1; j++){  
	           
	                System.out.print(" [ " + dist[i][j] + " ] ");
	        }
	        System.out.print("\n");
	    }
		***********/
		return dist[target.length()][source.length()]; 
	}
	
}
