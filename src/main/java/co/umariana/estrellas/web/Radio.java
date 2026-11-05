package co.umariana.estrellas.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * GET /radio  (tarea de SERVLETS)
 *
 * <p>Contrato (las pruebas lo revisan):
 * <ul>
 *   <li>Parámetro {@code accion}: {@code "siguiente"} usa {@code Contexto.servicio().siguiente()},
 *       {@code "anterior"} usa {@code anterior()}; sin parámetro (o con otro valor) usa {@code actual()}.</li>
 *   <li>Pone el sencillo resultante (puede ser {@code null}) en el atributo {@code "sencillo"} y la cantidad
 *       total en {@code "total"}.</li>
 *   <li>Reenvía a {@code /WEB-INF/vistas/radio.jsp}, que debe mostrar el sencillo, un radio
 *       {@code <audio>} (los archivos están en {@code media/}) y los botones Anterior / Siguiente.</li>
 * </ul>
 */
@WebServlet("/radio")
public class Radio extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        co.umariana.estrellas.modelo.Sencillo sencillo;

        if ("siguiente".equals(accion)) {
            sencillo = Contexto.servicio().siguiente();
        } else if ("anterior".equals(accion)) {
            sencillo = Contexto.servicio().anterior();
        } else {
            sencillo = Contexto.servicio().actual();
        }

        req.setAttribute("sencillo", sencillo);
        req.setAttribute("total", Contexto.servicio().total());
        req.getRequestDispatcher("/WEB-INF/vistas/radio.jsp").forward(req, resp);
    }
}
