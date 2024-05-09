/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.method.reference3;

public class ConverterDemo {
    public static void main(String[] args) {

        useConverter(str -> Integer.parseInt(str));

        useConverter(Integer::parseInt);
    }

    private static void useConverter(Converter c){
        int number = c.convert("666");
        System.out.println(number);
    }
}
