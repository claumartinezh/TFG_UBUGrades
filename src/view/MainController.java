package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.EnrolledUser;

/**
 * Clase para controlar la ventana principal
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class MainController implements Initializable {
	@FXML // nº participantes
	public Label lblCountParticipants;
	@FXML // curso actual
	public Label lblActualCourse;
	@FXML // usuario actual
	public Label lblActualUser;

	/*
	 * @FXML private javafx.scene.control.Button btnExit;
	 */

	@FXML
	public ListView<String> listParticipants;
	ObservableList<String> list;

	/**
	 * Función initialize. Muestra los usuarios matriculados en el curso.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			// UBUGrades.session.getCourse().setEnrolledUsers(UBUGrades.session.getToken(),
			// UBUGrades.session.getCourse().getId());
			ArrayList<EnrolledUser> users = (ArrayList<EnrolledUser>) UBUGrades.session.getCourse().getEnrolledUsers();
			// UBUGrades.user = new MoodleUser(UBUGrades.session.getToken(),
			// UBUGrades.session.getEmail());
			ArrayList<String> nameUsers = new ArrayList<String>();
			for (int i = 0; i < users.size(); i++) {
				nameUsers.add(users.get(i).getFullName());
			}
			list = FXCollections.observableArrayList(nameUsers);
			System.out.println("-- Mostrando Participantes");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listParticipants.setItems(list);

		new String();
		// Mostramos nº participantes a la izquierda de la ventana
		lblCountParticipants
				.setText("Participantes: " + String.valueOf(UBUGrades.session.getCourse().getEnrolledUsersCount()));

		// Mostramos curso actual en la parte superior de la ventana
		lblActualCourse.setText("Curso actual: " + UBUGrades.session.getCourse().getFullName());

		// Mostramos Usuario logeado en la parte superior de la ventana
		lblActualUser.setText("Usuario: " + UBUGrades.user.getFullName());
	}

	/**
	 * Función para el botón Salir, que cierra la aplicación.
	 * 
	 * @param event
	 * @throws Exception
	 */
	/*
	 * @FXML private void closeButtonAction(ActionEvent event) throws Exception
	 * { System.out.println("Presionado botón salir"); Stage stage = (Stage)
	 * btnExit.getScene().getWindow(); stage.close(); }
	 */
}
