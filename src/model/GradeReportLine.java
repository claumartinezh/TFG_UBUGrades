package model;

import java.util.ArrayList;

/**
 * Clase GradeReportLine. Representa a una línea en el calificador de un alumno.
 * Cada línea se compone de su id, nombre, nivel, nota, porcentaje, peso, rango
 * y tipo principalmente.
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class GradeReportLine {
	// private ArrayList<Node> nodes;
	private int id;
	private String name;
	private int level;
	private float grade;
	private float percentage;
	private float weight;
	private String rangeMin;
	private String rangeMax;
	private boolean type; // False = Category, True = Item
	private String nameType;
	private ArrayList<GradeReportLine> children;
	private Activity activity;

	/**
	 * Constructor de categorias con 4 parámetros. Se le añaden a posteriori los
	 * elementos de su suma de calificaciones
	 * 
	 * @param id
	 * @param name
	 * @param level
	 * @param type
	 */
	public GradeReportLine(int id, String name, int level, boolean type) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.type = type;
		this.children = new ArrayList<GradeReportLine>();
	}

	/**
	 * Constructor de items con 7 parámetros.
	 * 
	 * @param id
	 * @param name
	 * @param level
	 * @param type
	 * @param weight
	 * @param rangeMin
	 * @param rangeMax
	 */
	// RMS changed types float to String in ranges
	public GradeReportLine(int id, String name, int level, boolean type, float weight, String rangeMin, String rangeMax,
			float grade, float percentage, String nameType) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.weight = weight;
		this.type = type;
		this.rangeMax = rangeMax;
		this.rangeMin = rangeMin;
		this.grade = grade;
		this.percentage = percentage;
		this.nameType = nameType;
		this.children = new ArrayList<GradeReportLine>();
	}

	/**
	 * Devuelve el id del GradeReportLine
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Modifica el id del GradeReportLine
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devuelve el nombre del GradeReportLine
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Modifica el nombre del GradeReportLine
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Devuelve el nivel del GradeReportLine en el árbol del calificador
	 * 
	 * @return level
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Modifica el nivel del GradeReportLine en el árbol del calificador
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Devuelve la nota del GradeReportLine
	 * 
	 * @return grade
	 */
	public float getGrade() {
		return grade;
	}

	/**
	 * Modifica la nota del GradeReportLine
	 * 
	 * @param grade
	 */
	public void setGrade(float grade) {
		this.grade = grade;
	}

	/**
	 * Devuelve el peso del GradeReportLine
	 * 
	 * @return weight
	 */
	public float getWeight() {
		return this.weight;
	}

	/**
	 * Modifica el peso del GradeReportLine
	 * 
	 * @param weight
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * Devuelve el rango máximo de nota
	 * 
	 * @return rangeMax
	 */
	// RMS changed
	public String getRangeMax() {
		// public float getRangeMax() {
		return this.rangeMax;
	}

	/**
	 * Modifica el rango máximo de nota
	 * 
	 * @param rangeMax
	 */
	// RMS changed
	public void setRangeMax(String rangeMax) {
		// public void setRangeMax(float rangeMax) {
		this.rangeMax = rangeMax;
	}

	/**
	 * Devuelve el rango mínimo de nota
	 * 
	 * @return rangeMin
	 */
	// RMS changed
	public String getRangeMin() {
		// public float getRangeMin() {
		return this.rangeMin;
	}

	/**
	 * Modifica el rango mínimo de nota
	 * 
	 * @param rangeMin
	 */
	// RMS changed
	public void setRangeMin(String rangeMin) {
		// public void setRangeMin(float rangeMin) {
		this.rangeMin = rangeMin;
	}

	/**
	 * Devuelve el porcentaje del GradeReportLine
	 * 
	 * @return percentage
	 */
	public float getPercentage() {
		return percentage;
	}

	/**
	 * Modifica el porcentaje del GradeReportLine
	 * 
	 * @param percentage
	 */
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	/**
	 * Devuelve el tipo (boolean) de GradeReportLine
	 * 
	 * @return type
	 */
	public boolean getType() {
		return this.type;
	}

	/**
	 * Modifica el tipo (boolean) del GradeReportLine
	 * 
	 * @param type
	 */
	public void setType(boolean type) {
		this.type = type;
	}

	/**
	 * Devuelve el tipo de GradeReportLine
	 * 
	 * @return nameType
	 */
	public String getNameType() {
		return this.nameType;
	}

	/**
	 * Modifica el tipo de GradeReportLine
	 * 
	 * @param nameType
	 */
	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	/**
	 * Devuelve la actividad asociada al GradeReportLine
	 * 
	 * @return activity
	 */
	public Activity getActivity() {
		return this.activity;
	}

	/**
	 * Crea uan actividad a partir del GradeReportLine
	 */
	public void setActivity() {
		this.activity = new Activity(name, nameType, weight, rangeMin, rangeMax);
	}

	/**
	 * Devuelve los hijos que tiene el GradeReportLine
	 * 
	 * @return children
	 */
	public ArrayList<GradeReportLine> getChildren() {
		return this.children;
	}

	/**
	 * Añade un hijo al GradeReportLine
	 * 
	 * @param kid
	 */
	public void addChild(GradeReportLine kid) {
		this.children.add(kid);
	}

	/**
	 * Convierte el GradeReportLine a un String con su nombre.
	 */
	public String toString() {
		return this.getName();
	}
}
