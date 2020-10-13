package test;

import edu.princeton.cs.algs4.Stopwatch;

public class Test {
    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();
        System.out.println(args);
        double elapsedTime = stopwatch.elapsedTime();
        System.out.println(elapsedTime);
    }
}
