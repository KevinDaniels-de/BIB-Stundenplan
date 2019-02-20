package de.kevindaniels.bib_stundenplan.data;

public class ExamsItem {

    private String examsTopic;
    private String examsTime;

    public ExamsItem(String examsTopic, String examsTime) {
        this.examsTopic = examsTopic;
        this.examsTime = examsTime;
    }

    public String getExamsTopic() {
        return examsTopic;
    }

    public String getExamsTime() {
        return examsTime;
    }
}
