/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun4;

import java.util.function.Supplier;

public class SupplierTest {

    public static void main(String[] args) {

        int[] arr = {19, 50, 28, 37, 46};

        int maxValue = getMax(() -> {
            int max = arr[0];
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                }
            }
            return max;
        });

        System.out.println(maxValue);

    }

    private static int getMax(Supplier<Integer> supplier){
        return supplier.get();
    }
}
