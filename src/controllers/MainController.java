package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import model.EnrolledUser;
import model.GradeReportLine;
import model.Group;
import model.Role;
import webservice.CourseWS;

/**
 * Clase controlador de la ventana principal
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class MainController implements Initializable {

	// static final Logger logger = (Logger)
	// LoggerFactory.getLogger(MainController.class);

	@FXML
	public AnchorPane canvas;
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
	public ListView<EnrolledUser> listParticipants;
	ObservableList<EnrolledUser> enrList;

	@FXML // botón filtro por rol
	public MenuButton slcRole;
	MenuItem[] roleMenuItems;

	@FXML // botón filtro por grupo
	public MenuButton slcGroup;
	MenuItem[] groupMenuItems;

	@FXML // entrada de filtro de usuarios por patrón
	public TextField tfdParticipants;

	String filterRole = "Todos";
	String filterGroup = "Todos";
	String patternParticipants = "";

	// En cuanto a actividades del curso:
	@FXML // vista en árbol de actividades
	public TreeView<GradeReportLine> tvwGradeReport;
	ArrayList<GradeReportLine> gradeReportList;
	String patternCalifications = "";

	@FXML // entrada de filtro de actividades por patrón
	public TextField tfdItems;
	@FXML // botón filtro por tipo de actividad
	public MenuButton slcType;
	MenuItem[] typeMenuItems;
	String filterType = "Todos";

	// En cuanto al gráfico:
	@FXML
	private LineChart<String, Number> lineChart;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;

	@FXML // Media
	private CheckBox checkAverage;
	private XYChart.Series<String, Number> average;
	@FXML
	private AnchorPane Content2;

	@FXML // tabla de calificaciones
	private WebView webView;
	private WebEngine engine;

	/**
	 * Muestra los usuarios matriculados en el curso, así como las actividades
	 * de las que se compone.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			System.out.println(" Cargando curso '" + UBUGrades.session.getActualCourse().getFullName() + "'...");
			engine = webView.getEngine();
			// Establecemos los elementos de los cursos
			CourseWS.setEnrolledUsers(UBUGrades.session.getToken(), UBUGrades.session.getActualCourse());
			// Estableciendo calificador
			CourseWS.setGradeReportLines(UBUGrades.session.getToken(),
					UBUGrades.session.getActualCourse().getEnrolledUsers().get(0).getId(),
					UBUGrades.session.getActualCourse());

			// Establecemos la lista de todos participantes
			ArrayList<EnrolledUser> users = (ArrayList<EnrolledUser>) UBUGrades.session.getActualCourse()
					.getEnrolledUsers();
			// Cargamos una lista con los nombres de los participantes
			ArrayList<EnrolledUser> nameUsers = new ArrayList<EnrolledUser>();

			//////////////////////////////////////////////////////////////////////////
			// Manejo de roles (MenuButton Rol):
			EventHandler<ActionEvent> actionRole = selectRole();
			// Cargamos una lista con los nombres de los roles
			ArrayList<String> rolesList = UBUGrades.session.getActualCourse().getRoles();
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
			ArrayList<String> groupsList = UBUGrades.session.getActualCourse().getGroups();
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
				nameUsers.add(users.get(j));
			}
			enrList = FXCollections.observableArrayList(nameUsers);

			//////////////////////////////////////////////////////////////////////////
			// Manejo de actividades (TreeView<GradeReportLine>):
			EventHandler<ActionEvent> actionActivity = selectNameActivity();
			// Cargamos una lista de los nombres de los grupos
			ArrayList<String> nameActivityList = UBUGrades.session.getActualCourse().getActivities();
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

			// Asignamos la lista de grupos al MenuButton "Grupo"
			slcType.getItems().addAll(nameActivityItemsList);
			slcType.setText("Todos");

			// Inicializamos el listener del textField de participantes
			tfdParticipants.setOnAction(inputParticipant());

			// Inicializamos el listener del textField del calificador
			tfdItems.setOnAction(inputCalification());

			average = new XYChart.Series<String, Number>();
			average.setName("Media");
			/*
			 * try {
			 * UBUGrades.session.getCourse().setGradeReportConfigurationLines(
			 * UBUGrades.session.getToken(), actualUser.getId()); } catch
			 * (Exception e) { e.printStackTrace(); }
			 */
			// TODO: calcular la media de la clase
			/*
			 * for (GradeReportLine actualLine :
			 * UBUGrades.session.getCourse().getGRCL()) { float allGrade = 0;
			 * //Media de la actividad int count =1; for (EnrolledUser
			 * actualUser : UBUGrades.session.getCourse().enrolledUsers) {
			 * if(actualLine.getGrade() != Float.NaN){ float calculatedGrade =
			 * (actualLine.getGrade() / actualLine.getRangeMax()) * 10;
			 * count+=1; allGrade+=calculatedGrade; } } average.getData()
			 * .add(new XYChart.Data<String, Number>(actualLine.getName(),
			 * allGrade/count)); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Activamos la selección múltiple en la lista de participantes
		listParticipants.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// Asignamos el manejador de eventos de la lista
		// Al clickar en la lista, recalculamos el número de elementos
		// seleccionados
		listParticipants.setOnMouseClicked(new EventHandler<Event>() {
			// Manejador que llama a la función de mostrar gráfico de los
			// elementos selecionados
			@Override
			public void handle(Event event) { // HANDLE Participantes
				ObservableList<EnrolledUser> selectedParticipants = listParticipants.getSelectionModel()
						.getSelectedItems();
				ObservableList<TreeItem<GradeReportLine>> selectedGRL = tvwGradeReport.getSelectionModel()
						.getSelectedItems();
				// Por cada participante seleccionado
				// Recalculamos el gráfico
				lineChart.getData().clear();

				// Recalculamos la tabla
				String htmlTitle = "<tr><th style='background:#066db3; border: 1.0 solid grey; color:white;'> Alumno </th>";
				String content = "";
				int countA = 0;
				for (EnrolledUser actualUser : selectedParticipants) {
					// Establecemos el configurador del curso con este usuario
					String htmlRow = "<th style='color:#066db3; background: white; border: 1.0 solid grey;'>"
							+ actualUser.getFullName() + " </th>";
					try {
						CourseWS.setGradeReportLines(UBUGrades.session.getToken(), actualUser.getId(),
								UBUGrades.session.getActualCourse());
					} catch (Exception e) {
						e.printStackTrace();
						errorDeConexion();
					}

					// Mostramos el gráfico
					XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
					series.setName(actualUser.getLastName() + ", " + actualUser.getFirstName());

					int countB = 1;
					for (TreeItem<GradeReportLine> structTree : selectedGRL) {
						countA++;
						for (GradeReportLine actualLine : UBUGrades.session.getActualCourse().getGradeReportLines()) {

							// Por cada actividad que haya seleccionada,
							// lo buscamos en la estructura y obtenemos
							// los valores necesarios
							if (structTree.getValue().getId() == actualLine.getId()) {
								/*
								 * float calculatedGrade =
								 * (actualLine.getGrade() /
								 * Float.valueOf(actualLine.getRangeMax())) *
								 * 10;
								 */
								String calculatedGrade = actualLine.getGrade();
								// System.out.println(actualLine.getName()+":
								// "+actualLine.getGrade());

								// Ponemos el nombre de la actividad en la
								// cabecera en la tabla
								if (countA == countB) {
									htmlTitle += "<th style='border: 1.0 solid grey'> " + actualLine.getName()
											+ " </th>";
									countB++;
								}
								// Si es numerico lo graficamos
								if (!Float.isNaN(CourseWS.getFloat(calculatedGrade))) {
									series.getData().add(new XYChart.Data<String, Number>(actualLine.getName(),
											CourseWS.getFloat(calculatedGrade)));

									htmlRow += "<td style='border: 1.0 solid grey'> "
											+ Math.round(CourseWS.getFloat(calculatedGrade) * 100.0) / 100.0
											+ "/<b style='color:#ab263c'>" + actualLine.getRangeMax() + "</b> </td>";
								} else { // Si no solo lo mostramos en la tabla
									htmlRow += "<td style='border: 1.0 solid grey'> " + calculatedGrade + " </td>";
								}
							}
						}
					}
					htmlTitle += "</tr>";
					// Mostramos el gráfico
					lineChart.getData().add(series);
					htmlRow += "</tr>";
					content += htmlRow;
				}
				// Mostramos la tabla
				String head = "";
				try {
					engine.setUserStyleSheetLocation(getClass().getResource("../css/style.css").toString());
				} catch (Exception e) {
					head = "<style>table {margin-bottom: 3.0em; width: 100.0%;font-family:Arial, Verdana, sans-serif; text-align:center;border-radius: 1; border-collapse: collapse;}"
							+ "th{heigth:20%;font-size:80%;color: white;   background: #ab263c;   padding:1%;   border-collapse: collapse;}"
							+ "td {font-size:80%;heigth:20%;   background: white;   padding:1%; border-collapse: collapse;}</style>";
				}
				engine.loadContent("<html><head>" + head + "</head><body style='background-color:#f2f2f2'><table>"
						+ htmlTitle + content + "</table></body></html>");

				/*
				 * if(checkAverage.isSelected())
				 * lineChart.getData().add(average);
				 */
			}
		});

		/// Mostramos la lista de participantes
		listParticipants.setItems(enrList);

		////////////////////////////////////////////////////////
		// Mostramos la estructura en arbol del calificador
		ArrayList<GradeReportLine> grcl = (ArrayList<GradeReportLine>) UBUGrades.session.getActualCourse()
				.getGradeReportLines();
		// Establecemos la raiz del Treeview
		TreeItem<GradeReportLine> root = new TreeItem<GradeReportLine>(grcl.get(0));
		MainController.setIcon(root);
		// Llamamos recursivamente para llenar el Treeview
		for (int k = 0; k < grcl.get(0).getChildren().size(); k++) {
			TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(grcl.get(0).getChildren().get(k));
			MainController.setIcon(item);
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
			// Manejador que llama a la función de mostrar gráfico de los
			// elementos selecionados
			@Override
			public void handle(Event event) { // HANDLE calificaciones
				ObservableList<EnrolledUser> selectedParticipants = listParticipants.getSelectionModel()
						.getSelectedItems();
				ObservableList<TreeItem<GradeReportLine>> selectedGRL = tvwGradeReport.getSelectionModel()
						.getSelectedItems();
				lineChart.getData().clear();
				String htmlTitle = "<tr><th style='background:#066db3; border: 1.0 solid grey; color:white;'> Alumno </th>";
				String content = "";
				int countA = 0;
				for (EnrolledUser actualUser : selectedParticipants) {
					String htmlRow = "<th style='color:#066db3; background:white; border: 1.0 solid grey;'> "
							+ actualUser.getFullName() + " </th>";
					try {
						CourseWS.setGradeReportLines(UBUGrades.session.getToken(), actualUser.getId(),
								UBUGrades.session.getActualCourse());
					} catch (Exception e) {
						e.printStackTrace();
						errorDeConexion();
					}
					XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
					series.setName(actualUser.getLastName() + ", " + actualUser.getFirstName());
					int countB = 1;
					for (TreeItem<GradeReportLine> structTree : selectedGRL) {
						countA++;
						for (GradeReportLine actualLine : UBUGrades.session.getActualCourse().getGradeReportLines()) {
							try {
								if (structTree.getValue().getId() == actualLine.getId()) {
									// Al cambiar el rango a String ya no se
									// puede hacer este cálculo
									/*
									 * float calculatedGrade =
									 * (actualLine.getGrade() /
									 * Float.valueOf(actualLine.getRangeMax()))
									 * * 10;
									 */
									String calculatedGrade = actualLine.getGrade();
									// System.out.println(actualLine.getName()+":
									// "+actualLine.getGrade());
									// Ponemos el nombre de la actividad en la
									// cabecera en la tabla
									if (countA == countB) {
										htmlTitle += "<th style='border: 1.0 solid grey'> " + actualLine.getName()
												+ " </th>";
										countB++;
									}
									// Si es numerico lo graficamos
									if (!Float.isNaN(CourseWS.getFloat(calculatedGrade))) {
										series.getData().add(new XYChart.Data<String, Number>(actualLine.getName(),
												CourseWS.getFloat(calculatedGrade)));

										htmlRow += "<td style='border: 1.0 solid grey'> "
												+ Math.round(CourseWS.getFloat(calculatedGrade) * 100.0) / 100.0
												+ "/<b style='color:#ab263c'>" + actualLine.getRangeMax()
												+ "</b> </td>";
									} else { // Si no solo lo mostramos en la
												// tabla
										htmlRow += "<td style='border: 1.0 solid grey'> " + calculatedGrade + " </td>";
									}
								}
							} catch (Exception e) {
							}
						}
					}
					htmlTitle += "</tr>";
					lineChart.getData().add(series);
					htmlRow += "</tr>";
					content += htmlRow;
				}
				// Mostramos la tabla
				String head = "";
				try {
					engine.setUserStyleSheetLocation(getClass().getResource("../css/style.css").toString());
				} catch (Exception e) {
					head = "<style>table {margin-bottom: 3.0em; width: 100.0%;font-family:Arial, Verdana, sans-serif; text-align:center;border-radius: 1; border-collapse: collapse;}"
							+ "th{heigth:20%;font-size:80%;color: white;   background: #ab263c;   padding:1%;   border-collapse: collapse;}"
							+ "td {font-size:80%;heigth:20%;   background: white;   padding:1%; border-collapse: collapse;}</style>";
				}
				engine.loadContent("<html><head>" + head + "</head><body style='background-color:#f2f2f2'><table>"
						+ htmlTitle + content + "</table></body></html>");

				/*
				 * if(checkAverage.isSelected())
				 * lineChart.getData().add(average);
				 */
			}
		});

		// Mostramos nº participantes
		lblCountParticipants.setText(
				"Participantes: " + String.valueOf(UBUGrades.session.getActualCourse().getEnrolledUsersCount()));

		// Mostramos Usuario logeado
		lblActualUser.setText("Usuario: " + UBUGrades.user.getFullName());

		// Mostramos Curso actual
		lblActualCourse.setText("Curso actual: " + UBUGrades.session.getActualCourse().getFullName());

		// Mostramos Host actual
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
				// Obtenemos el rol por el que se quiere filtrar
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
				// Obtenemos el grupo por el que se quire filtrar
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
	 * Filtra los participantes según el rol, el grupo y el patrón indicados
	 */
	public void filterParticipants() {
		try {
			clearData();
			boolean roleYes;
			boolean groupYes;
			boolean patternYes;
			ArrayList<EnrolledUser> users = (ArrayList<EnrolledUser>) UBUGrades.session.getActualCourse()
					.getEnrolledUsers();
			// Cargamos la lista de los roles
			ArrayList<EnrolledUser> nameUsers = new ArrayList<EnrolledUser>();
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
					nameUsers.add(users.get(i));
			}
			enrList = FXCollections.observableArrayList(nameUsers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		listParticipants.setItems(enrList);
	}

	/**
	 * Rellena el árbol de actividades (GradeReportLines). Obtiene los hijos de
	 * la línea pasada por parámetro, los transforma en treeitems y los
	 * establece como hijos del elemento treeItem equivalente de line
	 * 
	 * @param parent
	 * @param line
	 */
	public void setTreeview(TreeItem<GradeReportLine> parent, GradeReportLine line) {
		for (int j = 0; j < line.getChildren().size(); j++) {
			TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(line.getChildren().get(j));
			MainController.setIcon(item);
			parent.getChildren().add(item);
			parent.setExpanded(true);
			setTreeview(item, line.getChildren().get(j));
		}
	}

	/**
	 * Añade un icono a cada elemento del árbol según su tipo de actividad
	 * 
	 * @param item
	 */
	public static void setIcon(TreeItem<GradeReportLine> item) {
		// System.out.println(item.getValue().getNameType());
		switch (item.getValue().getNameType()) {
		case "Assignment":
			item.setGraphic((Node) new ImageView(new Image("/img/assignment.png")));
			break;
		case "Quiz":
			item.setGraphic((Node) new ImageView(new Image("/img/quiz.png")));
			break;
		case "ManualItem":
			item.setGraphic((Node) new ImageView(new Image("/img/manual_item.png")));
			break;
		case "Category":
			item.setGraphic((Node) new ImageView(new Image("/img/folder.png")));
			break;
		case "Forum":
			item.setGraphic((Node) new ImageView(new Image("/img/forum.png")));
		default:
			break;
		}
	}

	/**
	 * Manejador de eventos para las actividades. Devuelve un manejador de
	 * eventos para cada item.
	 * 
	 * @return manejador de eventos para las actividades
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
	 * Manejador de eventos para el textField de filtro de actividades.
	 * 
	 * @return manejador de eventos para el patrón de filtro de actividades
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
	 * Filtra la lista de actividades del calificador según el tipo y el patrón
	 * introducidos.
	 */
	public void filterCalifications() {
		try {
			clearData();
			ArrayList<GradeReportLine> grcl = (ArrayList<GradeReportLine>) UBUGrades.session.getActualCourse()
					.getGradeReportLines();
			// Establecemos la raiz del Treeview
			TreeItem<GradeReportLine> root = new TreeItem<GradeReportLine>(grcl.get(0));
			MainController.setIcon(root);
			// Llamamos recursivamente para llenar el Treeview
			if (filterType.equals("Todos") && patternCalifications.equals("")) {
				// Sin filtro y sin patrón
				for (int k = 0; k < grcl.get(0).getChildren().size(); k++) {
					TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(grcl.get(0).getChildren().get(k));
					MainController.setIcon(item);
					root.getChildren().add(item);
					root.setExpanded(true);
					setTreeview(item, grcl.get(0).getChildren().get(k));
				}
			} else { // Con filtro
				for (int k = 0; k < grcl.get(0).getChildren().size(); k++) {
					TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(grcl.get(0).getChildren().get(k));
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
						MainController.setIcon(item);
						root.getChildren().add(item);
					}
					root.setExpanded(true);
					setTreeviewFilter(root, item, grcl.get(0).getChildren().get(k));
				}
			}
			// Establecemos la raiz del treeview
			tvwGradeReport.setRoot(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		listParticipants.setItems(enrList);
	}

	/**
	 * Crea un árbol filtrado en el que los hijos del root(raíz) son elementos
	 * de cualquier nivel que cumplen el filtro
	 * 
	 * @param root
	 * @param parent
	 * @param line
	 */
	public void setTreeviewFilter(TreeItem<GradeReportLine> root, TreeItem<GradeReportLine> parent,
			GradeReportLine line) {
		/*
		 * Obtiene los hijos de la linea pasada por parametro Los transforma en
		 * treeitems y los establece como hijos del elemento treeItem
		 * equivalente de line
		 */
		for (int j = 0; j < line.getChildren().size(); j++) {
			TreeItem<GradeReportLine> item = new TreeItem<GradeReportLine>(line.getChildren().get(j));
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
				MainController.setIcon(item);
				root.getChildren().add(item);
			}

			parent.setExpanded(true);
			setTreeviewFilter(root, item, line.getChildren().get(j));
		}

	}

	/**
	 * Cambia la asignatura actual y carga otra
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void changeCourse(ActionEvent actionEvent) throws Exception {
		System.out.println("Cambiando de asignatura...");
		// Accedemos a la siguiente ventana
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Welcome.fxml"));
		// UBUGrades.stage.getScene() setCursor(Cursor.WAIT);
		UBUGrades.stage.close();
		System.out.println("Accediendo a UBUGrades...");
		UBUGrades.stage = new Stage();
		Parent root = loader.load();
		// root.setCursor(Cursor.WAIT);
		Scene scene = new Scene(root);
		UBUGrades.stage.setScene(scene);
		UBUGrades.stage.getIcons().add(new Image("/img/logo_min.png"));
		UBUGrades.stage.setTitle("UBUGrades");
		UBUGrades.stage.show();
	}

	/**
	 * Exporta el gráfico. El usuario podrá elegir entre el formato .png o .jpg
	 * para guardar la imagen.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void saveChart(ActionEvent actionEvent) throws Exception {
		WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);

		File file = new File("chart.png");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar gráfico");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"),
				new FileChooser.ExtensionFilter("*.jpg", "*.jpg"), new FileChooser.ExtensionFilter("*.png", "*.png"));
		try {
			file = fileChooser.showSaveDialog(UBUGrades.stage);
			if (file != null) {
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
				} catch (IOException ex) {
					System.out.println(ex.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Exporta la tabla de calificaciones. El usuario podrá elegir entre el
	 * formato .png o .jpg para guardar la imagen.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void saveTable(ActionEvent actionEvent) throws Exception {
		WritableImage image = webView.snapshot(new SnapshotParameters(), null);

		File file = new File("table.png");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar gráfico");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"),
				new FileChooser.ExtensionFilter("*.jpg", "*.jpg"), new FileChooser.ExtensionFilter("*.png", "*.png"));
		try {
			file = fileChooser.showSaveDialog(UBUGrades.stage);
			if (file != null) {
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
				} catch (IOException ex) {
					System.out.println(ex.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Vuelve a la ventana de login de usuario
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void logOut(ActionEvent actionEvent) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));
		UBUGrades.stage.close();
		System.out.println("Cerrando sesión de usuario");
		UBUGrades.stage = new Stage();
		Parent root = loader.load();
		Scene scene = new Scene(root);
		UBUGrades.stage.setScene(scene);
		UBUGrades.stage.getIcons().add(new Image("/img/logo_min.png"));
		UBUGrades.stage.setTitle("UBUGrades");
		UBUGrades.stage.show();
	}

	/**
	 * Deja de seleccionar los participantes/actividades y borra el gráfico.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void clearSelection(ActionEvent actionEvent) throws Exception {
		listParticipants.getSelectionModel().clearSelection();
		tvwGradeReport.getSelectionModel().clearSelection();
		clearData();
	}

	public void clearData() {
		lineChart.getData().clear();
		engine.loadContent("<html><head></head><body style='background-color:#f2f2f2'></body></html>");
	}

	/**
	 * Abre en el navegador el repositorio del proyecto.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void aboutUBUGrades(ActionEvent actionEvent) throws Exception {
		Desktop.getDesktop().browse(new URL("https://github.com/claumartinezh/TFG_UBUGrades").toURI());
	}

	/**
	 * Botón "Salir". Cierra la aplicación.
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void closeApplication(ActionEvent actionEvent) throws Exception {
		System.out.println("Cerrando aplicación");
		UBUGrades.stage.close();
	}

	/**
	 * Muestra una alerta de error cuando se pierde la conexión.
	 */
	public void errorDeConexion() {
		System.out.println("¡Error de conexión!");
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error de conexión");
		alert.setHeaderText("¡No tiene conexión!");
		alert.setContentText("Su equipo ha perdido la conexión a Internet");

		ButtonType buttonSalir = new ButtonType("Cerrar UBUGrades");

		alert.getButtonTypes().setAll(buttonSalir);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonSalir) {
			UBUGrades.stage.close();
		}
	}
}