package co.umariana.estrellas.modelo;

import java.util.List;
import java.util.Map;

/**
 * Lo que la aplicación web necesita del modelo. NO se modifica.
 * {@link GestorSencillos} es la implementación (tarea de MODELO).
 */
public interface ServicioSencillos {

    /** Valida el sencillo, rechaza códigos repetidos y guarda el cambio en el archivo. */
    void agregar(Sencillo sencillo);

    /** Devuelve el sencillo con ese código, o {@code null} si no existe. */
    Sencillo buscar(String codigo);

    /** Elimina el sencillo con ese código y guarda el cambio. Devuelve {@code false} si no existía. */
    boolean eliminar(String codigo);

    /** Todos los sencillos, en el orden de la lista. */
    List<Sencillo> listar();

    /** Lista NUEVA ordenada de mayor a menor brillo (empate: por código). No altera el orden de la lista. */
    List<Sencillo> ranking();

    /** Promedio de brillo por categoría, ordenado alfabéticamente por categoría. */
    Map<String, Double> promedioPorCategoria();

    /** Cantidad de sencillos guardados. */
    int total();

    /** Sencillo donde está el cursor (la radio), o {@code null} si no hay sencillos. */
    Sencillo actual();

    /** Mueve el cursor al siguiente sencillo y lo devuelve ({@code null} si no hay sencillos). */
    Sencillo siguiente();

    /** Mueve el cursor al sencillo anterior y lo devuelve ({@code null} si no hay sencillos). */
    Sencillo anterior();
}
