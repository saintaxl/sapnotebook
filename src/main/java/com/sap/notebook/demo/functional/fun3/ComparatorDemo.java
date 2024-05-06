/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ComparatorDemo {
    public static void main(String[] args) {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("abcde");
        arrayList.add("abcdefghi");
        arrayList.add("abcdefg");

        System.out.println(arrayList);

        Collections.sort(arrayList);
        System.out.println(arrayList);

        Collections.sort(arrayList, getComparator());
    }

    private static Comparator<String> getComparator(){
//        Comparator<String> comparator = new Comparator<String>() {
//            @Override
//            public int compare(String s1, String s2) {
//                return s1.length() - s2.length();
//            }
//        };
//        return comparator;

        return (s1, s2) -> s1.length() - s2.length();
    }
}
