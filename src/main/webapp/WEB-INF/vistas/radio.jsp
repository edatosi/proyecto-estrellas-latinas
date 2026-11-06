<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.umariana.estrellas.modelo.Sencillo, co.umariana.estrellas.web.Html" %>
<%
    Sencillo sencillo = (Sencillo) request.getAttribute("sencillo");
    Object total = request.getAttribute("total");
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Radio | Estrellario</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/estrellario.css">
</head>
<body>
<header class="site-header">
    <a class="brand" href="<%= contextPath %>/"><span>✦</span>Estrellario</a>
    <nav class="site-nav" aria-label="Navegación principal">
        <a href="<%= contextPath %>/catalogo">Catálogo</a>
        <a href="<%= contextPath %>/listar">Sencillos</a>
        <a href="<%= contextPath %>/registrar">Registrar</a>
    </nav>
</header>

<main class="page">
    <p class="eyebrow">Sonando en el Estrellario</p>
    <h1>La radio</h1>
    <p class="lead">Una secuencia circular de canciones: sigue escuchando y la música siempre vuelve a empezar.</p>

    <section class="track-card" aria-live="polite">
        <div class="track-icon" aria-hidden="true">♫</div>
        <% if (sencillo == null) { %>
            <h2>Tu radio está en silencio</h2>
            <p class="muted">Todavía no hay sencillos para reproducir. Agrega uno para encender la radio.</p>
            <p class="muted">Sencillos guardados: <%= total == null ? 0 : total %></p>
        <% } else { %>
            <p class="eyebrow">Reproduciendo</p>
            <h2><%= Html.esc(sencillo.getTitulo()) %></h2>
            <p class="track-meta">
                <span><%= Html.esc(sencillo.getCodigo()) %></span>
                <span><%= Html.esc(sencillo.getCategoria()) %></span>
                <span>Brillo <%= sencillo.getBrillo() %>/10</span>
                <span><%= sencillo.getDuracionSeg() %> s</span>
            </p>
            <% if (sencillo.getDescripcion() != null && !sencillo.getDescripcion().isBlank()) { %>
                <p class="muted"><%= Html.esc(sencillo.getDescripcion()) %></p>
            <% } %>
            <audio controls preload="metadata">
                <source src="<%= contextPath %>/media/<%= Html.esc(sencillo.getArchivo()) %>">
                Tu navegador no puede reproducir este audio.
            </audio>
            <p class="muted">Sencillos en la radio: <%= total == null ? 0 : total %></p>
        <% } %>

        <div class="radio-actions">
            <a class="button" href="<%= contextPath %>/radio?accion=anterior">← Anterior</a>
            <a class="button button-primary" href="<%= contextPath %>/radio?accion=siguiente">Siguiente →</a>
        </div>
        <% if (sencillo == null) { %>
            <div class="radio-actions">
                <a class="button" href="<%= contextPath %>/registrar">＋ Registrar un sencillo</a>
                <a class="button" href="<%= contextPath %>/catalogo">Explorar catálogo</a>
            </div>
        <% } %>
    </section>
</main>

<footer class="site-footer">Estrellario · La radio de las estrellas latinas</footer>
</body>
</html>
