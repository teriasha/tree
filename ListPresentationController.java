package ru.krista.yargu;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path("/list")
public class ListPresentationController {
    private final List<String> list;

    /** @param list список, с которым будет работать контроллер.*/
    public ListPresentationController(List<String> list) {
        this.list = list;
    }

    @GET
    @Path("/")
    @Produces("text/html")
    public String getList() {
        StringBuilder html = new StringBuilder();
        html.append("<html>")
                .append("<head>")
                .append("<title>Вывод списка</title>")
                .append("</head>")
                .append("<body>")
                .append("<h1>Список</h1>")
                .append("<ul>");

        for (int i = 0; i < list.size(); i++) {
            String listItem = list.get(i);
            html.append("<li>").append(listItem).append(" <a href=\"edit/").append(i).append("\">Редактировать</a> </li>");
        }

        html.append("</ul>")
                .append("<br/>")
                .append("<form method=\"post\" action=\"addrandomitem\">")
                .append("<input type=\"submit\" value=\"Add random item\"/>")
                .append("</form>")
                .append("</body>")
                .append("</html>");

        return html.toString();
    }

    @GET
    @Path("/edit/{id}")
    @Produces("text/html")
    public String getEditPage(@PathParam("id") int itemId) {
        String listItem = list.get(itemId);
        StringBuilder html = new StringBuilder();
        html.append("<html>")
                .append("<head>")
                .append("<title>Редактирование элемента списка</title>")
                .append("</head>")
                .append("<body>")
                .append("<h1>Редактирование элемента списка</h1>")
                .append("<form method=\"post\" action=\"/edit/").append(itemId).append("\">")
                .append("<p>Значение</p>")
                .append("<input type=\"text\" name=\"value\" value=\"").append(listItem).append("\"/>")
                .append("<input type=\"submit\"/>")
                .append("</form>")
                .append("</body>")
                .append("</html>");

        return html.toString();
    }

    @POST
    @Path("/edit/{id}")
    @Produces("text/html")
    public Response editItem(@PathParam("id") int itemId, @FormParam("value") String itemValue) {
        list.set(itemId, itemValue);
        try {
            return Response.seeOther(new URI("/")).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Ошибка построения URI для перенаправления");
        }
    }

}

