package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controllers.UBUGrades;

/**
 * Clase curso (asignatura). Cada curso tiene un calificador (compuesto por
 * líneas de calificación); y varios grupos, roles de participantes, y tipos de
 * actividades.
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String shortname;
	private String fullname;
	private int enrolledusercount;
	private String idnumber;
	private String summary;
	public ArrayList<EnrolledUser> enrolledUsers;
	public Set<String> roles; // roles que hay en el curso
	public Set<String> groups; // grupos que hay en el curso
	public ArrayList<GradeReportLine> gradeReportConfigurationLines;
	public Set<String> typeActivities;

	public Course(String token, JSONObject obj) throws Exception {
		this.id = obj.getInt("id");
		if (obj.getString("shortname") != null)
			this.shortname = obj.getString("shortname");
		if (obj.getString("fullname") != null)
			this.fullname = obj.getString("fullname");
		if (obj.getInt("enrolledusercount") != 0)
			this.enrolledusercount = obj.getInt("enrolledusercount");
		if (obj.getString("idnumber") != null)
			this.idnumber = obj.getString("idnumber");
		if (obj.getString("summary") != null)
			this.summary = obj.getString("summary");
		this.enrolledUsers = new ArrayList<EnrolledUser>();
		this.setEnrolledUsers(token, this.id);
		// this.setGradeReportConfigurationLines(token, this.id,
		// this.enrolledUsers.get(0).getId());
	}

	public int getId() {
		return this.id;
	}

	public String getShortName() {
		return this.shortname;
	}

	public String getFullName() {
		return this.fullname;
	}

	public int getEnrolledUsersCount() {
		return this.enrolledusercount;
	}

	public String getIdNumber() {
		return this.idnumber;
	}

	public String getSummary() {
		return this.summary;
	}

	/**
	 * Función que establece los usuarios que están matriculados en un curso.
	 * 
	 * @param token
	 *            token de usuario
	 * @param idCurso
	 *            id del curso deseado
	 * @throws Exception
	 */
	public void setEnrolledUsers(String token, int idCurso) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(UBUGrades.host + "/webservice/rest/server.php?wstoken=" + token
					+ "&moodlewsrestformat=json&wsfunction=" + MoodleOptions.OBTENER_USUARIOS_MATRICULADOS
					+ "&courseid=" + idCurso);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONArray jsonArray = new JSONArray(respuesta);
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						if (jsonObject != null) {
							this.enrolledUsers.add(new EnrolledUser(token, jsonObject));
						}
					}
					this.setRoles(this.enrolledUsers);
					this.setGroups(this.enrolledUsers);
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	/**
	 * Función que devuelve una lista de los usuarios matriculados en el curso.
	 * 
	 * @return lista de usuarios
	 */
	public ArrayList<EnrolledUser> getEnrolledUsers() {
		Collections.sort(this.enrolledUsers, (o1, o2) -> o1.getFullName().compareTo(o2.getFullName()));
		return this.enrolledUsers;
	}

	/**
	 * Función almacena en un set los roles que hay en el curso.
	 * 
	 * @param users
	 *            usuarios matriculados en el curso
	 */
	public void setRoles(ArrayList<EnrolledUser> users) {
		// Creamos el set de roles
		roles = new HashSet<String>();
		// Recorremos la lista de usuarios matriculados en el curso
		for (int i = 0; i < users.size(); i++) {
			// sacamos el rol del usuario
			ArrayList<Role> roleArray = users.get(i).getRoles();
			// cada rol nuevo se añade al set roles
			for (int j = 0; j < roleArray.size(); j++) {
				roles.add(roleArray.get(j).getName());
			}
		}
	}

	/**
	 * Función para obtener los roles que hay en el curso.
	 * 
	 * @return lista de roles del curso
	 */
	public ArrayList<String> getRoles() {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> roleIt = this.roles.iterator();
		while (roleIt.hasNext()) {
			String data = roleIt.next();
			if (data != null && !data.trim().equals(""))
				result.add(data);
		}
		return result;
	}

	/**
	 * Función que almacena en una lista los grupos que hay en un curso, a
	 * partir de los usuarios que están matriculados.
	 * 
	 * @param users
	 *            usuarios del curso
	 */
	public void setGroups(ArrayList<EnrolledUser> users) {
		// creamos el set de grupos
		groups = new HashSet<String>();
		// recorremos la lista de usuarios matriculados en el curso
		for (int i = 0; i < users.size(); i++) {
			// sacamos el grupo del usuario
			ArrayList<Group> groupsArray = users.get(i).getGroups();
			// cada grupo nuevo se añade al set de grupos
			for (int j = 0; j < groupsArray.size(); j++) {
				groups.add(groupsArray.get(j).getName());
			}
		}
	}

	/**
	 * Función para obtener los grupos que hay en el curso.
	 * 
	 * @return lista de grupos del curso
	 */
	public ArrayList<String> getGroups() {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> groupsIt = this.groups.iterator();
		while (groupsIt.hasNext()) {
			String data = groupsIt.next();
			if (data != null && !data.trim().equals(""))
				result.add(data);
		}
		return result;
	}

	/**
	 * Función que establece los GradeReportConfigurationLine de un usuario en
	 * un curso. Esta función se usará para obtener todos los
	 * GradeReportConfigurationLine del primer usuario matriculado y así sacar
	 * la estructura del calificador del curso para después mostrarla como
	 * TreeView en la vista
	 * 
	 * @param token
	 *            token del profesor logueado
	 * @param courseId
	 *            curso del que se quieren cargar los datos
	 * @param userId
	 * @throws Exception
	 */
	public void setGradeReportConfigurationLines(String token, int userId) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String call = UBUGrades.host + "/webservice/rest/server.php?wstoken=" + token
					+ "&moodlewsrestformat=json&wsfunction=" + MoodleOptions.OBTENER_TABLA_NOTAS + "&courseid="
					+ this.getId() + "&userid=" + userId;
			//System.out.println(call);
			HttpGet httpget = new HttpGet(call);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONObject jsonArray = new JSONObject(respuesta);
				//System.out.println(jsonArray.toString());
				// lista de GradeReportConfigurationLines
				this.gradeReportConfigurationLines = new ArrayList<GradeReportLine>();
				// En esta pila sólo van a entrar Categorías. Se mantendrán en
				// la pila mientran tengan descendencia.
				// Una vez añadida al árbol toda la descendencia de un nodo,
				// este nodo se saca de la pila y se añade al árbol.
				Stack<GradeReportLine> deque = new Stack<GradeReportLine>();

				if (jsonArray != null) {
					JSONArray tables = (JSONArray) jsonArray.get("tables");
					JSONObject alumn = (JSONObject) tables.get(0);

					//System.out.println("FA:");
					
					//System.out.println(alumn);
					//System.out.println();

					JSONArray tableData = (JSONArray) alumn.getJSONArray("tabledata");
					/*
					 * System.out.println("TableData:");
					 * System.out.println(tableData); System.out.println();
					 */
					// El elemento table data tiene las líneas del configurador
					// (que convertiremos a GradeReportConfigurationLines)
					for (int i = 0; i < tableData.length(); i++) {
						JSONObject tableDataElement = tableData.getJSONObject(i);
						/*
						 * System.out.println("Leader or feedback:");
						 * System.out.println(leaderOrFeedback);
						 */
						// sea categoría o item, se saca de la misma manera el
						// nivel del itemname
						JSONObject itemname = tableDataElement.getJSONObject("itemname");
						int actualLevel = getActualLevel(itemname.getString("class"));
						int idLine = getIdLine(itemname.getString("id"));
						/*
						 * System.out.println("");
						 * System.out.println("   - Nivel de la línea: " +
						 * actualLevel);
						 */
						// --- Si es un feedback (item o suma de
						// calificaciones):
						if (tableDataElement.isNull("leader")) {
							String nameContainer = itemname.getString("content");
							String nameLine = "";
							String typeActivity = "";
							boolean typeLine = false; // true si es un item,
														// false si es una
														// categoría
							// Si es una actividad (assignment o quiz)
							// Se reconocen por la etiqueta "<a"
							if (nameContainer.substring(0, 2).equals("<a")) {
								// System.out.println(" - Es un assignment o
								// quiz");
								nameLine = getNameActivity(nameContainer);
								if (assignmentOrQuiz(nameContainer).equals("assignment"))
									typeActivity = "Assignment";
								else if (assignmentOrQuiz(nameContainer).equals("quiz"))
									typeActivity = "Quiz";
								typeLine = true;
							} else {
								// Si es un item manual o suma de calificaciones
								// Se reconocen por la etiqueta "<span"
								// System.out.println(" - Es un item manual o
								// suma de calificaciones");
								nameLine = getNameManualItemOrEndCategory(nameContainer);
								if (manualItemOrEndCategory(nameContainer).equals("manualItem")) {
									typeActivity = "ManualItem";
									typeLine = true;
								} else if (manualItemOrEndCategory(nameContainer).equals("endCategory")) {
									typeActivity = "Category";
									typeLine = false;
								}
							}
							/*
							 * System.out.println("   - Nombre de la línea: " +
							 * nameLine); System.out.println("   - Tipo : " +
							 * typeActivity);
							 */

							// Sacamos la nota (grade)
							JSONObject gradeContainer = tableDataElement.getJSONObject("grade");
							Float grade = Float.NaN;
							// Si hay nota numérica
							//System.out.println(tableDataElement);
							//System.out.println(gradeContainer.getString("content"));
							if (!gradeContainer.getString("content").contains("-")) {
								//grade = Float.parseFloat(gradeContainer.getString("content"));
								grade = getNumber(gradeContainer.getString("content"));
								//System.out.println(" - Nota item: " + grade);
							} /*
								 * else // Si no tiene nota registrada, se queda
								 * igual System.out.println("   - No hay nota: "
								 * + grade);
								 */

							// Sacamos el porcentaje
							JSONObject percentageContainer = tableDataElement.getJSONObject("percentage");
							Float percentage = Float.NaN;
							if (!percentageContainer.getString("content").contains("-")) {
								percentage = getNumber(percentageContainer.getString("content"));
								// System.out.println(" - Nota item: " +
								// percentage);
							}
							/*
							 * else{ System.out.
							 * println("   - No hay nota porcentaje: " +
							 * percentage); }
							 */
							// Sacamos el peso
							JSONObject weightContainer = tableDataElement.getJSONObject("weight");
							Float weight = Float.NaN;
							if (!weightContainer.getString("content").contains("-")) {
								weight =getNumber(weightContainer.getString("content"));
								// System.out.println(" - Nota item: " +
								// weight);
							} /*
								 * else{
								 * //System.out.println("   - No hay peso: " +
								 * weight); }
								 */

							// Sacamos el rango
							JSONObject rangeContainer = tableDataElement.getJSONObject("range");
							Float rangeMin = getRange(rangeContainer.getString("content"), true);
							Float rangeMax = getRange(rangeContainer.getString("content"), false);
							// System.out.println(" - Rango: " + rangeMin + "-"
							// + rangeMax);
							if (typeLine) { // Si es un item
								// Añadimos la linea actual
								GradeReportLine actualLine = new GradeReportLine(idLine,
										nameLine, actualLevel, typeLine, weight, rangeMin, rangeMax, grade, percentage, typeActivity);
								if (!deque.isEmpty()) {
									deque.lastElement().addChild(actualLine);
								}
								// Añadimos el elemento a la lista como item
								this.gradeReportConfigurationLines.add(actualLine);
							} else {
								// Obtenemos el elemento cabecera de la pila
								GradeReportLine actualLine = deque.pop();
								// Establecemos los valores restantes
								actualLine.setWeight(weight);
								actualLine.setRangeMin(rangeMin);
								actualLine.setRangeMax(rangeMax);
								actualLine.setNameType(typeActivity);
								// Modificamos la cabecera de esta suma, para
								// dejarla como una categoria completa
								updateGRCLList(actualLine);
							}

							/*
							 * System.out.println(feedback);
							 * System.out.println("");
							 * System.out.println(itemname);
							 */

							// --- Si es una categoría
						} else {
							// System.out.println(" - Tipo : Categoría");
							String nameLine = getNameCategorie(itemname.getString("content"));

							// System.out.println(" - Nombre de la línea: " +
							// nameLine);

							// Añadimos la cabecera de la categoria a la pila
							GradeReportLine actualLine = new GradeReportLine(idLine, nameLine,
									actualLevel, false);
							// Lo añadimos como hijo de la categoria anterior
							if (!deque.isEmpty()) {
								// System.out.println(deque.lastElement().getName());
								deque.lastElement().addChild(actualLine);

							}

							// Añadimos esta cabecera a la pila
							deque.add(actualLine);
							// Añadimos el elemento a la lista como cabecera por
							// ahora
							this.gradeReportConfigurationLines.add(actualLine);
						}
					} // End for
					this.setActivities(this.gradeReportConfigurationLines);
				} // End if
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	/**
	 * Función que devuelve la lista de gradeReportConfigurationLines que hay en
	 * el curso. (El calificador)
	 * 
	 * @return lista de gradeReportConfigurationLines
	 */
	public ArrayList<GradeReportLine> getGRCL() {
		return this.gradeReportConfigurationLines;
	}

	/**
	 * Función que almacena en un set las actividades que hay en el curso.
	 * 
	 * @param grcl
	 *            gradeReportConfigurationLines
	 */
	public void setActivities(ArrayList<GradeReportLine> grcl) {
		// Creamos el set de roles
		typeActivities = new HashSet<String>();
		// Recorremos la lista de usuarios matriculados en el curso
		for (int i = 0; i < grcl.size(); i++) {
			typeActivities.add(grcl.get(i).getNameType());
		}
	}

	/**
	 * Funciónque devuelve las actividades que hay en el curso.
	 * 
	 * @return lista de actividades
	 */
	public ArrayList<String> getTypeActivities() {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> grclIt = this.typeActivities.iterator();
		while (grclIt.hasNext()) {
			String data = grclIt.next();
			if (data != null && !data.trim().equals(""))
				result.add(data);
		}
		return result;
	}

	/**
	 * Función para obtener el nombre de una categoría
	 * 
	 * @param data
	 * @return
	 */
	public String getNameCategorie(String data) {
		String result = "";
		// busco el final de la cadena única a partir de la cual empieza el
		// nombre
		// de la categoría
		int begin = data.lastIndexOf("/>") + 2;
		// el nombre termina al final de todo el texto
		int end = data.length();
		// me quedo con la cadena entre esos índices
		result = data.substring(begin, end);

		return result;
	}

	/**
	 * Función para obtener el nombre de una actividad
	 * 
	 * @param data
	 * @return
	 */
	public String getNameActivity(String data) {
		int begin = data.indexOf(" />") + 3;
		int end = data.indexOf("</a>");
		return data.substring(begin, end);
	}

	/**
	 * Función para obtener el nombre de un item manual o un cierre de categoría
	 * 
	 * @param data
	 * @return
	 */
	public String getNameManualItemOrEndCategory(String data) {
		int begin = data.lastIndexOf("/>") + 2;
		int end = data.indexOf("</span>");
		return data.substring(begin, end);
	}

	/**
	 * Función para obtener el id del item
	 * 
	 * @param data
	 * @return id de un item
	 */
	public int getIdLine(String data) {
		String[] matrix = data.split("_");
		return Integer.parseInt(matrix[1]);
	}

	/**
	 * Función que obtiene el nivel del GradeReportConfigurationLine que está
	 * siendo leída.
	 * 
	 * @param data
	 * @return nivel de la línea
	 */
	public int getActualLevel(String data) {
		int result = 0;
		result = Integer.parseInt(data.substring(5, data.indexOf(" ")));
		return (int) result;
	}

	/**
	 * Función que obtiene el tipo de un GradeReportConfigurationLine (actividad
	 * o categoría)
	 * 
	 * @param data
	 * @return tipo de línea (cebecera de categoría, suma de calificaciones o
	 *         item)
	 */
	public boolean getTypeLine(String data) {
		String[] matrix = data.split(" ");
		return matrix[2].equals("item");
	}

	/**
	 * Función para comprobar si la linea es una suma de calificaciones de
	 * categoría (un cierre de categoría)
	 * 
	 * @param data
	 * @return true si la línea es una suma de calificaciones, false si no
	 */
	public boolean getBaggtLine(String data) {
		String[] matrix = data.split(" ");
		return matrix[3].equals("baggt");
	}

	/**
	 * Función que devuelve el rango mínimo o máximo
	 * 
	 * @param data
	 * @param option
	 * @return rango máximo o mínimo
	 */
	public float getRange(String data, boolean option) {
		String[] ranges = data.split("&ndash;");
		if (option) // true = rango mínimo
			return Float.parseFloat(ranges[0]);
		else // false = rango máximo
			return Float.parseFloat(ranges[1]);
	}

	/**
	 * Sustituimos el elemento de la lista que es una cabecera por el elemento
	 * que es una categoria completa con todos sus atributos
	 * 
	 * @param line
	 */
	public void updateGRCLList(GradeReportLine line) {
		for (int i = 0; i < this.gradeReportConfigurationLines.size(); i++) {
			if (this.gradeReportConfigurationLines.get(i).getId() == line.getId()) {
				this.gradeReportConfigurationLines.set(i, line);
			}
		}
	}

	/**
	 * Función para saber si una cadena de texto contiene un número decimal.
	 * 
	 * @param cad
	 *            texto
	 * @return true si hay un decimal, false si no
	 */
	public boolean esDecimal(String cad) {
		try {
			Float.parseFloat(cad);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Función que diferencia si la actividad es un quiz o un assignment.
	 * 
	 * @param nameContainer
	 * @return true si es un assignment, false si es un quiz
	 */
	private String assignmentOrQuiz(String data) {
		String url = data.substring(data.lastIndexOf("href="), data.indexOf("?id="));
		if (url.contains("mod/assign")) {
			return "assignment";
		} else if (url.contains("mod/quiz"))
			return "quiz";
		else
			return "";
	}

	/**
	 * Función que diferencia si la línea es un item manual o un cierre de
	 * categoría.
	 * 
	 * @param data
	 * @return
	 */
	private String manualItemOrEndCategory(String data) {
		String url = data.substring(data.lastIndexOf("src="), data.indexOf("/>"));
		if (url.contains("i/manual_item")) {
			return "manualItem";
		} else if (url.contains("i/agg_sum"))
			return "endCategory";
		else
			return "";
	}
	/**
	 * Función que devuelve un número en formato Float si se encuentra en la cadena pasada
	 * @param data
	 * @return
	 */
	private Float getNumber(String data){
		Pattern pattern = Pattern.compile("[0-9]{1,2},{1}[0-9]{1,2}");
		//Pattern pattern = Pattern.compile("[0-9]{1,2}");
		Matcher match = pattern.matcher(data);
		if (match.find()) {
			//System.out.println(data.substring(match.start(),match.end()));
			return Float.parseFloat(data.substring(match.start(),match.end()).replace(",", "."));
		}
		return Float.NaN;
		
	}
}
