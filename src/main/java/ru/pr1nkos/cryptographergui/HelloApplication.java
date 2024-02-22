package ru.pr1nkos.cryptographergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ComplexApplication_css.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
		stage.setTitle("Hello!");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}