package ru.itmo.tpo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class HashTable {

    static class Node {
        String key;
        String value;
        Node(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int size;
    private final List<LinkedList<Node>> table;
    private int elementCount;

    public HashTable(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер таблицы должен быть положительным");
        }
        this.size = size;
        this.table = new ArrayList<>(size);
        this.elementCount = 0;
        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<>());
        }
    }

    int calculateHashIndex(String key) {
        if (key == null || key.isEmpty()) return 0;
        int hash = 0;
        for (char c : key.toCharArray()) {
            hash = (hash << 4) + c;
            int high = hash & 0xF0000000;
            if (high != 0) {
                hash ^= high >>> 24;
            }
            hash &= ~high;
        }
        return Math.abs(hash % size);
    }

    public void insert(String key, String value) {
        onOperationStart();
        int index = calculateHashIndex(key);
        LinkedList<Node> bucket = table.get(index);
        onSearchingInChain();

        for (Node node : bucket) {
            if (Objects.equals(node.key, key)) {
                onKeyFound();
                node.value = value;
                onKeyUpdated();
                onOperationComplete();
                return;
            }
        }

        onKeyNotFound();
        bucket.add(new Node(key, value));
        elementCount++;
        onKeyInserted();
        onOperationComplete();
    }

    public String search(String key) {
        onOperationStart();
        int index = calculateHashIndex(key);
        LinkedList<Node> bucket = table.get(index);
        onSearchingInChain();

        for (Node node : bucket) {
            if (Objects.equals(node.key, key)) {
                onKeyFound();
                onOperationComplete();
                return node.value;
            }
        }

        onKeyNotFound();
        onOperationComplete();
        return null;
    }

    public boolean delete(String key) {
        onOperationStart();
        int index = calculateHashIndex(key);
        LinkedList<Node> bucket = table.get(index);
        onSearchingInChain();

        Node toRemove = null;
        for (Node node : bucket) {
            if (Objects.equals(node.key, key)) {
                onKeyFound();
                toRemove = node;
                break;
            }
        }

        if (toRemove != null) {
            bucket.remove(toRemove);
            elementCount--;
            onKeyDeleted();
            onOperationComplete();
            return true;
        }

        onKeyNotFound();
        onOperationComplete();
        return false;
    }

    void onOperationStart() {}
    void onSearchingInChain() {}
    void onKeyFound() {}
    void onKeyNotFound() {}
    void onKeyInserted() {}
    void onKeyUpdated() {}
    void onKeyDeleted() {} // Добавили новый маркер для удаления
    void onOperationComplete() {}

    public int getElementCount() {
        return elementCount;
    }
}


//package ru.itmo.tpo;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Objects;
//
//public class HashTable {
//
//    public enum TracingPoints {
//        OPERATION_START,
//        HASH_CALCULATED,
//        SEARCHING_IN_CHAIN,
//        KEY_FOUND,
//        KEY_NOT_FOUND,
//        KEY_INSERTED,
//        KEY_UPDATED,
//        KEY_DELETED,
//        OPERATION_COMPLETE
//    }
//
//    static class Node {
//        String key;
//        String value;
//
//        Node(String key, String value) {
//            this.key = key;
//            this.value = value;
//        }
//    }
//
//    private final int size;
//    private final List<LinkedList<Node>> table;
//    private int elementCount;
//
//    // Для отслеживания характерных точек
//    private boolean traceEnabled = false;
//    private final List<TracingPoints> trace = new ArrayList<>();
//
//    public HashTable(int size) {
//        this.size = size;
//        this.table = new ArrayList<>(size);
//        this.elementCount = 0;
//
//        for (int i = 0; i < size; i++) {
//            table.add(new LinkedList<>());
//        }
//    }
//
//    public void enableTrace() {
//        this.traceEnabled = true;
//        this.trace.clear();
//    }
//
//    public void disableTrace() {
//        this.traceEnabled = false;
//    }
//
//    public List<TracingPoints> getTrace() {
//        return new ArrayList<>(trace);
//    }
//
//    private void addPoint(TracingPoints point) {
//        if (traceEnabled) {
//            trace.add(point);
//        }
//    }
//
//    public int hashCode(String key) {
//        if (key == null || key.isEmpty()) {
//            return 0;
//        }
//
//        int hash = 0;
//
//        for (char c : key.toCharArray()) {
//            hash = hash << 4;
//            hash += c;
//
//            int high = hash & 0xF0000000;
//            hash ^= high >>> 12;
//        }
//
//        return hash;
//    }
//
//    public int hash(String key) {
//        int rawHash = hashCode(key);
//        int index = Math.abs(rawHash % size);
//        addPoint(TracingPoints.HASH_CALCULATED);
//        return index;
//    }
//
//    public void insert(String key, String value) {
//        addPoint(TracingPoints.OPERATION_START);
//
//        int index = hash(key);
//
//        LinkedList<Node> bucket = table.get(index);
//        addPoint(TracingPoints.SEARCHING_IN_CHAIN);
//
//        for (Node node : bucket) {
//            if (Objects.equals(node.key, key)) {
//                addPoint(TracingPoints.KEY_FOUND);
//                node.value = value;
//                addPoint(TracingPoints.KEY_UPDATED);
//                addPoint(TracingPoints.OPERATION_COMPLETE);
//                return;
//            }
//        }
//
//        addPoint(TracingPoints.KEY_NOT_FOUND);
//        bucket.add(new Node(key, value));
//        elementCount++;
//        addPoint(TracingPoints.KEY_INSERTED);
//        addPoint(TracingPoints.OPERATION_COMPLETE);
//    }
//
//    public String search(String key) {
//        addPoint(TracingPoints.OPERATION_START);
//
//        int index = hash(key);
//
//        LinkedList<Node> bucket = table.get(index);
//        addPoint(TracingPoints.SEARCHING_IN_CHAIN);
//
//        for (Node node : bucket) {
//            if (Objects.equals(node.key, key)) {
//                addPoint(TracingPoints.KEY_FOUND);
//                addPoint(TracingPoints.OPERATION_COMPLETE);
//                return node.value;
//            }
//        }
//
//        addPoint(TracingPoints.KEY_NOT_FOUND);
//        addPoint(TracingPoints.OPERATION_COMPLETE);
//        return null;
//    }
//
//    public boolean delete(String key) {
//        addPoint(TracingPoints.OPERATION_START);
//
//        int index = hash(key);
//
//        LinkedList<Node> bucket = table.get(index);
//        addPoint(TracingPoints.SEARCHING_IN_CHAIN);
//
//        for (Node node : bucket) {
//            if (Objects.equals(node.key, key)) {
//                addPoint(TracingPoints.KEY_FOUND);
//                bucket.remove(node);
//                elementCount--;
//                addPoint(TracingPoints.KEY_DELETED);
//                addPoint(TracingPoints.OPERATION_COMPLETE);
//                return true;
//            }
//        }
//
//        addPoint(TracingPoints.KEY_NOT_FOUND);
//        addPoint(TracingPoints.OPERATION_COMPLETE);
//        return false;
//    }
//
//    public int getElementCount() {
//        return elementCount;
//    }
//}
//
//// keypoint testing