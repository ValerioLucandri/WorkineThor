package workinethor.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import workinethor.Main;

public class MainHomeButtonsController {
	private Main main = Main.getIstance();
	private BorderPane mainLayout = null;

	@FXML
	private void initialize() {
		try {
			mainLayout = FXMLLoader.load(Main.class.getResource("view/MainCreateProjectItems.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void goCreate() throws IOException {
		main.setLayoutCenter(mainLayout);
	}

}
