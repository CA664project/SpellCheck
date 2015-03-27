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
 * suggesting alternative words for a misspelled word.
 *
 * @author Amy, Cian, Shaun
 */
public class SpellChecker {
    
    static ArrayList<String> dictionary = loadDictionary("dictXtra.txt");
    
    public static ArrayList<String> getText(String filename){
        //Stores the list of words
        File file = new File(filename);
        ArrayList<String> text = new ArrayList<>();
        try{
        Scanner inputText = new Scanner(file);
        while(inputText.hasNext()){
            text.add(inputText.next());
            //System.out.println(text.get(text.size()-1));            
        }
        } catch(FileNotFoundException e){
            text.add("File not found");
        }
        return text;
    }
    
    public static ArrayList<String> spellCheck(String input) {
        WordState state = new WordState(input);
        state.saveState();
        input = input.toLowerCase();
        /****check input edit Distance vs all words in dictionary, add all mins to list (temp for now)****/

         //  ArrayList<Integer> minDistance = new ArrayList<>(); //stores the minEditDist of each word in dict
        HashMap editDwords = new HashMap();
        ArrayList<String> mins = new ArrayList<>(); //stores the list of words with smallest minEditDist

        /****
            key = dictionary word
            value = minimum edit distance              
            put(key, value)
        ****/
        int minEditDist = Integer.MAX_VALUE;
      
        int currEditDist;
        for (int i = 0; i < dictionary.size(); i++) {
            currEditDist = DLdistance(input, dictionary.get(i));
            editDwords.put(dictionary.get(i), currEditDist);

            if(currEditDist < minEditDist) {
                minEditDist = currEditDist;
            }
        }
        
        System.out.println("min Edit Distance = " + minEditDist);
        Set set = editDwords.entrySet();
        Iterator<Map.Entry<String, Integer>> i = set.iterator();
        Map.Entry<String, Integer> me;
        
        
        int distance = minEditDist;
        int count = 0;
        while(count < 5 && i.hasNext()) {
            while(count < 5 && i.hasNext() ) {
                me = i.next();
                if (me.getValue() == distance) {
                    mins.add(state.applyState(me.getKey()));

                    System.out.print(me.getValue() + ": ");
                    System.out.println(me.getKey());
                    //System.out.println(distance);
                    count++;
                }	                
            }
            distance++; //Increment the distan as wce
            i = set.iterator();    //Reset the iterator        
        }
        	        			
	mins.add(state.applyState(input));
        //add the dict word with edit dist equal to distance(value)


        return mins;

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
    
    public static boolean inDictionary(String word){
        word = word.toLowerCase();
        word = WordState.clearPunctuation(word);
        return dictionary.contains(word);
    }    
    
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

        return dist[target.length()][source.length()]; 
    }
    
    private static ArrayList<String> loadDictionary(String filename){
        ArrayList<String> dict = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            for (String i = in.readLine(); i != null; i = in.readLine()) {
                //System.out.println(i);
                dict.add(i);
            }
        }catch (IOException e) {
                System.out.println("Error: IOException in spellCheck() method! (unable to read dict.txt?)");
        }
        return dict;
    }
    
    
}
