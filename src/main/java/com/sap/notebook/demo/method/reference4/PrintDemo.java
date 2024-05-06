/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.method.reference4;

public class PrintDemo {
    public static void main(String[] args) {
        usePrinter(str -> System.out.println(str.toUpperCase()));

        PrintString ps = new PrintString();
        usePrinter(ps::printUpper);
    }

    private static void usePrinter(Printer p){
        p.pringUpperCase("Helloword");
    }
}
