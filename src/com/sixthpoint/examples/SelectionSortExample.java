package com.sixthpoint.examples;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author sixthpoint
 */
public class SelectionSortExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // low is the lowest possible value, high is the highest possible value
        int low = 0, high = 100;

        ArrayList<Integer> numbers = new ArrayList<>();
        Random r = new Random();

        // Fill the array with random integers
        for (int i = 0; i < 100; i++) {
            numbers.add(r.nextInt(high - low) + low);
        }

        selectionSort(numbers);

        // Output the sorted arraylist using lambda's
        numbers.stream().forEach((num1) -> {
            System.out.println(num1);
        });
    }

    /**
     * Selection Sort a ArrayList of integers
     *
     * @param arr
     * @return
     */
    public static ArrayList<Integer> selectionSort(ArrayList<Integer> arr) {

        for (int i = 0; i < arr.size() - 1; i++) {
            int index = i;
            for (int j = i + 1; j < arr.size(); j++) {
                if (arr.get(j) < arr.get(index)) {
                    index = j;
                }
            }

            int smallerNumber = arr.get(index);
            arr.set(index, arr.get(i));
            arr.set(i, smallerNumber);
        }
        return arr;
    }
}
