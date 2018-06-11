package com.shotkin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

/**
 * @author David Shotkin
 *
 */
public class WordLengthTest {

	private final Logger log = LogManager.getLogger(getClass());

	@Test
	public void nullSentence() {
		log.info("TESTING a null sentence.");
		Sentence sentence = new Sentence(null);
		Optional<Entry<Integer, List<String>>> optional = sentence.getLongestWords();
		assertFalse(optional.isPresent(), "getLongestWords() returned a longest word for a null Sentence.");
		log.info("\tConfirmed that no longest word was returned.");
	}

	@Test
	public void emptySentence() {
		log.info("TESTING an empty sentence.");
		Sentence sentence = new Sentence("");
		Optional<Entry<Integer, List<String>>> optional = sentence.getLongestWords();
		assertFalse(optional.isPresent(), "getLongestWords() returned a longest word for an empty Sentence.");
		log.info("\tConfirmed that no longest word was returned.");
	}

	@Test
	public void oneWordSentence() {
		testGetLongestWords("Goose", 5, "Goose");
	}

	@Test
	public void sentenceWithMultipleLongestWords() {
		testGetLongestWords("Hey diddle diddle the cat and the fiddle", 6, "diddle", "diddle", "fiddle");
	}

	@Test
	public void multiwordSentenceWithOneLongestWord() {
		testGetLongestWords("The cow jumped over the moon", 6, "jumped");
	}

	@Test
	public void longestWordIsPunctuated() {
		testGetLongestWords("To be, or not to be - that is the question!", 8, "question");
	}

	@Test
	public void longestWordIsApostrephized() {
		testGetLongestWords("Shouldn't the longest word in this example have a length of nine?", 8, "Shouldnt");
	}

	@Test
	public void longestWordIsHyphenated() {
		testGetLongestWords("Shouldn't the longest word in this example have a properly-hyphenated length of nineteen?",
				18, "properlyhyphenated");
	}

	/**
	 * 
	 * @param sentenceText
	 *            The text of the sentence to be tested. Cannot be null or empty.
	 * @param expectedWordLength
	 *            The expected key of the entry returned by calling getLongestWords() on a Sentence object constructed
	 *            using sentenceText. (The key of the entry represents the word length).
	 * @param expectedLongestWords
	 *            The expected Strings contained in the value of the entry returned by calling getLongestWords() on a
	 *            Sentence object constructed using sentenceText. (The value of the entry should contain a List of the
	 *            longest word(s) in sentenceText).
	 */
	private void testGetLongestWords(String sentenceText, Integer expectedWordLength, String... expectedLongestWords) {
		if (sentenceText == null || sentenceText.isEmpty())
			throw new IllegalArgumentException("Cannot pass a null or empty sentenceText to testGetLongestWords()");
		Sentence sentence = new Sentence(sentenceText);
		Optional<Entry<Integer, List<String>>> optional = sentence.getLongestWords();
		Entry<Integer, List<String>> entry = optional.get();
		Integer actualWordLength = entry.getKey();
		List<String> actualLongestWordList = entry.getValue();
		List<String> expectedLongestWordList = Arrays.asList(expectedLongestWords);
		log.info(StringUtils.join("\n\tTEST SENTENCE: ", sentenceText,
				"\n\t\tLength of Longest Word (Expected / Actual): ", expectedWordLength, " / ", actualWordLength,
				"\n\t\tLongest Word(s) (Expected / Actual): ", Arrays.asList(expectedLongestWords), " / ",
				Arrays.asList(actualLongestWordList)));
		try {
			assertAll(
					StringUtils.join("values returned by getLongestWords() for a Sentence constructed using the text '",
							sentenceText, "'."),
					() -> assertTrue(optional.isPresent(), "a longest word was not returned"),
					() -> assertEquals(expectedWordLength, actualWordLength, "the length of the longest word"),
					() -> assertEquals(expectedLongestWordList, actualLongestWordList, "the longest word(s)"));
			log.info("\tAll values were returned correctly.");
		} catch (AssertionError e) {
			log.error("\n\t" + e.getLocalizedMessage());
			throw e;
		}
	}
};