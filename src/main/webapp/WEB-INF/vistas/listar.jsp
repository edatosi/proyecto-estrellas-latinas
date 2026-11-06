<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map, co.umariana.estrellas.modelo.Sencillo, co.umariana.estrellas.web.Html" %>
<%
    List<Sencillo> sencillos = (List<Sencillo>) request.getAttribute("sencillos");
    Map<String, Double> promedios = (Map<String, Double>) request.getAttribute("promedios");
    String contextPath = request.getContextPath();
    boolean ranking = "ranking".equals(request.getParameter("orden"));
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sencillos | Estrellario</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/estrellario.css">
</head>
<body>
<header class="site-header">
    <a class="brand" href="<%= contextPath %>/"><span>✦</span>Estrellario</a>
    <nav class="site-nav" aria-label="Navegación principal">
        <a href="<%= contextPath %>/catalogo">Catálogo</a>
        <a href="<%= contextPath %>/registrar">Registrar</a>
        <a href="<%= contextPath %>/radio">Radio</a>
    </nav>
</header>

<main class="page">
    <p class="eyebrow">Tu colección musical</p>
    <h1>Sencillos</h1>
    <p class="lead">Explora los audios guardados, consulta su brillo y revisa el promedio de cada categoría.</p>

    <section class="panel" aria-labelledby="tracks-heading">
        <div class="panel-heading">
            <h2 id="tracks-heading"><%= ranking ? "Ranking por brillo" : "Todos los sencillos" %></h2>
            <div class="site-nav">
                <a href="<%= contextPath %>/listar">Por orden de lista</a>
                <a href="<%= contextPath %>/listar?orden=ranking">Ver ranking</a>
                <a class="button-primary" href="<%= contextPath %>/registrar">＋ Agregar</a>
            </div>
        </div>

        <% if (sencillos == null || sencillos.isEmpty()) { %>
            <p class="empty-state">Aún no hay sencillos guardados. Agrega uno para empezar tu colección.</p>
        <% } else { %>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th scope="col">Sencillo</th>
                        <th scope="col">Categoría</th>
                        <th scope="col">Brillo</th>
                        <th scope="col">Duración</th>
                        <th scope="col">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Sencillo sencillo : sencillos) { %>
                        <tr>
                            <td>
                                <strong><%= Html.esc(sencillo.getTitulo()) %></strong>
                                <small><%= Html.esc(sencillo.getCodigo()) %></small>
                            </td>
                            <td><%= Html.esc(sencillo.getCategoria()) %></td>
                            <td><%= sencillo.getBrillo() %>/10</td>
                            <td><%= sencillo.getDuracionSeg() %> s</td>
                            <td>
                                <form class="inline-form" method="post" action="<%= contextPath %>/eliminar">
                                    <input type="hidden" name="codigo" value="<%= Html.esc(sencillo.getCodigo()) %>">
                                    <button type="submit" aria-label="Eliminar <%= Html.esc(sencillo.getTitulo()) %>">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </section>

    <section aria-labelledby="averages-heading">
        <div class="panel-heading">
            <h2 id="averages-heading">Promedios por categoría</h2>
        </div>
        <% if (promedios == null || promedios.isEmpty()) { %>
            <p class="empty-state">Los promedios aparecerán cuando agregues sencillos.</p>
        <% } else { %>
            <div class="averages">
                <% for (Map.Entry<String, Double> promedio : promedios.entrySet()) { %>
                    <article class="average-card">
                        <span><%= Html.esc(promedio.getKey()) %></span>
                        <strong><%= String.format(java.util.Locale.ROOT, "%.2f", promedio.getValue()) %> / 10</strong>
                    </article>
                <% } %>
            </div>
        <% } %>
    </section>
</main>

<footer class="site-footer">Estrellario · La radio de las estrellas latinas</footer>
</body>
</html>
