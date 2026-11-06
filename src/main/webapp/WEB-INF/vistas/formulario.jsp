<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="co.umariana.estrellas.web.Html" %>
<%
    String contextPath = request.getContextPath();
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registrar sencillo | Estrellario</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/estrellario.css">
</head>
<body>
<header class="site-header">
    <a class="brand" href="<%= contextPath %>/"><span>✦</span>Estrellario</a>
    <nav class="site-nav" aria-label="Navegación principal">
        <a href="<%= contextPath %>/catalogo">Catálogo</a>
        <a href="<%= contextPath %>/listar">Sencillos</a>
        <a href="<%= contextPath %>/radio">Radio</a>
    </nav>
</header>

<main class="page form-page">
    <div class="form-intro">
        <div>
            <p class="eyebrow">Una nueva voz en tu colección</p>
            <h1>Haz espacio para una estrella.</h1>
            <p class="lead">Agrega un sencillo al Estrellario y déjalo sonar en la radio. Completa sus datos para que todos puedan descubrirlo.</p>
        </div>
        <aside class="form-aside" aria-label="Consejo para completar el formulario">
            <span class="form-aside-icon" aria-hidden="true">✦</span>
            <p class="form-aside-title">Un buen comienzo</p>
            <p>Asigna un código único, elige su categoría y dale un nivel de brillo del 1 al 10.</p>
            <span class="form-aside-caption">La música siempre encuentra su lugar.</span>
        </aside>
    </div>

    <section class="form-card" aria-labelledby="form-heading">
        <div class="form-card-heading">
            <div>
                <p class="eyebrow">Nuevo sencillo</p>
                <h2 id="form-heading">Detalles del audio</h2>
            </div>
            <span class="required-note"><span aria-hidden="true">*</span> Campos obligatorios</span>
        </div>

        <% if (error != null && !error.isBlank()) { %>
            <p class="error" role="alert"><strong>No se pudo guardar.</strong> <%= Html.esc(error) %></p>
        <% } %>
        <form method="post" action="<%= contextPath %>/registrar">
            <div class="form-grid">
                <div class="field">
                    <label for="codigo">Código <span aria-hidden="true">*</span></label>
                    <input id="codigo" name="codigo" type="text" autocomplete="off" placeholder="Ej. L004" required value="<%= Html.esc(request.getParameter("codigo")) %>">
                    <small>Debe ser único en tu colección.</small>
                </div>
                <div class="field">
                    <label for="titulo">Título <span aria-hidden="true">*</span></label>
                    <input id="titulo" name="titulo" type="text" placeholder="Nombre del sencillo" required value="<%= Html.esc(request.getParameter("titulo")) %>">
                </div>
                <div class="field">
                    <label for="categoria">Categoría <span aria-hidden="true">*</span></label>
                    <input id="categoria" name="categoria" type="text" placeholder="Ej. Pop latino" required value="<%= Html.esc(request.getParameter("categoria")) %>">
                </div>
                <div class="field">
                    <label for="brillo">Nivel de brillo <span aria-hidden="true">*</span></label>
                    <div class="input-with-suffix">
                        <input id="brillo" name="brillo" type="number" min="1" max="10" step="1" placeholder="1–10" required value="<%= Html.esc(request.getParameter("brillo")) %>">
                        <span>/ 10</span>
                    </div>
                    <small>¿Qué tanto ilumina este sencillo?</small>
                </div>
                <div class="field">
                    <label for="duracion">Duración <span aria-hidden="true">*</span></label>
                    <div class="input-with-suffix">
                        <input id="duracion" name="duracion" type="number" min="0.01" step="any" placeholder="Ej. 3.25" required value="<%= Html.esc(request.getParameter("duracion")) %>">
                        <span>seg</span>
                    </div>
                </div>
                <div class="field">
                    <label for="archivo">Archivo de audio <span aria-hidden="true">*</span></label>
                    <input id="archivo" name="archivo" type="text" placeholder="cancion.mp3" required value="<%= Html.esc(request.getParameter("archivo")) %>">
                    <small>Escribe el nombre del archivo de audio.</small>
                </div>
                <div class="field field-wide">
                    <label for="descripcion">Descripción <span class="optional-label">(opcional)</span></label>
                    <textarea id="descripcion" name="descripcion" rows="4" placeholder="Cuéntanos algo sobre este sencillo..."><%= Html.esc(request.getParameter("descripcion")) %></textarea>
                </div>
            </div>
            <div class="form-actions">
                <a class="button" href="<%= contextPath %>/listar">Cancelar</a>
                <button class="button-primary" type="submit"><span aria-hidden="true">✦</span> Guardar sencillo</button>
            </div>
        </form>
    </section>
</main>

<footer class="site-footer">Estrellario · La radio de las estrellas latinas</footer>
</body>
</html>
