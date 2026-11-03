package co.umariana.estrellas.web;

import co.umariana.estrellas.estructura.ListaCircular;
import co.umariana.estrellas.modelo.GestorSencillos;
import co.umariana.estrellas.modelo.Sencillo;
import co.umariana.estrellas.persistencia.RepositorioTxt;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.file.Path;

/** Arma el modelo al arrancar el servidor (lista + archivo + gestor). NO se modifica. */
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        Path archivo = Path.of(System.getProperty("sencillos.archivo", "sencillos.txt"));
        try {
            Contexto.usar(new GestorSencillos(new ListaCircular<Sencillo>(), new RepositorioTxt(archivo)));
            evento.getServletContext().log("Estrellario listo. Datos en: " + archivo.toAbsolutePath());
        } catch (Throwable e) {
            // Mientras el equipo no termine sus clases, la aplicación arranca igual pero sin modelo.
            evento.getServletContext().log("El modelo todavía no está disponible: " + e);
            Contexto.usar(null);
        }
    }
}
