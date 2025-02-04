package com.student.rmi;

import java.io.Serializable;

public class Student implements Serializable {
    private String id;
    private String name;
    private double gpa; // Thay age bằng gpa
    private String className;
    private String schoolYear;
    private String major;

    public Student(String id, String name, double gpa, String className, String schoolYear, String major) {
        this.id = id;
        this.name = name;
        this.gpa = gpa; // Thay age bằng gpa
        this.className = className;
        this.schoolYear = schoolYear;
        this.major = major;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getGpa() { return gpa; } // Thay getAge bằng getGpa
    public String getClassName() { return className; }
    public String getSchoolYear() { return schoolYear; }
    public String getMajor() { return major; }

    public void setName(String name) { this.name = name; }
    public void setGpa(double gpa) { this.gpa = gpa; } // Thay setAge bằng setGpa
    public void setClassName(String className) { this.className = className; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }
    public void setMajor(String major) { this.major = major; }
}