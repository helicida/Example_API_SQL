package PreparedStatement;

import java.sql.*;

/**
 * Created by sergi on 24/11/15.
 */
public class selectSQLite {

    public static String archivoBaseDatos = theMovieDBProject.ficheroDB;
    static String peliculasTabla = theMovieDBProject.nombreTPeliculas;  // La tabla será similar al nombre que le otorgamos en el controlador
    static String actoresTabla = theMovieDBProject.nombreTActores; // La tabla será igual al nombre que le dimos en el controlador

    public static void listaPeliculas (){

        Connection conexion;
        Statement stmt;

        try{
            System.out.println("Lista de peliculas:"); //Imprimimos en pantalla la lista de peliculas
            System.out.println("--------------------------");

            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(archivoBaseDatos);
            conexion.setAutoCommit(false);

            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT ID, TITULO FROM '" + peliculasTabla + "';");

            while(rs.next()) {
                int idAPI = rs.getInt("ID");
                String titulo = rs.getString("TITULO");
                System.out.println("| " + idAPI + " | " + titulo);
            }

			rs.close();
			stmt.close();
			conexion.close();

        } catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        mensajeOK();    // Mostramos el mensaje de completado correctamente
    }

    public static void listaActores (){

        Connection conexion = null;
        Statement stmt = null;

        try{
            System.out.println("Lista de actores:"); //Imprimimos en pantalla la lista de peliculas
            System.out.println("--------------------------");

            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(archivoBaseDatos);
            conexion.setAutoCommit(false);

            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT DISTINCT ID_ACTOR, NOMBRE FROM " + actoresTabla);

            while(rs.next()) {
                int id = rs.getInt("ID_ACTOR");
                String nombre = rs.getString("NOMBRE");
                System.out.println("(" + id + ") - " + nombre);
            }

            rs.close();
            stmt.close();
            conexion.close();

        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        mensajeOK();    // Mostramos el mensaje de completado correctamente
    }


    public static void consultaPelis (int id){

        Connection conexion = null;
        Statement stmt = null;

        try{
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(archivoBaseDatos);
            conexion.setAutoCommit(false);

            stmt = conexion.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT * "
                    + "FROM '" + peliculasTabla + "' as peliculas, '" + actoresTabla +"' as actores "
                    + "WHERE peliculas.ID = actores.ID_PELICULA "
                    + "AND peliculas.ID = " + id);

            String titulo = rs.getString("TITULO");
            String fecha = rs.getString("FECHA");

            // Imprimimos en pantalla la pelicula con la fecha y sus personajes

            System.out.println("--------------------------------------");
            System.out.println("---- " + titulo + " | " + fecha + " ----");
            System.out.println("--------------------------------------");
            System.out.println("--            PERSONAJES            --");
            System.out.println("--------------------------------------");

            while(rs.next()){   // Mientras haya algo de los resultados que leer
                String personaje = rs.getString("PERSONAJE");   // Extraemos el personaje
                System.out.println(" - " + personaje);  // Y lo imprimimos en forma de lista
            }

            rs.close();
            stmt.close();
            conexion.close();

        } catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        mensajeOK();    // Mostramos el mensaje de completado correctamente
    }

    public static void consultaActores (int id){

        Connection conexion = null;
        Statement stmt = null;

        try{
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(archivoBaseDatos);
            conexion.setAutoCommit(false);
            stmt = conexion.createStatement();

            ResultSet rs = stmt.executeQuery( "SELECT NOMBRE, TITULO "
                    + "FROM '" + peliculasTabla + "' as peliculas, '" + actoresTabla + "' as actores "
                    + "WHERE peliculas.ID = actores.ID_PELICULA "
                    + "AND actores.ID_ACTOR = " + id);

            System.out.println("\n Peliculas del actor '" + rs.getString("NOMBRE") + "':");
            System.out.println("--------------------------------");

            while(rs.next()) {
                String titulo = rs.getString("TITULO");
                System.out.println("- " + titulo);
            }

            rs.close();
            stmt.close();
            conexion.close();

        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        mensajeOK();    // Mostramos el mensaje de completado correctamente
    }

    public static void mensajeOK(){
        System.out.println();
        System.out.println("Se han realizado las consultas correctamente");
        System.out.println();
    }
}