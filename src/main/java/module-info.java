module ru.pr1nkos.cryptographergui {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;

	opens ru.pr1nkos.cryptographergui to javafx.fxml;
	exports ru.pr1nkos.cryptographergui;
}