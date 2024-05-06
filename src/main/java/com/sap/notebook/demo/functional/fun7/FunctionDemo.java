/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun7;

import java.util.function.Function;

public class FunctionDemo {
    public static void main(String[] args) {

        convert("199", string -> Integer.parseInt(string));
        convert("199", Integer::parseInt);

        convert(100, integer -> String.valueOf(integer + 533));

        convert("100", string -> Integer.parseInt(string), i -> String.valueOf(i + 566));
    }

    private static void convert(String string, Function<String, Integer> fun){
        Integer result = fun.apply(string);
        System.out.println(result);
    }

    private static void convert(int i, Function<Integer, String> fun){
        String result = fun.apply(i);
        System.out.println(result);
    }

    private static void convert(String str, Function<String, Integer> fun1, Function<Integer, String> fun2){
//        Integer i = fun1.apply(str);
//        String result = fun2.apply(i);
//        System.out.println(result);

        String result = fun1.andThen(fun2).apply(str);
        System.out.println(result);
    }
}
