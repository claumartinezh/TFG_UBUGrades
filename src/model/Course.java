package model;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import view.UBUGrades;

/**
 * Clase curso
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class Course implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String shortname;
	private String fullname;
	private int enrolledusercount;
	private String idnumber;
	private String summary;
	public ArrayList<EnrolledUser> enrolledUsers;

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
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	public ArrayList<EnrolledUser> getEnrolledUsers() {
		return this.enrolledUsers;
	}
}
