package co.umariana.estrellas.web;

import co.umariana.estrellas.modelo.Sencillo;
import co.umariana.estrellas.modelo.ServicioSencillos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Servicio de mentira que anota lo que le piden: permite probar los servlets sin el resto del equipo. */
class ServicioFalso implements ServicioSencillos {

    final List<Sencillo> datos = new ArrayList<>();
    final List<Sencillo> ranking = new ArrayList<>();
    final Map<String, Double> promedios = new TreeMap<>();
    final List<String> llamadas = new ArrayList<>();
    RuntimeException fallarAlAgregar = null;
    Sencillo cursorActual = null;
    Sencillo cursorSiguiente = null;
    Sencillo cursorAnterior = null;

    @Override public void agregar(Sencillo l) {
        llamadas.add("agregar");
        if (fallarAlAgregar != null) throw fallarAlAgregar;
        datos.add(l);
    }
    @Override public Sencillo buscar(String codigo) { llamadas.add("buscar"); return null; }
    @Override public boolean eliminar(String codigo) { llamadas.add("eliminar:" + codigo); return true; }
    @Override public List<Sencillo> listar() { llamadas.add("listar"); return datos; }
    @Override public List<Sencillo> ranking() { llamadas.add("ranking"); return ranking; }
    @Override public Map<String, Double> promedioPorCategoria() { llamadas.add("promedios"); return promedios; }
    @Override public int total() { return datos.size(); }
    @Override public Sencillo actual() { llamadas.add("actual"); return cursorActual; }
    @Override public Sencillo siguiente() { llamadas.add("siguiente"); return cursorSiguiente; }
    @Override public Sencillo anterior() { llamadas.add("anterior"); return cursorAnterior; }
}
