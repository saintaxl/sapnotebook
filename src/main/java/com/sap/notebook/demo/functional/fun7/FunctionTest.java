/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun7;

import java.util.function.Function;

public class FunctionTest {
    public static void main(String[] args) {

        String str = "shawn,30";

        convert(str, string -> str.split(",")[1], string -> Integer.parseInt(string), integer -> integer + 70);
    }

    private static void convert(String string, Function<String,String> fun1, Function<String,Integer> fun2, Function<Integer,Integer> fun3) {
        Integer result = fun1.andThen(fun2).andThen(fun3).apply(string);
        System.out.println(result);
    }
}
