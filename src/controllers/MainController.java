package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import model.EnrolledUser;
import model.GradeReportConfigurationLine;
import model.Group;
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

	@FXML
	public ListView<String> listParticipants;
	ObservableList<String> list;

	@FXML
	public MenuButton slcRole;
	MenuItem[] roleMenuItems;

	@FXML
	public MenuButton slcGroup;
	MenuItem[] groupMenuItems;
	String filterRole = "Todos";
	String filterGroup = "Todos";
	String patternParticipants = "";
	@FXML
	public TextField tfdParticipants;
	// @FXML
	// public TextField tfdItems;

	@FXML
	public TreeView<String> tvwGradeReport;
	ArrayList<TreeItem> gradeReportList;

	/**
	 * Función initialize. Muestra los usuarios matriculados en el curso.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			// Establecemos la lista de todos participantes
			ArrayList<EnrolledUser> users = (ArrayList<EnrolledUser>) UBUGrades.session.getCourse().getEnrolledUsers();
			// Cargamos una lista con los nombres de los participantes
			ArrayList<String> nameUsers = new ArrayList<String>();

			//////////////////////////////////////////////////////////////////////////
			// Manejo de roles (MenuButton Rol):
			EventHandler<ActionEvent> actionRole = selectRole();
			// Cargamos una lista con los nombres de los roles
			ArrayList<String> rolesList = UBUGrades.session.getCourse().getRoles();
			// Convertimos la lista a una lista de MenuItems para el MenuButton
			ArrayList<MenuItem> rolesItemsList = new ArrayList<MenuItem>();
			// En principio se van a mostrar todos los participantes con
			// cualquier rol
			MenuItem mi = (new MenuItem("Todos"));
			// Añadimos el manejador de eventos al primer MenuItem
			mi.setOnAction(actionRole);
			rolesItemsList.add(mi);

			for (int i = 0; i < rolesList.size(); i++) {
				String rol = rolesList.get(i);
				mi = (new MenuItem(rol));
				mi.setOnAction(actionRole);
				// Añadimos el manejador de eventos a cada MenuItem
				rolesItemsList.add(mi);
			}

			// Asignamos la lista de MenuItems al MenuButton "Rol"
			slcRole.getItems().addAll(rolesItemsList);
			slcRole.setText("Todos");

			//////////////////////////////////////////////////////////////////////////
			// Manejo de grupos (MenuButton Grupo):
			EventHandler<ActionEvent> actionGroup = selectGroup();
			// Cargamos una lista de los nombres de los grupos
			ArrayList<String> groupsList = UBUGrades.session.getCourse().getGroups();
			// Convertimos la lista a una lista de MenuItems para el MenuButton
			ArrayList<MenuItem> groupsItemsList = new ArrayList<MenuItem>();
			// En principio se van a mostrar todos los participantes en
			// cualquier grupo
			mi = (new MenuItem("Todos"));
			// Añadimos el manejador de eventos al primer MenuItem
			mi.setOnAction(actionGroup);
			groupsItemsList.add(mi);

			for (int i = 0; i < groupsList.size(); i++) {
				String group = groupsList.get(i);
				mi = (new MenuItem(group));
				// Añadimos el manejador de eventos a cada MenuItem
				mi.setOnAction(actionGroup);
				groupsItemsList.add(mi);
			}
			// Asignamos la lista de MenuItems al MenuButton "Grupo"
			slcGroup.getItems().addAll(groupsItemsList);
			slcGroup.setText("Todos");

			////////////////////////////////////////////////////////
			// Mostramos todos los participantes
			for (int j = 0; j < users.size(); j++) {
				nameUsers.add(users.get(j).getFullName());
			}
			list = FXCollections.observableArrayList(nameUsers);
			System.out.println("-- Mostrando Participantes");

			// Inicializamos el listener del textField
			tfdParticipants.setOnAction(inputParticipant());

			////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////
			//Mostramos la estructura en arbol del calificador
			ArrayList<GradeReportConfigurationLine> grcl = (ArrayList<GradeReportConfigurationLine>) UBUGrades.session.getCourse().gradeReportConfigurationLines;
			//Establecemos la raiz del Treeview
			TreeItem<String> root = new TreeItem<String>(grcl.get(0).getName());
			//Llamamos recursivamente para llenar el Treeview
			for(int k = 0; k<grcl.get(0).getChildren().size();k++){
				TreeItem<String> item = new TreeItem<String>(grcl.get(0).getChildren().get(k).getName());
				root.getChildren().add(item);
				root.setExpanded(true);
				setTreeview(item,grcl.get(0).getChildren().get(k));
			}
			//Establecemos la raiz del treeview
			tvwGradeReport.setRoot(root);
			tvwGradeReport.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			//gradeReportList.add(grcl.get(grcl.size()).getName());
			//tvwGradeReport = new TreeView<String>();
			/*for(int k = 0;k<grcl.size();k++){
				TreeItem<String> item= new TreeItem<String>(grcl.get(k).getName());
				tvwGradeReport.setR
			}*/
			//tvwGradeReport.getTree
			// tvwGradeReport.set
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Activamos la selección múltiple en la lista de participantes
		listParticipants.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// Asignamos el manejador de eventos de la lista
		// Al clickar en la lista, recalculamos el número de elementos
		// seleccionados
		listParticipants.setOnMouseClicked(new EventHandler<Event>() {
			// TODO
			// Manejador que llama a la función de mostrar gráfico de los
			// elementos selecionados
			@Override
			public void handle(Event event) {
				// IMPLEMENTAR
				ObservableList<String> selectedItems = listParticipants.getSelectionModel().getSelectedItems();
				for (String s : selectedItems) {
					System.out.println("selected item " + s);
				}
				System.out.println();
			}
		});

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
	 * Manejador de eventos para el botón de filtro por roles. Devuelve un
	 * manejador de eventos para cada item.
	 * 
	 * @return
	 */
	private EventHandler<ActionEvent> selectRole() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un MenuItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				// Obtenemos el item que se ha seleccionado
				MenuItem mItem = (MenuItem) event.getSource();
				// Obtenemos el valor (rol) para filtrar la lista de
				// participantes
				filterRole = mItem.getText();
				filterParticipants();
				slcRole.setText(filterRole);
			}
		};
	}

	/**
	 * Manejador de eventos para el botón de filtro por grupos. Devuelve un
	 * manejador de eventos para cada item.
	 * 
	 * @return
	 */
	private EventHandler<ActionEvent> selectGroup() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un MenuItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				// Obtenemos el item que se ha seleccionado
				MenuItem mItem = (MenuItem) event.getSource();
				// Obtenemos el valor (rol) para filtrar la lista de
				// participantes
				filterGroup = mItem.getText();
				filterParticipants();
				slcGroup.setText(filterGroup);
			}
		};
	}

	/**
	 * Manejador de eventos para el textField de filtro de participantes.
	 * 
	 * @return
	 */
	private EventHandler<ActionEvent> inputParticipant() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un MenuItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				patternParticipants = tfdParticipants.getText();
				filterParticipants();
			}
		};
	}

	/**
	 * Función para filtra los participantes según el rol y el grupo
	 * seleccionado en los MenuButtons.
	 */
	public void filterParticipants() {

		try {
			boolean roleYes;
			boolean groupYes;
			boolean patternYes;
			ArrayList<EnrolledUser> users = (ArrayList<EnrolledUser>) UBUGrades.session.getCourse().getEnrolledUsers();
			// Cargamos la lista de los roles
			ArrayList<String> nameUsers = new ArrayList<String>();
			// Obtenemos los participantes que tienen el rol elegido
			for (int i = 0; i < users.size(); i++) {
				// Filtrado por rol:
				roleYes = false;
				ArrayList<Role> roles = users.get(i).getRoles();
				// Si no tiene rol
				if (roles.size() == 0 && filterRole.equals("Todos")) {
					roleYes = true;
				} else {
					for (int j = 0; j < roles.size(); j++) {
						// Comprobamos si el usuario pasa el filtro de "rol"
						if (roles.get(j).getName().equals(filterRole) || filterRole.equals("Todos")) {
							roleYes = true;
						}
					}
				}
				// Filtrado por grupo:
				groupYes = false;
				ArrayList<Group> groups = users.get(i).getGroups();
				if (groups.size() == 0 && filterGroup.equals("Todos")) {
					groupYes = true;
				} else {
					for (int k = 0; k < groups.size(); k++) {
						// Comprobamos si el usuario pasa el filtro de "grupo"
						if (groups.get(k).getName().equals(filterGroup) || filterGroup.equals("Todos")) {
							groupYes = true;
						}
					}
				}
				// Filtrado por patrón:
				patternYes = false;
				if (patternParticipants.equals("")) {
					patternYes = true;
				} else {
					Pattern pattern = Pattern.compile(patternParticipants);
					Matcher match = pattern.matcher(users.get(i).getFullName());
					if (match.find()) {
						patternYes = true;
					}
				}

				// Si el usuario se corresponde con los filtros
				if (groupYes && roleYes && patternYes)
					nameUsers.add(users.get(i).getFullName());
			}
			list = FXCollections.observableArrayList(nameUsers);
			System.out.println("-- Mostrando Participantes");
			for (String nameUser : nameUsers) {
				System.out.println("      " + nameUser);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listParticipants.setItems(list);
	}
	/**
	 * Función que rellena recursivamente el arbol de GradeReportConfigurationLines
	 */
	public void setTreeview(TreeItem<String> parent,GradeReportConfigurationLine line){
		
		/*Obtiene los hijos de la linea pasada por parametro
		Los transforma en treeitems y los establece como hijos del
		elemento treeItem equivalente de line*/
		for(int j = 0; j<line.getChildren().size(); j++){
			TreeItem<String> item = new TreeItem<String>(line.getChildren().get(j).getName());
			parent.getChildren().add(item);
			parent.setExpanded(true);
			setTreeview(item, line.getChildren().get(j));
		}	
	}
	
	/**
	 * Función para el botón "Salir". Cierra la aplicación.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void CloseApplication(ActionEvent actionEvent) throws Exception {
		System.out.println("Cerrando aplicación");
		UBUGrades.stage.close();
	}
}
