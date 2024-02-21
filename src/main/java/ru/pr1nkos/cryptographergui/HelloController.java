package ru.pr1nkos.cryptographergui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Random;

public class HelloController {
	@FXML
	private Label welcomeText;

	@FXML
	protected void onHelloButtonClick() {
		welcomeText.setText("Welcome to JavaFX Application!");
	}

	@FXML
	protected void handleButtonClick(ActionEvent event) {
		Random random = new Random();
		int randomNumber = random.nextInt(100);
		System.out.println("Button Clicked! Random Number: " + randomNumber);
	}
}