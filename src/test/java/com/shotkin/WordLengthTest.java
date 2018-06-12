package com.shotkin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.Assert.assertFalse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author David Shotkin
 *
 */
public class WordLengthTest {

	private static final Logger log = LogManager.getLogger(WordLengthTest.class);
	private static String workbookPath = "TestData.xlsx";
	private static String sheetName = "WordLength";

	@DataProvider
	public static Object[][] testSentences() throws InvalidFormatException, IOException, URISyntaxException {
		Object[][] data;
		try (ExcelSheet sheet = new ExcelSheet(workbookPath, sheetName)) {
			data = new Object[sheet.getLastRowNum()][3];
			int currentRow = 0;
			for (XSSFRow row : sheet.getRows()) {
				data[currentRow][0] = row.getCell(0).getStringCellValue();
				data[currentRow][1] = (int) row.getCell(1).getNumericCellValue();
				data[currentRow++][2] = row.getCell(2).getStringCellValue().split("\\s+");
			}
		}
		return data;
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
	@Test(dataProvider = "testSentences")
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

	@DataProvider
	public static Object[][] emptyNull() {
		Object[][] data = new Object[2][2];
		data[0][0] = "";
		data[0][1] = "Testing an empty String";
		data[1][0] = null;
		data[1][1] = "Testing a null String";
		return data;
	}

	/**
	 * Confirms that an empty or null string does not return a longest word
	 * 
	 * @param string
	 *            an empty or null String
	 * @param testDescription
	 */
	@Test(dataProvider = "emptyNull")
	public void nonSentence(String string, String testDescription) {
		log.info(testDescription);
		Sentence sentence = new Sentence(string);
		Optional<Entry<Integer, List<String>>> optional = sentence.getLongestWords();
		assertFalse(optional.isPresent(), "getLongestWords() returned a longest word for a non-sentence.");
		log.info("\tConfirmed that no longest word was returned.");
	}
};