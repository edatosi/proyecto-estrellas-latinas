package co.umariana.estrellas.persistencia;

import co.umariana.estrellas.modelo.Sencillo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda los sencillos en un archivo de texto (tarea de PERSISTENCIA).
 *
 * <p>Formato: UNA línea por sencillo, con los campos separados por {@code ;} y en este orden:
 * <pre>codigo;titulo;categoria;brillo;duracionSeg;descripcion;archivo</pre>
 * Ejemplo:
 * <pre>L001;Recall;Pop;2;1.32;Sencillo de ejemplo de todos los días;recall.mp3</pre>
 * <ul>
 *   <li>Codificación UTF-8 (para las tildes y la ñ).</li>
 *   <li>{@code duracionSeg} se escribe con punto decimal.</li>
 *   <li>{@code cargar}: si el archivo no existe devuelve una lista vacía; ignora las líneas en
 *       blanco y las líneas dañadas (cantidad de campos distinta de 7 o números inválidos).</li>
 *   <li>{@code guardar}: reemplaza todo el contenido del archivo y crea las carpetas que falten.
 *       Una lista vacía deja el archivo vacío.</li>
 * </ul>
 */
public class RepositorioTxt implements Repositorio {

    private final Path archivo;

    public RepositorioTxt(Path archivo) {
        this.archivo = archivo;
    }

    @Override
    public List<Sencillo> cargar() throws IOException {
        List<Sencillo> sencillos = new ArrayList<>();
        if (!Files.exists(archivo)) {
            return sencillos;
        }

        for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
            if (linea.isBlank()) {
                continue;
            }

            String[] campos = linea.split(";", -1);
            if (campos.length != 7) {
                continue;
            }

            try {
                int brillo = Integer.parseInt(campos[3]);
                double duracionSeg = Double.parseDouble(campos[4]);
                sencillos.add(new Sencillo(
                        campos[0], campos[1], campos[2], brillo, duracionSeg, campos[5], campos[6]));
            } catch (NumberFormatException ignorada) {
                // Una línea dañada no debe impedir cargar las demás.
            }
        }
        return sencillos;
    }

    @Override
    public void guardar(List<Sencillo> sencillos) throws IOException {
        Path carpeta = archivo.getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }

        List<String> lineas = sencillos.stream()
                .map(sencillo -> String.join(";",
                        sencillo.getCodigo(),
                        sencillo.getTitulo(),
                        sencillo.getCategoria(),
                        Integer.toString(sencillo.getBrillo()),
                        Double.toString(sencillo.getDuracionSeg()),
                        sencillo.getDescripcion(),
                        sencillo.getArchivo()))
                .toList();

        Files.write(archivo, lineas, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
    }
}
