package webservice;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import controllers.UBUGrades;
import model.Course;

/**
 * Clase sesi�n. Obtiene el token de usuario y guarda sus par�metros. Establece
 * la sesi�n.
 * 
 * @author Claudia Mart�nez Herrero
 * @version 1.0
 *
 */
public class Session {
	private String email;
	private String password;
	private String tokenUser;
	private Course actualCourse;

	/**
	 * Constructor de la clase Session
	 * 
	 * @param mail
	 *            correo del usuario
	 * @param pass
	 *            contrase�a de usuario
	 */
	public Session(String mail, String pass) {
		this.email = mail;
		this.password = pass;
	}

	/**
	 * Obtiene el token de usuario
	 * 
	 * @return
	 */
	public String getToken() {
		return this.tokenUser;
	}

	/**
	 * Establece el token del usuario a partir de usuario y contrase�a. Se
	 * realiza mediante una petici�n http al webservice de Moodle
	 * 
	 * @throws Exception
	 */
	public void setToken() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(UBUGrades.host + "/login/token.php?username=" + this.email + "&password="
					+ this.password + "&service=" + MoodleOptions.SERVICIO_WEB_MOODLE);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(respuesta);
				if (jsonObject != null) {
					this.tokenUser = jsonObject.getString("token");
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
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
	 * Devuelve el curso actual
	 * 
	 * @return actualCourse
	 */
	public Course getActualCourse() {
		return this.actualCourse;
	}

	/**
	 * Modifica el curso actual
	 * 
	 * @param course
	 */
	public void setActualCourse(Course course) {
		this.actualCourse = course;
	}
}
