package ru.pr1nkos.cryptographergui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class FileEncryptorController {
	@FXML
	private TextField keyTextField;

	@FXML
	private TextArea leftTextArea;

	@FXML
	private TextArea rightTextArea;

	protected static int keyFound = 0;
	private List<Character> fileContent;

	@FXML
	private void initialize() {
		keyTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, c ->
				(c.getControlNewText().matches("\\d*") && c.getControlNewText().length() <= 5) ? c : null));
	}

	@FXML
	private void handleOpenFileAction() {
		fileContent = FileOperations.readFile();
		leftTextArea.setText(fileContent.stream().map(Object::toString).collect(Collectors.joining()));
	}

	@FXML
	private void handleEncryptButtonAction() {
		if (fileContent != null && !fileContent.isEmpty()) {
			try {
				int key = Integer.parseInt(keyTextField.getText());
				List<Character> encryptedContent = Crypt.codeList(fileContent, key);
				rightTextArea.setText(encryptedContent.stream().map(Object::toString).collect(Collectors.joining()));
			} catch (NumberFormatException e) {
				e.printStackTrace(System.out);
			}
		}
	}

	@FXML
	private void handleDecryptWithKey() {
		if (fileContent != null && !fileContent.isEmpty()) {
			try {
				int key = Integer.parseInt(keyTextField.getText());
				List<Character> encryptedContent = Decoder.decode(fileContent, key);
				rightTextArea.setText(encryptedContent.stream().map(Object::toString).collect(Collectors.joining()));
			} catch (NumberFormatException e) {
				e.printStackTrace(System.out);
			}
		}
	}

	@FXML
	private void handleDecryptBySpaces() {
		if (fileContent != null && !fileContent.isEmpty()) {
			try {
				List<Character> encryptedContent = Decoder.decodeBySpaces(fileContent);
				rightTextArea.setText(encryptedContent.stream().map(Object::toString).collect(Collectors.joining()));
			} catch (NumberFormatException e) {
				e.printStackTrace(System.out);
			}
		}
		keyTextField.setText(String.valueOf(keyFound));
	}

	@FXML
	private void handleByDictionary() {
		if (fileContent != null && !fileContent.isEmpty()) {
			try {
				List<Character> encryptedContent = Decoder.decodeBruteForce(fileContent);
				rightTextArea.setText(encryptedContent.stream().map(Object::toString).collect(Collectors.joining()));
			} catch (NumberFormatException e) {
				e.printStackTrace(System.out);
			}
		}
		keyTextField.setText(String.valueOf(keyFound));
		resetCount();
	}
	private static void resetCount(){
		keyFound = 0;
	}


	@FXML
	private void handleStatistics() {
		if (fileContent != null && !fileContent.isEmpty()) {
			try {
				List<Character> encryptedContent = Decoder.decodeWithStatisticalAnalyser(fileContent);
				rightTextArea.setText(encryptedContent.stream().map(Object::toString).collect(Collectors.joining()));
			} catch (NumberFormatException e) {
				e.printStackTrace(System.out);
			}
		}
		keyTextField.setText(String.valueOf(keyFound));
		resetCount();
	}

	@FXML
	private void handleSaveFileAction() {
		if (fileContent != null && !fileContent.isEmpty()) {
			try {
				String encryptedContent = rightTextArea.getText();
				List<Character> encryptedContentList = new ArrayList<>();

				for (char ch : encryptedContent.toCharArray()) {
					encryptedContentList.add(ch);
				}
				FileOperations.writeToFileUTF8(encryptedContentList);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	@FXML
	private void handleSaveKeyAction() {
		try {
			int key = Integer.parseInt(keyTextField.getText());
			FileOperations.writeKey(key);
		} catch (NumberFormatException e) {
			e.printStackTrace(System.out);
		}
	}


}