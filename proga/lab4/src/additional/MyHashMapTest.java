package additional;

public class MyHashMapTest{
    public void test(){
        System.out.println("===== TESTING =====");
        testStringString();
        testIntegerString();
        System.out.println("===== END =====");
    }

    private void testStringString(){
        MyHashMap<String, String> map = new MyHashMap<>();

        map.put("key1", "value1");
        map.put("KEY", "VALUE");
        map.put(null, "s0m3_v4lu3");

        map.delete("key1");

        System.out.println(map.get("key1") == null);
        System.out.println(map.get("KEY").equals("VALUE"));
        System.out.println(map.get(null).equals("s0m3_v4lu3"));

        map.delete(null);
        System.out.println(map.get(null) == null);
    }

    private void testIntegerString(){
        MyHashMap<Integer, String> map = new MyHashMap<>();

        map.put(1, "test");
        map.put(1, "lol");
        map.put(52, "444");
        map.put(1337, "s0m3_v4lu3");
        map.put(null, "s0m3_v4lu3");
        map.put(0xdeadbeef, "0xcafebabe");

        map.delete(1337);
        map.delete(1337);
        map.delete(13377);

        System.out.println((map.get(1).equals("lol")));
        System.out.println(map.get(52).equals("444"));
        System.out.println(map.get(1337) == null);
        System.out.println(map.get(null).equals("s0m3_v4lu3"));
        System.out.println(map.get(0xdeadbeef).equals("0xcafebabe"));

        map.delete(null);
    }
}
