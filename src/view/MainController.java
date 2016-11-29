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
 * @author Claudia Mart�nez Herrero
 *
 */
public class MainController implements Initializable {
	@FXML // n� participantes
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
	 * Funci�n initialize. Muestra los usuarios matriculados en el curso.
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
		// Mostramos n� participantes a la izquierda de la ventana
		lblCountParticipants
				.setText("Participantes: " + String.valueOf(UBUGrades.session.getCourse().getEnrolledUsersCount()));

		// Mostramos curso actual en la parte superior de la ventana
		lblActualCourse.setText("Curso actual: " + UBUGrades.session.getCourse().getFullName());

		// Mostramos Usuario logeado en la parte superior de la ventana
		lblActualUser.setText("Usuario: " + UBUGrades.user.getFullName());
	}

	/**
	 * Funci�n para el bot�n Salir, que cierra la aplicaci�n.
	 * 
	 * @param event
	 * @throws Exception
	 */
	/*
	 * @FXML private void closeButtonAction(ActionEvent event) throws Exception
	 * { System.out.println("Presionado bot�n salir"); Stage stage = (Stage)
	 * btnExit.getScene().getWindow(); stage.close(); }
	 */
}
