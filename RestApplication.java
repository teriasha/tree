package ru.krista.yargu;

import org.example.Node;
import org.h2.store.Data;

import javax.ws.rs.core.Application;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RestApplication extends Application {
    private List<String> list = new ArrayList<>();
    private Node rootNode;

    public RestApplication() {
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        String Data = Node.readFile("", "tree.txt");
        if (Data == null) {
            rootNode = new Node("root", 0);
        } else {
            rootNode = Node.textTree(Data);
        }
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> resources = new HashSet<>();
        resources.add(new ru.krista.yargu.ListPresentationController(list));
        resources.add(new ru.krista.yargu.TreePresentationController(rootNode));
        return resources;
    }
}