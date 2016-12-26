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
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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

	@FXML // curso actual
	public Label lblActualCourse;
	@FXML // usuario actual
	public Label lblActualUser;
	@FXML // host actual
	public Label lblActualHost;

	// En cuanto a participantes del curso:
	@FXML // nº participantes
	public Label lblCountParticipants;
	@FXML // lista de nombres de participantes
	public ListView<String> listParticipants;
	ObservableList<String> list;

	@FXML // botón filtro por rol
	public MenuButton slcRole;
	MenuItem[] roleMenuItems;

	@FXML // botón filtro por grupo
	public MenuButton slcGroup;
	MenuItem[] groupMenuItems;
	String filterRole = "Todos";
	String filterGroup = "Todos";
	String patternParticipants = "";
	@FXML // entrada de filtro de usuarios por patrón
	public TextField tfdParticipants;

	// En cuanto a los items de calificación
	@FXML
	public TreeView<String> tvwGradeReport;
	ArrayList<TreeItem> gradeReportList;
	String patternCalifications = "";

	@FXML // entrada de filtro de actividades por patrón
	public TextField tfdItems;
	@FXML // botón filtro por tipo de actividad
	public MenuButton slcType;
	MenuItem[] typeMenuItems;
	String filterType = "Todos";

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
			// Añadimos todos los participantes a la lista de visualización
			for (int j = 0; j < users.size(); j++) {
				nameUsers.add(users.get(j).getFullName());
			}
			list = FXCollections.observableArrayList(nameUsers);

			//////////////////////////////////////////////////////////////////////////
			// Manejo de actividades (TreeView GRCL):
			EventHandler<ActionEvent> actionActivity = selectNameActivity();
			// Cargamos una lista de los nombres de los grupos
			ArrayList<String> nameActivityList = UBUGrades.session.getCourse().getTypeActivities();
			// Convertimos la lista a una lista de MenuItems para el MenuButton
			ArrayList<MenuItem> nameActivityItemsList = new ArrayList<MenuItem>();
			// En principio se van a mostrar todos los participantes en
			// cualquier grupo
			mi = (new MenuItem("Todos"));
			// Añadimos el manejador de eventos al primer MenuItem
			mi.setOnAction(actionActivity);
			nameActivityItemsList.add(mi);

			for (int i = 0; i < nameActivityList.size(); i++) {
				String nameActivity = nameActivityList.get(i);
				mi = (new MenuItem(nameActivity));
				// Añadimos el manejador de eventos a cada MenuItem
				mi.setOnAction(actionActivity);
				nameActivityItemsList.add(mi);
			}
			// Asignamos la lista de MenuItems al MenuButton "Grupo"
			slcType.getItems().addAll(nameActivityItemsList);
			slcType.setText("Todos");

			// Inicializamos el listener del textField de participantes
			tfdParticipants.setOnAction(inputParticipant());

			////////////////////////////////////////////////////////

			// Inicializamos el listener del textField del calificador
			tfdItems.setOnAction(inputCalification());

			// gradeReportList.add(grcl.get(grcl.size()).getName());
			// tvwGradeReport = new TreeView<String>();
			/*
			 * for(int k = 0;k<grcl.size();k++){ TreeItem<String> item= new
			 * TreeItem<String>(grcl.get(k).getName()); tvwGradeReport.setR }
			 */
			// tvwGradeReport.getTree
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

		/// Mostramos la lista de participantes
		listParticipants.setItems(list);
		System.out.println("-- Mostrando Participantes");

		////////////////////////////////////////////////////////
		// Mostramos la estructura en arbol del calificador
		ArrayList<GradeReportConfigurationLine> grcl = (ArrayList<GradeReportConfigurationLine>) UBUGrades.session
				.getCourse().getGRCL();
		// Establecemos la raiz del Treeview
		TreeItem<String> root = new TreeItem<String>(grcl.get(0).getName());
		// Llamamos recursivamente para llenar el Treeview
		for (int k = 0; k < grcl.get(0).getChildren().size(); k++) {
			TreeItem<String> item = new TreeItem<String>(grcl.get(0).getChildren().get(k).getName());
			root.getChildren().add(item);
			root.setExpanded(true);
			setTreeview(item, grcl.get(0).getChildren().get(k));
		}
		// Establecemos la raiz del treeview
		tvwGradeReport.setRoot(root);
		tvwGradeReport.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// Asignamos el manejador de eventos de la lista
		// Al clickar en la lista, recalculamos el número de elementos
		// seleccionados
		tvwGradeReport.setOnMouseClicked(new EventHandler<Event>() {
			// TODO
			// Manejador que llama a la función de mostrar gráfico de los
			// elementos selecionados
			@Override
			public void handle(Event event) {
				// IMPLEMENTAR
				ObservableList<TreeItem<String>> selectedItems = tvwGradeReport.getSelectionModel().getSelectedItems();
				for (TreeItem<String> s : selectedItems) {
					System.out.println("selected item " + s.getValue());
				}
				System.out.println();
			}
		});
		System.out.println("-- Mostrando Calificador");

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
	 * @return manejador de eventos de roles
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
				System.out.println("-> Filtrando participantes por rol: " + filterRole);
				filterParticipants();
				slcRole.setText(filterRole);
			}
		};
	}

	/**
	 * Manejador de eventos para el botón de filtro por grupos. Devuelve un
	 * manejador de eventos para cada item.
	 * 
	 * @return manejador de eventos de grupos
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
				System.out.println("-> Filtrando participantes por grupo: " + filterGroup);
				filterParticipants();
				slcGroup.setText(filterGroup);
			}
		};
	}

	/**
	 * Manejador de eventos para el textField de filtro de participantes.
	 * 
	 * @return manejador de eventos para el patrón de participantes
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
				System.out.println("-> Filtrando participantes por nombre: " + patternParticipants);
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
			/*
			 * System.out.println("-- Mostrando Participantes"); for (String
			 * nameUser : nameUsers) { System.out.println("      " + nameUser);
			 * }
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listParticipants.setItems(list);
	}

	/**
	 * Función que rellena recursivamente el árbol de
	 * GradeReportConfigurationLines
	 * 
	 * @param parent
	 * @param line
	 */
	public void setTreeview(TreeItem<String> parent, GradeReportConfigurationLine line) {

		/*
		 * Obtiene los hijos de la linea pasada por parametro Los transforma en
		 * treeitems y los establece como hijos del elemento treeItem
		 * equivalente de line
		 */
		for (int j = 0; j < line.getChildren().size(); j++) {
			TreeItem<String> item = new TreeItem<String>(line.getChildren().get(j).getName());
			parent.getChildren().add(item);
			parent.setExpanded(true);
			setTreeview(item, line.getChildren().get(j));
		}
	}

	/**
	 * Manejador de eventos para el botón de filtro por grupos. Devuelve un
	 * manejador de eventos para cada item.
	 * 
	 * @return manejador de eventos para el botón de filtro por grupos
	 */
	private EventHandler<ActionEvent> selectNameActivity() {
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
				filterType = mItem.getText();
				System.out.println("-> Filtrando calificador por tipo: " + filterType);
				filterCalifications();
				slcType.setText(filterType);
			}
		};
	}

	/**
	 * Manejador de eventos para el textField de filtro de calificaciones.
	 * 
	 * @return manejador de eventos para el patrón de filtro de calificaciones
	 */
	public EventHandler<ActionEvent> inputCalification() {
		return new EventHandler<ActionEvent>() {
			/**
			 * Recibe un evento (relacionado con un TreeItem) y responde en
			 * consecuencia. El usuario elige un menuItem y filtra la lista de
			 * participantes
			 */
			public void handle(ActionEvent event) {
				patternCalifications = tfdItems.getText();
				System.out.println("-> Filtrando calificador por nombre: " + patternCalifications);
				filterCalifications();
			}
		};
	}

	/**
	 * Función que filtra la lista de actividades del calificador según el tipo
	 * y el patrón introducido.
	 */
	public void filterCalifications() {
		try {
			ArrayList<GradeReportConfigurationLine> grcl = (ArrayList<GradeReportConfigurationLine>) UBUGrades.session
					.getCourse().getGRCL();
			// Establecemos la raiz del Treeview
			TreeItem<String> root = new TreeItem<String>(grcl.get(0).getName());
			// Llamamos recursivamente para llenar el Treeview
			if (filterType.equals("Todos") && patternCalifications.equals("")) {
				// Sin filtro y sin patrón
				for (int k = 0; k < grcl.get(0).getChildren().size(); k++) {
					TreeItem<String> item = new TreeItem<String>(grcl.get(0).getChildren().get(k).getName());
					root.getChildren().add(item);
					root.setExpanded(true);
					setTreeview(item, grcl.get(0).getChildren().get(k));
				}
			} else { // Con filtro
				for (int k = 0; k < grcl.get(0).getChildren().size(); k++) {
					TreeItem<String> item = new TreeItem<String>(grcl.get(0).getChildren().get(k).getName());
					boolean activityYes = false;
					if (grcl.get(0).getChildren().get(k).getNameType().equals(filterType)
							|| filterType.equals("Todos")) {
						activityYes = true;
					}
					Pattern pattern = Pattern.compile(patternCalifications);
					// System.out.println(grcl.get(0).getChildren().get(k).getName());
					Matcher match = pattern.matcher(grcl.get(0).getChildren().get(k).getName());
					boolean patternYes = false;
					if (patternCalifications.equals("") || match.find()) {
						patternYes = true;
					}
					if (activityYes && patternYes) {
						root.getChildren().add(item);
					}
					root.setExpanded(true);
					setTreeviewFilter(root, item, grcl.get(0).getChildren().get(k));
				}

			}
			// Establecemos la raiz del treeview
			tvwGradeReport.setRoot(root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listParticipants.setItems(list);
	}

	/**
	 * Función que crea un árbol filtrado en el que los hijos del root(raíz) son
	 * elementos de cualquier nivel que cumplen el filtro
	 * 
	 * @param root
	 * @param parent
	 * @param line
	 */
	public void setTreeviewFilter(TreeItem<String> root, TreeItem<String> parent, GradeReportConfigurationLine line) {

		/*
		 * Obtiene los hijos de la linea pasada por parametro Los transforma en
		 * treeitems y los establece como hijos del elemento treeItem
		 * equivalente de line
		 */
		for (int j = 0; j < line.getChildren().size(); j++) {
			TreeItem<String> item = new TreeItem<String>(line.getChildren().get(j).getName());
			boolean activityYes = false;
			if (line.getChildren().get(j).getNameType().equals(filterType) || filterType.equals("Todos")) {
				activityYes = true;
			}
			Pattern pattern = Pattern.compile(patternCalifications);
			Matcher match = pattern.matcher(line.getChildren().get(j).getName());
			boolean patternYes = false;
			if (patternCalifications.equals("") || match.find()) {
				patternYes = true;
			}
			if (activityYes && patternYes) {
				root.getChildren().add(item);
			}

			parent.setExpanded(true);
			setTreeviewFilter(root, item, line.getChildren().get(j));
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
