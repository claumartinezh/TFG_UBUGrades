package webservice;

/**
 * Clase que recoge las funciones de servicios web a utilizar.
 * 
 * @author Claudia Mart�nez Herrero
 * @version 1.0
 *
 */
public class MoodleOptions {
	// Devuelve token de usuario
	public static final String SERVICIO_WEB_MOODLE = "moodle_mobile_app";
	// Devuelve id de usuario y atributos
	public static final String OBTENER_INFO_USUARIO = "core_user_get_users_by_field";
	// Devuelve los cursos en los que est� matriculado el usuario
	public static final String OBTENER_CURSOS = "core_enrol_get_users_courses";
	// Devuelve los usuarios matriculados en un curso
	public static final String OBTENER_USUARIOS_MATRICULADOS = "core_enrol_get_enrolled_users";
	// Devuelve todas las categor�as y elementos evaluables de un curso
	public static final String OBTENER_TABLA_NOTAS = "gradereport_user_get_grades_table";
	// Devuelve informaci�n sobre los assignments (tareas) que hay en un curso
	public static final String OBTENER_ASSIGNMENTS = "mod_assign_get_assignments";
	// Devuelve informaci�n sobre los quizs (cuestionarios) que hay en un curso
	public static final String OBTENER_QUIZZES = "mod_quiz_get_quizzes_by_courses";
}
