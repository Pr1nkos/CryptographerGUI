package ru.pr1nkos.cryptographergui;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileOperations {


	public static List<Character> readFileToDecrypt() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Введите путь к файлу, который нужно расшифровать:");
		String inputFilePath = sc.next();
		return readFromFileUTF8(inputFilePath);
	}

	public static List<Character> readFileToCrypt() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Введите путь к файлу, который нужно зашифровать:");
		String inputFilePath = sc.next();
		return readFromFileUTF8(inputFilePath);
	}

	public static int readKeyFile() {
		StringBuilder stringBuilder = new StringBuilder();
		Scanner sc = new Scanner(System.in);
		System.out.println("Введите путь к файлу, который содержит ключ:");
		String inputFilePath = sc.next();
		List <Character> keyToConvert = readFromFileUTF8(inputFilePath);
		for (char c : keyToConvert) {
			stringBuilder.append(c);
		}
		return Integer.parseInt(stringBuilder.toString());
	}

	public static List<Character> readFromFileUTF8(String path) {
		List<Character> textInCharacters = new ArrayList<>();
		try (FileChannel fileChannel = FileChannel.open(Path.of(path), StandardOpenOption.READ)) {
			int bufferSize = 40000;
			ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
			while (fileChannel.read(buffer) != -1) {
				buffer.flip();
				CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
				CharBuffer charBuffer = decoder.decode(buffer);
				for (int i = 0; i < charBuffer.length(); i++) {
					textInCharacters.add(charBuffer.get(i));
				}
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return textInCharacters;
	}

	public static void writeToFileUTF8(List<Character> text) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Введите путь для сохранения файла:");
		String outputFile = sc.next();
		try (FileChannel fileChannel = FileChannel.open(Path.of(outputFile),
				StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
			StringBuilder stringBuilder = new StringBuilder();
			text.forEach(stringBuilder::append);
			Charset charset = StandardCharsets.UTF_8;
			CharsetEncoder encoder = charset.newEncoder();
			ByteBuffer buffer = encoder.encode(CharBuffer.wrap(stringBuilder.toString()));
			int bytesWritten = fileChannel.write(buffer);
			if (bytesWritten != buffer.limit()) {
				System.err.println("Not all bytes were written!");
			}
			fileChannel.force(true);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
	}

	public static void writeKey(int key) {
		Scanner sc = new Scanner(System.in);
		String saveKeyQuestion = sc.next();
		if ("yes".equalsIgnoreCase(saveKeyQuestion)) {
			System.out.println("Введите полный путь для сохранения ключа:");
			String pathForSaveKey = sc.next();
			try (FileChannel fileChannel = FileChannel.open(Path.of(pathForSaveKey),
					StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
				String keyString = Integer.toString(key);
				Charset charset = StandardCharsets.UTF_8;
				CharsetEncoder encoder = charset.newEncoder();
				ByteBuffer buffer = encoder.encode(CharBuffer.wrap(keyString));
				int bytesWritten = fileChannel.write(buffer);
				if (bytesWritten != buffer.limit()) {
					System.err.println("Not all bytes were written!");
				}
				fileChannel.force(true);

			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
		}

	}
}
