package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;

/**
 * Clase controlador de la pantalla de bienvenida en la que se muestran los
 * cursos del usuario logueado.
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class WelcomeController implements Initializable {
	@FXML
	private Label lblUser;
	@FXML
	private ListView<String> listCourses;
	private ObservableList<String> list;
	@FXML
	private Label lblNoSelect;

	/**
	 * Función initialize. Muestra la lista de cursos del usuario introducido.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			lblUser.setText(UBUGrades.user.getFullName());
			System.out.println("RMS: Cargando cursos");
			ArrayList<String> nameCourses = new ArrayList<String>();
			for (int i = 0; i < UBUGrades.user.getCourses().size(); i++) {
				nameCourses.add(UBUGrades.user.getCourses().get(i).getFullName());
			}
			list = FXCollections.observableArrayList(nameCourses);
			System.out.println("-- Mostrando Cursos");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listCourses.setItems(list);
	}

	/**
	 * Botón entrar, accede a la siguiente ventana
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void enterCourse(ActionEvent event) throws Exception {
		try {
			UBUGrades.init.getScene().setCursor(Cursor.WAIT);

			// Guardamos en una variable el curso seleccionado por el usuario
			String selectedCourse = listCourses.getSelectionModel().getSelectedItem();
			UBUGrades.session.setActualCourse(Course.getCourseByString(selectedCourse));
			System.out.println(" Curso seleccionado: " + UBUGrades.session.getActualCourse().getFullName());

			// Accedemos a la siguiente ventana:
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./../view/Main.fxml"));
			UBUGrades.stage.close();
			UBUGrades.stage = new Stage();
			Parent root = loader.load();
			Scene scene = new Scene(root);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			UBUGrades.stage.setScene(scene);
			UBUGrades.stage.getIcons().add(new Image("./img/logo_min.png"));
			UBUGrades.stage.setTitle("UBUGrades");
			UBUGrades.stage.setResizable(true);
			UBUGrades.stage.show();
			UBUGrades.init.getScene().setCursor(Cursor.DEFAULT);
			lblNoSelect.setText("");
			//System.out.println("-- Entrando al curso");
		} catch (Exception e) {
			lblNoSelect.setText("Debe seleccionar un curso");
			System.out.println("Debe seleccionar un curso");
		}

	}
}
