package ru.pr1nkos.cryptographergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class Main extends Application {

	public void start(Stage stage) throws IOException {
		Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Icon.png")));
		stage.getIcons().add(icon);
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ComplexApplication_css.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
		stage.setScene(scene);
		stage.setTitle("Проект 1 модуля Косач Серафима");
		stage.setMinWidth(700);
		stage.setMinHeight(600);

		stage.show();

	}


}

