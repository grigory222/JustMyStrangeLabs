package ru.itmo.tpo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;

@DisplayName("Тестирование хеш-таблицы")
public class HashTableTest {

    private HashTable hashTable;
    private HashTable hashTableSpy;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable(10);
        hashTableSpy = spy(hashTable);
    }

    @Nested
    @DisplayName("Проверка прохождения характерных точек алгоритма")
    class KeypointSequenceTests {

        @Test
        @DisplayName("insert(): добавление нового ключа")
        void insert_newKey_followsReferenceSequence() {
            hashTableSpy.insert("new_key", "value");

            InOrder inOrder = inOrder(hashTableSpy);
            inOrder.verify(hashTableSpy).onOperationStart();
            inOrder.verify(hashTableSpy).calculateHashIndex("new_key");
            inOrder.verify(hashTableSpy).onSearchingInChain();
            inOrder.verify(hashTableSpy).onKeyNotFound();
            inOrder.verify(hashTableSpy).onKeyInserted();
            inOrder.verify(hashTableSpy).onOperationComplete();
        }

        @Test
        @DisplayName("insert(): обновление существующего ключа")
        void insert_existingKey_followsReferenceSequence() {
            hashTableSpy.insert("existing_key", "val1");
            InOrder inOrder = inOrder(hashTableSpy);

            hashTableSpy.insert("existing_key", "val2");

            inOrder.verify(hashTableSpy).onOperationStart();
            inOrder.verify(hashTableSpy).calculateHashIndex("existing_key");
            inOrder.verify(hashTableSpy).onSearchingInChain();
            inOrder.verify(hashTableSpy).onKeyFound();
            inOrder.verify(hashTableSpy).onKeyUpdated();
            inOrder.verify(hashTableSpy).onOperationComplete();
        }

        @Test
        @DisplayName("search(): поиск существующего ключа")
        void search_existingKey_followsReferenceSequence() {
            hashTableSpy.insert("key", "value");
            InOrder inOrder = inOrder(hashTableSpy);

            hashTableSpy.search("key");

            inOrder.verify(hashTableSpy).onOperationStart();
            inOrder.verify(hashTableSpy).calculateHashIndex("key");
            inOrder.verify(hashTableSpy).onSearchingInChain();
            inOrder.verify(hashTableSpy).onKeyFound();
            inOrder.verify(hashTableSpy).onOperationComplete();
        }

        @Test
        @DisplayName("search(): поиск отсутствующего ключа")
        void search_missingKey_followsReferenceSequence() {
            hashTableSpy.search("missing");

            InOrder inOrder = inOrder(hashTableSpy);
            inOrder.verify(hashTableSpy).onOperationStart();
            inOrder.verify(hashTableSpy).calculateHashIndex("missing");
            inOrder.verify(hashTableSpy).onSearchingInChain();
            inOrder.verify(hashTableSpy).onKeyNotFound();
            inOrder.verify(hashTableSpy).onOperationComplete();
        }

        @Test
        @DisplayName("delete(): удаление существующего ключа")
        void delete_existingKey_followsReferenceSequence() {
            hashTableSpy.insert("key", "value");
            InOrder inOrder = inOrder(hashTableSpy);

            hashTableSpy.delete("key");

            inOrder.verify(hashTableSpy).onOperationStart();
            inOrder.verify(hashTableSpy).calculateHashIndex("key");
            inOrder.verify(hashTableSpy).onSearchingInChain();
            inOrder.verify(hashTableSpy).onKeyFound();
            inOrder.verify(hashTableSpy).onKeyDeleted();
            inOrder.verify(hashTableSpy).onOperationComplete();
        }

        @Test
        @DisplayName("delete(): попытка удаления отсутствующего ключа")
        void delete_missingKey_followsReferenceSequence() {
            hashTableSpy.delete("missing");

            InOrder inOrder = inOrder(hashTableSpy);
            inOrder.verify(hashTableSpy).onOperationStart();
            inOrder.verify(hashTableSpy).calculateHashIndex("missing");
            inOrder.verify(hashTableSpy).onSearchingInChain();
            inOrder.verify(hashTableSpy).onKeyNotFound();
            inOrder.verify(hashTableSpy).onOperationComplete();
        }
    }

    @Nested
    @DisplayName("Свойства хеш-функции")
    class HashFunctionTests {

        @Test
        @DisplayName("Детерминированность: одинаковые ключи дают одинаковый индекс")
        void hash_isDeterministic() {
            int hash1 = hashTable.calculateHashIndex("SameKey");
            int hash2 = hashTable.calculateHashIndex("SameKey");
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Распределение: разные ключи дают разные индексы")
        void hash_producesDifferentIndicesForDifferentKeys() {
            int hash1 = hashTable.calculateHashIndex("A");
            int hash2 = hashTable.calculateHashIndex("B");
            assertNotEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Граничные значения: возвращает 0 для null и пустой строки")
        void hash_handlesNullAndEmptyStrings() {
            assertEquals(0, hashTable.calculateHashIndex(null));
            assertEquals(0, hashTable.calculateHashIndex(""));
        }

        @Test
        @DisplayName("Обработка длинных строк (битовые смещения для строк > 8 символов)")
        void hash_processesLongStrings() {
            int index = hashTable.calculateHashIndex("LONG_STRING_OVER_EIGHT_CHARS");
            assertTrue(index >= 0 && index < 10);
        }
    }

    @Nested
    @DisplayName("Операции поиска и обновления значений")
    class SearchAndInsertTests {

        @Test
        @DisplayName("search(): возвращает null для отсутствующего ключа")
        void search_returnsNullForMissingKey() {
            assertNull(hashTable.search("missing_key"));
        }

        @Test
        @DisplayName("search(): возвращает корректное значение после insert()")
        void search_returnsValueAfterInsert() {
            hashTable.insert("key", "expected_val");
            assertEquals("expected_val", hashTable.search("key"));
        }

        @Test
        @DisplayName("insert(): успешно перезаписывает старое значение")
        void insert_overwritesExistingValue() {
            hashTable.insert("key", "old_val");
            hashTable.insert("key", "new_val");
            assertEquals("new_val", hashTable.search("key"));
        }
    }

    @Nested
    @DisplayName("Операции удаления")
    class DeleteTests {

        @Test
        @DisplayName("delete(): возвращает true при удалении существующего ключа")
        void delete_returnsTrueForExistingKey() {
            hashTable.insert("key", "val");
            assertTrue(hashTable.delete("key"));
        }

        @Test
        @DisplayName("delete(): элемент фактически недоступен после удаления")
        void delete_removesElementFromTable() {
            hashTable.insert("key", "val");
            hashTable.delete("key");
            assertNull(hashTable.search("key"));
        }

        @Test
        @DisplayName("delete(): возвращает false при попытке удалить отсутствующий ключ")
        void delete_returnsFalseForMissingKey() {
            assertFalse(hashTable.delete("missing_key"));
        }
    }

    @Nested
    @DisplayName("Логика счетчика элементов")
    class ElementCountTests {

        @Test
        @DisplayName("Изначально таблица пуста (счетчик = 0)")
        void count_isZeroInitially() {
            assertEquals(0, hashTable.getElementCount());
        }

        @Test
        @DisplayName("Счетчик увеличивается при добавлении нового ключа")
        void count_increasesOnNewInsert() {
            hashTable.insert("key", "val");
            assertEquals(1, hashTable.getElementCount());
        }

        @Test
        @DisplayName("Счетчик НЕ увеличивается при обновлении существующего ключа")
        void count_doesNotIncreaseOnUpdate() {
            hashTable.insert("key", "val1");
            hashTable.insert("key", "val2");
            assertEquals(1, hashTable.getElementCount());
        }

        @Test
        @DisplayName("Счетчик уменьшается при успешном удалении ключа")
        void count_decreasesOnDelete() {
            hashTable.insert("key", "val");
            hashTable.delete("key");
            assertEquals(0, hashTable.getElementCount());
        }
    }

    @Nested
    @DisplayName("Обработка коллизий")
    class CollisionTests {

        @BeforeEach
        void setUpCollision() {
            // Ключи "A" и "K" попадают в один бакет (индекс 5)
            hashTable.insert("A", "val_A");
            hashTable.insert("K", "val_K");
        }

        @Test
        @DisplayName("Поиск корректно работает внутри цепочки при коллизии")
        void search_findsElementsInCollisionChain() {
            assertEquals("val_A", hashTable.search("A"));
            assertEquals("val_K", hashTable.search("K"));
        }

        @Test
        @DisplayName("Удаление первого элемента из цепочки не затрагивает другие")
        void delete_removesFirstElementInChain() {
            hashTable.delete("A");
            assertNull(hashTable.search("A"));
            assertEquals("val_K", hashTable.search("K"));
        }

        @Test
        @DisplayName("search(): поиск отсутствующего ключа в корзине с другими элементами возвращает null")
        void search_returnsNullWhenKeyIsMissingInPopulatedBucket() {
            // "U" тоже имеет индекс 5, но его нет в таблице
            assertNull(hashTable.search("U"));
        }

        @Test
        @DisplayName("delete(): попытка удаления отсутствующего ключа из непустой корзины возвращает false")
        void delete_returnsFalseWhenKeyIsMissingInPopulatedBucket() {
            assertFalse(hashTable.delete("U"));
        }

        @Test
        @DisplayName("delete(): удаление второго элемента в цепочке коллизий проходит успешно")
        void delete_removesSubsequentElementInCollisionChain() {
            boolean isDeleted = hashTable.delete("K");

            assertTrue(isDeleted);
            assertNull(hashTable.search("K"));
            assertEquals("val_A", hashTable.search("A"), "Первый элемент цепочки не должен пострадать");
        }
    }

    @Nested
    @DisplayName("Поддержка null-ключей")
    class NullKeyTests {

        @Test
        @DisplayName("insert() и search(): хеш-таблица позволяет хранить и находить значение по null-ключу")
        void table_acceptsAndFindsNullKey() {
            hashTable.insert(null, "null_value");
            assertEquals("null_value", hashTable.search(null));
        }

        @Test
        @DisplayName("delete(): хеш-таблица корректно удаляет элемент с null-ключом")
        void table_successfullyDeletesNullKey() {
            hashTable.insert(null, "null_value");

            boolean isDeleted = hashTable.delete(null);

            assertTrue(isDeleted);
            assertNull(hashTable.search(null));
        }
    }

    @Nested
    @DisplayName("Создание таблицы и валидация размера")
    class ConstructorTests {

        @Test
        @DisplayName("Выбрасывает IllegalArgumentException при размере <= 0")
        void constructor_rejectsInvalidSize() {
            assertThrows(IllegalArgumentException.class, () -> new HashTable(0));
            assertThrows(IllegalArgumentException.class, () -> new HashTable(-5));
        }
    }
}