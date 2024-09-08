<%@ include file="/init.jsp" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<!-- Formulario de búsqueda -->
<form action="<portlet:actionURL name='searchUsers'/>" method="POST">
    Nombre: <input type="text" name="searchName" value="<%= ParamUtil.getString(request, "searchName", "") %>"/>
    Apellido1: <input type="text" name="searchSurname1" value="<%= ParamUtil.getString(request, "searchSurname1", "") %>"/>
    Apellido2: <input type="text" name="searchSurname2" value="<%= ParamUtil.getString(request, "searchSurname2", "") %>"/>
    Email: <input type="text" name="searchEmail" value="<%= ParamUtil.getString(request, "searchEmail", "") %>"/>
    <input type="submit" value="Buscar"/>
</form>

<!-- Listado de usuarios -->
<table>
    <thead>
        <tr>
            <th>Id</th>
            <th>Nombre</th>
            <th>Apellido1</th>
            <th>Apellido2</th>
            <th>Email</th>
        </tr>
    </thead>
    <tbody>
        <%
            // Obtengo la lista de usuarios del atributo del request
            JSONArray users = (JSONArray) request.getAttribute("users");
            if (users != null) {
                // Itero sobre cada usuario en la lista y muestro los detalles en una fila de la tabla
                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = users.getJSONObject(i);
        %>
                    <tr>
                        <td><%= user.getInt("id") %></td>
                        <td><%= user.getString("name") %></td>
                        <td><%= user.getString("surname1") %></td>
                        <td><%= user.getString("surname2") %></td>
                        <td><%= user.getString("email") %></td>
                    </tr>
        <%
                }
            }
        %>
    </tbody>
</table>

<!-- Paginación -->
<liferay-ui:search-paginator
    total="<%= (Integer) request.getAttribute("total") %>"
    delta="<%= (Integer) request.getAttribute("pageSize") %>"
    cur="<%= (Integer) request.getAttribute("currentPage") %>"/>
