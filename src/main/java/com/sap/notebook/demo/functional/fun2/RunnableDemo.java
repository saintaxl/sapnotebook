/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.functional.fun2;

public class RunnableDemo {
    public static void main(String[] args) {

        startThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });

        startThread(() -> System.out.println(Thread.currentThread().getName()));
    }

    private static void startThread(Runnable r){
        new Thread(r).start();
    }
}
