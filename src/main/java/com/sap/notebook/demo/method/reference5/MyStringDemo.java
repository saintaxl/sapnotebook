/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.method.reference5;

public class MyStringDemo {
    public static void main(String[] args) {
        useMyString((str, x, y) -> {
            String substring = str.substring(x, y);
            return substring;
        });

        useMyString(((str, x, y) -> str.substring(x, y)));

        useMyString(String::substring);
    }
    private static void useMyString(MyString myString){
        String s = myString.mySubString("Helloword", 2, 5);
        System.out.println(s);
    }
}
