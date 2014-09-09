package com.sixthpoint.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * A simple binary search arrayList example
 *
 * @author sixthpoint
 */
public class BinarySearchExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // low is the lowest possible value, high is the highest possible value
        int find = 9, low = 0, high = 10;

        ArrayList<Integer> numbers = new ArrayList<>();
        Random r = new Random();

        // Fill the array with random integers
        for (int i = 0; i < 10; i++) {
            numbers.add(r.nextInt(high - low) + low);
        }

        // Sort the arraylist
        Collections.sort(numbers);

        // Output the sorted arraylist using lambda's
        numbers.stream().forEach((num1) -> {
            System.out.println(num1);
        });

        Integer result = binarySearch(numbers, find);

        if (result >= 0) {
            System.out.println("The number was found in arrayList position " + result);
        } else {
            System.out.println("The number " + find + " is not in of the " + numbers.size() + " positions of the arrayList.");
        }
    }

    /**
     * Perform a binarySearch on a given arrayList
     *
     * @param array
     * @param key
     * @return
     */
    public static int binarySearch(ArrayList<Integer> array, int key) {

        int position, lower = 0, upper = array.size() - 1;

        // To start, find the subscript of the middle position.
        position = (lower + upper) / 2;

        while ((array.get(position) != key) && (lower <= upper)) {
            if (array.get(position) > key) {
                upper = position - 1;
            } else {
                lower = position + 1;
            }
            position = (lower + upper) / 2;
        }

        return ((lower <= upper) ? position : -1);
    }
}
