/**
 * 
 */
package com.shotkin;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author David Shotkin
 *
 */
public class Sentence {

	/**
	 * The sentence passed into the constructor of this class, stripped of all punctuation (or an empty String if
	 * {@link #sentence} was null.)
	 */
	private final String sentence;
	/**
	 * A map whose keys are the lengths of the words in the sentence which was passed into this Object's constructor,
	 * and whose values are lists of all the words in that sentence whose lengths equal their key
	 */
	private Map<Integer, List<String>> wordLengthMap;
	/**
	 * A comparator which can be used to sort a Map<Integer, List<String>> in the reverse order of its key
	 */
	private static Comparator<? super Entry<Integer, List<String>>> reverseOrder =
			Map.Entry.comparingByKey(Comparator.reverseOrder());

	/**
	 * @param sentence
	 */
	public Sentence(String sentence) {
		if (sentence == null) sentence = "";
		this.sentence = sentence.replaceAll("\\p{P}", ""); // strip out all punctuation
	}

	/**
	 * @return an Optional containing a Map.Entry whose key is the length of the longest word in {@link #sentence} and
	 *         whose value is a list of all the words in the sentence of that length. An empty or null sentence will
	 *         return an empty optional.
	 */
	public Optional<Entry<Integer, List<String>>> getLongestWords() {
		return getWordLengthStream().sorted(reverseOrder).findFirst();
	}

	/**
	 * @return a Stream of the EntrySet of {@link #wordLengthMap}
	 */
	private Stream<Entry<Integer, List<String>>> getWordLengthStream() {
		if (wordLengthMap == null) {
			String[] words = sentence.split("\\s+");
			if (words.length == 1 && words[0] == "")
				wordLengthMap = new HashMap<Integer, List<String>>();
			else
				wordLengthMap = Arrays.stream(words).collect(Collectors.groupingBy(String::length));
		}
		return wordLengthMap.entrySet().stream();
	}
}
