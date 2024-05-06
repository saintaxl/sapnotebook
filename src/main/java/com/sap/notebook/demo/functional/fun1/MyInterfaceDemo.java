/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun1;

public class MyInterfaceDemo {
    public static void main(String[] args) {
        MyInterface my = () -> System.out.println("Functional Interface");
        my.show();
    }
}
