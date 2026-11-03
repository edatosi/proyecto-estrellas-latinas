package co.umariana.estrellas.web;

import co.umariana.estrellas.modelo.ServicioSencillos;

/**
 * Punto único de acceso al modelo desde los servlets. NO se modifica.
 * Los servlets hacen {@code Contexto.servicio().listar()}, etc.
 */
public final class Contexto {

    private static volatile ServicioSencillos servicio;

    private Contexto() {
    }

    public static ServicioSencillos servicio() {
        ServicioSencillos s = servicio;
        if (s == null) {
            throw new IllegalStateException("El modelo no está disponible todavía. Revise la consola del servidor: "
                    + "probablemente falta implementar alguna clase (GestorSencillos, ListaCircular o RepositorioTxt).");
        }
        return s;
    }

    /** Lo usa {@link AppListener} al arrancar y las pruebas para poner un servicio de mentira. */
    public static void usar(ServicioSencillos nuevo) {
        servicio = nuevo;
    }
}
