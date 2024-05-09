/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun6;

import java.util.function.Predicate;

public class PredicateDemo1 {
    public static void main(String[] args) {
        boolean b1 = checkString("hello", s -> s.length() > 8);
        System.out.println(b1);

        boolean b2 = checkString("helloword", s -> s.length() > 8);
        System.out.println(b2);


    }

    private static boolean checkString(String s, Predicate<String> pre){
//        return pre.test(s);
        return pre.negate().test(s);
    }
}
