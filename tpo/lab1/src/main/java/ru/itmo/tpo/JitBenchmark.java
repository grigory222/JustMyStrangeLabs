package ru.itmo.tpo;

public class JitBenchmark {
    public static void main(String[] args) {
        HashTable table = new HashTable(100);

        for (int i = 0; i < 20000; i++) {
            table.insert("key" + i, "value" + i);
        }
    }
}