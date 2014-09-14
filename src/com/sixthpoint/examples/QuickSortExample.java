package com.sixthpoint.examples;

import java.util.ArrayList;
import java.util.Random;

/**
 * QuickSort an arrayList example
 *
 * @author sixthpoint
 */
public class QuickSortExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // low is the lowest possible value, high is the highest possible value, size is the number of values to add
        int low = 0, high = 10000, size = 100;

        ArrayList<Integer> numbers = new ArrayList<>();
        Random r = new Random();

        // Fill the array with random integers
        for (int i = 0; i < size; i++) {
            numbers.add(r.nextInt(high - low) + low);
        }

        quickSort(numbers);

        // Output the sorted arraylist using lambda's
        numbers.stream().forEach((num1) -> {
            System.out.println(num1);
        });
    }

    /**
     * Overload method for quick sort
     *
     * @param array
     */
    public static void quickSort(ArrayList<Integer> array) {
        quickSort(array, 0, array.size() - 1);
    }

    /**
     * Sorts a array by finding the middle then dividing and conquering
     *
     * @param array
     * @param start
     * @param end
     */
    public static void quickSort(ArrayList<Integer> array, int start, int end) {

        // Align the left and right starting positions
        int leftPos = start;
        int rightPos = end;

        // if there is only one element in the partition, do not do any sorting
        if ((end - start) < 1) {
            return;
        }

        int pivot = array.get(start);

        // keep scanning because the left and right indecies have not met
        while (rightPos > leftPos) {

            while (array.get(leftPos) <= pivot && leftPos <= end && rightPos > leftPos) {
                leftPos++; // start left look for element greater than the pivot
            }

            while (array.get(rightPos) > pivot && rightPos >= start && rightPos >= leftPos) {
                rightPos--; // from right look for element not greater than the pivot
            }

            if (rightPos > leftPos) {
                swap(array, leftPos, rightPos); // if the left index is still smaller than the right index, swap the corresponding elements
            }
        }

        // after the indices have crossed, swap the last element in the left partition with the pivot
        swap(array, start, rightPos);

        // quicksort the left partition
        quickSort(array, start, rightPos - 1);

        // quicksort the right partition
        quickSort(array, rightPos + 1, end);
    }

    /**
     * Swaps indexes in a given array
     *
     * @param array
     * @param index1
     * @param index2
     */
    public static void swap(ArrayList<Integer> array, int index1, int index2) {
        int temp = array.get(index1);
        array.set(index1, array.get(index2));
        array.set(index2, temp);
    }
}
