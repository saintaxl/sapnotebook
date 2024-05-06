/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.notebook.demo.method.reference6;

public class StudentDemo {
    public static void main(String[] args) {
        useStudentBuilder((name, age) -> {
            Student student = new Student(name, age);
            return student;
        });

        useStudentBuilder((name, age) -> new Student(name, age));

        useStudentBuilder(Student::new);
    }

    private static void useStudentBuilder(StudentBuilder studentBuilder){
        Student student = studentBuilder.build("wang", 30);
        System.out.println(student.getName() + "," + student.getAge());
    }
}
