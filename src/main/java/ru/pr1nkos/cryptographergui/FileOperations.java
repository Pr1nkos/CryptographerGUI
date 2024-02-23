package ru.pr1nkos.cryptographergui;


import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

import java.nio.file.StandardOpenOption;
import java.util.*;


public class FileOperations {

	private FileOperations() {
		throw new IllegalStateException("Utility class");
	}

	private static final String TEXTFILES = "Text Files";
	private static final String ALLFILES = "All Files";
	private static final String EXT = "*.txt";


	public static List<Character> readFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Выберите файл для открытия");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter(TEXTFILES, EXT),
				new FileChooser.ExtensionFilter(ALLFILES, "*.*")
		);

		Stage stage = new Stage();
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile == null) {
			return new ArrayList<>();
		}


		return readFromFileUTF8(selectedFile);
	}


	public static List<Character> readFromFileUTF8(File file) {
		List<Character> textInCharacters = new ArrayList<>();
		try (FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
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
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Сохранить файл");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter(TEXTFILES, EXT),
				new FileChooser.ExtensionFilter(ALLFILES, "*.*")
		);

		Stage stage = new Stage();
		File selectedFile = fileChooser.showSaveDialog(stage);
		if (selectedFile == null) {
			return;
		}

		try (FileChannel fileChannel = FileChannel.open(selectedFile.toPath(),
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
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Выберите файл для сохранения ключа");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter(TEXTFILES, EXT),
				new FileChooser.ExtensionFilter(ALLFILES, "*.*")
		);

		Stage stage = new Stage();
		File selectedFile = fileChooser.showSaveDialog(stage);
		if (selectedFile != null) {
			try (FileChannel fileChannel = FileChannel.open(selectedFile.toPath(),
					StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
				String keyString = Integer.toString(key);
				ByteBuffer buffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(keyString));
				int bytesWritten = fileChannel.write(buffer);
				if (bytesWritten != keyString.length()) {
					System.out.println("Ошибка при записи ключа в файл");
				}
				else {
					System.out.println("Ключ успешно записан в файл");
				}
				fileChannel.force(true);
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
		}
	}
}
