package com.sixthpoint.examples;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
        int find = 10000000, sampleSize = 10000000, iterations = 50, x = 0, y = 0;
        long integerTime = 0, arrayListTime = 0;
        BinarySearchExample bse = new BinarySearchExample();

        while (x <= iterations) {
            // Int array speed test
            integerTime = integerTime + bse.testIntegerArray(find, sampleSize);
            x++;
        }

        System.out.println(TimeUnit.MICROSECONDS.convert((integerTime / iterations), TimeUnit.NANOSECONDS));

        while (y <= iterations) {
            // Arraylist speed test
            arrayListTime = arrayListTime + bse.testArrayList(find, sampleSize);
            y++;
        }
        System.out.println(TimeUnit.MICROSECONDS.convert((arrayListTime / iterations), TimeUnit.NANOSECONDS));

    }

    /**
     * Test the speed of a int array binary search
     *
     * @param find
     * @param sampleSize
     * @return 
     */
    public long testIntegerArray(int find, int sampleSize) {

        // Now using a int array
        int[] numbers = new int[sampleSize];
        // Fill the array with random integers
        for (int i = 0; i < sampleSize; i++) {
            numbers[i] = (i * 2);
        }

        // Get the start time
        long startTime = System.nanoTime();

        // Perform the search
        Integer result = binarySearch(numbers, find);

        // Get the end time
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        if (result >= 0) {
            //System.out.println("The number was found in integer array in position " + result);
        } else {
            //System.out.println("The number " + find + " is not in of the " + numbers.length + " positions of the integer array.");
        }
        return duration;
    }

    /**
     * Test the speed of arraylist binary sear
     * @return ch
     *
     * @param find
     * @param sampleSize
     */
    public long testArrayList(int find, int sampleSize) {

        ArrayList<Integer> numbers = new ArrayList<>();
        // Fill the array with random integers
        for (int i = 0; i < sampleSize; i++) {
            numbers.add(i * 2);
        }

        // Get the start time
        long startTime = System.nanoTime();

        // Perform the search
        Integer result = binarySearch(numbers, find);

        // Get the end time
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        if (result >= 0) {
            //System.out.println("The number was found in arrayList position " + result);
        } else {
            //System.out.println("The number " + find + " is not in of the " + numbers.size() + " positions of the arrayList.");
        }
        return duration;
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

    /**
     * Perform a binarySearch on a given int array
     *
     * @param array
     * @param key
     * @return
     */
    public static int binarySearch(int[] array, int key) {

        int position, lower = 0, upper = array.length - 1;

        // To start, find the subscript of the middle position.
        position = (lower + upper) / 2;

        while ((array[position] != key) && (lower <= upper)) {
            if (array[position] > key) {
                upper = position - 1;
            } else {
                lower = position + 1;
            }
            position = (lower + upper) / 2;
        }

        return ((lower <= upper) ? position : -1);
    }
}
