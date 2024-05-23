package org.example;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.Deque;
import java.util.LinkedList;

public class Node {
    private int id;
    private String name;
    ArrayList<Node> children = new ArrayList<>(); /** создание дерева**/
    public Node(String name, int id) {
        this.name = name;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public int getId(){
        return id;
    }

    public void setName(String name) {
        this.name = this.name;
        return;
    }

    public void setId(){
        this.id = id;
        return;
    }

    /**добавление узла (дочернего) в дерево**/
    public void addNode(Node node2) {
        children.add(node2);
    }

    /**поиск дочернего узла по имени**/
    public Node searchNode(String name) {
        for (Node child : children) {
            if (child.getName().equals(name)) return child;
        }
        return null;
    }

    /**удаление дочернего узла по имени или идентификатору**/
    public void deleteNode(String name) {
        for (int i=0; i<children.size(); i++){
            if (children.get(i).getName().equals(name)){
                children.remove(i);
            }
        }

    }

    public void deleteAllNodes() {
        children.clear();
    }  /*удаление всех дочерних узлов*/

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    public void rename(String newName) {
        this.name = newName;
    } /* изменение значения(имени) узла*/

    /**Печать дерева **/
    public void printTree(Node node) {
        System.out.print("\nРодитель: " + node.getName() + ", дети: ");
        for (Node child : node.getChildren()) {
            System.out.print(child.getName() + ", ");
            printTree(child);
        }
    }

    /**Реализовать итерированиe дерева (Глубина дерева для теста - ).**/
    public static String readFile(String path, String filename) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path + filename))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        }
        catch (IOException e){
            System.err.println("Ошибка записи" + e.getMessage());
        }
        return null;
    }
    public Node textTree(String Data) {
        Node rootNode = null;
        Node existNode;
        Deque<Node> stack = new LinkedList<>();

        String[] lines = Data.split("\n");
        for (String line : lines) {
            int depth = getDepth(line);
            String name = line.trim();
            Node newNode = new Node(name, getId());

            if (rootNode == null) {
                rootNode = newNode;
                stack.push(rootNode);
            } else {
                while (stack.size() > depth) {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    existNode = stack.peek();
                    existNode.addNode(newNode);
                }
                stack.push(newNode);
            }
        }
        return rootNode;
    }
    private static int getDepth(String line) {
        int depth = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                depth++;
            } else {
                break;
            }
        }
        return depth / 2;
    }

    /**Вывод в HTML**/
    public void printTreeAsHtml(Node node, PrintWriter writer) {
        writer.println("<ul>");
        writer.println("<li>" + node.getName() + " (ID: " + node.getId() + ")</li>");
        if (!node.getChildren().isEmpty()) {
            writer.println("<ul>");
            for (Node child : node.getChildren()) {
                printTreeAsHtml(child, writer);
            }
            writer.println("</ul>");
        }
        writer.println("</ul>");
    }

    public void printTreeAsHtmlToFile(Node node, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printTreeAsHtml(node, printWriter);
            printWriter.close();
            System.out.println("Дерево успешно выведено в HTML файл: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл");
        }
    }

    public static void main(String[] args) {
        Node root = new Node("Root", 1);
        Node child1 = new Node("Child1", 2);
        Node child2 = new Node("Child2", 3);
        root.addNode(child1);
        root.addNode(child2);

        child1.addNode(new Node("Grandchild1", 4));
        child1.addNode(new Node("Grandchild2", 5));

        root.printTree(root);

        root.printTreeAsHtmlToFile(root, "tree.html");
    }
}