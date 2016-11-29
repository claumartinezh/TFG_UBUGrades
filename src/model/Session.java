package model;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import view.UBUGrades;

/**
 * Clase sesi�n. Obtiene el token de usuario y guarda sus par�metros
 * 
 * @author Claudia Mart�nez Herrero
 *
 */
public class Session {
	private String eMail;
	private String password;
	private String tokenUser;
	private Course actualCourse;

	public Session(String mail, String pass) {
		this.eMail = mail;
		this.password = pass;
	}

	/**
	 * Obtiene el token del usuario a partir de usuario y contrase�a. Se realiza
	 * mediante una petici�n http al webservice de Moodle
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

	public String getToken() {
		return this.tokenUser;
	}

	public String getEmail() {
		return this.eMail;
	}

	public void setCourse(Course course) {
		this.actualCourse = course;
	}

	public Course getCourse() {
		return this.actualCourse;
	}

}
