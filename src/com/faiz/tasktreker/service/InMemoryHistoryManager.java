package com.faiz.tasktreker.service;

import com.faiz.tasktreker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node<T> {
        public T elem;
        public Node<T> next;
        public Node<T> prev;

        Node(Node<T> prev, T elem, Node<T> next) {
            this.elem = elem;
            this.next = next;
            this.prev = prev;
        }
    }

    HashMap<Integer, Node<Task>> history = new HashMap<>();

    Node<Task> first;
    Node<Task> last;

    @Override
    public void add(Task task) {
        Node<Task> node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = history.get(id);
        if (node != null) { // Защита от null указателя
            removeNode(node);
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    void linkLast(Task task) {
        final Node<Task> l = last;
        final Node<Task> newNode = new Node<>(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        history.put(task.getId(), newNode);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> returnList = new ArrayList<>();
        Node<Task> current = first; // Указывайте на Node<Task>
        while (current != null) {
            returnList.add(current.elem); // Добавляй элемент, а не узел
            current = current.next;
        }
        return returnList;
    }

    public void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }

        Node<Task> prevNode = node.prev;
        Node<Task> nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            first = nextNode; // Если это первый элемент
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            last = prevNode; // Если это последний элемент
        }
    }
}
