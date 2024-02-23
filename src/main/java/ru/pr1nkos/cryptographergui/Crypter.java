package ru.pr1nkos.cryptographergui;




import java.util.ArrayList;
import java.util.List;


public class Crypter {


	public static final char[] ALPHABET = {
			'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',
			'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
			'-', '_', '=', '+', '[', ']', '{', '}', ';', ':',
			'<', '>', '.', ',', '/', '?', '|', '\\', '`', '~', ' '
	};

	public static List<Character> codeList(List<Character> textToCode, int key) {
		List<Character> codedListOfCharacters = new ArrayList<>();
		for (char ch : textToCode) {
			if (ch == '\n' || ch == '\t') {
				codedListOfCharacters.add(ch);
			}
			if (ch == 'ё') {
				codedListOfCharacters.add('е');
			}
			if (ch == 'Ё') {
				codedListOfCharacters.add('Е');
			}
			else if (isInAlphabet(ch)) {
				int index = findAlphabetIndex(ch);
				int newIndex = (index + key + ALPHABET.length) % ALPHABET.length;

				char codedChar = ALPHABET[newIndex];
				codedListOfCharacters.add(codedChar);
			}
		}
		return codedListOfCharacters;
	}


	private static boolean isInAlphabet(char ch) {
		for (char alphabetChar : ALPHABET) {
			if (ch == alphabetChar) {
				return true;
			}
		}
		return false;
	}

	private static int findAlphabetIndex(char ch) {
		for (int i = 0; i < ALPHABET.length; i++) {
			if (ch == ALPHABET[i]) {
				return i;
			}
		}
		return -1;
	}
}
