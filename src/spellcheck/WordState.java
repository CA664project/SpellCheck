
package spellcheck;

/** The {@code WordState} is a simple class which stores 
 * initial capitalisation and punctuation parameters for 
 * an input word. It can then apply those parameters to 
 * a given input word. 
 *
 * @author Amy, Cian, Shaun
 */
public class WordState {
    boolean isCapitalised;
    String punctuation;
    String numbers;
    String text;
    
    /*public static void main(String[] args){
        WordState word = new WordState("HullaBalloo.");
        word.saveState();
        System.out.println(isCapital("TesTer"));
        System.out.println(word.applyState("tester"));
    }*/
    
    
    public WordState(String input){
        this.isCapitalised = false;
        this.punctuation = "";
        this.numbers = "";
        this.text = input;
    }
    
    
    /**
     * Saves the capitalisation and punctuation state of the word used in the constructor
     */
    public void saveState(){
        /**
         * Gets capitalisation and punctuation state         * 
         */
        this.isCapitalised = isCapital(this.text);
        this.punctuation = getPunctuation(this.text);    
        this.numbers = getNumbers(this.text);
    }
    
    /**
     * Checks if a word has any capital letters
     * @param input
     * @return 
     */
    private boolean isCapital(String input){
        /**
         * Currently returns true if the word contains any capital letters
         */
        return (!input.equals(input.replaceAll("[A-Z]","")));
    }
    
    
    /**
     * Gets returns the last character in a String that is not a letter as a {@code String}
     * @param input
     * @return 
     */
    private String getPunctuation(String input){
        /**
         * Retrieves the last character that isn't a letter or a number.
         */
        input = input.replaceAll("[a-zA-Z0-9'`]", "");
        if(input.length() > 0){
            return input.substring(input.length()-1);
        } else {
            return "";
        }
    }
    
    
    /**
     * Returns all numbers in a String as a String
     * @param input
     * @return 
     */
    private String getNumbers(String input){
        /**
         * Returns all numbers in the input string as a String
         */
        return input.replaceAll("[^0-9]", "");
    }
    
    
    /**
     * Removes every character that isn't a letter or apostrophe
     * @param input
     * @return 
     */
    public static String clean(String input){
        return input.replaceAll("[^a-zA-Z'`]", "");
    }
    
    /**
     * Applies the saved state to an input word
     * @param input
     * @return 
     */
    public String applyState(String input){
        input = clean(input);
        Character first;
        if(input.length() > 0){
            first = input.charAt(0);
            if(isCapitalised){            
                first = Character.toUpperCase(first);
            }
        
            return this.numbers + first + input.substring(1) + this.punctuation;
        } else {
            return this.numbers + this.punctuation;
        }
    }
}
