package model;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase que representa un usuario matriculado en una asignatura
 * 
 * @author Claudia Mart�nez Herrero
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

	/**
	 * Constructor de EnrolledUser
	 * 
	 * @param token
	 *            token de usuario logueado
	 * @param obj
	 *            objeto JSON con la informaci�n del usuario
	 * @throws Exception
	 */
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
		// RMS
		if (obj.optJSONArray("groups") != null) {
			// if (obj.getJSONArray("groups") != null) {
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
		// Added by RMS
		else {
			groups = new ArrayList<>(); // to have an empty list, not a null
		}
		this.courses = new ArrayList<Integer>();
		// this.setCourses(token);
	}

	/**
	 * Devuelve el id del usuario
	 * 
	 * @return id de usuario matriculado
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
	 * Devuelve el nombre del usuario
	 * 
	 * @return fullName
	 */
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * Modifica el nombre del usuario
	 * 
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Devuelve el primer acceso del usuario a la plataforma
	 * 
	 * @return firstAccess
	 */
	public Date getFirstAccess() {
		return this.firstAccess;
	}

	/**
	 * Modifica el primer acceso del usuario a la plataforma
	 * 
	 * @param fisrtAccess
	 */
	public void setFirstAccess(Date firstAccess) {
		this.firstAccess = firstAccess;
	}

	/**
	 * Devuelve la �ltima fecha de acceso a la plataforma
	 * 
	 * @return lastAccess
	 */
	public Date getLastAccess() {
		return this.lastAccess;
	}

	/**
	 * Modifica la �ltima fecha de acceso a la plataforma
	 * 
	 * @param lastAccess
	 */
	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	/**
	 * Devuelve la descripci�n del usuario
	 * 
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Modifica la descripci�n del usuario
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Devuelve el formato de la descripci�n
	 * 
	 * @return descriptionFormat
	 */
	public String getDescriptionFormat() {
		return this.descriptionFormat;
	}

	/**
	 * Modifica el formato de la descripci�n
	 * 
	 * @param descriptionFormat
	 */
	public void setDescriptionFormat(String descriptionFormat) {
		this.descriptionFormat = descriptionFormat;
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
	 * Devuelve el pa�s del usuario
	 * 
	 * @return country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * Modifica el pa�s del usuario
	 * 
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Devuelve la url de la foto de usuario en icono
	 * 
	 * @return profileImageUrlSmall
	 */
	public String getProfileImageUrlSmall() {
		return this.profileImageUrlSmall;
	}

	/**
	 * Modifica la url de la foto de usuario en icono
	 * 
	 * @param profileImageUrlSmall
	 */
	public void setProfileImageUrlSmall(String profileImageUrlSmall) {
		this.profileImageUrlSmall = profileImageUrlSmall;
	}

	/**
	 * Devuelve la url de la foto del usuario
	 * 
	 * @return profileImageUrl
	 */
	public String getProfileImageUrl() {
		return this.profileImageUrl;
	}

	/**
	 * Modifica la url de la foto del usuario
	 * 
	 * @param profileImageUrl
	 */
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	/**
	 * Devuelve la lista de roles que tiene el usuario
	 * 
	 * @return roles
	 */
	public ArrayList<Role> getRoles() {
		return this.roles;
	}

	/**
	 * Modifica la lista de roles que tiene el usuario
	 * 
	 * @param roles
	 */
	public void setRoles(ArrayList<Role> roles) {
		this.roles.clear();
		for (Role role : roles) {
			this.roles.add(role);
		}
	}

	/**
	 * Devuelve la lista de grupos en los que est� el usuario
	 * 
	 * @return groups
	 */
	public ArrayList<Group> getGroups() {
		return this.groups;
	}

	/**
	 * Modifica la lista de grupos en los que est� el usuario
	 * 
	 * @param groups
	 */
	public void setGroups(ArrayList<Group> groups) {
		this.groups.clear();
		for (Group group : groups) {
			this.groups.add(group);
		}
	}

	/**
	 * Devuelve la lista de cursos en los que est� matriculado el usuario
	 * 
	 * @return courses
	 */
	public ArrayList<Integer> getEnrolledCourses() {
		return this.courses;
	}

	/**
	 * Modifica la lista de cursos en los que est� matriculado el usuario
	 * 
	 * @param courses
	 */
	public void setEnrolledCourses(ArrayList<Integer> courses) {
		this.courses.clear();
		for (Integer course : courses) {
			this.courses.add(course);
		}
	}

	/**
	 * Convierte el EnrolledUser a un String con su nombre
	 */
	public String toString() {
		return this.getFullName();
	}
}
