package additional;
import java.util.ArrayList;
import java.util.Iterator;


public class MyHashMap<K, V> implements Iterable<K>{
    private final int num = 16;
    private ArrayList<Node<K, V>> table = new ArrayList<>();

    // Возвращает объект-итератор
    public Iterator<K> iterator(){
        // найдем первое звено
        int cur = 0;
        Node<K, V> startNode = null;
        while (cur < table.size()){
            if (table.get(cur) != null){
                startNode = table.get(cur);
                break;
            }
            cur++;
        }
        return new HashMapIterator(startNode);
    }
    private class HashMapIterator implements Iterator<K>{
            private int currentIndex = 0;
            private Node<K, V> currentNode = null;
            HashMapIterator(Node<K, V> startNode){
                currentNode = startNode;
            }
            @Override
            public boolean hasNext() {
                // Проверяем, есть ли следующий элемент в хешмапе
                if (currentNode == null){
                    return currentIndex + 1 < table.size() && table.get(currentIndex + 1) != null;
                } else{
                    if (currentNode.getNextNode() == null) // проверить следующий элемент массива
                        return currentIndex + 1 < table.size() && table.get(currentIndex + 1) != null;
                    else
                        return true;
                }
            }
            @Override
            public K next() {
                // Переходим к следующему элементу в хешмапе
                while (currentIndex < table.size()){
                    if (table.get(currentIndex) == null){
                        currentIndex++;
                        continue;
                    }
                    currentNode = table.get(currentIndex);
                    while(currentNode.getNextNode() != null){
                        currentNode = currentNode.getNextNode();
                    }
                    try{
                        return currentNode.getKey();
                    } finally {
                        currentNode = currentNode.getNextNode();
                    }


                }
                return currentNode.getKey();
            }
    }


    public MyHashMap(){
        // инициализировать массив table пустыми 16-ю значениями
        while (table.size() < num) table.add(null);
    }

    @FunctionalInterface
    interface CheckInterface<K>{
        boolean check(K key1, K key2);
    }

    CheckInterface<K> check = (K key1, K key2) -> {
        if (key1 == key2)
            return true;
        if (key1 == null || key2 == null)
            return false;
        return (hash(key1) == hash(key2) && key1.equals(key2));
    };

    public V get(K key){
        Node<K, V> node = table.get(getIndex(key));
        while (node != null){
            if (check.check(key, node.getKey())){
                return node.getValue();
            }
            node = node.getNextNode();
        }
        return null;
    }

    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value);
        addNode(getIndex(key), newNode);
    }

    public void delete(K key){
        Node<K, V> node = table.get(getIndex(key));
        // нет элемента с таким ключом
        if (node == null) return;
        while (node != null){
            if (check.check(key, node.getKey())) {
                deleteNode(node);
                return;
            }
            node = node.getNextNode();
        }
    }

    private void deleteNode(Node<K, V> nodeToDelete){
        Node<K, V> node = table.get(getIndex(nodeToDelete.getKey()));

        // если это первое звено
        if (node == nodeToDelete){
            table.set(getIndex(nodeToDelete.getKey()), null);
            return;
        }

        // найти звено перед удаляемым
        while (node.getNextNode() != nodeToDelete) node = node.getNextNode();
        node.setNextNode(nodeToDelete.getNextNode());
    }

    private void addNode(int tableIndex, Node<K, V> newNode){
        Node<K, V> node = table.get(tableIndex);

        // если еще нет звеньев с таким индексом
        if (node == null){
            table.set(tableIndex, newNode);
            return;
        }

        // случай, когда единственный элемент в списке, ключ которог совпадает с новым
        if (node.getNextNode() == null && check.check(newNode.getKey(), node.getKey())){
            node.setValue(newNode.getValue());
            return;
        }

        // если уже есть звенья с таким индексом
        // найти последний
        while (node.getNextNode() != null){
            // если уже есть элемент с таким ключом
            if (check.check(newNode.getKey(), node.getKey())){
                node.setValue(newNode.getValue());
                return;
            }
            node = node.getNextNode();
        }

        node.setNextNode(newNode);
    }

    private int hash(Object obj){
        return obj == null ? 0 : obj.hashCode();
    }
    private int getIndex(K key){
        return hash(key) & (num - 1);
    }
}
