
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;
import webservice.*;

/**
 * Clase controlador de la ventana de Login
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class LoginController {
	@FXML
	private Label lblStatus;
	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private TextField txtHost;
	@FXML
	private Button btnLogin;

	/**
	 * Hace el login de usuario al pulsar el botón Entrar. Si el usuario es
	 * incorrecto, muestra un mensaje de error.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void Login(ActionEvent event) throws Exception {
		// Almacenamos los parámetros introducidos por el usuario:
		UBUGrades.host = txtHost.getText();
		UBUGrades.session = new Session(txtUsername.getText(), txtPassword.getText());
		Boolean correcto = true;
		try {
			UBUGrades.session.setToken();
		} catch (Exception e) {
			correcto = false;
		}
		// Si el login es correcto
		if (correcto) {
			System.out.println(" Login Correcto");
			// Accedemos a la siguiente ventana
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./../view/Welcome.fxml"));
			// UBUGrades.stage.getScene() setCursor(Cursor.WAIT);
			UBUGrades.stage.close();
			System.out.println("Accediendo a UBUGrades...");
			UBUGrades.stage = new Stage();
			// TODO esta línea no estaba antes
			MoodleUserWS.setMoodleUser(UBUGrades.session.getToken(), UBUGrades.session.getEmail(),
					UBUGrades.user = new MoodleUser());
			MoodleUserWS.setCourses(UBUGrades.session.getToken(), UBUGrades.user);
			Parent root = loader.load();
			// root.setCursor(Cursor.WAIT);
			Scene scene = new Scene(root);
			UBUGrades.stage.setScene(scene);
			UBUGrades.stage.getIcons().add(new Image("./img/logo_min.png"));
			UBUGrades.stage.setTitle("UBUGrades");
			UBUGrades.stage.show();
			lblStatus.setText("");
		} else {
			lblStatus.setText(" Usuario incorrecto");
			System.out.println("Login Incorrecto");
			txtUsername.setText("");
			txtPassword.setText("");
		}
	}

	/**
	 * Borra los parámetros introducidos de los campos
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void Clear(ActionEvent event) throws Exception {
		txtUsername.setText("");
		txtPassword.setText("");
		txtHost.setText("");
	}
}
