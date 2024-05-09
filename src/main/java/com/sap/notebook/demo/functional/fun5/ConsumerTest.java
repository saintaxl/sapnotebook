/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun5;

import java.util.function.Consumer;

public class ConsumerTest {
    public static void main(String[] args) {

        String[] strArrays = new String[]{"shawn,30", "jack,34", "eric,49"};

        printInfo(strArrays, string -> {
            String name = string.split(",")[0];
            System.out.println(name);
        }, string -> {
            int age = Integer.parseInt(string.split(",")[1]);
            System.out.println(age);
        });

    }

    private static void printInfo(String[] stringArray, Consumer<String> con1, Consumer<String> con2){
        for (String string: stringArray){
            con1.andThen(con2).accept(string);
        }
    }
}
