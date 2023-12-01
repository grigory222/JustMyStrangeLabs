package additional;

class Node<K, V>{
    int hash;
    K key;
    V value;
    Node<K, V> nextNode;

    Node(int hash, K key, V value){
        this.hash = hash;
        this.key = key;
        this.value = value;
        nextNode = null;
    }

    public int getHash() {
        return hash;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Node<K, V> getNextNode() {
        return nextNode;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setNextNode(Node<K, V> nextNode) {
        this.nextNode = nextNode;
    }
}
