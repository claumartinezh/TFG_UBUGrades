package webservice;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.MainController;
import controllers.UBUGrades;
import model.Course;
import model.EnrolledUser;
import model.GradeReportLine;

/**
 * Clase Course para webservices. Recoge funciones �tiles para servicios web
 * relacionados con un curso.
 * 
 * @author Claudia Mart�nez Herrero
 * @version 1.0
 *
 */
public class CourseWS {

	static final Logger logger = LoggerFactory.getLogger(CourseWS.class);

	/**
	 * Establece los usuarios que est�n matriculados en un curso junto con su
	 * rol y grupo.
	 * 
	 * @param token
	 *            token de usuario
	 * @param idCurso
	 *            id del curso
	 * @throws Exception
	 */
	public static void setEnrolledUsers(String token, Course course) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		ArrayList<EnrolledUser> eUsers = new ArrayList<EnrolledUser>();
		try {
			HttpGet httpget = new HttpGet(UBUGrades.host + "/webservice/rest/server.php?wstoken=" + token
					+ "&moodlewsrestformat=json&wsfunction=" + MoodleOptions.OBTENER_USUARIOS_MATRICULADOS
					+ "&courseid=" + course.getId());
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONArray jsonArray = new JSONArray(respuesta);
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						if (jsonObject != null) {
							eUsers.add(new EnrolledUser(token, jsonObject));
						}
					}
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		course.setEnrolledUsers(eUsers);
		course.setRoles(eUsers);
		course.setGroups(eUsers);
	}

	/**
	 * Establece los GradeReportLine de un usuario en
	 * un curso. Esta funci�n se usar� para obtener todos los
	 * GradeReportLine del primer usuario matriculado y as� sacar
	 * la estructura del calificador del curso para despu�s mostrarla como
	 * TreeView en la vista
	 * 
	 * @param token
	 *            token del profesor logueado
	 * @param courseId
	 *            curso del que se quieren cargar los datos
	 * @param userId
	 *            id del usuario a cargar
	 * @throws Exception
	 */
	public static void setGradeReportLines(String token, int userId, Course course) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String call = UBUGrades.host + "/webservice/rest/server.php?wstoken=" + token
					+ "&moodlewsrestformat=json&wsfunction=" + MoodleOptions.OBTENER_TABLA_NOTAS + "&courseid="
					+ course.getId() + "&userid=" + userId;
			// logger.info(call);
			HttpGet httpget = new HttpGet(call);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONObject jsonArray = new JSONObject(respuesta);
				// logger.info("response: "+jsonArray.toString());
				// lista de GradeReportLines
				course.gradeReportLines = new ArrayList<GradeReportLine>();
				// En esta pila s�lo van a entrar Categor�as. Se mantendr�n en
				// la pila mientran tengan descendencia.
				// Una vez a�adida al �rbol toda la descendencia de un nodo,
				// este nodo se saca de la pila y se a�ade al �rbol.
				Stack<GradeReportLine> deque = new Stack<GradeReportLine>();

				if (jsonArray != null) {
					JSONArray tables = (JSONArray) jsonArray.get("tables");
					JSONObject alumn = (JSONObject) tables.get(0);

					JSONArray tableData = (JSONArray) alumn.getJSONArray("tabledata");

					// logger.info("TableData:");
					// logger.info(tableData); logger.info();

					// El elemento table data tiene las l�neas del configurador
					// (que convertiremos a GradeReportLines)
					for (int i = 0; i < tableData.length(); i++) {
						JSONObject tableDataElement = tableData.getJSONObject(i);
						// sea categor�a o item, se saca de la misma manera el
						// nivel del itemname
						JSONObject itemname = tableDataElement.getJSONObject("itemname");
						int actualLevel = getActualLevel(itemname.getString("class"));
						int idLine = getIdLine(itemname.getString("id"));
						// Si es un feedback (item o suma de
						// calificaciones):
						if (tableDataElement.isNull("leader")) {
							String nameContainer = itemname.getString("content");
							String nameLine = "";
							String typeActivity = "";
							// true=item, false=categor�a
							boolean typeLine = false;
							// Si es una actividad (assignment o quiz)
							// Se reconocen por la etiqueta "<a"
							if (nameContainer.substring(0, 2).equals("<a")) {
								nameLine = getNameActivity(nameContainer);
								if (assignmentOrQuizOrForum(nameContainer).equals("assignment"))
									typeActivity = "Assignment";
								else if (assignmentOrQuizOrForum(nameContainer).equals("quiz"))
									typeActivity = "Quiz";
								else if (assignmentOrQuizOrForum(nameContainer).equals("forum"))
									typeActivity = "Forum";
								typeLine = true;
							} else {
								// Si es un item manual o suma de calificaciones
								// Se reconocen por la etiqueta "<span"
								nameLine = getNameManualItemOrEndCategory(nameContainer);
								if (manualItemOrEndCategory(nameContainer).equals("manualItem")) {
									typeActivity = "ManualItem";
									typeLine = true;
								} else if (manualItemOrEndCategory(nameContainer).equals("endCategory")) {
									typeActivity = "Category";
									typeLine = false;
								}
							}
							// Sacamos la nota (grade)
							JSONObject gradeContainer = tableDataElement.getJSONObject("grade");
							String grade = "-";
							// Si no hay nota num�rica
							if (!gradeContainer.getString("content").contains("-")) {
								grade = getNumber(gradeContainer.getString("content"));
								// logger.info(" - Nota item: " + grade);
							}

							// Sacamos el porcentaje
							JSONObject percentageContainer = tableDataElement.getJSONObject("percentage");
							Float percentage = Float.NaN;
							if (!percentageContainer.getString("content").contains("-")) {
								percentage = getFloat(percentageContainer.getString("content"));
							}
							// Sacamos el peso
							JSONObject weightContainer = tableDataElement.optJSONObject("weight");
							Float weight = Float.NaN;
							if (weightContainer != null) {
								if (!weightContainer.getString("content").contains("-")) {
									weight = getFloat(weightContainer.getString("content"));
									// logger.info(" - Nota item: " + weight);
								}
							}
							// Sacamos el rango
							JSONObject rangeContainer = tableDataElement.getJSONObject("range");
							String rangeMin = getRange(rangeContainer.getString("content"), true);
							String rangeMax = getRange(rangeContainer.getString("content"), false);
							// logger.info("-Rango: " + rangeMin + "-" +
							// rangeMax);
							// logger.info(" - Rango: " + rangeMin + "-" +
							// rangeMax);
							if (typeLine) { // Si es un item
								// A�adimos la linea actual
								GradeReportLine actualLine = new GradeReportLine(idLine, nameLine, actualLevel,
										typeLine, weight, rangeMin, rangeMax, grade, percentage, typeActivity);
								if (!deque.isEmpty()) {
									deque.lastElement().addChild(actualLine);
								}
								// A�adimos el elemento a la lista como item
								course.gradeReportLines.add(actualLine);
							} else {
								// Obtenemos el elemento cabecera de la pila
								GradeReportLine actualLine = deque.pop();
								// Establecemos los valores restantes
								actualLine.setWeight(weight);
								actualLine.setRangeMin(rangeMin);
								actualLine.setRangeMax(rangeMax);
								actualLine.setNameType(typeActivity);
								actualLine.setGrade(grade);
								// Modificamos la cabecera de esta suma, para
								// dejarla como una categoria completa
								course.updateGRLList(actualLine);
							}
						} else {// --- Si es una categor�a
							String nameLine = getNameCategorie(itemname.getString("content"));

							// A�adimos la cabecera de la categoria a la pila
							GradeReportLine actualLine = new GradeReportLine(idLine, nameLine, actualLevel, false);
							// Lo a�adimos como hijo de la categoria anterior
							if (!deque.isEmpty()) {
								deque.lastElement().addChild(actualLine);
							}

							// A�adimos esta cabecera a la pila
							deque.add(actualLine);
							// A�adimos el elemento a la lista como cabecera por
							// ahora
							course.gradeReportLines.add(actualLine);
						}
					} // End for
					course.setActivities(course.gradeReportLines);
				} // End if
			} finally {
				response.close();
			}
		} catch (Exception e) {
			logger.error("Error de conexi�n");
			MainController.errorDeConexion();
		} finally {
			httpclient.close();
		}
	}

	/**
	 * Devuelve el id del item
	 * 
	 * @param data
	 * @return id de un item
	 */
	public static int getIdLine(String data) {
		String[] matrix = data.split("_");
		return Integer.parseInt(matrix[1]);
	}

	/**
	 * Devuelve el nivel del GradeReportLine que est� siendo le�da.
	 * 
	 * @param data
	 * @return nivel de la l�nea
	 */
	public static int getActualLevel(String data) {
		int result = 0;
		result = Integer.parseInt(data.substring(5, data.indexOf(" ")));
		return (int) result;
	}

	/**
	 * Devuelve el nombre de una categor�a
	 * 
	 * @param data
	 * @return
	 */
	public static String getNameCategorie(String data) {
		String result = "";
		// Busco el final de la cadena �nica a partir de la cual empieza el
		// nombre de la categor�a
		int begin = data.lastIndexOf("/>") + 2;
		// El nombre termina al final de todo el texto
		int end = data.length();
		// Me quedo con la cadena entre esos �ndices
		result = data.substring(begin, end);

		return result;
	}

	/**
	 * Devuelve el nombre de una actividad
	 * 
	 * @param data
	 * @return
	 */
	public static String getNameActivity(String data) {
		int begin = data.indexOf(" />") + 3;
		int end = data.indexOf("</a>");
		return data.substring(begin, end);
	}

	/**
	 * Devuelve el nombre de un item manual o un cierre de categor�a
	 * 
	 * @param data
	 * @return
	 */
	public static String getNameManualItemOrEndCategory(String data) {
		int begin = data.lastIndexOf("/>") + 2;
		int end = data.indexOf("</span>");
		return data.substring(begin, end);
	}

	/**
	 * Devuelve el tipo de un GradeReportLine (actividad o categor�a)
	 * 
	 * @param data
	 * @return tipo de l�nea (cebecera de categor�a, suma de calificaciones o
	 *         item)
	 */
	public static boolean getTypeLine(String data) {
		String[] matrix = data.split(" ");
		return matrix[2].equals("item");
	}

	/**
	 * Comprueba si la l�nea es una suma de calificaciones de categor�a (un
	 * cierre de categor�a)
	 * 
	 * @param data
	 * @return true si la l�nea es una suma de calificaciones, false si no
	 */
	public static boolean getBaggtLine(String data) {
		String[] matrix = data.split(" ");
		return matrix[3].equals("baggt");
	}

	/**
	 * Devuelve el rango m�nimo o m�ximo
	 * 
	 * @param data
	 * @param option
	 * @return rango m�ximo o m�nimo
	 */
	public static String getRange(String data, boolean option) {
		String[] ranges = data.split("&ndash;");
		if (option) // true = rango m�nimo
			return ranges[0];
		else // false = rango m�ximo
			return ranges[1];
	}

	/**
	 * Comprueba si una cadena de texto contiene un n�mero decimal.
	 * 
	 * @param cad
	 *            texto
	 * @return true si hay un decimal, false si no
	 */
	public static boolean esDecimal(String cad) {
		try {
			Float.parseFloat(cad);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Diferencia si la actividad es un quiz o un assignment.
	 * 
	 * @param nameContainer
	 * @return true si es un assignment, false si es un quiz
	 */
	private static String assignmentOrQuizOrForum(String data) {
		String url = data.substring(data.lastIndexOf("href="), data.indexOf("?id="));
		if (url.contains("mod/assign")) {
			return "assignment";
		} else if (url.contains("mod/quiz"))
			return "quiz";
		else if (url.contains("mod/forum"))
			return "forum";
		else
			return "";
	}

	/**
	 * Diferencia si la l�nea es un item manual o un cierre de categor�a.
	 * 
	 * @param data
	 * @return
	 */
	private static String manualItemOrEndCategory(String data) {
		String url = data.substring(data.lastIndexOf("src="), data.indexOf("/>"));
		if (url.contains("i/manual_item")) {
			return "manualItem";
		} else if (url.contains("i/agg_sum"))
			return "endCategory";
		else
			return "";
	}

	/**
	 * Devuelve un n�mero en formato Float si se encuentra en la cadena pasada
	 * 
	 * @param data
	 * @return
	 */
	public static Float getFloat(String data) {
		Pattern pattern = Pattern.compile("[0-9]{1,3},{1}[0-9]{1,2}");
		Matcher match = pattern.matcher(data);
		if (match.find()) {
			return Float.parseFloat(data.substring(match.start(), match.end()).replace(",", "."));
		}
		return Float.NaN;

	}

	/**
	 * Devuelve el numero, con el formato especificado, encontrado en la cadena
	 * pasada Si no es un numero devuelve todo el contenido
	 * 
	 * @param data
	 * @return
	 */
	private static String getNumber(String data) {
		Pattern pattern = Pattern.compile("[0-9]{1,3},{1}[0-9]{1,2}");
		Matcher match = pattern.matcher(data);
		if (match.find()) {
			return data.substring(match.start(), match.end());
		}
		return data;
	}
}
