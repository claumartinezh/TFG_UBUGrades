package model;

import java.util.ArrayList;
import java.util.HashMap;

public class GradeReportConfigurationLine {
	// private ArrayList<Node> nodes;
	private int id;
	private String name;
	private int level;
	private float weight;
	private float rangeMin;
	private float rangeMax;
	private boolean type; // False = Category, True = Item
	private ArrayList<GradeReportConfigurationLine> children;
	/**
	 * Constructor de categorias
	 * Se le añaden a posteriori los elementos de su suma de calificaciones
	 * @param id
	 * @param name
	 * @param level
	 * @param type
	 */
	public GradeReportConfigurationLine(int id, String name, int level, boolean type){
		this.id = id;
		this.name = name;
		this.level = level;
		this.type = type;
		this.children = new ArrayList<GradeReportConfigurationLine>();
	}
	/**
	 * Constructor de items
	 * @param id
	 * @param name
	 * @param level
	 * @param type
	 * @param weight
	 * @param rangeMin
	 * @param rangeMax
	 */
	public GradeReportConfigurationLine(int id, String name, int level, boolean type, float weight, float rangeMin,
			float rangeMax) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.weight = weight;
		this.type = type;
		this.rangeMax = rangeMax;
		this.rangeMin = rangeMin;
		this.children = new ArrayList<GradeReportConfigurationLine>();
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

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setRangeMax(float rangeMax) {
		this.rangeMax = rangeMax;
	}

	public void setRangeMin(float rangeMin) {
		this.rangeMin = rangeMin;
	}

	public void setType(boolean type) {
		this.type = type;
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

	public float getRangeMax() {
		return this.rangeMax;
	}

	public float getRangeMin() {
		return this.rangeMin;
	}

	public boolean getType() {
		return this.type;
	}
	public void addChild(GradeReportConfigurationLine kid){
		this.children.add(kid);
	}
	public ArrayList<GradeReportConfigurationLine> getChildren(){
		return this.children;
	}

}
