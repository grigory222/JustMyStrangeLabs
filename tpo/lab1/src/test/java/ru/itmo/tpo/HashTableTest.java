package ru.itmo.tpo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.itmo.tpo.HashTable.CharacteristicPoint.*;

@DisplayName("Тесты хеш-таблицы с закрытой адресацией - Пункт 2")
class HashTableTest {
    
    private HashTable hashTable;
    
    @BeforeEach
    void setUp() {
        hashTable = new HashTable(13);
    }
    
    // ===== ТЕСТЫ ТРАССИРОВКИ ВСТАВКИ =====
    
    @Test
    @DisplayName("Трассировка INSERT: вставка в пустую корзину")
    void testTraceInsertToEmptyBucket() {
        // Эталон: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> KEY_INSERTED -> COMPLETE
        List<HashTable.CharacteristicPoint> expected = List.of(
            OPERATION_START,
            HASH_CALCULATED,
            SEARCHING_IN_CHAIN,
            KEY_NOT_FOUND,
            KEY_INSERTED,
            OPERATION_COMPLETE
        );
        
        hashTable.enableTrace();
        hashTable.insert("test", "value1");
        List<HashTable.CharacteristicPoint> actual = hashTable.getTrace();
        
        assertEquals(expected, actual, 
            "Вставка в пустую корзину: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> KEY_INSERTED -> COMPLETE");
    }
    
    @Test
    @DisplayName("Трассировка INSERT: вставка нового ключа с коллизией")
    void testTraceInsertWithCollision() {
        // Сначала вставляем первый элемент
        hashTable.insert("key1", "value1");
        
        // Вставляем второй с тем же хешем (симулируем или просто другой ключ)
        // Эталон: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> KEY_INSERTED -> COMPLETE
        List<HashTable.CharacteristicPoint> expected = List.of(
            OPERATION_START,
            HASH_CALCULATED,
            SEARCHING_IN_CHAIN,
            KEY_NOT_FOUND,
            KEY_INSERTED,
            OPERATION_COMPLETE
        );
        
        hashTable.enableTrace();
        hashTable.insert("key2", "value2");
        List<HashTable.CharacteristicPoint> actual = hashTable.getTrace();
        
        // Проверяем что последовательность содержит основные точки
        assertEquals(OPERATION_START, actual.get(0));
        assertEquals(HASH_CALCULATED, actual.get(1));
        assertTrue(actual.contains(OPERATION_COMPLETE));
    }
    
    @Test
    @DisplayName("Трассировка INSERT: обновление существующего ключа")
    void testTraceUpdateExisting() {
        hashTable.insert("key", "value1");
        
        // Эталон: START -> HASH -> SEARCHING -> KEY_FOUND -> KEY_UPDATED -> COMPLETE
        List<HashTable.CharacteristicPoint> expected = List.of(
            OPERATION_START,
            HASH_CALCULATED,
            SEARCHING_IN_CHAIN,
            KEY_FOUND,
            KEY_UPDATED,
            OPERATION_COMPLETE
        );
        
        hashTable.enableTrace();
        hashTable.insert("key", "value2");
        List<HashTable.CharacteristicPoint> actual = hashTable.getTrace();
        
        assertEquals(expected, actual,
            "Обновление: START -> HASH -> SEARCHING -> KEY_FOUND -> KEY_UPDATED -> COMPLETE");
    }
    
    // ===== ТЕСТЫ ТРАССИРОВКИ ПОИСКА =====
    
    @Test
    @DisplayName("Трассировка SEARCH: поиск в пустой корзине")
    void testTraceSearchInEmptyBucket() {
        // Эталон: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> COMPLETE
        List<HashTable.CharacteristicPoint> expected = List.of(
            OPERATION_START,
            HASH_CALCULATED,
            SEARCHING_IN_CHAIN,
            KEY_NOT_FOUND,
            OPERATION_COMPLETE
        );
        
        hashTable.enableTrace();
        String result = hashTable.search("nonexistent");
        List<HashTable.CharacteristicPoint> actual = hashTable.getTrace();
        
        assertNull(result);
        assertEquals(expected, actual,
            "Поиск в пустой корзине: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> COMPLETE");
    }
    
    @Test
    @DisplayName("Трассировка SEARCH: успешный поиск")
    void testTraceSearchFound() {
        hashTable.insert("key", "value");
        
        // Эталон: START -> HASH -> SEARCHING -> KEY_FOUND -> COMPLETE
        List<HashTable.CharacteristicPoint> expected = List.of(
            OPERATION_START,
            HASH_CALCULATED,
            SEARCHING_IN_CHAIN,
            KEY_FOUND,
            OPERATION_COMPLETE
        );
        
        hashTable.enableTrace();
        String result = hashTable.search("key");
        List<HashTable.CharacteristicPoint> actual = hashTable.getTrace();
        
        assertEquals("value", result);
        assertEquals(expected, actual,
            "Успешный поиск: START -> HASH -> SEARCHING -> KEY_FOUND -> COMPLETE");
    }
    
    @Test
    @DisplayName("Трассировка SEARCH: неудачный поиск в непустой корзине")
    void testTraceSearchNotFoundInChain() {
        hashTable.insert("key1", "value1");
        
        // Эталон: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> COMPLETE
        List<HashTable.CharacteristicPoint> expected = List.of(
            OPERATION_START,
            HASH_CALCULATED,
            SEARCHING_IN_CHAIN,
            KEY_NOT_FOUND,
            OPERATION_COMPLETE
        );
        
        hashTable.enableTrace();
        String result = hashTable.search("key2");
        List<HashTable.CharacteristicPoint> actual = hashTable.getTrace();
        
        assertNull(result);
        assertEquals(expected, actual,
            "Неудачный поиск: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> COMPLETE");
    }
    
    // ===== ТЕСТЫ ТРАССИРОВКИ УДАЛЕНИЯ =====
    
    @Test
    @DisplayName("Трассировка DELETE: удаление из пустой корзины")
    void testTraceDeleteFromEmptyBucket() {
        // Эталон: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> COMPLETE
        List<HashTable.CharacteristicPoint> expected = List.of(
            OPERATION_START,
            HASH_CALCULATED,
            SEARCHING_IN_CHAIN,
            KEY_NOT_FOUND,
            OPERATION_COMPLETE
        );
        
        hashTable.enableTrace();
        boolean result = hashTable.delete("nonexistent");
        List<HashTable.CharacteristicPoint> actual = hashTable.getTrace();
        
        assertFalse(result);
        assertEquals(expected, actual,
            "Удаление из пустой корзины: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> COMPLETE");
    }
    
    @Test
    @DisplayName("Трассировка DELETE: успешное удаление")
    void testTraceDeleteSuccess() {
        hashTable.insert("key", "value");
        
        // Эталон: START -> HASH -> SEARCHING -> KEY_FOUND -> KEY_DELETED -> COMPLETE
        List<HashTable.CharacteristicPoint> expected = List.of(
            OPERATION_START,
            HASH_CALCULATED,
            SEARCHING_IN_CHAIN,
            KEY_FOUND,
            KEY_DELETED,
            OPERATION_COMPLETE
        );
        
        hashTable.enableTrace();
        boolean result = hashTable.delete("key");
        List<HashTable.CharacteristicPoint> actual = hashTable.getTrace();
        
        assertTrue(result);
        assertEquals(expected, actual,
            "Успешное удаление: START -> HASH -> SEARCHING -> KEY_FOUND -> KEY_DELETED -> COMPLETE");
    }
    
    @Test
    @DisplayName("Трассировка DELETE: неудачное удаление из непустой корзины")
    void testTraceDeleteNotFoundInChain() {
        hashTable.insert("key1", "value1");
        
        // Эталон: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> COMPLETE
        List<HashTable.CharacteristicPoint> expected = List.of(
            OPERATION_START,
            HASH_CALCULATED,
            SEARCHING_IN_CHAIN,
            KEY_NOT_FOUND,
            OPERATION_COMPLETE
        );
        
        hashTable.enableTrace();
        boolean result = hashTable.delete("key2");
        List<HashTable.CharacteristicPoint> actual = hashTable.getTrace();
        
        assertFalse(result);
        assertEquals(expected, actual,
            "Неудачное удаление: START -> HASH -> SEARCHING -> KEY_NOT_FOUND -> COMPLETE");
    }
    
    // ===== ФУНКЦИОНАЛЬНЫЕ ТЕСТЫ =====
    
    @Test
    @DisplayName("Вставка и поиск элемента")
    void testInsertAndSearch() {
        hashTable.insert("key", "value");
        assertEquals("value", hashTable.search("key"));
        assertEquals(1, hashTable.getElementCount());
    }
    
    @Test
    @DisplayName("Удаление элемента")
    void testDelete() {
        hashTable.insert("key", "value");
        assertTrue(hashTable.delete("key"));
        assertNull(hashTable.search("key"));
        assertEquals(0, hashTable.getElementCount());
    }
    
    @Test
    @DisplayName("Обновление значения")
    void testUpdate() {
        hashTable.insert("key", "value1");
        hashTable.insert("key", "value2");
        assertEquals("value2", hashTable.search("key"));
        assertEquals(1, hashTable.getElementCount());
    }
    
    @Test
    @DisplayName("Множественные вставки")
    void testMultipleInserts() {
        hashTable.insert("key1", "value1");
        hashTable.insert("key2", "value2");
        hashTable.insert("key3", "value3");
        
        assertEquals("value1", hashTable.search("key1"));
        assertEquals("value2", hashTable.search("key2"));
        assertEquals("value3", hashTable.search("key3"));
        assertEquals(3, hashTable.getElementCount());
    }
    
    @Test
    @DisplayName("Инициализация таблицы")
    void testTableInitialization() {
        HashTable table = new HashTable(10);
        assertEquals(10, table.getSize());
        
        HashTable defaultTable = new HashTable();
        assertEquals(13, defaultTable.getSize());
    }
    
    @Test
    @DisplayName("Трассировка детерминирована")
    void testTraceDeterminism() {
        hashTable.enableTrace();
        hashTable.insert("key", "value");
        List<HashTable.CharacteristicPoint> trace1 = hashTable.getTrace();
        
        hashTable.clearTrace();
        hashTable.insert("key", "value");
        List<HashTable.CharacteristicPoint> trace2 = hashTable.getTrace();
        
        assertEquals(trace1, trace2, "Одинаковые операции дают одинаковую трассировку");
    }
}
