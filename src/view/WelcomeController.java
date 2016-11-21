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
import model.*;

public class WelcomeController implements Initializable {
	@FXML
	public Label lblUser;
	@FXML
	public ListView<String> listCourses;
	ObservableList<String> list;

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
			System.out.println(" Mostrando Cursos");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listCourses.setItems(list);
	}
}
