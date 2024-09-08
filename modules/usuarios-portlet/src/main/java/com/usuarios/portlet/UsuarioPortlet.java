package com.usuarios.portlet;

import com.liferay.portal.kernel.json.JSONException;
import com.usuarios.constants.UsuarioPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Joseph
 */
@Component(
		property = {
				"com.liferay.portlet.display-category=category.sample",
				"com.liferay.portlet.header-portlet-css=/css/main.css",
				"com.liferay.portlet.instanceable=true",
				"javax.portlet.display-name=Usuario",
				"javax.portlet.init-param.template-path=/",
				"javax.portlet.init-param.view-template=/view.jsp",
				"javax.portlet.name=" + UsuarioPortletKeys.USUARIO,
				"javax.portlet.resource-bundle=content.Language",
				"javax.portlet.security-role-ref=power-user,user"
		},
		service = Portlet.class
)
public class UsuarioPortlet extends MVCPortlet {

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {

		// Obtengo los parámetros de busqueda desde la solicitud del usuario
		// Estos parámetros los uso para filtrar los resultados
		String searchName = ParamUtil.getString(renderRequest, "searchName", "");
		String searchSurname1 = ParamUtil.getString(renderRequest, "searchSurname1", "");
		String searchSurname2 = ParamUtil.getString(renderRequest, "searchSurname2", "");
		String searchEmail = ParamUtil.getString(renderRequest, "searchEmail", "");


		// Obtengo el numero de pagina actual y el tamaño de la pagina.
		// Si no me dan el número de página utilizo 1 por defecto.
		int page = ParamUtil.getInteger(renderRequest, "page", 1);
		int pageSize = 5;

		// Simulo la respuesta de la API con los datos de ejemplo
		// En una implementacion real aquí se haría una llamada HTTP a una API externa
		String jsonResponse = getUsersFromAPI(searchName, searchSurname1, searchSurname2, searchEmail, page, pageSize);

		//Intento parsear el JSON de la respuesta simulada
		//Si ocurre un error durante el parseo lanzo una excepción con un mensaje
		JSONObject jsonObject;
		try {
			jsonObject = JSONFactoryUtil.createJSONObject(jsonResponse);
		} catch (JSONException e) {
			throw new PortletException("Error al parsear el JSON", e);
		}

		// Guardo los datos para usarlo en la JSP, es decir seteo las cosas que voy a mostrar despues, por ejemplo el atributo users
		renderRequest.setAttribute("users", jsonObject.getJSONArray("usuarios"));
		renderRequest.setAttribute("total", jsonObject.getInt("total"));
		renderRequest.setAttribute("currentPage", page);
		renderRequest.setAttribute("pageSize", pageSize);

		// Llamo al método de la clase base para procesar la vista.
		super.doView(renderRequest, renderResponse);
	}

	//Método donde simula la respuesta de una API de usuarios que a su vez crea una lista de usuarios ficticios y los devuelve en JSON

	private String getUsersFromAPI(String searchName, String searchSurname1, String searchSurname2, String searchEmail, int page, int pageSize) {
		// Creo una lista de usuarios ficticios para simular la respuesta de una API.
		List<JSONObject> users = new ArrayList<>();

		//En este caso he agregado solo 20 usuarios
		for (int i = 1; i <= 20; i++) {
			JSONObject user = JSONFactoryUtil.createJSONObject();
			user.put("id", i);
			user.put("email", "admin" + i + "@yopmail.com");
			user.put("name", "admin" + i);
			user.put("surname1", "admin" + i);
			user.put("surname2", "admin" + i);
			users.add(user);
		}

		// Aplico la paginación a la lista de usuarios y determino que usuarios
		// mostrar en función del número de página y el tamaño de la página.
		int start = (page - 1) * pageSize;
		int end = Math.min(start + pageSize, users.size());

		// Creo un objeto JSON para la respuesta de la API, que va a incluir la pagina actual y el numero total de usuarios
		JSONObject response = JSONFactoryUtil.createJSONObject();
		response.put("usuarios", users.subList(start, end));
		response.put("total", users.size());

		return response.toString();
	}
}
