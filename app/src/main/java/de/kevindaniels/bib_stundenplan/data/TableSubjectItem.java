package de.kevindaniels.bib_stundenplan.data;

public class TableSubjectItem {

    private String timeIndex;
    private String topic;
    private String timeStart;
    private String timeEnd;
    private String time;
    private boolean emptyDate;

    public TableSubjectItem(String timeIndex, String topic, String timeStart, String timeEnd, boolean emptyDate) {
        this.timeIndex = timeIndex;
        this.topic = topic.replace(" ", "       ");
        this.timeStart = timeStart.substring(0,2) + timeStart.substring(2,4);
        this.timeEnd = timeEnd.substring(0,2) + timeEnd.substring(2,4);
        this.emptyDate = emptyDate;

        if(this.timeStart.equals("")) {
            this.time = "";
        } else {
            this.time = this.timeStart +" - "+ this.timeEnd;
        }
    }

    public boolean isEmptyDate() { return emptyDate; }

    public String getTimeIndex() {
        return timeIndex;
    }

    public String getTopic() {
        return topic;
    }

    public String getTime() {
        return time;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public static String superscript(String str) {
        str = str.replaceAll("0", "⁰");
        str = str.replaceAll("1", "¹");
        str = str.replaceAll("2", "²");
        str = str.replaceAll("3", "³");
        str = str.replaceAll("4", "⁴");
        str = str.replaceAll("5", "⁵");
        str = str.replaceAll("6", "⁶");
        str = str.replaceAll("7", "⁷");
        str = str.replaceAll("8", "⁸");
        str = str.replaceAll("9", "⁹");
        return str;
    }

    @Override
    public String toString() {
        return "TableSubjectItem{" +
                "timeIndex='" + timeIndex + '\'' +
                ", topic='" + topic + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
