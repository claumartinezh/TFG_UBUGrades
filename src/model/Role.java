package model;

/**
 * Clase Role para distinguir el rol de los usuarios matriculados en un curso.
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class Role {
	private int id;
	private String name;
	private String shortName;

	public Role(int id, String name, String shortName) {
		// super();
		this.id = id;
		this.name = name;
		this.shortName = shortName;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
