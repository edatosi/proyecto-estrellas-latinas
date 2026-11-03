package co.umariana.estrellas.persistencia;

import co.umariana.estrellas.modelo.Sencillo;

import java.io.IOException;
import java.util.List;

/** Contrato de la persistencia. NO se modifica. {@link RepositorioTxt} es la implementación. */
public interface Repositorio {

    /** Lee todos los sencillos guardados. Si todavía no hay nada guardado, devuelve una lista vacía. */
    List<Sencillo> cargar() throws IOException;

    /** Guarda TODOS los sencillos, reemplazando lo que hubiera antes. */
    void guardar(List<Sencillo> sencillos) throws IOException;
}
