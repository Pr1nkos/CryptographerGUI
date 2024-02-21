package ru.pr1nkos.cryptographergui;



import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

public class Decoder {

	public static void decode(List<Character> textToDecrypt, int key) {
		List<Character> decodedListOfCharacters = new ArrayList<>();
		for (char ch : textToDecrypt) {
			if (ch == '\n' || ch == '\t') {
				decodedListOfCharacters.add(ch);
			}
			else if (isInAlphabet(ch)) {
				int index = findAlphabetIndex(ch);
				int newIndex = (index - key + Crypter.ALPHABET.length) % Crypter.ALPHABET.length;
				char codedChar = Crypter.ALPHABET[newIndex];
				decodedListOfCharacters.add(codedChar);
			}
		}
		FileOperations.writeToFileUTF8(decodedListOfCharacters);
	}

	public static void decodeWithKey() {
		List<Character> textToDecrypt = FileOperations.readFileToDecrypt();
		int key = FileOperations.readKeyFile();
		decode(textToDecrypt, key);
	}

	private static boolean isInAlphabet(char ch) {
		for (char alphabetChar : Crypter.ALPHABET) {
			if (ch == alphabetChar) {
				return true;
			}
		}
		return false;
	}

	private static int findAlphabetIndex(char ch) {
		for (int i = 0; i < Crypter.ALPHABET.length; i++) {
			if (ch == Crypter.ALPHABET[i]) {
				return i;
			}
		}
		return -1;
	}

	public static void decodeWithStatisticalAnalyser() {
		List<Character> textToDecryptViaStatisticalAnalyser = FileOperations.readFileToDecrypt();
		Map<Character, Double> combinedRepresentativeMap = combineStatistics();
		Map<Character, Double> encryptedStats = Decoder.calculateLetterFrequency(textToDecryptViaStatisticalAnalyser);
		int bestShift = findBestShift(combinedRepresentativeMap, encryptedStats);
		decode(textToDecryptViaStatisticalAnalyser, bestShift);
	}

	public static void decodeBruteForce() {
		List<Character> textToDecrypt = FileOperations.readFileToDecrypt();
		Map<String, Integer> filteredDictionary = buildDictionary();
		List<List<Character>> decodedListOfListsOfCharacters = new ArrayList<>();
		for (int i = 0; i < Crypter.ALPHABET.length; i++) {
			List<Character> decodedListOfCharacters = new ArrayList<>();

			for (char ch : textToDecrypt) {
				if (ch == '\n' || ch == '\t') {
					decodedListOfCharacters.add(ch);
				} else if (isInAlphabet(ch)) {
					int index = findAlphabetIndex(ch);
					int newIndex = (index - i + Crypter.ALPHABET.length) % Crypter.ALPHABET.length;
					char decodedChar = Crypter.ALPHABET[newIndex];
					decodedListOfCharacters.add(decodedChar);
				}
			}
			decodedListOfListsOfCharacters.add(decodedListOfCharacters);
		}
		decodedListOfListsOfCharacters.reversed();
		List<Character> bestText = findBestTextFromList(decodedListOfListsOfCharacters, filteredDictionary);
		FileOperations.writeToFileUTF8(bestText);
	}

	public static void decodeBySpaces() {
		List<Character> textToDecrypt = FileOperations.readFileToDecrypt();
		List<List<Character>> decodedListOfListsOfCharacters = new ArrayList<>();
		for (int i = 0; i < Crypter.ALPHABET.length; i++) {
			List<Character> decodedListOfCharacters = new ArrayList<>();

			for (char ch : textToDecrypt) {
				if (ch == '\n' || ch == '\t') {
					decodedListOfCharacters.add(ch);
				} else if (isInAlphabet(ch)) {
					int index = findAlphabetIndex(ch);
					int newIndex = (index - i + Crypter.ALPHABET.length) % Crypter.ALPHABET.length;
					char decodedChar = Crypter.ALPHABET[newIndex];
					decodedListOfCharacters.add(decodedChar);
				}
			}
			decodedListOfListsOfCharacters.add(decodedListOfCharacters);
		}
		decodedListOfListsOfCharacters.reversed();
		List<Character> decryptedText = findBestTextFromListBySpace(decodedListOfListsOfCharacters);
		FileOperations.writeToFileUTF8(decryptedText);
	}

	public static List<Character> findBestTextFromListBySpace(List<List<Character>> listOfPossibleDecodedTexts) {
		int maxSpaceCount = Integer.MIN_VALUE;
		List<Character> bestMatchText = new ArrayList<>();

		for (List<Character> decodedText : listOfPossibleDecodedTexts) {
			int currentSpaceCount = countSpaces(decodedText);
			if (currentSpaceCount > maxSpaceCount) {
				maxSpaceCount = currentSpaceCount;
				bestMatchText = new ArrayList<>(decodedText);
			}
		}

		return bestMatchText;
	}

	private static int countSpaces(List<Character> text) {
		int spaceCount = 0;
		for (char c : text) {
			if (c == ' ') {
				spaceCount++;
			}
		}
		return spaceCount;
	}

	public static List<Character> findBestTextFromList(List<List<Character>> listOfPossibleDecodedTexts, Map<String, Integer> dictionary) {
		int bestMatchScore = Integer.MAX_VALUE;
		List<Character> bestMatchText = new ArrayList<>();

		for (List<Character> decodedText : listOfPossibleDecodedTexts) {
			int currentScore = calculateMatchScore(decodedText, dictionary);
			if (currentScore < bestMatchScore) {
				bestMatchScore = currentScore;
				bestMatchText = new ArrayList<>(decodedText);
			}
		}

		return bestMatchText;
	}

	private static int calculateMatchScore(List<Character> text, Map<String, Integer> dictionary) {
		int matchScore = 0;

		for (Map.Entry<String, Integer> entry : dictionary.entrySet()) {
			String word = entry.getKey();
			int wordFrequency = entry.getValue();
			int countInText = countOccurrences(text, word);
			matchScore = matchScore + (int) Math.pow((double)wordFrequency - (double)countInText, 2);
		}

		return matchScore;
	}

	private static int countOccurrences(List<Character> text, String word) {
		int count = 0;
		int index = 0;

		while (index != -1) {
			index = indexOfWord(text, word, index);
			if (index != -1) {
				count++;
				index += word.length();
			}
		}

		return count;
	}

	private static int indexOfWord(List<Character> text, String word, int startIndex) {
		StringBuilder textString = new StringBuilder();
		for (char character : text) {
			textString.append(character);
		}

		return textString.indexOf(word, startIndex);
	}

	public static Map<String, Integer> buildDictionary() {
		Map<String, Integer> dictionary = new HashMap<>();
		StringBuilder currentWord = new StringBuilder();
		Scanner sc = new Scanner(System.in);
		System.out.println("Введите путь к файлу для создания словаря:");
		String inputFileForDict = sc.next();
		List<Character> textForDict = FileOperations.readFromFileUTF8(inputFileForDict);
		for (char ch : textForDict) {
			if (Character.isLetter(ch)) {
				currentWord.append(ch);
			}
			else if (!currentWord.isEmpty()) {
				String word = currentWord.toString().toLowerCase();
				dictionary.put(word, dictionary.getOrDefault(word, 0) + 1);
				currentWord.setLength(0);
			}
		}

		if (!currentWord.isEmpty()) {
			String word = currentWord.toString().toLowerCase();
			dictionary.put(word, dictionary.getOrDefault(word, 0) + 1);
		}
		Map<String, Integer> sortedDictionary = QuickSortMethod.sortDictionary(dictionary);
		return sortedDictionary.entrySet()
				.stream()
				.filter(entry -> entry.getKey().length() >= 4)
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.limit(5)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}


	public static Map<Character, Double> combineStatistics() {
		Scanner sc = new Scanner(System.in);
		List<Map<Character, Double>> listOfRepresentativeTexts = new ArrayList<>();
		while (true) {
			System.out.println("Введите путь к файлу (-ам), для проведения статистического анализа: ");
			String inputFileForStatistics = sc.next();
			List<Character> textForStatisticAnalise = FileOperations.readFromFileUTF8(inputFileForStatistics);
			listOfRepresentativeTexts.add(calculateLetterFrequency(textForStatisticAnalise));
			System.out.println("Загрузить ещё один файл?");
			String condition = sc.next();
			if (condition.equals("no")) break;
		}

		Map<Character, Double> combinedStats = new HashMap<>();

		for (Map<Character, Double> mapToAdd : listOfRepresentativeTexts) {
			for (char letter : Crypter.ALPHABET) {
				double combinedFrequency = (combinedStats.getOrDefault(letter, 0.0) + mapToAdd.getOrDefault(letter, 0.0)) / 2.0;
				combinedStats.put(letter, combinedFrequency);
			}
		}

		return combinedStats;
	}

	public static Map<Character, Double> calculateLetterFrequency(List<Character> text) {
		Map<Character, Integer> frequencyMap = new HashMap<>();
		for (char ch : text) {
			if (ch == '\n' || ch == '\t') {
				continue;
			}
			frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
		}
		int totalLetters = frequencyMap.values().stream().mapToInt(Integer::intValue).sum();
		Map<Character, Double> frequencyPercentageMap = new HashMap<>();
		for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
			double percentage = ((double) entry.getValue() / totalLetters) * 100;
			frequencyPercentageMap.put(entry.getKey(), percentage);
		}
		return frequencyPercentageMap;
	}
	public static int findBestShift(Map<Character, Double> representativeStats, Map<Character, Double> encryptedStats) {
		int bestShift = 0;
		double minDeviation = Double.MAX_VALUE;

		for (int shift = 0; shift < Crypter.ALPHABET.length; shift++) {

			double deviation;
			deviation = calculateDeviation(representativeStats, encryptedStats, shift);
			if (deviation < minDeviation) {
				minDeviation = deviation;
				bestShift = shift;
			}
		}
		return bestShift;
	}

	private static char shiftChar(char ch, int shift) {
		if (Character.isLetter(ch)) {
			int base = Crypter.ALPHABET[0];
			return (char) ((ch - base + shift + Crypter.ALPHABET.length) % Crypter.ALPHABET.length + base);
		}
		else {
			return ch;
		}
	}

	private static double calculateDeviation(Map<Character, Double> combinedTextForAnalyse, Map<Character, Double> cryptText, int shift) {
		double deviation = 0;
		for (Map.Entry<Character, Double> entry : combinedTextForAnalyse.entrySet()) {
			char representativeChar = entry.getKey();
			double representativeFreq = entry.getValue();

			char shiftedChar = shiftChar(representativeChar, shift);
			double encryptedFreq = cryptText.getOrDefault(shiftedChar, 0.0);

			deviation += pow(representativeFreq - encryptedFreq, 2);
		}
		return deviation;
	}

}
