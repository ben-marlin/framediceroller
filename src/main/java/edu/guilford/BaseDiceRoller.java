package edu.guilford;

import java.util.Scanner;
import java.util.Random;

public class BaseDiceRoller {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("We will be rolling some dice.");
        System.out.print("How many dice? ");
        int number = scan.nextInt();
        System.out.print("\nHow many sides per die? ");
        int sides = scan.nextInt();

        System.out.println("\n" + number + "d" + sides + " = " + rollDice(number, sides));
        scan.close();
    }

    public static int rollDice(int num, int size) {
        Random rand = new Random();
        int sum = 0;
        for (int i = 0; i < num; i++) {
            sum += rand.nextInt(size)+1;
        }
        return sum;
    }

}