package model;

import java.util.Date;

import org.json.JSONObject;

public class Quiz extends Activity {
	private Date timeOpen;
	private Date timeClose;
	private String password;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Quiz(String token, JSONObject obj) throws Exception {
		super(token, obj);
		// TODO Auto-generated constructor stub
	}
}
