
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;

/**
 * Clase para controlar la ventana de Login
 * 
 * @author Claudia Mart�nez Herrero
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
	 * Funci�n para hacer el login de usuario con el bot�n Entrar. Si el usuario
	 * es incorrecto, muestra un mensaje de error.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void Login(ActionEvent event) throws Exception {
		// Almacenamos los par�metros introducidos por el usuario:
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
			//UBUGrades.stage.getScene() setCursor(Cursor.WAIT);
			UBUGrades.stage.close();
			System.out.println("Accediendo a UBUGrades...");
			UBUGrades.stage = new Stage();
			Parent root = loader.load();
			//root.setCursor(Cursor.WAIT);
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
	 * Funci�n para borrar los par�metros introducidos
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void Clear(ActionEvent event) throws Exception {
		txtUsername.setText("");
		txtPassword.setText("");
	}
}
