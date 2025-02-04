package com.student.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            StudentManager studentManager = (StudentManager) registry.lookup("StudentManager");

            studentManager.addStudent(new Student("001", "Le Thanh Tung", 3.5, "K14", "2020-2024", "CNTT"));
            studentManager.addStudent(new Student("002", "Tran Thi B", 3.7, "K15", "2021-2025", "KT"));

            Student student = studentManager.getStudent("001");
            System.out.println("Student: " + student.getName() + ", GPA: " + student.getGpa() + 
                               ", Class: " + student.getClassName() + ", School Year: " + student.getSchoolYear() + 
                               ", Major: " + student.getMajor());

            studentManager.getAllStudents().forEach(s -> 
                System.out.println("ID: " + s.getId() + ", Name: " + s.getName() + ", GPA: " + s.getGpa() + 
                                   ", Class: " + s.getClassName() + ", School Year: " + s.getSchoolYear() + 
                                   ", Major: " + s.getMajor())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}