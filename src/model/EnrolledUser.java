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

import controllers.UBUGrades;

/**
 * Clase para un usuario matriculado en una asignatura
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class EnrolledUser {
	private int id;
	// private String firstName;
	// private String lastName;
	private String fullName;
	// private String email;
	private Date firstAccess;
	private Date lastAccess;
	public String description;
	private String descriptionFormat;
	private String city;
	private String country;
	private String profileImageUrlSmall;
	private String profileImageUrl;
	private ArrayList<Role> roles;
	private ArrayList<Group> groups;
	private ArrayList<Integer> courses;

	public EnrolledUser(String token, JSONObject obj) throws Exception {
		this.id = obj.getInt("id");
		/*
		 * if (obj.getString("firstname") != null) this.firstName =
		 * obj.getString("firstname"); if (obj.getString("lastname") != null)
		 * this.lastName = obj.getString("lastname");
		 */
		if (obj.getString("fullname") != null)
			this.fullName = obj.getString("fullname");
		/*
		 * if (obj.getString("email") != null) this.email =
		 * obj.getString("email");
		 */
		if (new Date(obj.getLong("firstaccess")) != null)
			this.firstAccess = new Date(obj.getLong("firstaccess") * 1000);
		if (new Date(obj.getLong("lastaccess")) != null)
			this.lastAccess = new Date(obj.getLong("lastaccess") * 1000);
		/*
		 * if (obj.getString("description") != null) this.description =
		 * obj.getString("description");
		 */
		if (obj.getString("profileimageurl") != null)
			this.profileImageUrl = obj.getString("profileimageurl");

		if (obj.getJSONArray("roles") != null) {
			JSONArray roleArray = obj.getJSONArray("roles");
			roles = new ArrayList<Role>();
			for (int i = 0; i < roleArray.length(); i++) {
				// Establece un rol con el id, name y shortname obtenido de cada
				// JSONObject del JSONArray
				Role rol = new Role(roleArray.getJSONObject(i).getInt("roleid"),
						roleArray.getJSONObject(i).getString("name"),
						roleArray.getJSONObject(i).getString("shortname"));
				if (rol != null)
					roles.add(rol);
			}
		}

		if (obj.getJSONArray("groups") != null) {
			JSONArray groupArray = obj.getJSONArray("groups");
			groups = new ArrayList<Group>();
			for (int i = 0; i < groupArray.length(); i++) {
				// Establece un grupo con el id, name y description obtenido de
				// cada
				// JSONObject del JSONArray
				Group group = new Group(groupArray.getJSONObject(i).getInt("id"),
						groupArray.getJSONObject(i).getString("name"),
						groupArray.getJSONObject(i).getString("description"));
				if (group != null)
					groups.add(group);
			}
		}
		this.courses = new ArrayList<Integer>();
		// this.setCourses(token);
	}

	public int getId() {
		return this.id;
	}

	// public String getFirstName() {
	// return this.firstName;
	// }
	//
	// public String getLastName() {
	// return this.lastName;
	// }

	public String getFullName() {
		return this.fullName;
	}

	// public String getEmail() {
	// return this.email;
	// }

	public Date getFirstAccess() {
		return this.firstAccess;
	}

	public Date getLastAccess() {
		return this.lastAccess;
	}

	public String getDescription() {
		return this.description;
	}

	public String getDescriptionFormat() {
		return this.descriptionFormat;
	}

	public String getCity() {
		return this.city;
	}

	public String getCountry() {
		return this.country;
	}

	public String getProfileImageUrlSmall() {
		return this.profileImageUrlSmall;
	}

	public String getProfileImageUrl() {
		return this.profileImageUrl;
	}

	public ArrayList<Role> getRoles() {
		return this.roles;
	}

	public ArrayList<Group> getGroups() {
		return this.groups;
	}

	/**
	 * Función que almacena los cursos de un usuario matriculado
	 * 
	 * @param token
	 *            token de usuario
	 * @throws Exception
	 */
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
