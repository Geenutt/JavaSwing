package com.student.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            StudentManagerImpl studentManager = new StudentManagerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("StudentManager", studentManager);
            System.out.println("Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}