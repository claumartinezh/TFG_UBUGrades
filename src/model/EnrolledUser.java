package model;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import view.UBUGrades;

public class EnrolledUser {
	private int id;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private Date firstAccess;
	private Date lastAccess;
	private String description;
	private String profileImageUrl;
	private int roleId;
	private String roleShortName;
	private ArrayList<Integer> courses;

	public EnrolledUser(String token, JSONObject obj) throws Exception {
		this.id = obj.getInt("id");
		if (obj.getString("firstname") != null)
			this.firstName = obj.getString("firstname");
		if (obj.getString("lastname") != null)
			this.lastName = obj.getString("lastname");
		if (obj.getString("fullname") != null)
			this.fullName = obj.getString("fullname");
		if (obj.getString("email") != null)
			this.email = obj.getString("email");
		if (new Date(obj.getLong("firstaccess")) != null)
			this.firstAccess = new Date(obj.getLong("firstaccess") * 1000);
		if (new Date(obj.getLong("lastaccess")) != null)
			this.lastAccess = new Date(obj.getLong("lastaccess") * 1000);
		if (obj.getString("description") != null)
			this.description = obj.getString("description");
		if (obj.getString("profileimageurl") != null)
			this.profileImageUrl = obj.getString("profileimageurl");

		/*
		 * if (obj.getInt("roleid") != 0) this.roleId = obj.getInt("roleid"); if
		 * (obj.getString("roleshortname") != null) this.roleShortName =
		 * obj.getString("roleshortname"); if (obj.getString("summary") != null)
		 * this.summary = obj.getString("summary");
		 */
		this.courses = new ArrayList<Integer>();
		//this.setCourses(token);
	}

	public int getId() {
		return this.id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
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

	public String getDescription() {
		return this.description;
	}

	public String getProfileImageUrl() {
		return this.profileImageUrl;
	}

	public int getRoleId() {
		return this.id;
	}

	public String getRoleShortName() {
		return this.roleShortName;
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
							this.courses.add(jsonObject.getInt("id"));
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

	public ArrayList<Integer> getEnrolledCourses() {
		return this.courses;
	}
}
