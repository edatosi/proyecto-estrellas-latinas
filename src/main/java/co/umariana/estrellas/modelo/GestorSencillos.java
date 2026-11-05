package co.umariana.estrellas.modelo;

import co.umariana.estrellas.estructura.Lista;
import co.umariana.estrellas.persistencia.Repositorio;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Reglas de negocio del Estrellario (tarea de MODELO).
 *
 * <p>Guarda los sencillos en una {@link Lista} (la estructura de datos del equipo) y, después de
 * cada cambio, los guarda con el {@link Repositorio}. No debe usar {@code ArrayList} ni otra
 * colección de Java para guardar los datos: eso lo hace la {@code Lista}.
 */
public class GestorSencillos implements ServicioSencillos {

    private final Lista<Sencillo> lista;
    private final Repositorio repositorio;

    /**
     * Al crearse, carga con el repositorio los sencillos ya guardados y los agrega a la lista.
     * Si el repositorio lanza {@link java.io.IOException}, se relanza como {@link java.io.UncheckedIOException}.
     */
    public GestorSencillos(Lista<Sencillo> lista, Repositorio repositorio) {
        this.lista = lista;
        this.repositorio = repositorio;
        try {
            for (Sencillo sencillo : repositorio.cargar()) {
                lista.agregar(sencillo);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void agregar(Sencillo sencillo) {
        if (sencillo == null) {
            throw new IllegalArgumentException("El sencillo no puede ser nulo.");
        }
        sencillo.validar();
        if (buscar(sencillo.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un sencillo con ese código.");
        }
        lista.agregar(sencillo);
        guardar();
    }

    @Override
    public Sencillo buscar(String codigo) {
        if (codigo == null) return null;
        for (Sencillo sencillo : lista) {
            if (codigo.equals(sencillo.getCodigo())) return sencillo;
        }
        return null;
    }

    @Override
    public boolean eliminar(String codigo) {
       if (codigo == null) return false;
        for (int i = 0; i < lista.tamano(); i++) {
            if (codigo.equals(lista.obtener(i).getCodigo())) {
                lista.eliminar(i);
                guardar();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Sencillo> listar() {
       List<Sencillo> resultado = new ArrayList<>();
        for (Sencillo sencillo : lista) resultado.add(sencillo);
        return resultado;
    }

    @Override
    public List<Sencillo> ranking() {
       List<Sencillo> resultado = listar();
        resultado.sort(Comparator.comparingInt(Sencillo::getBrillo).reversed()
                .thenComparing(Sencillo::getCodigo));
        return resultado;
    }

    @Override
    public Map<String, Double> promedioPorCategoria() {
        Map<String, int[]> acumulados = new TreeMap<>();
        for (Sencillo sencillo : lista) {
            int[] valores = acumulados.computeIfAbsent(sencillo.getCategoria(), k -> new int[2]);
            valores[0] += sencillo.getBrillo();
            valores[1]++;
        }
        Map<String, Double> promedios = new LinkedHashMap<>();
        acumulados.forEach((categoria, valores) ->
                promedios.put(categoria, (double) valores[0] / valores[1]));
        return promedios;
       
    }

    @Override
    public int total() {
        return lista.tamano();
    }

    @Override
    public Sencillo actual() {
       return lista.actual();
    }

    @Override
    public Sencillo siguiente() {
       if (lista.estaVacia()) return null;
        lista.avanzar();
        return lista.actual();
    }

    @Override
    public Sencillo anterior() {
       if (lista.estaVacia()) return null;
        lista.retroceder();
        return lista.actual();
    }

    private void guardar() {
        try {
            repositorio.guardar(listar());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    
    }
}
