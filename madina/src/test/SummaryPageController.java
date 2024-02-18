package test;

import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;


import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;


public class SummaryPageController {

    @FXML
    private TableView<SummaryRecord> summaryTableView;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableColumn<SummaryRecord, String> studentNameColumn;

    @FXML
    private TableColumn<SummaryRecord, Integer> totalPagesColumn;

    @FXML
    private TableColumn<SummaryRecord, String> evaluationColumn;
    
    @FXML
    private TableColumn<SummaryRecord, String> memorizationTypeColumn;
    @FXML
    public void initialize() {
        // Initialize the columns with the appropriate cell value factories
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        totalPagesColumn.setCellValueFactory(cellData -> cellData.getValue().totalPagesProperty().asObject());
        evaluationColumn.setCellValueFactory(cellData -> cellData.getValue().evaluationProperty());
        memorizationTypeColumn.setCellValueFactory(cellData -> cellData.getValue().memorizationTypeProperty());


        // Set the date picker's initial value to today
        datePicker.setValue(LocalDate.now());

        // Populate the table with data based on the selected date
        populateSummaryTable();
    }

    @FXML
    public void populateSummaryTable() {
        // Clear existing data from the table
        summaryTableView.getItems().clear();

        // Query the database for summary records based on the selected date
        LocalDate selectedDate = datePicker.getValue();
        String dateString = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        String query = "SELECT student_name, total_pages, memorization_type, evaluation FROM memorizationrecord WHERE date = ?";
        
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, dateString);
            ResultSet resultSet = statement.executeQuery();

           
            while (resultSet.next()) {
                String studentName = resultSet.getString("student_name");
                int totalPages = resultSet.getInt("total_pages");
                String type= resultSet.getString("memorization_type");
                String evaluation = resultSet.getString("evaluation");
                SummaryRecord record = new SummaryRecord(studentName, totalPages, type, evaluation);
                summaryTableView.getItems().add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }

    @FXML
    private void downloadSummaryAsPDF()  {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as PDF");
        fileChooser.setInitialFileName("تقرير الحفاظ.pdf");
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try {
                createPDF(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error
            }
        }
    }

    private void createPDF(File file) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Load your custom font from file
                PDType0Font font = PDType0Font.load(document, new File("C:\\Users\\TCC\\eclipse-workspace\\madina\\src\\test\\AdobeArabicRegular.ttf"));

                // Set the loaded font and size
                contentStream.setFont(font, 12);
                ArabicLigaturizer arabicLigaturizer = new ArabicLigaturizer();

                // Table headers
                float yPosition = 700;
                float pageWidth = page.getMediaBox().getWidth();
                float cellWidth = 150;
                float cellHeight = 20;

                // Calculate the x-position to start from the right edge of the page
                float xPosition = pageWidth - cellWidth;
                
             // Draw the table headers
                drawCellBorder(contentStream, xPosition, yPosition, cellWidth, cellHeight);
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition + 2, yPosition - 12); // Adjust for text alignment
                contentStream.showText(arabicLigaturizer.process("اسم الطالب"));
                contentStream.endText();
                xPosition -= cellWidth;
                
                drawCellBorder(contentStream, xPosition, yPosition, cellWidth, cellHeight);
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition + 2, yPosition - 12); // Adjust for text alignment
                contentStream.showText(arabicLigaturizer.process("عدد الصفحات"));
                contentStream.endText();

               
                // Adjust xPosition for the next column
                xPosition -= cellWidth;

                drawCellBorder(contentStream, xPosition, yPosition, cellWidth, cellHeight);
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition + 2, yPosition - 12); // Adjust for text alignment
                contentStream.showText(arabicLigaturizer.process("نوع التسميع"));
                contentStream.endText();

                // Adjust xPosition for the next column
                xPosition -= cellWidth;

                drawCellBorder(contentStream, xPosition, yPosition, cellWidth, cellHeight);
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition + 2, yPosition - 12); // Adjust for text alignment
                contentStream.showText(arabicLigaturizer.process("التقييم"));
                contentStream.endText();

                // Iterate over summary records and draw the table data
                for (SummaryRecord record : summaryTableView.getItems()) {
                    yPosition -= cellHeight;
                    xPosition = pageWidth - cellWidth; // Reset xPosition for each row

                    drawCellBorder(contentStream, xPosition, yPosition, cellWidth, cellHeight);
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(xPosition + 2, yPosition - 12); // Adjust for text alignment
                    contentStream.showText(arabicLigaturizer.process(record.getStudentName()));
                    contentStream.endText();

                    // Adjust xPosition for the next column
                    xPosition -= cellWidth;

                    drawCellBorder(contentStream, xPosition, yPosition, cellWidth, cellHeight);
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(xPosition + 2, yPosition - 12); // Adjust for text alignment
                    contentStream.showText(String.valueOf(record.getTotalPages()));
                    contentStream.endText();

                    // Adjust xPosition for the next column
                    xPosition -= cellWidth;

                    drawCellBorder(contentStream, xPosition, yPosition, cellWidth, cellHeight);
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(xPosition + 2, yPosition - 12); // Adjust for text alignment
                    contentStream.showText(arabicLigaturizer.process(record.getMemorizationType()));
                    contentStream.endText();

                    // Adjust xPosition for the next column
                    xPosition -= cellWidth;

                    drawCellBorder(contentStream, xPosition, yPosition, cellWidth, cellHeight);
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(xPosition + 2, yPosition - 12); // Adjust for text alignment
                    contentStream.showText(arabicLigaturizer.process(record.getEvaluation()));
                    contentStream.endText();
                }
            }

            document.save(file);
        }
    }




    

    // Helper method to draw cell borders
    private void drawCellBorder(PDPageContentStream contentStream, float x, float y, float width, float height) throws IOException {
        contentStream.addRect(x, y - height, width, height);
        contentStream.stroke();
    }








}
