package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class MainPageController {

	@FXML
    private Button refreshButton;

	@FXML
	private TableView<Student> studentTable;

	@FXML
	private TableColumn<Student, String> addressColumn;

	@FXML
	private TableColumn<Student, String> familyPhoneColumn;

	@FXML
	private TableColumn<Student, String> gradeColumn;

	@FXML
	private TableColumn<Student, String> nameColumn;

	@FXML
	private TableColumn<Student, Integer> studentIdColumn;
	
	@FXML
    private TableColumn<Student, Void> arrowColumn;

    @FXML
    private TableColumn<Student, Void> deleteColumn;
    
    @FXML
    private Button addStudentButton;
    
    private ObservableList<Student> studentData = FXCollections.observableArrayList();

   
    public void initialize() {
        // Initialize TableView columns
    	studentIdColumn.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        familyPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().familyPhoneProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        // Delete icon column
        deleteColumn.setCellFactory(new Callback<TableColumn<Student, Void>, TableCell<Student, Void>>() {
            @Override
            public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
                return new TableCell<Student, Void>() {
                    private final Button deleteButton = new Button();

                    {
                    	deleteButton.getStyleClass().add("delete-button");
                        deleteButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("x-button.png"))));
                        ((ImageView) deleteButton.getGraphic()).setFitWidth(26);
                        ((ImageView) deleteButton.getGraphic()).setFitHeight(26);
                        deleteButton.setOnAction(event -> {
                            // Handle delete button click
                            Student student = getTableView().getItems().get(getIndex());
                            deleteStudent(student);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });
      
     // Arrow icon column
        arrowColumn.setCellFactory(new Callback<TableColumn<Student, Void>, TableCell<Student, Void>>() {
            @Override
            public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
                return new TableCell<Student, Void>() {
                    private final Button arrowButton = new Button();

                    {
                        arrowButton.getStyleClass().add("arrow-button");
                        arrowButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("left-arrow.png"))));
                        ((ImageView) arrowButton.getGraphic()).setFitWidth(26);
                        ((ImageView) arrowButton.getGraphic()).setFitHeight(26);
                        arrowButton.setOnAction(event -> {
                            // Handle arrow button click
                            Student student = getTableView().getItems().get(getIndex());
                            navigateToMemorizationPage(student);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(arrowButton);
                        }
                    }
                };
            }
        });
        // Populate student table
        populateStudentTable();
    }

 // Method to delete a student from the TableView and data source
    private void deleteStudent(Student student) {
        // Prompt confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Student");
        alert.setContentText("Are you sure you want to delete this student?");

        // Wait for user response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove student from TableView
            studentTable.getItems().remove(student);
            
            // Delete student from database
            try (Connection connection = DatabaseConnectionManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM student WHERE student_id = ?")) {
                statement.setInt(1, student.getStudentId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database error
            }
        }
    }
    
    public void navigateToAddStudent()
    {
    	try {
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddStudent.fxml"));
    		Parent root = loader.load();
//    		AddStudentController controller = loader.getController();
    		
    		Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("AddStudent Page");
            stage.show();
    	}
    	
    	catch(IOException e)
    	{
    		e.printStackTrace();
    }
    }

    private void navigateToMemorizationPage(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MemorizationPage.fxml"));
            Parent root = loader.load();

            MemorizationController controller = loader.getController();

            // Retrieve memorization data for the selected student from the database
            ObservableList<MemorizationRecord> memorizationRecords = FXCollections.observableArrayList();
            try (Connection connection = DatabaseConnectionManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT mr.record_id, mr.date, mr.memorization_type, mr.total_pages, mr.start_page, mr.end_page, mr.mistakes_count, mr.evaluation FROM memorizationrecord mr JOIN student s ON mr.student_name = s.name WHERE s.name = ?")) {
                statement.setString(1, student.getName());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String date = resultSet.getString("date");
                        String memorizationType = resultSet.getString("memorization_type");
                        int id= resultSet.getInt("record_id");
                        int startPage = resultSet.getInt("start_page");
                        int endPage = resultSet.getInt("end_page");
                        int mistakesCount = resultSet.getInt("mistakes_count");
                        String evaluation = resultSet.getString("evaluation");
                        MemorizationRecord record = new MemorizationRecord(id,date, startPage, endPage, memorizationType, mistakesCount, evaluation);
                        memorizationRecords.add(record);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle errors
            }

  

            // Pass the memorization data to the controller
            controller.setMemorizationRecords(memorizationRecords);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Memorization Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefreshButtonAction() {
        populateStudentTable();
    }
    // Method to retrieve student data from the database and populate the TableView
    public void populateStudentTable() {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM student");
             ResultSet resultSet = statement.executeQuery()) {

            studentData.clear(); // Clear previous data

            while (resultSet.next()) {
                // Retrieve student data from ResultSet
                int studentId = resultSet.getInt("student_id");
                String name = resultSet.getString("name");
                String familyPhone = resultSet.getString("family_phone");
                String grade = resultSet.getString("grade");
                String address = resultSet.getString("address");
                // Add data to ObservableList
                studentData.add(new Student(studentId, name, familyPhone, grade, address));
            }

            // Populate TableView with data
            studentTable.setItems(studentData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle errors
        }
    }
    
    @FXML
    private void navigateToAddMemorization() {
        try {
            // Load the Add Memorization FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddMemorization.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            AddMemorizationController addMemorizationController = loader.getController();
            

            // Set up the scene
            Scene scene = new Scene(root);
            
            // Set the stage
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Memorization");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void navigateToSummary() {
        try {
            // Load the Add Memorization FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Summary.fxml"));
            Parent root = loader.load();

            // Get the controller instance
             SummaryPageController summaryPageController = loader.getController();
            

            // Set up the scene
            Scene scene = new Scene(root);
            
            // Set the stage
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Summary");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


