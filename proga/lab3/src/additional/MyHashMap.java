package additional;
import java.util.ArrayList;


public class MyHashMap<K, V> {
    private final int num = 16;
    private ArrayList<Node<K, V>> table = new ArrayList<>();

    public MyHashMap(){
        // инициализировать массив table пустыми 16-ю значениями
        while (table.size() < num) table.add(null);
    }

    @FunctionalInterface
    interface CheckInterface<K>{
        boolean check(K key1, K key2);
    }
    CheckInterface<K> check = (K key1, K key2) -> (hash(key1) == hash(key2) && key1.equals(key2));


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
