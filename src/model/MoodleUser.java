package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import view.UBUGrades;

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

	public MoodleUser(String token, String eMail) throws Exception {
		// Gson gson = new Gson();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(UBUGrades.host + "/webservice/rest/server.php?wstoken=" + token
					+ "&moodlewsrestformat=json&wsfunction=" + MoodleOptions.OBTENER_ID_USUARIO
					+ "&field=email&values[0]=" + eMail);
			CloseableHttpResponse response = httpclient.execute(httpget);

			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				// System.out.println(respuesta);
				// this.tokenUser =
				// gson.fromJson(respuesta,Token.class).getToken();
				// this.userId =
				// gson.fromJson(respuesta,Curso.class).getUserId();
				JSONArray jsonArray = new JSONArray(respuesta);
				// JSONObject jsonObject = new JSONObject(respuestaHTTP);
				if (jsonArray != null) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(0);
					if (jsonObject != null) {
						this.id = jsonObject.getInt("id");
						if (jsonObject.getString("username") != null)
							this.userName = jsonObject.getString("username");
						if (jsonObject.getString("fullname") != null)
							this.fullName = jsonObject.getString("fullname");
						if (jsonObject.getString("email") != null)
							this.email = jsonObject.getString("email");
						if (new Date(jsonObject.getLong("firstaccess")) != null)
							this.firstAccess = new Date(jsonObject.getLong("firstaccess") * 1000);
						if (new Date(jsonObject.getLong("lastaccess")) != null)
							this.lastAccess = new Date(jsonObject.getLong("lastaccess") * 1000);
						/*
						 * if (jsonObject.getString("city") != null) this.city =
						 * jsonObject.getString("city"); if
						 * (jsonObject.getString("country") != null)
						 * this.country = jsonObject.getString("country");
						 */
						this.courses = new ArrayList<Course>();
					}
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		this.setCourses(token);
	}

	public int getId() {
		return this.id;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getEmail() {
		return this.email;
	}

	public Date getFirstAccess() {
		return this.firstAccess;
	}

	public Date getLastAccess() {
		return this.lastAccess;
	}

	public String getCity() {
		return this.city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCourses(String token) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(UBUGrades.host + "/webservice/rest/server.php?wstoken=" + token
					+ "&moodlewsrestformat=json&wsfunction=" + MoodleOptions.OBTENER_CURSOS + "&userid=" + id);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				String respuesta = EntityUtils.toString(response.getEntity());
				JSONArray jsonArray = new JSONArray(respuesta);
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						if (jsonObject != null) {
							this.courses.add(new Course(token, jsonObject));
						}
					}
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	public List<Course> getCourses() {
		return this.courses;
	}

}
