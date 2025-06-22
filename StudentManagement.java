import java.sql.*;
import java.util.Scanner;

public class StudentManagement {
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";       // your MySQL username
    static final String PASS = "root";   // your MySQL password

    public static void main(String[] args) {
        try (
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Scanner sc = new Scanner(System.in);
        ) {
            while (true) {
                System.out.println("\n1. Add Student\n2. View All Students\n3. Search Student\n4. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Roll No: ");
                        int roll = sc.nextInt();
                        sc.nextLine(); // consume newline
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Grade: ");
                        String grade = sc.nextLine();

                        String insertSQL = "INSERT INTO students (roll_no, name, grade) VALUES (?, ?, ?)";
                        try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                            stmt.setInt(1, roll);
                            stmt.setString(2, name);
                            stmt.setString(3, grade);
                            stmt.executeUpdate();
                            System.out.println("Student added successfully!");
                        }
                        break;

                    case 2:
                        String selectSQL = "SELECT * FROM students";
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(selectSQL)) {
                            System.out.println("\nStudent List:");
                            while (rs.next()) {
                                System.out.println("Roll No: " + rs.getInt("roll_no") +
                                        ", Name: " + rs.getString("name") +
                                        ", Grade: " + rs.getString("grade"));
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter Roll No to Search: ");
                        int searchRoll = sc.nextInt();
                        String searchSQL = "SELECT * FROM students WHERE roll_no = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(searchSQL)) {
                            stmt.setInt(1, searchRoll);
                            ResultSet rs = stmt.executeQuery();
                            if (rs.next()) {
                                System.out.println("Found: Name = " + rs.getString("name") +
                                        ", Grade = " + rs.getString("grade"));
                            } else {
                                System.out.println("Student not found!");
                            }
                        }
                        break;

                    case 4:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}