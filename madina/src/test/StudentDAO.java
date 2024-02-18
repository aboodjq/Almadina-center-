package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    // SQL queries
    private static final String SELECT_ALL_QUERY = "SELECT * FROM students";
    private static final String INSERT_QUERY = "INSERT INTO students (student_id, name, family_phone, grade, address) VALUES (?, ?, ?, ?, ?)";

    // Method to retrieve all students from the database
    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String name = resultSet.getString("name");
                String familyPhone = resultSet.getString("family_phone");
                String grade = resultSet.getString("grade");
                String address = resultSet.getString("address");
                students.add(new Student(studentId, name, familyPhone, grade, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

//     Method to insert a new student into the database
    public static void addStudent(Student student) {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {

            statement.setInt(1, student.getStudentId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getFamilyPhone());
            statement.setString(4, student.getGrade());
            statement.setString(5, student.getAddress());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//     You can similarly implement update and delete methods based on your requirements.
}
