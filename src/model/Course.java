package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import controllers.UBUGrades;

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
	public Set<String> roles; // roles que hay en el curso
	// public Map<Integer, Group> groups;
	public Set<String> groups;

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

		//////////
		// groups = new HashMap<>();
	}

	////////
	/*
	 * public void setGroups(Map<Integer, Group> groups) { this.groups = groups;
	 * }
	 * 
	 * public List<Group> getGroups() { return new
	 * ArrayList<Group>(groups.values()); }
	 */

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
	 * Función que crea una lista (set) de los roles que hay en el curso.
	 * 
	 * @param users
	 *            usuarios matriculados en el curso
	 */
	public void setRoles(ArrayList<EnrolledUser> users) {
		// Cargamos la lista de los usuarios
		// ArrayList<String> nameUsers = new ArrayList<String>();
		// Collections.sort(users, (o1, o2) ->
		// o1.getFullName().compareTo(o2.getFullName()));

		roles = new HashSet<String>();
		// recorremos la lista de usuarios
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
	 * @return lista de roles
	 */
	public ArrayList<String> getRoles() {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> roleIt = this.roles.iterator();
		while (roleIt.hasNext()) {
			result.add(roleIt.next());
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

		groups = new HashSet<String>();
		// recorremos la lista de usuarios
		for (int i = 0; i < users.size(); i++) {
			// sacamos el grupo del usuario
			ArrayList<Group> groupsArray = users.get(i).getGroups();
			// cada grupo nuevo se añade al set de grupos
			for (int j = 0; j < groupsArray.size(); j++) {
				roles.add(groupsArray.get(j).getName());
			}
		}
	}

	public ArrayList<String> getGroups() {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> groupsIt = this.groups.iterator();
		while (groupsIt.hasNext()) {
			result.add(groupsIt.next());
		}
		return result;
	}
}
