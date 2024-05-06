package com.sap.notebook.demo.method.reference2;

import com.sap.notebook.demo.method.reference2.Printable;

public class PrintableDemo {

    public static void main(String[] args) {
        usePrintable(i -> System.out.println(i));

        usePrintable(i -> System.out.println(i));

        usePrintable(System.out::println);

    }

    private static void usePrintable(Printable p) {
        p.printInt(888);
    }

}
