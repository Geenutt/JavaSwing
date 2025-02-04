package com.student.rmi;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class StudentManagerImpl extends UnicastRemoteObject implements StudentManager {
    private List<Student> students;

    protected StudentManagerImpl() throws RemoteException {
        students = new ArrayList<>();
    }

    @Override
    public void addStudent(Student student) throws RemoteException {
        students.add(student);
    }

    @Override
    public void removeStudent(String id) throws RemoteException {
        students.removeIf(student -> student.getId().equals(id));
    }

    @Override
    public Student getStudent(String id) throws RemoteException {
        return students.stream()
         .filter(student -> student.getId().equals(id))
         .findFirst()
         .orElse(null);
    }

    @Override
    public List<Student> getAllStudents() throws RemoteException {
        return students;
    }

    @Override
    public void updateStudent(String id, String newName, double newGpa, String newClassName, String newSchoolYear, String newMajor) throws RemoteException {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                student.setName(newName);
                student.setGpa(newGpa); // Thay setAge bằng setGpa
                student.setClassName(newClassName);
                student.setSchoolYear(newSchoolYear);
                student.setMajor(newMajor);
                break;
            }
        }
    }

    @Override
    public List<Student> searchStudents(String id, String name, String gpa, String className, String schoolYear, String major) throws RemoteException {
    List<Student> searchResults = new ArrayList<>();

    for (Student student : students) {
        boolean match = true;

        // Kiểm tra từng trường dữ liệu
        if (!id.isEmpty() && !student.getId().contains(id)) match = false;
        if (!name.isEmpty() && !student.getName().contains(name)) match = false;
        if (!gpa.isEmpty() && !String.valueOf(student.getGpa()).contains(gpa)) match = false;
        if (!className.isEmpty() && !student.getClassName().contains(className)) match = false;
        if (!schoolYear.isEmpty() && !student.getSchoolYear().contains(schoolYear)) match = false;
        if (!major.isEmpty() && !student.getMajor().contains(major)) match = false;

        if (match) {
            searchResults.add(student);
        }
    }

    return searchResults;
    }
}