package ui;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner SC = new Scanner(System.in);

    public static String readLine(String prompt) {
        System.out.print(prompt);
        try {
            return SC.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println("\nInput closed. Exiting.");
            System.exit(0);
            return "";
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
