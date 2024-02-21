package ru.pr1nkos.cryptographergui;


import java.util.*;

public class Main {
	//TODO Сократить объем Main. Вынести механизмы по классам.
	//TODO Сделать пользовательский интерфейс
	//TODO добавить Exception на неправильные пути, на неправильный ввод данных и т.д.
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Выберите режим работы программы:");
			System.out.println("    1. Шифрование");
			System.out.println("    2. Расшифровка");
			System.out.println("    3. Завершение работы программы");

			int choice = scanner.nextInt();

			switch (choice) {
				case 1:
					encryptMenu();
					break;
				case 2:
					decryptMenu(scanner);
					break;
				case 3:
					System.out.println("Программа завершает работу.");
					System.exit(0);
					break;
				default:
					System.out.println("Некорректный ввод. Попробуйте снова.");
			}
		}
	}

	private static void encryptMenu() {
		Crypter.codeList();
	}

	private static void decryptMenu(Scanner scanner) {
		System.out.println("Выберите метод расшифровки:");
		System.out.println("    1. По ключу");
		System.out.println("    2. BruteForce");
		System.out.println("    3. Статистический анализ");
		System.out.println("    4. Выход в предыдущее меню");

		int decryptChoice = scanner.nextInt();

		switch (decryptChoice) {
			case 1:
				Decoder.decodeWithKey();
				break;
			case 2:
				bruteForceMenu(scanner);
				break;
			case 3:
				Decoder.decodeWithStatisticalAnalyser();
				break;
			case 4:
				System.out.println("Возвращение в предыдущее меню.");
				break;
			default:
				System.out.println("Некорректный выбор. Попробуйте снова.");
		}
	}

	private static void bruteForceMenu(Scanner scanner) {
		System.out.println("Выберите режим BruteForce:");
		System.out.println("1. Режим 'По пробелу'");
		System.out.println("2. Режим 'По наибольшему соответствию'");
		System.out.println("3. Выход в предыдущее меню");

		int bruteForceChoice = scanner.nextInt();

		switch (bruteForceChoice) {

			case 1:
				Decoder.decodeBySpaces();
				break;
			case 2:
				Decoder.decodeBruteForce();
				break;
			case 3:
				System.out.println("Возвращение в предыдущее меню.");
				break;
			default:
				System.out.println("Некорректный выбор. Попробуйте снова.");
		}
	}
}

