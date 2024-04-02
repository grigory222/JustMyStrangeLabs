package ru.ifmo.se;

import ru.ifmo.se.runner.Runner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Runner runner = new Runner("localhost", 5252);
        runner.run();
    }
}