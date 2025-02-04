# Hướng dẫn biên dịch và chạy chương trình

## Biên dịch code
Để biên dịch code, chạy lệnh sau trong terminal:

```bash
javac -d bin src/com/student/rmi/*.java

## Chạy Server
Sau khi biên dịch, để khởi động Server, chạy lệnh sau:

```bash
java -cp bin com.student.rmi.Server

## Chạy Client
Để khởi động Client, chạy lệnh sau:
```bash
java -cp bin com.student.rmi.ClientGUI
