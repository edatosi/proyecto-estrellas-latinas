package co.umariana.estrellas.web;

import co.umariana.estrellas.modelo.Sencillo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * /registrar  (tarea de SERVLETS)
 *
 * <p>Contrato (las pruebas lo revisan):
 * <ul>
 *   <li><b>GET:</b> reenvía a {@code /WEB-INF/vistas/formulario.jsp}.</li>
 *   <li><b>POST:</b> lee los parámetros {@code codigo, titulo, categoria, brillo, duracion,
 *       descripcion, archivo}; crea un {@code Sencillo} (el brillo es un entero y la duración un
 *       decimal) y llama a {@code Contexto.servicio().agregar(...)}. Si todo sale bien, redirige a
 *       {@code "listar"} ({@code resp.sendRedirect("listar")}).</li>
 *   <li><b>Si algo falla</b> ({@link NumberFormatException} o {@link IllegalArgumentException}), NO redirige:
 *       pone el mensaje de la excepción en el atributo {@code "error"} y reenvía de nuevo a
 *       {@code formulario.jsp}, que debe mostrar ese error.</li>
 * </ul>
 */
@WebServlet("/registrar")
public class RegistrarSencillo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/vistas/formulario.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String codigo = req.getParameter("codigo");
            String titulo = req.getParameter("titulo");
            String categoria = req.getParameter("categoria");
            int brillo = Integer.parseInt(req.getParameter("brillo"));
            double duracion = Double.parseDouble(req.getParameter("duracion"));
            String descripcion = req.getParameter("descripcion");
            String archivo = req.getParameter("archivo");

            Sencillo sencillo = new Sencillo(codigo, titulo, categoria, brillo, duracion, descripcion, archivo);
            Contexto.servicio().agregar(sencillo);
            resp.sendRedirect("listar");
        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException || e instanceof NumberFormatException) {
                req.setAttribute("error", e.getMessage());
                req.getRequestDispatcher("/WEB-INF/vistas/formulario.jsp").forward(req, resp);
                return;
            }
            throw e;
        }
    }
}
