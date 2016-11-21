package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

public class LoginController {
	@FXML
	private Label lblStatus;

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private TextField txtHost;

	public void Login(ActionEvent event) throws Exception {
		UBUGrades.host = txtHost.getText();
		UBUGrades.session = new Session(txtUsername.getText(), txtPassword.getText());
		Boolean correcto = true;
		try {
			UBUGrades.session.setToken();
		} catch (Exception e) {
			correcto = false;
		}
		if (correcto) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./Welcome.fxml"));
			UBUGrades.stage =new Stage();
			Parent root = loader.load();
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			UBUGrades.stage.setScene(scene);
			UBUGrades.stage.show();
			lblStatus.setText("");
			System.out.println(" Login Correcto");
		} else {
			lblStatus.setText(" Usuario incorrecto");
			System.out.println("Login Incorrecto");
			txtUsername.setText("");
			txtPassword.setText("");
		}
	}
	public void Clear(ActionEvent event) throws Exception {
		txtUsername.setText("");
		txtPassword.setText("");
	}

}
