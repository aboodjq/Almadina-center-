package test;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;


public class AddMemorizationController implements Initializable {

	@FXML
    private ComboBox<String> NameComboBox;
    @FXML
    private ComboBox<String> memorizationTypeComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField startPageTextField;

    @FXML
    private TextField endPageTextField;

    @FXML
    private TextField mistakeCountTextField;

    @FXML
    private ComboBox<String> evaluationComboBox;



    private static final String INSERT_MEMORIZATION_QUERY = "INSERT INTO memorizationrecord (student_name,memorization_type, date, start_page, end_page, mistakes_count, evaluation) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String[] MEMORIZATION_TYPES = { "مراجعة", "حفظ" };
    private static final String[] EVALUATION_OPTIONS = {"جيد", "جيد جدا", "ممتاز"};
    private static final String Names_Query ="Select name from Student ";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	 populateMemorizationTypes();
         populateEvaluationOptions();   
         populateNameComboBox();

         }
    
    private void populateNameComboBox() {
        NameComboBox.getItems().clear();

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(Names_Query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                NameComboBox.getItems().add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch names from the database.", false);
        }
    }

    private void populateMemorizationTypes() {
        memorizationTypeComboBox.getItems().addAll(MEMORIZATION_TYPES);
    }

    private void populateEvaluationOptions() {
        evaluationComboBox.getItems().addAll(EVALUATION_OPTIONS);
    }
    @FXML
    private void saveMemorization() {
        String studentName = NameComboBox.getValue();
        String memorizationType = memorizationTypeComboBox.getValue();
        LocalDate date = datePicker.getValue(); // Use LocalDate instead of String
        String startPage = startPageTextField.getText();
        String endPage = endPageTextField.getText();
        String mistakeCount = mistakeCountTextField.getText();
        String evaluation = evaluationComboBox.getValue();

        if (studentName == null || memorizationType == null || date == null || startPage.isEmpty() || endPage.isEmpty()
                || mistakeCount.isEmpty() || evaluation.isEmpty()) {
            showAlert("Error", "Please fill in all fields.", false);
            return;
        }

        String dateString = date.toString(); // Convert LocalDate to String

        if (checkForExistingRecords(studentName, memorizationType, startPage, endPage)) {
            if (memorizationType.equals("Memorization")) {
                showAlert("Error", "You have already memorized pages " + startPage + "-" + endPage + ". Please revise instead.", false);
            } else {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmation Dialog");
                confirmAlert.setHeaderText("Duplicate Revision");
                confirmAlert.setContentText("You have already revised pages " + startPage + "-" + endPage +
                                             ". Do you want to add another revision record for these pages?");

                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    saveMemorizationToDatabase(studentName, memorizationType, dateString, startPage, endPage, mistakeCount, evaluation);
                }
            }
        } else {
            saveMemorizationToDatabase(studentName, memorizationType, dateString, startPage, endPage, mistakeCount, evaluation);
        }
    }


    private void saveMemorizationToDatabase(String studentName, String memorizationType, String date, String startPage, String endPage, String mistakeCount, String evaluation) {
        // Save memorization to database
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_MEMORIZATION_QUERY)) {
            statement.setString(1, studentName);
            statement.setString(2, memorizationType);
            statement.setString(3, date);
            statement.setString(4, startPage);
            statement.setString(5, endPage);
            statement.setString(6, mistakeCount);
            statement.setString(7, evaluation);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
            	showAlert("Success", "Memorization added successfully.", true);
                // Clear fields after successful insertion if needed
                clearFields();
            } else {
            	showAlert("Error", "Failed to add memorization.", false);
            }
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, boolean isSuccess) {
        Alert alert;
        if (isSuccess) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
        }
        alert.setHeaderText(null);
        alert.setContentText(message);

       

        alert.showAndWait();
    }

    private void clearFields() {
    	NameComboBox.getSelectionModel().clearSelection();
        memorizationTypeComboBox.getSelectionModel().clearSelection();
        datePicker.getEditor().clear();
        startPageTextField.clear();
        endPageTextField.clear();
        mistakeCountTextField.clear();
        evaluationComboBox.getSelectionModel().clearSelection();
    }
    private boolean checkForExistingRecords(String studentName, String memorizationType, String startPage, String endPage) {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM memorizationrecord " +
                     "WHERE student_name = ? AND memorization_type = ? AND start_page = ? AND end_page = ?")) {
            statement.setString(1, studentName);
            statement.setString(2, memorizationType);
            statement.setString(3, startPage);
            statement.setString(4, endPage);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
            return false;
        }
    }


    
}
