package co.umariana.estrellas.modelo;

import co.umariana.estrellas.estructura.Lista;
import co.umariana.estrellas.persistencia.Repositorio;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Comparator;
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
        sencillo.validar();
        if (buscar(sencillo.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un sencillo con el código " + sencillo.getCodigo() + ".");
        }
        lista.agregar(sencillo);
        guardar();
    }

    @Override
    public Sencillo buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Sencillo sencillo : lista) {
            if (codigo.equals(sencillo.getCodigo())) {
                return sencillo;
            }
        }
        return null;
    }

    @Override
    public boolean eliminar(String codigo) {
        int indice = 0;
        for (Sencillo sencillo : lista) {
            if (codigo != null && codigo.equals(sencillo.getCodigo())) {
                lista.eliminar(indice);
                guardar();
                return true;
            }
            indice++;
        }
        return false;
    }

    @Override
    public List<Sencillo> listar() {
        List<Sencillo> sencillos = new ArrayList<>(lista.tamano());
        for (Sencillo sencillo : lista) {
            sencillos.add(sencillo);
        }
        return sencillos;
    }

    @Override
    public List<Sencillo> ranking() {
        List<Sencillo> ranking = listar();
        ranking.sort(Comparator.comparingInt(Sencillo::getBrillo)
                .reversed()
                .thenComparing(Sencillo::getCodigo));
        return ranking;
    }

    @Override
    public Map<String, Double> promedioPorCategoria() {
        Map<String, double[]> acumulados = new TreeMap<>();
        for (Sencillo sencillo : lista) {
            double[] acumulado = acumulados.computeIfAbsent(sencillo.getCategoria(), categoria -> new double[2]);
            acumulado[0] += sencillo.getBrillo();
            acumulado[1]++;
        }

        Map<String, Double> promedios = new TreeMap<>();
        for (Map.Entry<String, double[]> entrada : acumulados.entrySet()) {
            double[] acumulado = entrada.getValue();
            promedios.put(entrada.getKey(), acumulado[0] / acumulado[1]);
        }
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
        lista.avanzar();
        return lista.actual();
    }

    @Override
    public Sencillo anterior() {
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
