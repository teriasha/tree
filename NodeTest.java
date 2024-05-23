package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    Node node = new Node("node", 0);
    Node node1 = new Node("node1", 1);
    Node node2 = new Node("node2", 2);
    Node node3 = new Node("node3", 3);
    /**
     * Создание дерева
     */
    @Test
    void createNode(){
        assertNotNull(node, "Узла не существует");
        assertEquals("node1", node.getName(), "Ошибка создания узла");
        assertEquals(node.getId(), 1);
        assertEquals(node.getChildren().size(), 0);
    }

    /**
     * Добавление узла (дочернего) в дерево
     */
    @Test
    void addNodeTest(){
        node1.addNode(node2);
        assertTrue(node1.getChildren().contains(node2));
    }

    /**
    * Поиск дочернего узла по имени
    */
    @Test
    void searchNodeTest(){
        node1.addNode(node2);
        assertEquals(node1.searchNode("node2"), node2);
    }

    @Test
    void searchNoNodeTest(){
        node1.addNode(node2);
        assertEquals(node1.searchNode("node3"), null);
    }

    /**
     * Удаление дочернего узла по имени
     */
    @Test
    void deleteNodeTest(){
        node1.addNode(node2);
        node1.deleteNode("node2");
        assertFalse(node1.getChildren().contains(node2));
    }

    /**
     * Удаление несуществующего дочернего узла по имени
     */
    @Test
    void deleteFakeNodeTest(){
        node1.addNode(node2);
        ArrayList <Node> childrenBefore = node1.getChildren();
        node1.deleteNode("node3");
        assertEquals(node1.getChildren(), childrenBefore);
    }

    /**
     * Удаление всех дочерних узлов
     */
    @Test
    void deleteAllNodesTest(){
        node1.addNode(node2);
        node1.addNode(node3);
        node1.deleteAllNodes();
        assertEquals((node1.getChildren()).size(), 0);
    }

    /**
     * Изменение имени узла
     */
    @Test
    void renameNodeTest(){
        node.rename("not node");
        assertEquals(node.getName(), "not node");
    }
}
