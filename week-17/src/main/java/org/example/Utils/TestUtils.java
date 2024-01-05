package org.example.Utils;

import java.util.Random;

public class TestUtils {
public static String getRamdomValue() {
    Random random = new Random();
    int randomInt = random.nextInt(10000);
    return Integer.toString(randomInt);
}
}
