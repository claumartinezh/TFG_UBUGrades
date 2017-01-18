package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase para el usuario logeado en la aplicación
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class MoodleUser {
	private int id;
	private String userName;
	private String fullName;
	private String email;
	private Date firstAccess;
	private Date lastAccess;
	private String city;
	private String country;
	private ArrayList<Course> courses;

	/**
	 * Constructor de MoodleUser sin parámetros
	 */
	public MoodleUser() {
		this.setId(0);
		this.courses = new ArrayList<Course>();
	}

	/**
	 * Constructor de MoodleUser con parámetros
	 * 
	 * @param id
	 *            id del usuario logueado
	 * @param userName
	 *            nombre de usuario
	 * @param fullName
	 *            nombre completo
	 * @param eMail
	 *            correo electrónico
	 * @param firstAccess
	 *            fecha de primer acceso
	 * @param lastAccess
	 *            fecha de último acceso
	 * @param city
	 *            ciudad
	 * @param country
	 *            país
	 */
	public MoodleUser(int id, String userName, String fullName, String eMail, Date firstAccess, Date lastAccess,
			String city, String country) {
		this.setId(id);
		this.setUserName(userName);
		this.setFullName(fullName);
		this.setEmail(eMail);
		this.setFirstAccess(firstAccess);
		this.setLastAccess(lastAccess);
		this.setCity(city);
		this.setCountry(country);
		this.courses = new ArrayList<Course>();
	}
	/*
	 * public MoodleUser(String token, String eMail) throws Exception { // Gson
	 * gson = new Gson(); CloseableHttpClient httpclient =
	 * HttpClients.createDefault(); try { HttpGet httpget = new
	 * HttpGet(UBUGrades.host + "/webservice/rest/server.php?wstoken=" + token +
	 * "&moodlewsrestformat=json&wsfunction=" +
	 * MoodleOptions.OBTENER_INFO_USUARIO + "&field=email&values[0]=" + eMail);
	 * CloseableHttpResponse response = httpclient.execute(httpget);
	 * 
	 * try { String respuesta = EntityUtils.toString(response.getEntity()); //
	 * System.out.println(respuesta); // this.tokenUser = //
	 * gson.fromJson(respuesta,Token.class).getToken(); // this.userId = //
	 * gson.fromJson(respuesta,Curso.class).getUserId(); JSONArray jsonArray =
	 * new JSONArray(respuesta); // JSONObject jsonObject = new
	 * JSONObject(respuestaHTTP); if (jsonArray != null) { JSONObject jsonObject
	 * = (JSONObject) jsonArray.get(0); if (jsonObject != null) { this.id =
	 * jsonObject.getInt("id"); if (jsonObject.getString("username") != null)
	 * this.userName = jsonObject.getString("username"); if
	 * (jsonObject.getString("fullname") != null) this.fullName =
	 * jsonObject.getString("fullname"); if (jsonObject.getString("email") !=
	 * null) this.email = jsonObject.getString("email"); if (new
	 * Date(jsonObject.getLong("firstaccess")) != null) this.firstAccess = new
	 * Date(jsonObject.getLong("firstaccess") * 1000); if (new
	 * Date(jsonObject.getLong("lastaccess")) != null) this.lastAccess = new
	 * Date(jsonObject.getLong("lastaccess") * 1000); /* if
	 * (jsonObject.getString("city") != null) this.city =
	 * jsonObject.getString("city"); if (jsonObject.getString("country") !=
	 * null) this.country = jsonObject.getString("country");
	 *
	 * this.courses = new ArrayList<Course>(); } } } finally { response.close();
	 * } } finally { httpclient.close(); } this.setCourses(token); }
	 */

	/**
	 * Devuelve el id del usuario
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Modifica el id del usuario
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre de usuario
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Modifica el nombre del usuario
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Devuelve el nombre completo del usuario
	 * 
	 * @return fullName
	 */
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * Modifica el nombre completo del usuario
	 * 
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Devuelve el email del usuario
	 * 
	 * @return email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Modifica el email del usuario
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Devuelve la fecha de primer acceso
	 * 
	 * @return firstAccess
	 */
	public Date getFirstAccess() {
		return this.firstAccess;
	}

	/**
	 * Modifica la fecha de primer acceso
	 * 
	 * @param firstAccess
	 */
	public void setFirstAccess(Date firstAccess) {
		this.firstAccess = firstAccess;
	}

	/**
	 * Devuelve la fecha de último acceso
	 * 
	 * @return lastAccess
	 */
	public Date getLastAccess() {
		return this.lastAccess;
	}

	/**
	 * Modifica la fecha de último acceso
	 * 
	 * @param lastAccess
	 */
	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	/**
	 * Devuelve la ciudad del usuario
	 * 
	 * @return city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Modifica la ciudad del usuario
	 * 
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Devuelve el país
	 * 
	 * @return country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * Modifica el país del usuario
	 * 
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Devuelve la lista de cursos que en los que está atriculado el usuario
	 * 
	 * @return lista de cursos
	 */
	public List<Course> getCourses() {
		return this.courses;
	}

	/**
	 * Modifica la lista de cursos en los que está atriculado el usuario
	 * 
	 * @param courses
	 */
	public void setCourses(ArrayList<Course> courses) {
		this.courses.clear();
		for (Course element : courses) {
			this.courses.add(element);
		}
	}

}
