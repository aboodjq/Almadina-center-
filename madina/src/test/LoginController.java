package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

   
    private static final String LOGIN_QUERY = "SELECT * FROM instructor WHERE username = ? AND password = ?";

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(LOGIN_QUERY)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Login successful
                    String fullName = resultSet.getString("full_name");
                    showInfoAlert("Login Successful", "Welcome, " + fullName + "!");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
                    Parent root = loader.load();

                    // Pass username to MainPageController
                    MainPageController mainPageController = loader.getController();
              
                    // Retrieve student data and populate table
                    mainPageController.populateStudentTable();

                    // Set the new scene
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    // Invalid credentials
                    showErrorAlert("Login Failed", "Invalid username or password.");
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle database connection or query errors
            showErrorAlert("Error", "An error occurred while processing your request.");
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
