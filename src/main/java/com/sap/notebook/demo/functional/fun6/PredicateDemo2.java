/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun6;

import java.util.function.Predicate;

public class PredicateDemo2 {
    public static void main(String[] args) {
        boolean b1 = checkString("hello", s -> s.length() > 8, s -> s.length() < 15);
        System.out.println(b1);
    }

    private static boolean checkString(String s, Predicate<String> pre1,Predicate<String> pre2){
//        boolean b1 = pre1.test(s);
//        boolean b2 = pre2.test(s);
//        return b1 && b2;

        return pre1.and(pre2).test(s);
    }
}
