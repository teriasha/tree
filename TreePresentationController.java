package ru.krista.yargu;

import org.example.Node;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Контроллер для представления дерева.
 */
@Path("/")
public class TreePresentationController {
    private final Node rootNode;

    public TreePresentationController(Node rootNode) {
        this.rootNode = rootNode;
    }

    private String generateHTML(Node node, int level) {
        StringBuilder html = new StringBuilder();
        html.append("<tr><td style=\"padding-left: ").append(level * 20).append("px;\">").append(node.getName()).append("</td></tr>");
        if (!node.getChildren().isEmpty()) {
            for (Node child : node.getChildren()) {
                html.append(generateHTML(child, level + 1));
            }
        }
        return html.toString();
    }

    @GET
    @Path("/")
    @Produces("text/html")
    public String getTreePage() {
        return "<html>" +
                "  <head>" +
                "    <title>Дерево</title>" +
                "    <style>" +
                "      body { text-align: center; }" +
                "      table { margin: 0 auto; text-align: left; }" +
                "      h1, h2 { text-align: center; }" +
                "      form { display: inline-block; text-align: center; margin-bottom: 10px; }" +
                "      input[type=\"text\"], button { padding: 8px; font-size: 16px; border: 1px solid #000; border-radius: 5px; }" +
                "      button { background-color: #fff; color: #000; cursor: pointer; }" +
                "      button:hover { background-color: #f0f0f0; }" +
                "    </style>" +
                "  </head>" +
                "  <body>" +
                "    <h1>Дерево</h1>" +
                "    <table border=\"1\">" +
                generateHTML(rootNode, 0) +
                "    </table>" +
                "    <div style=\"margin-top: 20px;\">"+
                "      <h2>Действия над деревом:</h2>" +
                "      <div style=\"margin-bottom: 10px;\">" +
                "        <form method=\"post\" action=\"/add_node\">" +
                "          <input type=\"text\" placeholder=\"ID родителя\" name=\"parent_id\" required>\n" +
                "          <input type=\"text\" placeholder=\"Имя нового элемента\" name=\"name\" required>\n" +
                "          <button type=\"submit\">Добавить элемент</button>" +
                "        </form>" +
                "      </div>" +
                "      <div style=\"margin-bottom: 10px;\">" +
                "        <form method=\"post\" action=\"/edit_node\">" +
                "          <input type=\"text\" placeholder=\"ID элемента\" name=\"node_id\" required>\n" +
                "          <input type=\"text\" placeholder=\"Новое имя элемента\" name=\"name\" required>\n" +
                "          <button type=\"submit\">Изменить элемент</button>" +
                "        </form>" +
                "      </div>" +
                "      <div style=\"margin-bottom: 10px;\">" +
                "        <form method=\"post\" action=\"/delete_node\">" +
                "          <input type=\"text\" placeholder=\"ID элемента\" name=\"node_id\" required>\n" +
                "          <button type=\"submit\">Удалить элемент</button>" +
                "        </form>" +
                "      </div>" +
                "    </div>" +
                "  </body>" +
                "</html>";
    }

    @POST
    @Path("/add_node")
    @Produces("text/html")
    public Response addNodeToTree(@FormParam("parent_id") String parentId, @FormParam("name") String name) {
        Node parentNode = findNode(rootNode, parentId);
        if (parentNode != null) {
            Node newNode = new Node(name, parentNode.getId());
            parentNode.getChildren(newNode);
            writeToFile(rootNode, "tree.txt");

            return redirectTo();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Родительский узел не найден").build();
        }
    }

    private void writeToFile(Node rootNode, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(rootNode.printTree());
            System.out.println("Успешно" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/editnode")
    @Produces("text/html")
    public Response editNode(@FormParam("nodeid") String nodeId, @FormParam("name") String name) {
        Node node = findNode(rootNode, nodeId);
        if (node != null) {
            node.setName(name);
            writeToFile(rootNode, "tree.txt");
            return redirectTo();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Узел не найден").build();
        }
    }

    @POST
    @Path("/deletenode")
    @Produces("text/html")
    public Response deleteNode(@FormParam("nodeid") String nodeId) {
        Node node = findNode(rootNode, nodeId);
        if (node != null) {
            Node parentNode = findParentNode(rootNode, nodeId);
            if (parentNode != null) {
                parentNode.deleteNode(nodeId);
                writeToFile(rootNode, "tree.txt");
                return redirectTo();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Не удалось найти родительский узел").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Узел не найден").build();
        }
    }

    private Node findNode(Node currentNode, String id) {
        if (currentNode.getId() != null && currentNode.getId().equals(id)) {
            return currentNode;
        }
        for (Node child : currentNode.getChildren()) {
            Node foundNode = findNode(child, id);
            if (foundNode != null) {
                return foundNode;
            }
        }
        return null;
    }

    private Node findParentNode(Node currentNode, String id) {
        for (Node child : currentNode.getChildren()) {
            if (child.getId().equals(id)) {
                return currentNode;
            }
            Node parentNode = findParentNode(child, id);
            if (parentNode != null) {
                return parentNode;
            }
        }
        return null;
    }

    private Response redirectTo() {
        try {
            return Response.seeOther(new URI("/")).build();
        } catch (URISyntaxException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Ошибка перенаправления").build();
        }
    }
}