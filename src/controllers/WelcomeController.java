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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;

public class WelcomeController implements Initializable {
	@FXML
	public Label lblUser;
	@FXML
	public ListView<String> listCourses;
	ObservableList<String> list;

	/**
	 * Función initialize. Muestra la lista de cursos del usuario introducido.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			UBUGrades.user = new MoodleUser(UBUGrades.session.getToken(), UBUGrades.session.getEmail());
			lblUser.setText(UBUGrades.user.getFullName());
			ArrayList<Course> courses = (ArrayList<Course>) UBUGrades.user.getCourses();
			ArrayList<String> nameCourses = new ArrayList<String>();
			for (int i = 0; i < courses.size(); i++) {
				nameCourses.add(courses.get(i).getFullName());
			}
			list = FXCollections.observableArrayList(nameCourses);
			System.out.println("-- Mostrando Cursos");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listCourses.setItems(list);
		/*
		 * listCourses.getSelectionModel().selectedItemProperty().addListener(
		 * new ChangeListener<String>() {
		 * 
		 * @Override public void changed(ObservableValue<? extends String>
		 * observable, String oldValue, String newValue) { // Your action here
		 * System.out.println("Selected item: " + newValue); =newValue;
		 * 
		 * } });
		 */
	}

	/**
	 * Función para el botón entrar, accede a la siguiente ventana
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void EnterCourse(ActionEvent event) throws Exception {
		String selectedCourse = listCourses.getSelectionModel().getSelectedItem();
		UBUGrades.session.setCourse(getCourseByString(selectedCourse));
		System.out.println(" Curso seleccionado: " + UBUGrades.session.getCourse().getFullName());
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("./../view/Main.fxml"));
		UBUGrades.stage = new Stage();
		Parent root = loader.load();
		Scene scene = new Scene(root);
		// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		UBUGrades.stage.setScene(scene);
		UBUGrades.stage.getIcons().add(new Image("./img/logo_min.png"));
		UBUGrades.stage.show();
		// lblStatus.setText("");
		System.out.println("-- Entrando curso");
	}

	/**
	 * Función que devuelve el id de un curso a partir de su nombre
	 * 
	 * @param courseName
	 * @return
	 */
	public static Course getCourseByString(String courseName) {
		Course course = null;

		ArrayList<Course> courses = (ArrayList<Course>) UBUGrades.user.getCourses();
		System.out.println(" Nº de cursos: " + courses.size());
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).getFullName().equals(courseName)) {
				course = courses.get(i);
			}
		}

		return course;
	}
}
