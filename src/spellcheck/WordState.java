/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellcheck;

/**
 *
 * @author Shaun
 */
public class WordState {
    boolean isCapitalised = false;
    String punctuation;
    String text;
    
    public static void main(String[] args){
        WordState word = new WordState("HullaBalloo.");
        word.saveState();
        System.out.println(isCapital("TesTer"));
        System.out.println(word.applyState("tester"));
    }
    
    public WordState(String input){
        this.text = input;
    }
    
    public void saveState(){
        this.isCapitalised = isCapital(this.text);
        this.punctuation = getPunctuation(this.text);       
    }
    
    private static boolean isCapital(String input){
        return (!input.equals(input.replaceAll("[A-Z]","")));
    }
    
    private static String getPunctuation(String input){
        input = input.replaceAll("[a-zA-Z']", "");
        if(input.length() > 0){
            return input.substring(input.length()-1);
        } else {
            return "";
        }
    }
    
    public static String clearPunctuation(String input){
        return input.replaceAll("[^a-zA-Z']", "");
    }
    
    public String applyState(String input){
        input = clearPunctuation(input);
        Character first = input.charAt(0);
        if(isCapitalised){            
            first = Character.toUpperCase(first);
        }
        return first + input.substring(1) + this.punctuation;
    }
}
