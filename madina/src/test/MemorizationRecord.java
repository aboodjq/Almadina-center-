package test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MemorizationRecord {
    // Attributes
    private IntegerProperty id;
    private StringProperty date;
    private StringProperty pagesNumbers;
    private IntegerProperty startPage;
    private IntegerProperty endPage;
    private IntegerProperty totalPages;
    private StringProperty memorizationType;
    private IntegerProperty mistakesCount;
    private StringProperty evaluation;

    // Constructor
    public MemorizationRecord(int id, String date, int startPage, int endPage, String memorizationType,
            int mistakesCount, String evaluation) {
this.id = new SimpleIntegerProperty(id);
this.date = new SimpleStringProperty(date);
this.startPage = new SimpleIntegerProperty(startPage);
this.endPage = new SimpleIntegerProperty(endPage);
this.pagesNumbers = new SimpleStringProperty(startPage + "-" + endPage); // Concatenation
this.totalPages = new SimpleIntegerProperty(endPage - startPage + 1); // Calculate total pages
this.memorizationType = new SimpleStringProperty(memorizationType);
this.mistakesCount = new SimpleIntegerProperty(mistakesCount);
this.evaluation = new SimpleStringProperty(evaluation);
}

  




    public StringProperty dateProperty() {
        return date;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public StringProperty pagesNumbersProperty() {
        return pagesNumbers;
    }

    public String getPagesNumbers() {
        return pagesNumbers.get();
    }

    public void setPagesNumbers(String pagesNumbers) {
        this.pagesNumbers.set(pagesNumbers);
    }

    public IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public int getTotalPages() {
        return totalPages.get();
    }

    public void setTotalPages(int totalPages) {
        this.totalPages.set(totalPages);
    }

    public StringProperty memorizationTypeProperty() {
        return memorizationType;
    }

    public String getMemorizationType() {
        return memorizationType.get();
    }

    public void setMemorizationType(String memorizationType) {
        this.memorizationType.set(memorizationType);
    }

    public IntegerProperty mistakesCountProperty() {
        return mistakesCount;
    }

    public int getMistakesCount() {
        return mistakesCount.get();
    }

    public void setMistakesCount(int mistakesCount) {
        this.mistakesCount.set(mistakesCount);
    }

    public StringProperty evaluationProperty() {
        return evaluation;
    }

    public String getEvaluation() {
        return evaluation.get();
    }

    public void setEvaluation(String evaluation) {
        this.evaluation.set(evaluation);
    }

    // Getter and Setter for startPage
    public IntegerProperty startPageProperty() {
        return startPage;
    }

    public int getStartPage() {
        return startPage.get();
    }

    public void setStartPage(int startPage) {
        this.startPage.set(startPage);
    }

    // Getter and Setter for endPage
    public IntegerProperty endPageProperty() {
        return endPage;
    }

    public int getEndPage() {
        return endPage.get();
    }

    public void setEndPage(int endPage) {
        this.endPage.set(endPage);
    }
    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }
}
