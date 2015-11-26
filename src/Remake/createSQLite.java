package Remake;

import java.sql.*;

/**
 * Created by sergi on 24/11/15.
 */
public class createSQLite {

    public static void createDataBase() {

        try {
            Connection conexion = null;
            Statement stmt = null;

            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(theMovieDBProject.ficheroDB);
            stmt = conexion.createStatement();

            System.out.println("Se ha accedido correctamente a la base de datos");
            System.out.println("----------------------------------------------------");
            System.out.println("Se procederá a crear las tablas.");
            System.out.println("----------------------------------------------------");

            // Hacemos dos tablas una para peliculas y otra para actores

            // La de peliculas tendrá la ID de la pelicula en la API, el título de la pelicula y la fecha de salida
            String createPeliculas = "CREATE TABLE " + theMovieDBProject.nombreTPeliculas
                    + " (ID INT PRIMARY KEY     NOT NULL,"
                    + " TITULO CHAR(150),"
                    + " FECHA CHAR(20))";

            // La de acotres tendrá la ID del actor en la API, su nombre...
            String createActor = "CREATE TABLE " + theMovieDBProject.nombreTActores
                    + "(ID INT PRIMARY KEY NOT NULL,"
                    + " NOMBRE         CHAR(100),"
                    + " ID_ACTOR       INT,"
                    + " PERSONAJE      CHAR(100),"
                    + " ID_PELICULA    INT)";


            // Creamos las dos tablas con nuestros script SQL de arriba
            stmt.executeUpdate(createPeliculas);
            stmt.executeUpdate(createActor);

            // Cerramos las conexiones a la base de datos
            stmt.close();
            conexion.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Se han creado correctamente las tablas " + theMovieDBProject.nombreTPeliculas + " y " + theMovieDBProject.nombreTActores);
    }
}
