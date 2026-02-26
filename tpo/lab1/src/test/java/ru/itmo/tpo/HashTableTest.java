package ru.itmo.tpo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.itmo.tpo.HashTable.TracingPoints.*;

@DisplayName("Тесты хеш-таблицы с закрытой адресацией")
class HashTableTest {

    private HashTable hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable(13);
    }

    @Test
    @DisplayName("Трассировка INSERT: вставка в пустую корзину")
    void testTraceInsertToEmptyBucket() {
        List<HashTable.TracingPoints> expected = List.of(
                OPERATION_START,
                HASH_CALCULATED,
                SEARCHING_IN_CHAIN,
                KEY_NOT_FOUND,
                KEY_INSERTED,
                OPERATION_COMPLETE
        );

        hashTable.enableTrace();
        hashTable.insert("test", "value");

        assertEquals(expected, hashTable.getTrace());
        
        assertEquals("value", hashTable.search("test"), "Вставленное значение должно быть найдено");
        assertEquals(1, hashTable.getElementCount(), "Количество элементов должно быть 1");
    }

    @Test
    @DisplayName("Трассировка INSERT: обновление существующего ключа")
    void testTraceInsertUpdate() {
        hashTable.insert("key", "value1");
        assertEquals("value1", hashTable.search("key"), "Первое значение должно быть сохранено");

        List<HashTable.TracingPoints> expected = List.of(
                OPERATION_START,
                HASH_CALCULATED,
                SEARCHING_IN_CHAIN,
                KEY_FOUND,
                KEY_UPDATED,
                OPERATION_COMPLETE
        );

        hashTable.enableTrace();
        hashTable.insert("key", "value2");

        assertEquals(expected, hashTable.getTrace());
        
        assertEquals("value2", hashTable.search("key"), "Значение должно быть обновлено");
        assertEquals(1, hashTable.getElementCount(), "Количество элементов не должно измениться");
    }

    @Test
    @DisplayName("Трассировка SEARCH: успешный поиск")
    void testTraceSearchFound() {
        hashTable.insert("key", "value");

        List<HashTable.TracingPoints> expected = List.of(
                OPERATION_START,
                HASH_CALCULATED,
                SEARCHING_IN_CHAIN,
                KEY_FOUND,
                OPERATION_COMPLETE
        );

        hashTable.enableTrace();
        String result = hashTable.search("key");
        assertEquals("value", result, "Найденное значение должно совпадать");
        assertEquals(expected, hashTable.getTrace());
    }

    @Test
    @DisplayName("Трассировка SEARCH: ключ не найден")
    void testTraceSearchNotFound() {
        hashTable.insert("A", "1");
        hashTable.insert("N", "2");

        List<HashTable.TracingPoints> expected = List.of(
                OPERATION_START,
                HASH_CALCULATED,
                SEARCHING_IN_CHAIN,
                KEY_NOT_FOUND,
                OPERATION_COMPLETE
        );

        hashTable.enableTrace();
        String result = hashTable.search("[");
        assertNull(result, "Несуществующий ключ должен вернуть null");
        assertEquals(expected, hashTable.getTrace());
    }

    @Test
    @DisplayName("Трассировка DELETE: успешное удаление")
    void testTraceDeleteSuccess() {
        hashTable.insert("key", "value");
        assertEquals("value", hashTable.search("key"), "Значение должно быть найдено перед удалением");
        assertEquals(1, hashTable.getElementCount());

        List<HashTable.TracingPoints> expected = List.of(
                OPERATION_START,
                HASH_CALCULATED,
                SEARCHING_IN_CHAIN,
                KEY_FOUND,
                KEY_DELETED,
                OPERATION_COMPLETE
        );

        hashTable.enableTrace();
        boolean deleteResult = hashTable.delete("key");
        assertTrue(deleteResult, "Удаление существующего ключа должно вернуть true");
        assertEquals(expected, hashTable.getTrace());
        
        assertNull(hashTable.search("key"), "Удаленный ключ не должен быть найден");
        assertEquals(0, hashTable.getElementCount(), "Количество элементов должно уменьшиться");
    }

    @Test
    @DisplayName("Трассировка DELETE: ключ не найден")
    void testTraceDeleteNotFound() {
        hashTable.insert("A", "1");
        hashTable.insert("N", "2");
        int initialCount = hashTable.getElementCount();

        List<HashTable.TracingPoints> expected = List.of(
                OPERATION_START,
                HASH_CALCULATED,
                SEARCHING_IN_CHAIN,
                KEY_NOT_FOUND,
                OPERATION_COMPLETE
        );

        hashTable.enableTrace();
        boolean deleteResult = hashTable.delete("[");
        assertFalse(deleteResult, "Удаление несуществующего ключа должно вернуть false");
        assertEquals(expected, hashTable.getTrace());
        
        assertEquals("1", hashTable.search("A"), "Другие элементы не должны быть затронуты");
        assertEquals("2", hashTable.search("N"), "Другие элементы не должны быть затронуты");
        assertEquals(initialCount, hashTable.getElementCount(), "Количество элементов не должно измениться");
    }

    @Test
    @DisplayName("disableTrace не добавляет точки трассировки")
    void testDisableTrace() {
        hashTable.enableTrace();
        hashTable.disableTrace();
        hashTable.insert("key", "value");

        assertTrue(hashTable.getTrace().isEmpty());
    }

    @Test
    @DisplayName("hashCode: null и пустая строка")
    void testHashCodeNullAndEmpty() {
        assertEquals(0, hashTable.hashCode(null));
        assertEquals(0, hashTable.hashCode(""));
    }

    @Test
    @DisplayName("hashCode: непустая строка")
    void testHashCodeNonEmpty() {
        assertNotEquals(0, hashTable.hashCode("abc"));
    }

    @Test
    @DisplayName("Множественная вставка и поиск")
    void testMultipleInsertAndSearch() {
        hashTable.insert("apple", "fruit");
        hashTable.insert("carrot", "vegetable");
        hashTable.insert("milk", "dairy");
        hashTable.insert("bread", "bakery");
        
        assertEquals("fruit", hashTable.search("apple"));
        assertEquals("vegetable", hashTable.search("carrot"));
        assertEquals("dairy", hashTable.search("milk"));
        assertEquals("bakery", hashTable.search("bread"));
        assertEquals(4, hashTable.getElementCount());
    }

    @Test
    @DisplayName("Обработка коллизий")
    void testCollisionHandling() {
        hashTable.insert("A", "value_A");
        hashTable.insert("N", "value_N");
        hashTable.insert("[", "value_[");
        
        assertEquals("value_A", hashTable.search("A"));
        assertEquals("value_N", hashTable.search("N"));
        assertEquals("value_[", hashTable.search("["));
        assertEquals(3, hashTable.getElementCount());
    }

    @Test
    @DisplayName("Обновление значений в цепочке")
    void testUpdateInChain() {
        hashTable.insert("A", "first");
        hashTable.insert("N", "second");
        
        hashTable.insert("A", "updated_A");
        hashTable.insert("N", "updated_N");
        
        assertEquals("updated_A", hashTable.search("A"));
        assertEquals("updated_N", hashTable.search("N"));
        assertEquals(2, hashTable.getElementCount(), "Обновление не должно увеличивать счетчик");
    }

    @Test
    @DisplayName("Удаление из цепочки")
    void testDeleteFromChain() {
        hashTable.insert("A", "value_A");
        hashTable.insert("N", "value_N");
        hashTable.insert("[", "value_[");
        
        assertTrue(hashTable.delete("N"));
        
        assertEquals("value_A", hashTable.search("A"), "Другие элементы в цепочке должны остаться");
        assertNull(hashTable.search("N"), "Удаленный элемент не должен быть найден");
        assertEquals("value_[", hashTable.search("["), "Другие элементы в цепочке должны остаться");
        assertEquals(2, hashTable.getElementCount());
    }

    @Test
    @DisplayName("Поиск несуществующих ключей")
    void testSearchNonexistentKeys() {
        hashTable.insert("existing", "value");
        
        assertNull(hashTable.search("nonexistent"));
        assertNull(hashTable.search(""));
        assertNull(hashTable.search("another_missing_key"));
        assertEquals(1, hashTable.getElementCount());
    }

    @Test
    @DisplayName("Удаление несуществующих ключей")
    void testDeleteNonexistentKeys() {
        hashTable.insert("key", "value");
        
        assertFalse(hashTable.delete("nonexistent"));
        assertFalse(hashTable.delete("another"));
        assertEquals(1, hashTable.getElementCount());
        assertEquals("value", hashTable.search("key"), "Существующий элемент не должен быть затронут");
    }

    @Test
    @DisplayName("Работа с null и пустыми значениями")
    void testNullAndEmptyValues() {
        hashTable.insert("key1", null);
        hashTable.insert("key2", "");
        
        assertNull(hashTable.search("key1"));
        assertEquals("", hashTable.search("key2"));
        assertEquals(2, hashTable.getElementCount());
    }
}