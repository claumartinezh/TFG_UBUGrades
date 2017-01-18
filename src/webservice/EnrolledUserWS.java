package webservice;

import java.util.ArrayList;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import controllers.UBUGrades;
import model.EnrolledUser;
import model.MoodleOptions;

/**
 * Clase EnrolledUser para webservices.
 * 
 * @author Claudia Mart�nez Herrero
 *
 */
public class EnrolledUserWS {
	/**
	 * Almacena los cursos de un usuario matriculado
	 * 
	 * @param token
	 *            token de usuario
	 * @throws Exception
	 */
	public void setCourses(String token, EnrolledUser eUser) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		ArrayList<Integer> courses = new ArrayList<Integer>();
		try {
			HttpGet httpget = new HttpGet(UBUGrades.host + "/webservice/rest/server.php?wstoken=" + token
					+ "&moodlewsrestformat=json&wsfunction=" + MoodleOptions.OBTENER_CURSOS + "&userid="
					+ eUser.getId());
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONArray jsonArray = new JSONArray(respuesta);
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						if (jsonObject != null) {
							courses.add(jsonObject.getInt("id"));
						}
					}
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		eUser.setEnrolledCourses(courses);
	}
}
