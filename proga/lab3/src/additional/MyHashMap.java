package additional;
import java.util.HashMap;
import java.util.ArrayList;
public class MyHashMap<K, V> {
    private final int num = 16;
    ArrayList<Node<K, V>> table = new ArrayList<>();

    public MyHashMap(){

        // инициализировать массив table пустыми 16-ю значениями
        while (table.size() < num) table.add(null);
    }

    public V get(K key){
        Node<K, V> node = table.get(getIndex(key));
        while (node != null){
            if (key.equals(node.getKey())){
                return node.getValue();
            }
            node = node.getNextNode();
        }
        return null;
    }

    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key.hashCode(), key, value);
        addNode(getIndex(key), newNode);
    }

    private void addNode(int tableIndex, Node<K, V> newNode){
        // если еще нет звеньев с таким индексом
        if (table.get(tableIndex) == null){
            table.set(tableIndex, newNode);
            return;
        }

        // если уже есть звенья с таким индексом
        Node<K, V> node = table.get(tableIndex);
        // найти последний
        while (node.getNextNode() != null){
            // если уже есть элемент с таким ключом
            if (newNode.getKey().equals(node.getKey())){
                node.setValue(newNode.getValue());
                return;
            }
            node = node.getNextNode();
        }

        node.setNextNode(newNode);
    }

    private int getIndex(K key){
        return key.hashCode() % num;
    }
}
