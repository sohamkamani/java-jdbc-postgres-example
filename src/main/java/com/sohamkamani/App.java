package com.sohamkamani;

import com.github.ricksbrown.cowsay.Cowsay;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        int result = Calculator.add(4, 5);
        String[] cowArgs = new String[] { String.valueOf(result) };
        String cowString = Cowsay.say(cowArgs);
        System.out.printf(cowString);
    }

    public static int add(int n1, int n2) {
        return n1 + n2;
    }
}
