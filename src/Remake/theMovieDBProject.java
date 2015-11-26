package Remake;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by sergi on 24/11/15.
 */
public class theMovieDBProject {

    // Nombres de la base de datos y de las tablas
    public static String ficheroDB = "jdbc:sqlite:moviesDB_sbarjola.db";
    public static String nombreTPeliculas = "PELICULAS";
    public static String nombreTActores = "ACTORES";

    // Iteradores e identificadores de los actores, peliculas
    public static int idPeliculas = 801;    // A partir de que ID de las pelliculas vamos a leer
    public static int idActor = 1;  // ID interno de nuestra BBDD
    public static int cantidadPeliculas = 10;     // Y cuantas vamos a leer

    public static String apiKey = "a7ec645f7c4f6bffaab8a964820325f7"; // Key para acceder a la API

    public static void main(String[] args){

        Scanner teclat = new Scanner(System.in);  // Teclat

        String auxString = "";  // String auxiliar

        deleteDB(); // Nos cargamos la base de datos antigua
        createSQLite.createDataBase(); // Preparamos y creamos la tabla antes de insertar nada

        for (int iterador = 0; iterador < cantidadPeliculas; iterador++) {   // Insertamos los registros con el for

            int contadorPeliculas = idPeliculas + iterador; //
            String peliculaID = String.valueOf(contadorPeliculas);

            String direccionActores = "https://api.themoviedb.org/3/movie/" + peliculaID + "/credits?api_key=" + apiKey; // Dirección en la API que dirige a los actores de una pelicula
            String direccionPeliculas = "https://api.themoviedb.org/3/movie/" + peliculaID + "?api_key=" + apiKey;  // Dirección en la API que dirige a una pelicula

            try {
                auxString = getHTML(direccionPeliculas);    // Usamos nuestro string para obtener el json del HTML
                insertarJsonTPelis(auxString, contadorPeliculas); // Hacemos nuestra tabla de Peliculas insertando las peliculas que extraemos del JSON
                auxString = getHTML(direccionActores);  // Sacamos el otro Json de actores
                insertarJsonTActores(auxString, contadorPeliculas);   // Hacemos nuestra tabla de Actores insertando los actores extraidos del JSON
            } catch (Exception e) {
                System.out.println("La pelicula " + peliculaID + " no está disponoble o no existe " + e);  // Si falla, entonces la pelicula no está dipsonible
            }
        }

        System.out.println(" - " + cantidadPeliculas + " peliculas se han añadido correctamente \n"); // Una vez ha terminado se muestra el mensaje al finalizar

        int opcio = 0;  // Variable auxiliar

        while (true){

            // Menú per escollir quina consulta fer

            System.out.print("***************   ***********   *****       ***   ***     *** "
                    + "\n***************   ***********   *******     ***   ***     *** "
                    + "\n***   ***   ***   ****          ***  ***    ***   ***     *** "
                    + "\n***   ***   ***   ***********   ***   ***   ***   ***     *** "
                    + "\n***   ***   ***   ***********   ***    ***  ***   ***     *** "
                    + "\n***   ***   ***   ****          ***     *** ***   ***     *** "
                    + "\n***   ***   ***   ***********   ***      ******   *********** "
                    + "\n***   ***   ***   ***********   ***       *****   *********** ");

            System.out.println("\n \n ");

            // Mostramos las opciones de nuestro menú

            System.out.println("Escull una opció:");
            System.out.println("-------------------------------");
            System.out.println("1 - Consultar peliculas");
            System.out.println("2 - Consultar actors");

            opcio = teclat.nextInt();   //Demanem per pantalla en numero del menu

            switch (opcio){

                case 1:
                    selectSQLite.listaPeliculas();  // Hacemos una consulta para recibir la lista de peliculas

                    System.out.println("Escribe el ID de una de las peliculas"); // Pedimos el ID para buscar
                        int peliAMostrar = teclat.nextInt();

                    selectSQLite.consultaPelis(peliAMostrar);   // Mostramos los personajes de la pelicula seleccionada
                break;

                case 2:
                    selectSQLite.listaActores();    // Hacemos una consulta para recibir la lista de actores

                    System.out.println("Escribe la ID de uno de los actores");  // Pedimos el ID para buscar
                        int actorAMosrar = teclat.nextInt();

                    selectSQLite.consultaActores(actorAMosrar); // Mostramos las peliculas en las que aparece el actor seleccionado
                break;
            }
        }
    }

    public static void insertarJsonTPelis (String cadena, int id){

        Object jsonObj = JSONValue.parse(cadena);
        JSONObject jsonItem = (JSONObject)jsonObj;

        String titulo = (String) jsonItem.get("original_title");    // Obtenemos el titulo original
        titulo = formateoComillas(titulo);  // Tenemos cuidado con las comillas

        String fecha = (String) jsonItem.get("release_date");   // La fecha de lanzamiento

        insertSQLite.insertPelis(id, titulo, fecha);    // Insertamos la ID, el titulo
    }

    public static void insertarJsonTActores (String cadena, int idPelicula){

        Object jsonObj = JSONValue.parse(cadena);
        JSONObject jsonItem = (JSONObject)jsonObj;
        JSONArray casting = (JSONArray)jsonItem.get("cast");    // Obtenemos el reparto de la pelicula

        for (int iterador = 0; iterador < casting.size(); iterador++) { // Para cada actor
            JSONObject jo = (JSONObject)casting.get(iterador);            String nombre = (String) jo.get("name");    // Obtenemos el nombre
            nombre = formateoComillas(nombre);  // Tenemos cuidado con las comillas

            long actor = (long) jo.get("id");   // El id del actor
            String personaje = (String)jo.get("character");    // El mombre del personaje
            personaje = formateoComillas(personaje);

            insertSQLite.insertActores(idActor, nombre, actor, personaje, idPelicula);   //Insertamos en la tabla de actores todo lo extraido
            idActor++;   // Sumamos uno al iterador para que cada actor tenga un identificador único
        }
    }

    public static String getHTML(String urlToRead) throws Exception {

        // Leemos de una URL y obtenemos le JSON que devolvemos como string

        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));   // BufferReader para leer
        String line;    // String para leer linea a linea

        while ((line = rd.readLine()) != null) {    // Mientras haya algo que leer que vaya cogiendo...
            result.append(line); // Lo va añadiendo
        }

        rd.close(); // Se cierra el BufferedReader

        return result.toString();
    }

    public static String formateoComillas (String frase){

        // Las comillas y algunos caracteres que pueden hacer petar el programa. Lo paliamos con este método.

        String comillas = "\'";

        if (frase.contains(comillas)) {
            frase = frase.replace(comillas,"\"");
        }
        return frase;
    }

    public static void deleteDB(){

        File file = new File("moviesDB_sbarjola.db");   // Ruta del archivo de nuestra base de datos

        try{
            if(file.delete()){  // Lo eliminamos
                System.out.println(file.getName() + ": se ha eliminado la base de datos antigua");
            }
            else{   // Si no se ha podido eliminar
                System.out.println("No existe ninguna base de datos anterior o no se ha podido eliminar");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
