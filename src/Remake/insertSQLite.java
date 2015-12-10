package Remake;

import java.sql.*;

/**
 * Created by sergi on 24/11/15.
 */
public class insertSQLite {

    private static String archivoDB = theMovieDBProject.ficheroDB;

    public static void insertPelis(int id, String titulo, String fecha) {

        Connection conexion = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(archivoDB);
            conexion.setAutoCommit(false);
            stmt = conexion.createStatement();

            // Hacemos nuestra consulta para insertar pelicula con los parámetros recibidos
            String sql = "INSERT INTO " + theMovieDBProject.nombreTPeliculas+" (ID,TITULO,FECHA) "
                    +"VALUES (" + id + ",'" + titulo + "','" + fecha + "');";

            stmt.executeUpdate(sql);     // Insertamos pelicula a pelicula en su tabla

            stmt.close();
            conexion.commit();
            conexion.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("- Se ha insertado '" + titulo  + "' correctamente");
    }

    public static void insertActores(int id, String nombre, long actor, String personaje, int idPeli) {

        Connection conexion = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(archivoDB);
            conexion.setAutoCommit(false);
            stmt = conexion.createStatement();

            // Hacemos nuestra consulta para insertar actores con los parámetros recibidos
            String sql = "INSERT INTO " + theMovieDBProject.nombreTActores + " (ID,NOMBRE,ID_ACTOR,PERSONAJE,ID_PELICULA) "
                    +"VALUES (" + id + ",'" + nombre + "'," + actor + ",'" + personaje + "'," + idPeli + ");";

            stmt.executeUpdate(sql);     // Insertamos pelicula a pelicula en su tabla

            stmt.close();
            conexion.commit();
            conexion.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + e.getClass().getMethods() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println(" · Actor '" + nombre + "' añadido correctamente");
    }
}