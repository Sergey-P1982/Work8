package com.epam.task8_4;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Runner {
    private static int numberOfElements = 1_000_000;
    private static int[] intArray = createRandomArray();
    private static final int NUMBER_THREADS = 8;

    public void run() {
        System.out.println(Runtime.getRuntime().availableProcessors() + " cores are in this processor");
        long startTime = System.nanoTime();
        ForkJoinPool pool = new ForkJoinPool(NUMBER_THREADS);
        long computedSum = pool.invoke(new RecArrayElemsSum(0, numberOfElements - 1));
        long finishTime = System.nanoTime();

        System.out.println("Total sum with ForkJoinPool usage = " + computedSum + ", execution time = " +
                (finishTime - startTime) / 1_000_000 + " ms");

        countBySimpleLoop();
    }

    public static void countBySimpleLoop() {
        long startTime = System.nanoTime();
        long count = 0;
        for (int i = 0; i < numberOfElements; i++) {
            count += intArray[i];
        }
        long finishTime = System.nanoTime();
        System.out.println("Total sum by simple array counter = " + count + ", execution time = "
                + (finishTime - startTime) / 1_000_000 + " ms");
    }

    private static int[] createRandomArray() {
        Random random = new Random();
        int[] randomArray = new int[numberOfElements];
        for (int i = 0; i < numberOfElements; i++) {
            randomArray[i] = random.nextInt(100);
        }

        return randomArray;
    }

    static class RecArrayElemsSum extends RecursiveTask<Long> {
        int startIndex, finishIndex;

        public RecArrayElemsSum(int startIndex, int finishIndex) {
            this.startIndex = startIndex;
            this.finishIndex = finishIndex;
        }

        @Override
        protected Long compute() {
            if ((finishIndex - startIndex) < 20) {
                long localSum = 0;
                for (int i = startIndex; i <= finishIndex; i++) {
                    localSum += intArray[i];
                }
//                System.out.println("Start index " + startIndex + " to finishIndex " + finishIndex + " sum = " + localSum);
                return localSum;
            } else {
                int midIndex = (finishIndex + startIndex) / 2;
//                System.out.println("Forking " + startIndex + " to "+ midIndex + " and from " + (midIndex+1) + " to " + finishIndex);
                RecArrayElemsSum firstHalf = new RecArrayElemsSum(startIndex, midIndex);
                firstHalf.fork();
                RecArrayElemsSum secondHalf = new RecArrayElemsSum(midIndex + 1, finishIndex);
                long secondResult = secondHalf.compute();
                return firstHalf.join() + secondResult;

            }

        }
    }
}
