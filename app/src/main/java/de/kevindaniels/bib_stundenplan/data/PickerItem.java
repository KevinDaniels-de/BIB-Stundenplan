package de.kevindaniels.bib_stundenplan.data;

public class PickerItem {

    private String dateMonth;
    private String dateDay;

    public PickerItem(String month, String day) {
        this.dateMonth = month;
        this.dateDay = day;
    }

    public String getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(String dateMonth) {
        this.dateMonth = dateMonth;
    }

    public String getDateDay() {
        return dateDay;
    }

    public void setDateDay(String dateDay) {
        this.dateDay = dateDay;
    }

    @Override
    public String toString() {
        return "PickerItem{" +
                "dateMonth='" + dateMonth + '\'' +
                ", dateDay='" + dateDay + '\'' +
                '}';
    }
}
