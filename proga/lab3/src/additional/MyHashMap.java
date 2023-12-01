package additional;
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
            if (hash(key) == hash(node.getKey()) && (node.getKey() == key || key.equals(node.getKey()))){
                return node.getValue();
            }
            node = node.getNextNode();
        }
        return null;
    }

    public void put(K key, V value) {
        //Node<K, V> newNode = new Node<>(key.hashCode(), key, value);
        Node<K, V> newNode = new Node<>(hash(key), key, value);
        addNode(getIndex(key), newNode);
    }

    private void addNode(int tableIndex, Node<K, V> newNode){
        Node<K, V> node = table.get(tableIndex);
        // если еще нет звеньев с таким индексом
        if (node == null){
            table.set(tableIndex, newNode);
            return;
        }

        // ???
        CheckInterface check = (node) -> {
            return newNode.getHash() == node.getHash() && (newNode.getKey() == node.getKey() || newNode.getKey().equals(node.getKey()))
        };
        // если уже есть звенья с таким индексом
        // найти последний
        while (node.getNextNode() != null);{
            // если уже есть элемент с таким ключом
            if (newNode.getHash() == node.getHash() && (newNode.getKey() == node.getKey() || newNode.getKey().equals(node.getKey()))){
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
