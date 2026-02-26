package ru.itmo.tpo;

import ru.itmo.tpo.domain.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. tg(x) ===");
        System.out.println("tg(0) = " + TangentFunction.tg(0));
        System.out.println("tg(1) = " + TangentFunction.tg(1));
        
        System.out.println("\n=== 2. Хеш-таблица ===");
        HashTable table = new HashTable(13);
        table.insert("apple", "fruit");
        table.insert("carrot", "vegetable");
        System.out.println("search(apple) = " + table.search("apple"));
        System.out.println("delete(apple) = " + table.delete("apple"));
        System.out.println("size = " + table.getElementCount());
        
        System.out.println("\n=== 3. История ===");
        Story story = new Story();
        story.playFullScene();
        System.out.println("Артур в шоке: " + story.getArthur().getEmotionalState());
        System.out.println("Зафод в кресле: " + story.getZaphod().isLounging());
    }
}
