import additional.MyHashMap;
import exceptions.ZeroSubstancePropertyException;
import people.Znayka;
import substances.*;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        Znayka znayka = new Znayka();
        Water water = new Water(0.0, 0.001);
        SulfuricAcid sulfuricAcid = new SulfuricAcid(0.3, 0.0003);
        Alcohol alcohol = new Alcohol(0.5, 0.0005);

        // Локальный класс. "Новый" лунный камень, который меняет вес
        class NewMoonRock extends MoonRock {
            void updateWeight() {
                Random random = new Random();
                double left = 0.1;
                double right = 2.0;
                double cut = left + (right - left) * random.nextDouble();
                weight = cut * weight;
            }
            public double getWeight() {
                updateWeight();
                return weight;
            }
        }
        NewMoonRock moonRock = new NewMoonRock();
        NewMoonRock moonRock2 = new NewMoonRock();
        NewMoonRock moonRock3 = new NewMoonRock();

        // Создаём анонимный класс и сразу вызываем метод sink
        System.out.println(new NewMoonRock() {
            public String sink() {
                double power = 1000.0 * 9.8 * getVolume(); // сила Архимеда
                return power < getWeight() * 9.8 ? "Лунный камень тонет" : "Лунный камень всплывает";
            }
        }.sink());

        // Вложенный статический класс. Статистика
        MoonRock.Statistics stat = new MoonRock.Statistics();
        System.out.println(stat.getStatistics());

        // Внутренние классы
        System.out.println(znayka.heatAnalyze(moonRock));

        // 3 лаба
        System.out.println();
        System.out.println();
        System.out.println(znayka.analyze(water));
        System.out.println(znayka.analyze(moonRock));

        System.out.println(moonRock.isItGlowing() ? "Лунный камень светится" : "Лунный камень не светится");
        System.out.println(moonRock.isItGlowing() ? "Лунный камень светится" : "Лунный камень не светится");
        System.out.println(moonRock.isItGlowing() ? "Лунный камень светится" : "Лунный камень не светится");
        System.out.println(moonRock.isItGlowing() ? "Лунный камень светится" : "Лунный камень не светится");

        System.out.println("Знайка начал химический анализ " + moonRock.getName());

        try {
            System.out.println(znayka.joinSubstances(moonRock, water));
        } catch (ZeroSubstancePropertyException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(znayka.joinSubstances(moonRock, sulfuricAcid));
        } catch (ZeroSubstancePropertyException e) {
            System.out.println(e.getMessage());
        }

        MyHashMap<String, Integer> m = new MyHashMap<>();
        m.put("test1", 123);
        m.put("test2", 456);
        m.put("test3", 789);
        for (String x : m){
            System.out.println(x);
        }
    }
}
