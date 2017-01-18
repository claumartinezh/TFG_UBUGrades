package webservice;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import controllers.UBUGrades;
import model.Course;
import model.MoodleOptions;

/**
 * Clase sesión. Obtiene el token de usuario y guarda sus parámetros
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class Session {
	private String eMail;
	private String password;
	private String tokenUser;
	private Course actualCourse;

	/**
	 * Constructor de la clase Session
	 * 
	 * @param mail
	 *            correo del usuario
	 * @param pass
	 *            contraseña de usuario
	 */
	public Session(String mail, String pass) {
		this.eMail = mail;
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
	 * Establece el token del usuario a partir de usuario y contraseña. Se
	 * realiza mediante una petición http al webservice de Moodle
	 * 
	 * @throws Exception
	 */
	public void setToken() throws Exception {
		// Gson gson = new Gson();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(UBUGrades.host + "/login/token.php?username=" + this.eMail + "&password="
					+ this.password + "&service=" + MoodleOptions.SERVICIO_WEB_MOODLE);
			CloseableHttpResponse response = httpclient.execute(httpget);

			// en localhost (da error)
			// HttpGet httpget = new
			// HttpGet("https://localhost/login/token.php?username=" +
			// this.eMail + "&password=" + this.password + "&service=" +
			// OpcionesMoodle.SERVICIO_WEB_MOODLE);
			// CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(respuesta);
				if (jsonObject != null) {
					this.tokenUser = jsonObject.getString("token");
				}
				// this.tokenUser =
				// gson.fromJson(respuesta,Token.class).getToken();
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
		return this.eMail;
	}

	/**
	 * Modifica el email del usuario
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.eMail = email;
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
