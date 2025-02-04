package com.student.rmi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientGUI extends JFrame {
    private StudentManager studentManager;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, gpaField, classField, yearField, majorField;

    public ClientGUI() {
        setTitle("Quản lý Sinh viên");
        setSize(1080, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel nhập thông tin sinh viên
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Nhập thông tin sinh viên"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        addLabelAndField(inputPanel, gbc, "ID:", idField = new JTextField(15), 0, 0);
        addLabelAndField(inputPanel, gbc, "Họ tên:", nameField = new JTextField(15), 0, 1);
        addLabelAndField(inputPanel, gbc, "GPA:", gpaField = new JTextField(15), 0, 2);
        addLabelAndField(inputPanel, gbc, "Lớp:", classField = new JTextField(15), 0, 3);
        addLabelAndField(inputPanel, gbc, "Niên khóa:", yearField = new JTextField(15), 0, 4);
        addLabelAndField(inputPanel, gbc, "Chuyên ngành:", majorField = new JTextField(15), 0, 5);

        // Panel nút chức năng
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = createButton("Thêm Sinh viên", new AddStudentListener());
        JButton viewButton = createButton("Xem Danh sách", new ViewStudentsListener());
        JButton deleteButton = createButton("Xóa Sinh viên", new DeleteStudentListener());
        JButton updateButton = createButton("Chỉnh sửa Sinh viên", new UpdateStudentListener());
        JButton searchButton = createButton("Tìm kiếm", new SearchStudentListener());

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(searchButton);

        // Bảng hiển thị danh sách sinh viên
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Họ tên");
        tableModel.addColumn("GPA");
        tableModel.addColumn("Lớp");
        tableModel.addColumn("Niên khóa");
        tableModel.addColumn("Chuyên ngành");

        studentTable = new JTable(tableModel);
        studentTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách sinh viên"));

        // Thêm các panel vào frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        connectToServer();

        setVisible(true);
    }

    private class SearchStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Tạo JDialog để hiển thị các trường tìm kiếm
            JDialog searchDialog = new JDialog(ClientGUI.this, "Tìm kiếm sinh viên", true);
            searchDialog.setLayout(new GridLayout(7, 2, 10, 10));
            searchDialog.setSize(400, 300);

            JTextField searchIdField = new JTextField();
            JTextField searchNameField = new JTextField();
            JTextField searchGpaField = new JTextField();
            JTextField searchClassField = new JTextField();
            JTextField searchYearField = new JTextField();
            JTextField searchMajorField = new JTextField();

            searchDialog.add(new JLabel("ID:"));
            searchDialog.add(searchIdField);
            searchDialog.add(new JLabel("Họ tên:"));
            searchDialog.add(searchNameField);
            searchDialog.add(new JLabel("GPA:"));
            searchDialog.add(searchGpaField);
            searchDialog.add(new JLabel("Lớp:"));
            searchDialog.add(searchClassField);
            searchDialog.add(new JLabel("Niên khóa:"));
            searchDialog.add(searchYearField);
            searchDialog.add(new JLabel("Chuyên ngành:"));
            searchDialog.add(searchMajorField);

            JButton searchButton = new JButton("Tìm kiếm");
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String id = searchIdField.getText().trim();
                        String name = searchNameField.getText().trim();
                        String gpa = searchGpaField.getText().trim();
                        String className = searchClassField.getText().trim();
                        String schoolYear = searchYearField.getText().trim();
                        String major = searchMajorField.getText().trim();

                        List<Student> searchResults = studentManager.searchStudents(id, name, gpa, className, schoolYear, major);
                        refreshTable(searchResults);
                        searchDialog.dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(searchDialog, "Lỗi khi tìm kiếm sinh viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            searchDialog.add(searchButton);
            searchDialog.setVisible(true);
        }
    }

    private void refreshTable(List<Student> students) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ

        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getGpa(),
                    student.getClassName(),
                    student.getSchoolYear(),
                    student.getMajor()
            });
        }
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField textField, int x, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            studentManager = (StudentManager) registry.lookup("StudentManager");
            JOptionPane.showMessageDialog(this, "Đã kết nối server thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối server: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private class AddStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText().trim();

                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(ClientGUI.this, "Chưa nhập ID Sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Student existingStudent = studentManager.getStudent(id);
                if (existingStudent != null) {
                    JOptionPane.showMessageDialog(ClientGUI.this, "ID đã tồn tại! Vui lòng nhập ID khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String name = nameField.getText();
                double gpa = Double.parseDouble(gpaField.getText());
                String className = classField.getText();
                String schoolYear = yearField.getText();
                String major = majorField.getText();

                studentManager.addStudent(new Student(id, name, gpa, className, schoolYear, major));
                JOptionPane.showMessageDialog(ClientGUI.this, "Đã thêm sinh viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ClientGUI.this, "GPA phải là một số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Lỗi khi thêm sinh viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class ViewStudentsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Lỗi khi lấy danh sách sinh viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class DeleteStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText().trim();

                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(ClientGUI.this, "Chưa nhập ID Sinh viên muốn xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Student student = studentManager.getStudent(id);
                if (student == null) {
                    JOptionPane.showMessageDialog(ClientGUI.this, "ID này không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                studentManager.removeStudent(id);
                JOptionPane.showMessageDialog(ClientGUI.this, "Đã xóa sinh viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Lỗi khi xóa sinh viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class UpdateStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText();
                String newName = nameField.getText();
                double newGpa = Double.parseDouble(gpaField.getText());
                String newClassName = classField.getText();
                String newSchoolYear = yearField.getText();
                String newMajor = majorField.getText();

                studentManager.updateStudent(id, newName, newGpa, newClassName, newSchoolYear, newMajor);
                JOptionPane.showMessageDialog(ClientGUI.this, "Đã cập nhật sinh viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Lỗi khi cập nhật sinh viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        gpaField.setText("");
        classField.setText("");
        yearField.setText("");
        majorField.setText("");
    }

    private void refreshTable() {
        try {
            List<Student> students = studentManager.getAllStudents();
            tableModel.setRowCount(0);

            for (Student student : students) {
                tableModel.addRow(new Object[]{
                        student.getId(),
                        student.getName(),
                        student.getGpa(),
                        student.getClassName(),
                        student.getSchoolYear(),
                        student.getMajor()
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI());
    }
}