package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;
import webservice.Session;

/**
 * Clase main. Inicializa la ventana de login
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class UBUGrades extends Application {
	public static String host = "";
	public static Stage stage;
	public static Stage init;
	public static Session session;
	public static MoodleUser user;

	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println("[Bienvenido a UBUGrades]");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("./../view/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			// scene.getStylesheets().add(UBUGrades.class.getResource("/config/style.css").toExternalForm());
			init = primaryStage;
			init.setScene(scene);
			init.getIcons().add(new Image("./img/logo_min.png"));
			UBUGrades.init.setTitle("UBUGrades");
			init.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Main comando
	public static void main(String[] args) {
		launch(args);
	}
}