package model;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * Clase Activity. Podrá ser Tarea, Quiz, Foro...
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class Activity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String itemName;
	private String activityType;
	private String level;
	private int weight;
	private int grade;
	private int minRange;
	private int maxRange;
	private int percentage;
	private String feedback;
	private int contributionCourseTotal;

	public Activity(String token, JSONObject obj) throws Exception {
		// TODO
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getActivityType() {
		return this.activityType;
	}

	public String getLevel() {
		return this.level;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getGrade() {
		return this.grade;
	}

	public int getMinRange() {
		return this.minRange;
	}

	public int getMaxRange() {
		return this.maxRange;
	}

	public int getPercentage() {
		return this.percentage;
	}

	public String getFeedback() {
		return this.feedback;
	}

	public int getContributionCourseTotal() {
		return this.contributionCourseTotal;
	}
}
