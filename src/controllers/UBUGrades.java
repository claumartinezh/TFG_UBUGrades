package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;

/**
 * Clase main. Inicializa la ventana de login
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class UBUGrades extends Application {
	public static String host = "";
	public static Stage stage;
	public static Session session;
	public static MoodleUser user;

	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println("Bienvenido a UBUGrades");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./../view/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage = primaryStage;

			stage.setScene(scene);
			stage.getIcons().add(new Image("./img/logo_min.png"));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Main comando

	/*
	 * public static void main(String[] args) throws Exception { String email =
	 * ""; String pass = ""; try { BufferedReader bufferRead = new
	 * BufferedReader(new InputStreamReader(System.in));
	 * System.out.println("Introduce email: "); email = bufferRead.readLine();
	 * 
	 * System.out.println("Introduce contraseña: "); pass =
	 * bufferRead.readLine(); host = args[0]; } catch (IOException e) {
	 * e.printStackTrace(); } Session session = new Session(email, pass);
	 * session.setToken(); System.out.println();
	 * System.out.println("El token de usuario es: ");
	 * System.out.println(session.getToken()); MoodleUser usuario = new
	 * MoodleUser(session.getToken(), email); //
	 * usuario.setCourses(session.getToken()); System.out.println();
	 * System.out.println("El id del usuario es: ");
	 * System.out.println(usuario.getId());
	 * System.out.println("  Cursos del usuario:"); for (int i = 0; i <
	 * usuario.getCourses().size(); i++) {
	 * System.out.println(usuario.getCourses().get(i).getShortName());
	 * System.out.println(" Nº Usuarios:" +
	 * usuario.getCourses().get(i).getEnrolledUsers().size()); for (int j = 0; j
	 * < usuario.getCourses().get(i).getEnrolledUsers().size(); j++) {
	 * System.out.println(" ->" +
	 * usuario.getCourses().get(i).getEnrolledUsers().get(j).getFullName()); } }
	 * }
	 */

}