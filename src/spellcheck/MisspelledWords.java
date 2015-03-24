package spellcheck;

import java.util.*;

/**************
 Create map of misspelled words, index and word
 Map badWords(ArrayList words){
 	for each misspelled word,
 	add index of word and word value to badWords
 }
 **************/

public class MisspelledWords {
	
	/************** TEST DRIVER **************
	public static void main (String [] args){
		ArrayList<String> w = new ArrayList<String>();
		ArrayList<String> d = new ArrayList<String>();
		w.add("aarwddvark");
		w.add("cow");
		w.add("badgser");
		w.add("elephant");
		
		d.add("aardvark");
		d.add("cow");
		d.add("badger");
		d.add("elephant");
		d.add("fish");
		
		Hashtable<Integer, String> baddies = badWords(w,d);
		for(int i=0;i<w.size();i++){
			System.out.println("index is "+i+" and bad word is "+baddies.get(i));
		}
	}
	******************************************/

	public static Hashtable<Integer, String> badWords (ArrayList<String> words, ArrayList<String> dict){
		Hashtable<Integer, String> badWords = new Hashtable<Integer, String>(words.size());
		
		for(int i=0;i<words.size();i++){
			if(dict.contains(words.get(i))==false){
				System.out.println(words.get(i));
				badWords.put(i, words.get(i));
			}
		}
		
		return badWords;
	}
	
}
