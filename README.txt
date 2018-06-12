OVERVIEW
* Instantiate a Sentence object and then call getLongestWords() on it to return the length and longest words in the sentence which was passed to the object constructor. The values are returned in the format described in that method's JavaDocs.

FUNTIONALITY
* Multiple occurences of a longest word will be returned as separate instances.
* All punctuation is stripped from the sentence before calculating the word lengths.
* A possible enhancement to this class would be to include apostrophes and hyphens in the character count for words which contain them.

EXECUTING THE TESTS
* You can execute the test cases in WordLengthTest.java by running testng/testng.xml.
* The required dependencies are listed in the pom file
* Test output will be output to the console as well as to a log file in the directory logs. A unique log file will be created for each test run.
* You can add additional test cases to the "WordLength" sheet of workbook "src/test/resources/TestData.xlsx"