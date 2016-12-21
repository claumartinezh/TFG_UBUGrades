package model;

import java.util.Date;

import org.json.JSONObject;

public class Assignment extends Activity {
	private Date dueDate;
	private Date allowSubmissionsFromDate;
	public Date timeModified;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Assignment(String token, JSONObject obj) throws Exception {
		super(token, obj);
		// TODO Auto-generated constructor stub
	}

}
