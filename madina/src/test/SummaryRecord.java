package test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SummaryRecord {
    private final SimpleStringProperty studentName;
    private final SimpleIntegerProperty totalPages;
    private final SimpleStringProperty evaluation;
    private final SimpleStringProperty memorizationType;

    public SummaryRecord(String studentName, int totalPages, String memorizationType, String evaluation) {
        this.studentName = new SimpleStringProperty(studentName);
        this.totalPages = new SimpleIntegerProperty(totalPages);
        this.memorizationType = new SimpleStringProperty(memorizationType);
        this.evaluation = new SimpleStringProperty(evaluation);
 
    }

    public String getStudentName() {
        return studentName.get();
    }

    public SimpleStringProperty studentNameProperty() {
        return studentName;
    }

    public int getTotalPages() {
        return totalPages.get();
    }

    public SimpleIntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public String getEvaluation() {
        return evaluation.get();
    }

    public SimpleStringProperty evaluationProperty() {
        return evaluation;
    }

    public String getMemorizationType() {
        return memorizationType.get();
    }

    public SimpleStringProperty memorizationTypeProperty() {
        return memorizationType;
    }
}
