package ru.pr1nkos.cryptographergui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

import java.util.stream.Collectors;

import static java.lang.Math.pow;

public class Decoder {

	public static int keyFound = 0;

	public static List<Character> decode(List<Character> textToDecrypt, int key) {
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
		return decodedListOfCharacters;
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

	public static List<Character> decodeWithStatisticalAnalyser(List<Character> textToDecrypt) {
		List<Character> decodedText = new ArrayList<>();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Выберите файлы для сбора статистики");
		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

		if (selectedFiles != null && !selectedFiles.isEmpty()) {

			Map<Character, Double> combinedRepresentativeMap = combineStatistics(selectedFiles);
			Map<Character, Double> encryptedStats = calculateLetterFrequency(textToDecrypt);
			findBestShift(combinedRepresentativeMap, encryptedStats);
			decodedText.addAll(decode(textToDecrypt, keyFound));
		}
		return decodedText;
	}




	public static Map<Character, Double> combineStatistics(List<File> files) {
		List<Map<Character, Double>> listOfRepresentativeTexts = new ArrayList<>();

		for (File file : files) {
			List<Character> textForStatisticAnalysis = FileOperations.readFromFileUTF8(file);
			listOfRepresentativeTexts.add(calculateLetterFrequency(textForStatisticAnalysis));
		}

		Map<Character, Double> combinedStats = new HashMap<>();

		int numberOfFiles = listOfRepresentativeTexts.size();

		for (Map<Character, Double> mapToAdd : listOfRepresentativeTexts) {
			for (char letter : Crypter.ALPHABET) {
				double combinedFrequency = combinedStats.getOrDefault(letter, 0.0) + mapToAdd.getOrDefault(letter, 0.0);
				combinedStats.put(letter, combinedFrequency);
			}
		}
		combinedStats.replaceAll((key, value) -> value / numberOfFiles);

		return combinedStats;
	}


	public static List<Character> decodeBruteForce(List<Character> textToDecrypt) {
		Map<String, Integer> filteredDictionary = buildDictionary();
		List<List<Character>> decodedListOfListsOfCharacters = new ArrayList<>();
		for (int i = 0; i < Crypter.ALPHABET.length; i++) {
			List<Character> decodedListOfCharacters = new ArrayList<>();

			for (char ch : textToDecrypt) {
				if (ch == '\n' || ch == '\t') {
					decodedListOfCharacters.add(ch);
				}
				else if (isInAlphabet(ch)) {
					int index = findAlphabetIndex(ch);
					int newIndex = (index - i + Crypter.ALPHABET.length) % Crypter.ALPHABET.length;
					char decodedChar = Crypter.ALPHABET[newIndex];
					decodedListOfCharacters.add(decodedChar);
				}
			}
			decodedListOfListsOfCharacters.add(decodedListOfCharacters);
		}
		decodedListOfListsOfCharacters.reversed();
		return findBestTextFromList(decodedListOfListsOfCharacters, filteredDictionary);

	}

	public static List<Character> decodeBySpaces(List<Character> textToDecrypt) {
		List<List<Character>> decodedListOfListsOfCharacters = new ArrayList<>();
		for (int i = 0; i < Crypter.ALPHABET.length; i++) {
			List<Character> decodedListOfCharacters = new ArrayList<>();

			for (char ch : textToDecrypt) {
				if (ch == '\n' || ch == '\t') {
					decodedListOfCharacters.add(ch);
				}
				else if (isInAlphabet(ch)) {
					int index = findAlphabetIndex(ch);
					int newIndex = (index - i + Crypter.ALPHABET.length) % Crypter.ALPHABET.length;
					char decodedChar = Crypter.ALPHABET[newIndex];
					decodedListOfCharacters.add(decodedChar);
				}
			}
			decodedListOfListsOfCharacters.add(decodedListOfCharacters);
		}
		decodedListOfListsOfCharacters.reversed();
		return findBestTextFromListBySpace(decodedListOfListsOfCharacters);
	}

	public static List<Character> findBestTextFromListBySpace(List<List<Character>> listOfPossibleDecodedTexts) {
		int maxSpaceCount = Integer.MIN_VALUE;
		List<Character> bestMatchText = new ArrayList<>();

		for (int i = 0; i < listOfPossibleDecodedTexts.size(); i++) {
			List<Character> decodedText = listOfPossibleDecodedTexts.get(i);
			int currentSpaceCount = countSpaces(decodedText);
			if (currentSpaceCount > maxSpaceCount) {
				maxSpaceCount = currentSpaceCount;
				bestMatchText = new ArrayList<>(decodedText);
				keyFound = i;
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
			keyFound++;
		}

		return bestMatchText;
	}

	private static int calculateMatchScore(List<Character> text, Map<String, Integer> dictionary) {
		int matchScore = 0;

		for (Map.Entry<String, Integer> entry : dictionary.entrySet()) {
			String word = entry.getKey();
			int wordFrequency = entry.getValue();
			int countInText = countOccurrences(text, word);
			matchScore = matchScore + (int) Math.pow((double) wordFrequency - (double) countInText, 2);
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
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Выберите файл для построения словаря");
		File selectedFile = fileChooser.showOpenDialog(new Stage());
		if (selectedFile != null) {
			List<Character> textForDict = FileOperations.readFromFileUTF8(selectedFile);
			StringBuilder currentWord = new StringBuilder();
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
		}
		Map<String, Integer> sortedDictionary = QuickSortMethod.sortDictionary(dictionary);
		return sortedDictionary.entrySet()
				.stream()
				.filter(entry -> entry.getKey().length() >= 4)
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.limit(5)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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

	public static void findBestShift(Map<Character, Double> representativeStats, Map<Character, Double> encryptedStats) {
		double minDeviation = Double.MAX_VALUE;

		for (int shift = 0; shift < Crypter.ALPHABET.length; shift++) {

			double deviation;
			deviation = calculateDeviation(representativeStats, encryptedStats, shift);
			if (deviation < minDeviation) {
				minDeviation = deviation;
				keyFound = shift;
			}
		}
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
