package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MemorizationController {

    @FXML
    private Label nameLabel;

    @FXML
    private TableView<MemorizationRecord> memorizationTable;

    @FXML
    private TableColumn<MemorizationRecord, String> dateColumn;
    @FXML
    private TableColumn<MemorizationRecord, Integer> memorizationIdColumn;
    @FXML
    private TableColumn<MemorizationRecord, String> pagesNumberColumn;
 
    @FXML
    private TableColumn<MemorizationRecord, String> typeColumn;

    @FXML
    private TableColumn<MemorizationRecord, Integer> totalPagesColumn;

    @FXML
    private TableColumn<MemorizationRecord, String> evaluationColumn;
    
    @FXML
    private TableColumn<MemorizationRecord, Void> deleteColumn;

    private Student currentStudent; 

    // Method to set the current student and update the label
    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
        if (student != null) {
            nameLabel.setText("Name: " + student.getName());
        } else {
            nameLabel.setText("Name: N/A"); // Set a default value if student is null
        }
    }

    
    // Method to set memorization records in the table
    public void setMemorizationRecords(ObservableList<MemorizationRecord> records) {
        memorizationTable.setItems(records);
    }

    // Initialize method to set up table columns
    @FXML
    private void initialize() {
    	memorizationIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        totalPagesColumn.setCellValueFactory(cellData -> cellData.getValue().totalPagesProperty().asObject());
        pagesNumberColumn.setCellValueFactory(cellData -> cellData.getValue().startPageProperty().asString().concat("-").concat(cellData.getValue().endPageProperty().asString()));
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().memorizationTypeProperty());
        evaluationColumn.setCellValueFactory(cellData -> cellData.getValue().evaluationProperty());
        
        // Delete icon column
        deleteColumn.setCellFactory(new Callback<TableColumn<MemorizationRecord, Void>, TableCell<MemorizationRecord, Void>>() {
            @Override
            public TableCell<MemorizationRecord, Void> call(final TableColumn<MemorizationRecord, Void> param) {
                return new TableCell<MemorizationRecord, Void>() {
                    private final Button deleteButton = new Button();

                    {
                        deleteButton.getStyleClass().add("delete-button");
                        deleteButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("x-button.png"))));
                        ((ImageView) deleteButton.getGraphic()).setFitWidth(26);
                        ((ImageView) deleteButton.getGraphic()).setFitHeight(26);
                        deleteButton.setOnAction(event -> {
                            // Handle delete button click
                            MemorizationRecord record = getTableView().getItems().get(getIndex());
                            deleteRecord(record);
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
    }
   
    private void deleteRecord(MemorizationRecord record) {
        // Prompt confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Record");
        alert.setContentText("Are you sure you want to delete this record?");

        // Wait for user response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove record from TableView
            memorizationTable.getItems().remove(record);

            // Delete record from database
            try (Connection connection = DatabaseConnectionManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM memorizationrecord WHERE record_id  = ?")) {
                statement.setInt(1, record.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database error
            }
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
 
    }


