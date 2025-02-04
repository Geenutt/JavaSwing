package com.student.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StudentManager extends Remote {
    void addStudent(Student student) throws RemoteException;
    void removeStudent(String id) throws RemoteException;
    Student getStudent(String id) throws RemoteException;
    List<Student> getAllStudents() throws RemoteException;
    void updateStudent(String id, String newName, double newGpa, String newClassName, String newSchoolYear, String newMajor) throws RemoteException;
    List<Student> searchStudents(String id, String name, String gpa, String className, String schoolYear, String major) throws RemoteException; // Thêm phương thức tìm kiếm
}