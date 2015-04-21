package spellcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**The {@code SpellChecker} class contains a number of methods
 * which produce an edit distance between two strings along with
 * suggesting alternative words for a misspelled word. The last 
 * revision was on March 30th 2015.
 *
 * @author Amy, Cian, Shaun
 * 
 */
public class SpellChecker {
    
    static ArrayList<String> dictionary = loadDictionary("dictXtra.txt");
    static int suggestions = 5;
    
    /*public static void main(String[] args){
        spellCheck("hrttmm", 1000);
    }*/

    /**
     * Takes in a file and adds all tokens separated by a space to a List
     * @param filename The location of the input file
     * @return List of strings
     */
    public static ArrayList<String> getText(String filename){
        //Stores the list of words
        File file = new File(filename);
        ArrayList<String> text = new ArrayList<>();
        try{
            long beforeTime = System.nanoTime();
            Scanner inputText = new Scanner(file);
            while(inputText.hasNext()){
                text.add(inputText.next());
                //System.out.println(text.get(text.size()-1));            
            }
            long afterTime = System.nanoTime();
            System.out.println("The time to load the " + filename + " was: " + 
                    (afterTime - beforeTime) + " ns");
        } catch(FileNotFoundException e){
            text.add("File not found");
        }
        return text;
    }
    
    
    /**
     * Takes in an input string and the number of suggested words to be produced
     * Returns a List of these suggested words
     * @param input The input String
     * @return List of strings
     */
    public static ArrayList<String> spellCheck(String input){
        return spellCheck(input, suggestions);
    }
    
    
    /**
     * Takes in an input string and the number of suggested words to be produced
     * Returns a List of these suggested words
     * @param input The input String
     * @param suggs The number of suggested words to output
     * @return List of strings
     */
    public static ArrayList<String> spellCheck(String input, int suggs) {

        
        //long startTime = System.currentTimeMillis();
        
        /**
         * Create a map: Key - word; Value - edit distance
         */        
        HashMap editDwords = new HashMap();
        ArrayList<String> mins = new ArrayList<>(); 
            //stores the list of words with smallest minEditDist
        
        //Get capitalisation/punctuation state of the word
        WordState state = new WordState(input); 
        state.saveState();    
       
        //If the word is all numbers, ignore and return. This would perhaps 
        //be better outside of this method before it is called;
        if(WordState.clean(input).replaceAll("[0-9]", "").length() == 0){
            mins.add(input);
            return mins;
        }

        //Make lower case in case of accidental capitalisation
        input = input.toLowerCase();
        int minEditDist = Integer.MAX_VALUE;          
        int currEditDist;
        
        //System.out.println("Before Map: " + 
        //              (System.currentTimeMillis() - startTime) + " ms");
        
        //Generates the map
        for (int i = 0; i < dictionary.size(); i++) {
            currEditDist = DLdistance(WordState.clean(input), dictionary.get(i).toLowerCase());
            editDwords.put(dictionary.get(i), currEditDist);
            if(currEditDist < minEditDist) {
                minEditDist = currEditDist;
            }
        }
        
        //System.out.println("After Map: " + (System.currentTimeMillis() - startTime) + " ms");        
        //System.out.println("min Edit Distance = " + minEditDist);
        
        //Setup so that we can interate through the map
        Set set = editDwords.entrySet();
        Iterator<Map.Entry<String, Integer>> i = set.iterator();
        Map.Entry<String, Integer> me;
        
        //System.out.println("Before List: " + (System.currentTimeMillis() - startTime) + " ms");
        int distance = minEditDist;
        int count = 0;
        
        //Iterate through the map until a word with the minimum edit distance is found
        //Add that word to the List
        //If the desired number of suggestions has not been reached, 
        //Go around again adding words of a larger distance
        while(count < suggs && i.hasNext()) {
            while(count < suggs && i.hasNext() ) {
                me = i.next();
                if (me.getValue() == distance) {
                    mins.add(state.applyState(me.getKey()));

                    System.out.print(me.getValue() + ": ");
                    System.out.println(me.getKey());
                    //System.out.println(distance);
                    count++;
                }	                
            }
            distance++; //Increment the distance
            i = set.iterator();    //Reset the iterator        
        }
        	        			
	mins.add(state.applyState(input));//Apply capitalisation and punctuation
        //System.out.println("After List: " + (System.currentTimeMillis() - startTime) + " ms");
        //add the dict word with edit dist equal to distance(value)
        //long afterTime = System.currentTimeMillis();
        //System.out.println("Time to generate suggested words: " + (afterTime - startTime) + " ms");
        return mins;

    }
    
    
    /**
     * Returns the minimum of the three input integers
     * @param a 
     * @param b
     * @param c
     * @return int
     */
    private static int minimum(int a, int b, int c) {                            
        return Math.min(Math.min(a, b), c);                                      
    }

    /**
     * Returns the Levenshtein edit distance between the two Strings. 
     * @param target
     * @param source
     * @return 
     */
    public static int Ldistance(String target, String source){	
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

            /*
            *for(int i=0; i<target.length()+1; i++){  
            *    for(int j=0; j<source.length()+1; j++){  
            * 
            *            System.out.print(" [ " + dist[i][j] + " ] ");
            *    }
            *    System.out.print("\n");
            *}
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
        /*
            *for(int i=0; i<target.length()+1; i++){  
            *for(int j=0; j<source.length()+1; j++){  
            *
            *        System.out.print(" [ " + dist[i][j] + " ] ");
            *}
            *System.out.print("\n");
        }
            ***********/
            return dist[target.length()][source.length()]; 
    }
    
    /**
     * Checks if a word is in the dictionary, ignoring case
     * @param word
     * @return 
     */
    
    public static boolean inDictionary(String word){
        //long beforeTime = System.nanoTime();
        if(dictionary.contains(word)){
            return true;
        }
        word = word.toLowerCase();
        word = WordState.clean(word);
        boolean is = dictionary.contains(word);
        //long afterTime = System.nanoTime();
        //System.out.println("Time to find word in dictionary is: " + (afterTime - beforeTime) + "ns");
        return is;
    }    
    
    
    /**
     * Returns the Damerau-Levenshetein distance between the two Strings
     * @param target
     * @param source
     * @return 
     */
    public static int DLdistance(String target, String source) {
		
        int[][] dist = new int[target.length()+1][source.length()+1];  
        dist[0][0] = 0;

        for (int i=0; i<target.length()+1;i++){
            dist[i][0] = i;
        }

        for (int j=0;j<source.length()+1;j++){
            dist[0][j]= j;
        }


        for (int i=1; i<=target.length();i++){
            for (int j=1; j<=source.length(); j++){

                dist[i][j]= minimum(
                        dist[i-1][j]+1,
                        dist[i][j-1]+1,
                        dist[i-1][j-1]+((target.charAt(i-1) == source.charAt(j-1)) ? 0 : 1)); // boolean to int here

                if ( i > 1 && j > 1 && target.charAt(i-2) == source.charAt(j-1) 
                                && target.charAt(i-1) == source.charAt(j-2) ) {
                    dist[i][j]= Math.min(
                                dist[i][j],
                                dist[i-2][j-2]+((target.charAt(i-1) == source.charAt(j-1)) ? 0 : 1));
                }

            }
        }
        
        //Display the array
        /*for(int i=0; i<target.length()+1; i++){  
            for(int j=0; j<source.length()+1; j++){  

                    System.out.print(" [ " + dist[i][j] + " ] ");
            }
            System.out.print("\n");
        }
            
        System.out.println(dist[target.length()][source.length()]);*/
        return dist[target.length()][source.length()]; 
    }
 
    
    /**
     * Returns a List of Strings from an input file where each word is is on a new line
     * @param filename
     * @return An entire dictionary as a list of {@code Strings}
     */
    private static ArrayList<String> loadDictionary(String filename){
        ArrayList<String> dict = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            for (String i = in.readLine(); i != null; i = in.readLine()) {
                //System.out.println(i);
                dict.add(i);
            }
        }catch (IOException e) {
                System.out.println("Error: IOException in spellCheck() method! (unable to read " +filename + "");
        }
        return dict;
    }
    
    
}
