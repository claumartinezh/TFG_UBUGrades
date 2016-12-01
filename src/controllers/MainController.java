package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;
import model.EnrolledUser;
import model.Role;

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
	@FXML
	public Label lblActualHost;

	/*
	 * @FXML private javafx.scene.control.Button btnExit;
	 */

	@FXML
	public ListView<String> listParticipants;
	ObservableList<String> list;

	@FXML
	public MenuButton slcRole;
	MenuItem[] roleMenuItems;

	/**
	 * Función initialize. Muestra los usuarios matriculados en el curso.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ArrayList<EnrolledUser> users = (ArrayList<EnrolledUser>) UBUGrades.session.getCourse().getEnrolledUsers();
			// Cargamos la lista de los usuarios
			ArrayList<String> nameUsers = new ArrayList<String>();
			
			// cargamos botón de roles {
			ArrayList<String> rolesList = UBUGrades.session.getCourse().getRoles();

			// guardamos el nombre de los roles que existen en un array:
			ArrayList<MenuItem> roleNames = new ArrayList<MenuItem>();
			for (int i = 0; i < rolesList.size(); i++) {
				String rol = rolesList.get(i);
				roleNames.add(new MenuItem(rol));
			}
			
			// almacenamos el array de nombres de roles al Botón
			roleMenuItems = new MenuItem[roleNames.size()];
			for (int i = 0; i < roleNames.size(); i++) {
				roleMenuItems[i] = roleNames.get(i);
			}
			List<MenuItem> cosa3 = new ArrayList<>();
			for (MenuItem mi : roleMenuItems) {
				cosa3.add(mi);
			}
			System.out.println(roleMenuItems.toString());

			assert slcRole != null : "Botón no enganchado en la interfaz";
			// slcRole = new MenuButton("Rol2", null, cosa2);
			// slcRole.getItems().addAll(new MenuItem("Texto1"), new
			// MenuItem("Texto2"));
			slcRole.getItems().addAll(cosa3);
			// slcRole.getItems().addAll(cosa);
			for (int j = 0; j < users.size(); j++) {
				nameUsers.add(users.get(j).getFullName());
			}
			// } carga de roles en el botón
			
			list = FXCollections.observableArrayList(nameUsers);
			System.out.println("-- Mostrando Participantes");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listParticipants.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// Mostramos la lista de participantes
		listParticipants.setItems(list);

		// Mostramos los roles posibles para filtrar
		// slcRole.getItems().addAll(roles);

		// Mostramos nº participantes a la izquierda de la ventana
		lblCountParticipants
				.setText("Participantes: " + String.valueOf(UBUGrades.session.getCourse().getEnrolledUsersCount()));

		// Mostramos curso actual en la parte superior de la ventana
		lblActualCourse.setText("Curso actual: " + UBUGrades.session.getCourse().getFullName());

		// Mostramos Usuario logeado en la parte superior de la ventana
		lblActualUser.setText("Usuario: " + UBUGrades.user.getFullName());

		// Mostramos Host actual en la parte inferior de la ventana
		lblActualHost.setText("Host: " + UBUGrades.host);

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

	public void SelectRole(ActionEvent event) throws Exception {
		String selectedRole = slcRole.getText();
		try {
			ArrayList<EnrolledUser> users = (ArrayList<EnrolledUser>) UBUGrades.session.getCourse().getEnrolledUsers();
			// Cargamos la lista de los usuarios
			ArrayList<String> rolesList = UBUGrades.session.getCourse().getRoles();
			ArrayList<String> nameUsers = new ArrayList<String>();
			slcRole = new MenuButton("-");
			rolesList.remove("-");
			for (int i = 0; i < users.size(); i++) {
				ArrayList<Role> roles = users.get(i).getRoles();
				// añadimos los nombres de usuario
				for (int j = 0; j < roles.size(); j++) {
					if (roles.get(j).getName().equals(selectedRole)) {
						nameUsers.add(users.get(i).getFullName());
					}
				}
			}
			list = FXCollections.observableArrayList(nameUsers);
			System.out.println("-- Mostrando Participantes");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
