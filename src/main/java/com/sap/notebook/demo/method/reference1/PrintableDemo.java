/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.method.reference1;

public class PrintableDemo {

    public static void main(String[] args) {
        usePrintable((String str) -> {
            System.out.println(str);
        });

        usePrintable(str -> System.out.println(str));

        usePrintable(System.out::println);

    }

    private static void usePrintable(Printable p) {
        p.printString("SAP");
    }

}
