/**
 * Home controler, Home should be the main page shown sfter login, 
 * containing the various bounderies for the possible use cases
 */
package logic.workinethor.view;

import logic.bean.ProjectBean;
import logic.database.ProjectDAO;
import logic.model.Project;
import logic.model.Session;
import logic.workinethor.Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.util.Callback;
import javafx.event.EventHandler;



public class HomePageView {
	
	private static final String DUTY_STRING = "Duty1"; 
	private static final String ARIAL_STRING = "Arial";
	private static final String FILL_STRING = "-fx-text-fill: #cfd1dd";
	
	private BorderPane mainLayout = Main.getMainLayout();
	
	@FXML
	private BorderPane background;
	
	private Session activeSession = Session.getSession();
	
	@FXML
	private void initialize() {
		
		Logger logger = Logger.getLogger(HomePageView.class.getName());
		
		ProjectDAO projectDAO = new ProjectDAO();
		AnchorPane items = new AnchorPane();
			
		Popup popup = new Popup();
		popup.setAutoHide(true);
		ListView<String> popupListView = new ListView<>();
		popupListView.getItems().add(DUTY_STRING);
		popupListView.getItems().add(DUTY_STRING);
		popupListView.getItems().add(DUTY_STRING);
		popupListView.getItems().add(DUTY_STRING);
		popupListView.getItems().add(DUTY_STRING);
		
		popup.getContent().add(popupListView);
		
		popupListView.setStyle("-fx-background-color:yellow");
		
		Label loggedUserLabel = new Label();
		loggedUserLabel.setText("Username: " + activeSession.getLoggedUser().getUsername());
		loggedUserLabel.setFont(new Font(ARIAL_STRING, 24));
		loggedUserLabel.setTranslateY(5);
		loggedUserLabel.setStyle(FILL_STRING);
		
		Label projectLabel = new Label();
		projectLabel.setText("Your Projects:");
		projectLabel.setFont(new Font(ARIAL_STRING, 24));
		projectLabel.setTranslateY(50);
		projectLabel.setStyle(FILL_STRING);
		
		ObservableList<String> userProjects = projectDAO.getAllUserProjects(Session.getSession().getLoggedUser());
		ListView<String> allProjectsListView = new ListView<>(userProjects);
		allProjectsListView.setTranslateY(100);
		allProjectsListView.setTranslateX(1);
		allProjectsListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				
				ListCell<String> cell = new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            setFont(new Font(ARIAL_STRING, 18));
                        }
					}
				};
				cell.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if(event.isPrimaryButtonDown() && !cell.isEmpty()) {
							Project project = new Project();
							ProjectBean bean = new ProjectBean();
							bean.setProjectName(cell.getText());
							project.setProjectName(cell.getText());
							Session.getSession().setCurrentBrowsingProject(bean);
							Session.getSession().setProject(project);

							BorderPane projectBorderPane = null;
							try {
								projectBorderPane = FXMLLoader.load(HomePageView.class.getResource("Project.fxml"));
							} catch (IOException e) {
								logger.log(Level.SEVERE, "failed to load fxml");
							}
							
							mainLayout.setCenter(projectBorderPane);
							
						}
					}
				});
				
				return cell;
			}
		});
		
		allProjectsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(oldValue != newValue) {
					logger.log(Level.INFO, newValue);
				}
			}
		});
		
		items.getChildren().add(loggedUserLabel);
		items.getChildren().add(projectLabel);
		items.getChildren().add(allProjectsListView);
		
		
		background.setCenter(items);
		background.setStyle("-fx-background-color: #2d3447");
	}
}
