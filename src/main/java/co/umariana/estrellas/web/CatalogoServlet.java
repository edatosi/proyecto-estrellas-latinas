package co.umariana.estrellas.web;

import co.umariana.estrellas.modelo.Sencillo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Página del catálogo: muestra los audios que vienen en el proyecto y permite agregarlos al
 * Estrellario con un botón. Es la forma de tener datos para probar la aplicación.
 */
@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String ok = req.getParameter("ok");
        String error = req.getParameter("error");
        String contextPath = req.getContextPath();
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        out.println("<meta name=\"theme-color\" content=\"#080b19\">");
        out.println("<meta name=\"description\" content=\"Explora los audios disponibles y agrégalos a tu colección Estrellario.\">");
        out.println("<title>Catálogo | Estrellario</title>");
        out.println("<link rel=\"stylesheet\" href=\"" + Html.esc(contextPath) + "/css/estrellario.css\">");
        out.println("</head><body>");
        out.println("<header class=\"site-header\">");
        out.println("<a class=\"brand\" href=\"" + Html.esc(contextPath) + "/\"><span aria-hidden=\"true\">✦</span>Estrellario</a>");
        out.println("<nav class=\"site-nav\" aria-label=\"Navegación principal\">");
        out.println("<a href=\"" + Html.esc(contextPath) + "/catalogo\" aria-current=\"page\">Catálogo</a>");
        out.println("<a href=\"" + Html.esc(contextPath) + "/listar\">Sencillos</a>");
        out.println("<a href=\"" + Html.esc(contextPath) + "/radio\">Radio</a>");
        out.println("</nav></header>");
        out.println("<main class=\"page catalog-page\">");
        out.println("<section class=\"catalog-intro\">");
        out.println("<p class=\"eyebrow\">Descubre nuevos sonidos</p>");
        out.println("<h1>Catálogo de audios</h1>");
        out.println("<p class=\"lead\">Escucha cada sencillo y agrégalo a tu colección para que también suene en la radio.</p>");
        out.println("</section>");
        if (ok != null) {
            out.println("<p class=\"notice\" role=\"status\">✦ Se agregó " + Html.esc(ok) + " a tu colección.</p>");
        }
        if (error != null) {
            out.println("<p class=\"error\" role=\"alert\">" + Html.esc(error) + "</p>");
        }
        out.println("<section class=\"catalog-grid\" aria-label=\"Audios disponibles\">");
        for (Catalogo.Entrada e : Catalogo.entradas()) {
            Sencillo l = e.sencillo();
            boolean agregado = yaAgregado(l.getCodigo());
            out.println("<article class=\"catalog-card\">");
            out.println("<div class=\"catalog-card-heading\">");
            out.println("<span class=\"catalog-card-icon\" aria-hidden=\"true\">♫</span>");
            out.println("<span class=\"catalog-code\">" + Html.esc(l.getCodigo()) + "</span></div>");
            out.println("<h2>" + Html.esc(l.getTitulo()) + "</h2>");
            out.println("<div class=\"track-meta catalog-meta\">");
            out.println("<span>" + Html.esc(l.getCategoria()) + "</span>");
            out.println("<span>Brillo " + l.getBrillo() + "/10</span>");
            out.println("<span>" + l.getDuracionSeg() + " s</span>");
            out.println("</div>");
            out.println("<p class=\"catalog-description\">" + Html.esc(l.getDescripcion()) + "</p>");
            out.println("<p class=\"catalog-author\">Por <strong>" + Html.esc(e.autor()) + "</strong></p>");
            out.println("<audio controls preload=\"none\" aria-label=\"Escuchar " + Html.esc(l.getTitulo()) + "\">");
            out.println("<source src=\"" + Html.esc(contextPath) + "/media/" + Html.esc(l.getArchivo())
                    + "\" type=\"audio/mpeg\">Tu navegador no puede reproducir este audio.</audio>");
            out.println("<form method=\"post\" action=\"" + Html.esc(contextPath) + "/catalogo\">"
                    + "<input type=\"hidden\" name=\"codigo\" value=\""
                    + Html.esc(l.getCodigo()) + "\">"
                    + (agregado
                    ? "<button class=\"button-primary\" type=\"submit\" disabled>✓ Ya está en tu colección</button>"
                    : "<button class=\"button-primary\" type=\"submit\">＋ Agregar a mi colección</button>")
                    + "</form>");
            out.println("</article>");
        }
        out.println("</section></main>");
        out.println("<footer class=\"site-footer\">Estrellario <span aria-hidden=\"true\">✦</span> La radio de las estrellas latinas</footer>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Catalogo.Entrada entrada = Catalogo.buscar(req.getParameter("codigo"));
        if (entrada == null) {
            resp.sendRedirect("catalogo?error=" + codificar("Ese audio no está en el catálogo."));
            return;
        }
        try {
            Contexto.servicio().agregar(entrada.sencillo());
            resp.sendRedirect("catalogo?ok=" + codificar(entrada.sencillo().getTitulo()));
        } catch (UnsupportedOperationException | IllegalStateException e) {
            resp.sendRedirect("catalogo?error=" + codificar("Todavía falta implementar una parte: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            resp.sendRedirect("catalogo?error=" + codificar(e.getMessage()));
        }
    }

    private static boolean yaAgregado(String codigo) {
        try {
            return Contexto.servicio().buscar(codigo) != null;
        } catch (RuntimeException e) {
            return false; // el modelo aún no está listo: se deja el botón activo
        }
    }

    private static String codificar(String texto) {
        return URLEncoder.encode(String.valueOf(texto), StandardCharsets.UTF_8);
    }
}
