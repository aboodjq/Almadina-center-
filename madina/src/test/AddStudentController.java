package test;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStudentController implements Initializable{
	@FXML
    private ComboBox<String> gradeField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField familyPhoneField;


    @FXML
    private TextField addressField;

    private static final String INSERT_QUERY = "INSERT INTO student (name, family_phone, grade, address) VALUES (?, ?, ?, ?)";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gradeField.getItems().addAll("الصف الخامس", "الصف السادس", "الصف السابع");
        // Add more grades as needed
    }
    public void saveStudent() {
        String name = nameField.getText();
        String familyPhone = familyPhoneField.getText();
        String grade = gradeField.getValue();
        String address = addressField.getText();

        if (name.isEmpty() || grade == null || grade.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Name and grade cannot be empty!");
            alert.showAndWait();
            return; // Exit the method if validation fails
        }
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {

            statement.setString(1, name);
            statement.setString(2, familyPhone);
            statement.setString(3, grade);
            statement.setString(4, address);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Student added successfully, show a success message
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Student added successfully!");
                alert.showAndWait();

                closeCurrentWindow();
            } else {
                // No rows were affected, indicate an error occurred
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to add student!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exception
        }
    }
    
    private void closeCurrentWindow() {
        // Get the current window
        Stage stage = (Stage) nameField.getScene().getWindow();

        // Close the window
        stage.close();
    }
}
    