/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun5;

import java.util.function.Consumer;

public class ConsumerDemo {
    public static void main(String[] args) {

        operatorString("shawn", string -> System.out.println(string));


        operatorString("shawn1", System.out::println);

        operatorString("shawn2", string -> {
            String result = new StringBuffer(string).reverse().toString();
            System.out.println(result);
        });

        System.out.println("----------------");

        operatorString("shawn3", string -> System.out.println(string), string -> System.out.println(new StringBuffer(string).reverse()));
    }

    private static void operatorString(String name, Consumer<String> con1,Consumer<String> con2){
//        con1.accept(name);
//        con2.accept(name);

        con1.andThen(con2).accept(name);
    }

    private static void operatorString(String name, Consumer<String> con){
        con.accept(name);
    }
}
