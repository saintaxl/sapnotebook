/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun4;

import java.util.function.Supplier;

public class SupplierDemo {
    public static void main(String[] args) {
        String string = getString(() -> "abc");
        System.out.println(string);

        Integer integer = getInteger(() -> 1);
        System.out.println(integer);

    }

    private static String getString(Supplier<String> sup){
        return sup.get();
    }

    private static Integer getInteger(Supplier<Integer> sup){
        return sup.get();
    }
}
