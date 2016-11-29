package model;

/**
 * Clase en la que se encuentran las funciones de servicios web a utilizar.
 * 
 * @author Claudia Martínez Herrero
 *
 */
public class MoodleOptions {
	// Devuelve token de usuario
	public static final String SERVICIO_WEB_MOODLE = "moodle_mobile_app";
	// Devuelve id de usuario y atributos
	public static final String OBTENER_ID_USUARIO = "core_user_get_users_by_field";
	// Devuelve los cursos en los que está matriculado el usuario
	public static final String OBTENER_CURSOS = "core_enrol_get_users_courses";
	// Devuelve los usuarios matriculados en un curso
	public static final String OBTENER_USUARIOS_MATRICULADOS = "core_enrol_get_enrolled_users";

	public static final String OBTENER_CONTENIDOS = "core_course_get_contents";
	public static final String OBTENER_INFO_USER = "core_user_get_users_by_field";
}
