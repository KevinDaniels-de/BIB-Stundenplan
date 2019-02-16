package de.kevindaniels.bib_stundenplan.data;

public class TableDayItem {

    private String planSubject1;
    private String planTime1;
    private String planSubject2;
    private String planTime2;
    private String planSubject3;
    private String planTime3;
    private String planSubject4;
    private String planTime4;
    private String planSubject5;
    private String planTime5;

    public TableDayItem(String s1, String t1, String s2, String t2, String s3, String t3, String s4, String t4, String s5, String t5) {
        this.planSubject1 = s1;
        this.planTime1 = t1;
        this.planSubject2 = s2;
        this.planTime2 = t2;
        this.planSubject3 = s3;
        this.planTime3 = t3;
        this.planSubject4 = s4;
        this.planTime4 = t4;
        this.planSubject5 = s5;
        this.planTime5 = t5;
    }

    public String getPlanSubject1() {
        return planSubject1;
    }

    public String getPlanTime1() {
        return planTime1;
    }

    public String getPlanSubject2() {
        return planSubject2;
    }

    public String getPlanTime2() {
        return planTime2;
    }

    public String getPlanSubject3() {
        return planSubject3;
    }

    public String getPlanTime3() {
        return planTime3;
    }

    public String getPlanSubject4() {
        return planSubject4;
    }

    public String getPlanTime4() {
        return planTime4;
    }

    public String getPlanSubject5() {
        return planSubject5;
    }

    public String getPlanTime5() {
        return planTime5;
    }
}
