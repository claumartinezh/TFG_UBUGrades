package model;

import java.util.ArrayList;

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
	 * Constructor de categorias Se le añaden a posteriori los elementos de su
	 * suma de calificaciones
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
	 * Constructor de items
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

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setGrade(float grade) {
		this.grade = grade;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	// RMS changed
	public void setRangeMax(String rangeMax) {
		// public void setRangeMax(float rangeMax) {
		this.rangeMax = rangeMax;
	}

	// RMS changed
	public void setRangeMin(String rangeMin) {
		// public void setRangeMin(float rangeMin) {
		this.rangeMin = rangeMin;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getLevel() {
		return this.level;
	}

	public float getWeight() {
		return this.weight;
	}

	// RMS changed
	public String getRangeMax() {
		// public float getRangeMax() {
		return this.rangeMax;
	}

	// RMS changed
	public String getRangeMin() {
		// public float getRangeMin() {
		return this.rangeMin;
	}

	public boolean getType() {
		return this.type;
	}

	public String getNameType() {
		return this.nameType;
	}

	public float getPercentage() {
		return percentage;
	}

	public float getGrade() {
		return grade;
	}

	public void addChild(GradeReportLine kid) {
		this.children.add(kid);
	}

	public ArrayList<GradeReportLine> getChildren() {
		return this.children;
	}

	public String toString() {
		return this.getName();
	}
}
