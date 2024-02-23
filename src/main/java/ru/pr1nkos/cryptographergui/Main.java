package ru.pr1nkos.cryptographergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ComplexApplication_css.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
		stage.setScene(scene);
		stage.setTitle("Проект 1 модуля Косач Серафима");
		stage.setMinWidth(700);
		stage.setMinHeight(600);

		stage.show();

	}


}

