package ru.itmo.tpo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashTable {
    
    /**
     * Характерные точки алгоритма для трассировки выполнения
     */
    public enum CharacteristicPoint {
        OPERATION_START,
        HASH_CALCULATED,
        SEARCHING_IN_CHAIN,
        KEY_FOUND,
        KEY_NOT_FOUND,
        KEY_INSERTED,
        KEY_UPDATED,
        KEY_DELETED,
        OPERATION_COMPLETE
    }
    
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
    
    // Для отслеживания характерных точек
    private boolean traceEnabled = false;
    private List<CharacteristicPoint> trace = new ArrayList<>();
    
    public HashTable() {
        this(13);
    }
    
    public HashTable(int size) {
        this.size = size;
        this.table = new ArrayList<>(size);
        this.elementCount = 0;
        
        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<>());
        }
    }
    
    /**
     * Включить трассировку характерных точек
     */
    public void enableTrace() {
        this.traceEnabled = true;
        this.trace.clear();
    }
    
    /**
     * Выключить трассировку
     */
    public void disableTrace() {
        this.traceEnabled = false;
    }
    
    /**
     * Получить зафиксированную последовательность характерных точек
     */
    public List<CharacteristicPoint> getTrace() {
        return new ArrayList<>(trace);
    }
    
    /**
     * Очистить трассировку
     */
    public void clearTrace() {
        trace.clear();
    }
    
    /**
     * Добавить точку в трассировку (если включена)
     */
    private void recordPoint(CharacteristicPoint point) {
        if (traceEnabled) {
            trace.add(point);
        }
    }
    
    public int hashCode(String key) {
        if (key == null || key.isEmpty()) {
            return 0;
        }
        
        int hash = 0;
        
        for (char c : key.toCharArray()) {
            hash = hash << 4;
            hash += c;
            
            int high = hash & 0xF0000000;
            hash ^= high >>> 12;
        }
        
        return hash;
    }
    
    public int hash(String key) {
        int rawHash = hashCode(key);
        return Math.abs(rawHash % size);
    }
    
    /**
     * Вставить элемент в таблицу
     * @param key ключ
     * @param value значение
     */
    public void insert(String key, String value) {
        recordPoint(CharacteristicPoint.OPERATION_START);
        
        int index = hash(key);
        recordPoint(CharacteristicPoint.HASH_CALCULATED);
        
        LinkedList<Node> bucket = table.get(index);
        recordPoint(CharacteristicPoint.SEARCHING_IN_CHAIN);
        
        // Проверяем, есть ли уже такой ключ
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                recordPoint(CharacteristicPoint.KEY_FOUND);
                node.value = value;
                recordPoint(CharacteristicPoint.KEY_UPDATED);
                recordPoint(CharacteristicPoint.OPERATION_COMPLETE);
                return;
            }
        }
        
        // Ключ не найден, вставляем новый элемент
        recordPoint(CharacteristicPoint.KEY_NOT_FOUND);
        bucket.add(new Node(key, value));
        elementCount++;
        recordPoint(CharacteristicPoint.KEY_INSERTED);
        recordPoint(CharacteristicPoint.OPERATION_COMPLETE);
    }
    
    /**
     * Найти элемент по ключу
     * @param key ключ
     * @return значение или null, если не найдено
     */
    public String search(String key) {
        recordPoint(CharacteristicPoint.OPERATION_START);
        
        int index = hash(key);
        recordPoint(CharacteristicPoint.HASH_CALCULATED);
        
        LinkedList<Node> bucket = table.get(index);
        recordPoint(CharacteristicPoint.SEARCHING_IN_CHAIN);
        
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                recordPoint(CharacteristicPoint.KEY_FOUND);
                recordPoint(CharacteristicPoint.OPERATION_COMPLETE);
                return node.value;
            }
        }
        
        recordPoint(CharacteristicPoint.KEY_NOT_FOUND);
        recordPoint(CharacteristicPoint.OPERATION_COMPLETE);
        return null;
    }
    
    /**
     * Удалить элемент по ключу
     * @param key ключ
     * @return true если удалён, false если не найден
     */
    public boolean delete(String key) {
        recordPoint(CharacteristicPoint.OPERATION_START);
        
        int index = hash(key);
        recordPoint(CharacteristicPoint.HASH_CALCULATED);
        
        LinkedList<Node> bucket = table.get(index);
        recordPoint(CharacteristicPoint.SEARCHING_IN_CHAIN);
        
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                recordPoint(CharacteristicPoint.KEY_FOUND);
                bucket.remove(node);
                elementCount--;
                recordPoint(CharacteristicPoint.KEY_DELETED);
                recordPoint(CharacteristicPoint.OPERATION_COMPLETE);
                return true;
            }
        }
        
        recordPoint(CharacteristicPoint.KEY_NOT_FOUND);
        recordPoint(CharacteristicPoint.OPERATION_COMPLETE);
        return false;
    }
    
    /**
     * Получить количество элементов в таблице
     */
    public int getElementCount() {
        return elementCount;
    }
    
    /**
     * Получить длину цепочки в указанной корзине
     */
    public int getChainLength(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс выходит за границы таблицы");
        }
        return table.get(index).size();
    }
    
    public int getSize() {
        return size;
    }
}
