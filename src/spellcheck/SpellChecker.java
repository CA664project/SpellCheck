package spellcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Shaun
 */
public class SpellChecker {
    public static void main(String[] args){
        ArrayList<String> test = new ArrayList<>();
        test.add("only");
        test.add("one");
        test.add("mistakle");
        System.out.println(dictionary.contains("a"));
        /*for(String word: dictionary){
            System.out.println(word);
            if(word.equals("id")){
               break;
            }
           
        }*/
        //fixWords(test);
        
    }
    
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
        input = input.toLowerCase();
        /****check input edit Distance vs all words in dictionary, add all mins to list (temp for now)****/

        ArrayList<Integer> minDistance = new ArrayList<>(); //stores the minEditDist of each word in dict
        ArrayList<String> mins = new ArrayList<>(); //stores the list of words with smallest minEditDist

        /****
                Reads in the Dictionary file for now
                can be replaced later to only accept another Dwords arrayList!?

        ****/
        int minEditDist = Integer.MAX_VALUE;
        int currEditDist;
        for (int i = 0; i < dictionary.size(); i++) {
                currEditDist = minDist(input, dictionary.get(i));
                if(currEditDist < minEditDist){
                    mins = new ArrayList<>();
                    minEditDist = currEditDist;
                }
                if(currEditDist == minEditDist){
                    mins.add(dictionary.get(i));
                }
        }

        return mins;

    }
    
    public static ArrayList<String> fixWords(ArrayList<String> text){
        //ArrayList<String> output = new ArrayList<>();
        for(int i = 0; i < text.size(); i++){
            System.out.println("Checking " + text.get(i));
            if(!inDictionary(text.get(i))){
                System.out.println("Fixing " + text.get(i));
                text.set(i, spellCheck(text.get(i)).get(0));
                System.out.println("Is now " + text.get(i));
            }
        }
        
        
        return text;
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
    
    public static boolean inDictionary(String word){
        word = word.toLowerCase();
        return dictionary.contains(word);
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
