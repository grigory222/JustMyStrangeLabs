import additional.MyHashMapTest;
import people.Znayka;
import substances.Alcohol;
import substances.MoonRock;
import substances.SulfuricAcid;
import substances.Water;

public class Main {
    public static void main(String[] args){

        MyHashMapTest test = new MyHashMapTest();
        test.test();


        Znayka znayka = new Znayka();
        Water water = new Water();
        MoonRock moonRock = new MoonRock();
        SulfuricAcid sulfuricAcid = new SulfuricAcid();
        Alcohol alcohol = new Alcohol();


        System.out.println(znayka.analyze(water));
        System.out.println(znayka.analyze(moonRock));

        System.out.println(moonRock.isItGlowing() ? "Лунный камень светится" : "Лунный камень не светится");
        System.out.println(moonRock.isItGlowing() ? "Лунный камень светится" : "Лунный камень не светится");
        System.out.println(moonRock.isItGlowing() ? "Лунный камень светится" : "Лунный камень не светится");
        System.out.println(moonRock.isItGlowing() ? "Лунный камень светится" : "Лунный камень не светится");

        System.out.println("Знайка начал химический анализ " + moonRock.getName());

        System.out.println(znayka.joinSubstances(moonRock, water));
        System.out.println(znayka.joinSubstances(moonRock, alcohol));
        System.out.println(znayka.joinSubstances(moonRock, sulfuricAcid));

    }
}
