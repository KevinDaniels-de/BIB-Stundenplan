package de.kevindaniels.bib_stundenplan.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.danlew.android.joda.JodaTimeAndroid;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.kevindaniels.bib_stundenplan.R;
import de.kevindaniels.bib_stundenplan.adapter.PickerAdapter;
import de.kevindaniels.bib_stundenplan.adapter.TableAdapter;
import de.kevindaniels.bib_stundenplan.data.PickerItem;
import de.kevindaniels.bib_stundenplan.data.TableDayItem;
import de.kevindaniels.bib_stundenplan.data.TableSubjectItem;
import de.kevindaniels.bib_stundenplan.helper.FileReader;
import de.kevindaniels.bib_stundenplan.helper.StartSnapHelper;
import de.kevindaniels.bib_stundenplan.helper.UrlThread;

public class FragmentTimeTable extends Fragment implements PickerAdapter.ItemClickListener {

    // Globale Variablen
    private FragmentTimeTable fragmentTimeTable;
    private FragmentExams fragmentExams;
    private PickerAdapter adapterPicker;
    private TableAdapter adapterPlan;
    private LinearLayoutManager linearLayoutManagerPicker;
    private LinearLayoutManager linearLayoutManagerPlan;
    private RecyclerView.SmoothScroller smoothScroller;
    public static long calenderRange;
    public static Date calenderStart;
    public static ArrayList<TableSubjectItem> tableSubjectList;
    public static ArrayList<TableDayItem> tableList;
    public static ArrayList<PickerItem> pickerList;
    public static List<String> dataFromFile;
    private static int indexCurrentDay = 0;

    public static class CustomComparator implements Comparator<TableSubjectItem> {
        @Override
        public int compare(TableSubjectItem o1, TableSubjectItem o2) {
            return o1.getTimeIndex().compareTo(o2.getTimeIndex());
        }
    }

    // Wenn eine neue Instanz erzeugt wird
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(getActivity());
    }

    // Erzeugt die View (Layout)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Setzt das Layout
        View layout = inflater.inflate(R.layout.fragment_timetable, null);

        // User Daten
        String URL ="http://intranet.bib.de/ical/pbd2h17a.ics";

        //Start URL parser thread
        UrlThread t = new UrlThread();
        t.myCustomTask(getActivity());
        t.execute(URL);
        t.onPostExecute(getActivity(),"stundenplan.ics");

        /******** RECYCLERVIEW - PICKER ********/
        final RecyclerView RECYCLER_VIEW_PICKER = (RecyclerView) layout.findViewById(R.id.layoutPlanRecycleViewPicker);
        linearLayoutManagerPicker = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RECYCLER_VIEW_PICKER.setLayoutManager(linearLayoutManagerPicker);
        adapterPicker = new PickerAdapter(getActivity(), pickerList);
        RECYCLER_VIEW_PICKER.setAdapter(adapterPicker);
        adapterPicker.setClickListener(this);
        SnapHelper startSnapHelperPicker = new StartSnapHelper();
        startSnapHelperPicker.attachToRecyclerView(RECYCLER_VIEW_PICKER);

        smoothScroller = new LinearSmoothScroller(getActivity()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        /******** RECYCLERVIEW - PICKER END ********/


        /******** RECYCLERVIEW - PLAN ********/
        final RecyclerView RECYCLER_VIEW_PLAN = (RecyclerView) layout.findViewById(R.id.layoutPlanRecycleViewTable);
        linearLayoutManagerPlan = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RECYCLER_VIEW_PLAN.setLayoutManager(linearLayoutManagerPlan);
        adapterPlan = new TableAdapter(getActivity(), tableList);
        RECYCLER_VIEW_PLAN.setAdapter(adapterPlan);
        SnapHelper pagerSnapHelperPlan = new PagerSnapHelper();
        pagerSnapHelperPlan.attachToRecyclerView(RECYCLER_VIEW_PLAN);
        linearLayoutManagerPlan.scrollToPosition(indexCurrentDay);
        RECYCLER_VIEW_PLAN.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = linearLayoutManagerPlan.findFirstVisibleItemPosition();
                    scrollToPosition(position);

                    if(RECYCLER_VIEW_PICKER.findViewHolderForAdapterPosition(position) != null) {
                        RECYCLER_VIEW_PICKER.findViewHolderForAdapterPosition(position).itemView.performClick();
                    }
                }

            }
        });
        /******** RECYCLERVIEW - PLAN END ********/


        //Scrollt zum aktuellen Tag
        scrollToPosition(indexCurrentDay);

        RECYCLER_VIEW_PICKER.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if(RECYCLER_VIEW_PICKER.findViewHolderForAdapterPosition(indexCurrentDay) != null) {
                    RECYCLER_VIEW_PICKER.findViewHolderForAdapterPosition(indexCurrentDay).itemView.performClick();
                }
            }
        },50);

        return layout;
    }

    // View-Elemente interagieren & manipulieren
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // Scrollt den Picker und Plan zum angegebenen Index
    public void scrollToPosition(int i) {
        smoothScroller.setTargetPosition(i);
        linearLayoutManagerPicker.startSmoothScroll(smoothScroller);
        adapterPicker.notifyItemChanged(i);
        linearLayoutManagerPicker.scrollToPositionWithOffset(i, 70);

        if(linearLayoutManagerPlan.findFirstVisibleItemPosition() != i) {
            linearLayoutManagerPlan.scrollToPosition(i);
        }

    }

    // OnClick Methode zum PickerAdapter
    @Override
    public void onPickerItemClick(View view, int i) {
        scrollToPosition(i);
    }

    public static ArrayList<TableSubjectItem> createTimetable() {

        // Daten aus Datei
        List<String> data = dataFromFile;

        ArrayList<TableSubjectItem> tableList = new ArrayList<>();

        // Füllt die Stunden-Liste
        for (int i = 27; i < data.size(); i+=9) {
            tableList.add(new TableSubjectItem(
                    data.get(i).substring(27, 40),
                    data.get(i - 3).substring(8),
                    data.get(i).substring(36, 40),
                    data.get(i + 1).substring(34, 38),
                    false
            ));
        }

        // Sortiert die Liste nach Datum
        Collections.sort(tableList, new CustomComparator());

        // HashMap die den kompletten Stundenplan enthalten wird
        LinkedHashMap<String, TableSubjectItem> stundenplan = new LinkedHashMap<>();

        // Besorgt sich das Start- und End-Datum
        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        try {
            cStart.setTime(new SimpleDateFormat("yyyyMMdd").parse(tableList.get(0).getTimeIndex().substring(0,8)));
            cEnd.setTime(new SimpleDateFormat("yyyyMMdd").parse(tableList.get(tableList.size()-1).getTimeIndex().substring(0,8)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Für jeden Kalender-Tag werden jeweils 5 leere Blöcke erstellt
        for (Date date = cStart.getTime(); cStart.before(cEnd); cStart.add(Calendar.DATE, 1), date = cStart.getTime()) {

            DateFormat df = new SimpleDateFormat("yyyyMMdd");

            stundenplan.put(df.format(date)+"T0800", new TableSubjectItem(df.format(date)+"T0800", "", "0000", "0000", true));
            stundenplan.put(df.format(date)+"T0950", new TableSubjectItem(df.format(date)+"T0950", "", "0000", "0000", true));
            stundenplan.put(df.format(date)+"T1130", new TableSubjectItem(df.format(date)+"T1130", "", "0000", "0000", true));
            stundenplan.put(df.format(date)+"T1345", new TableSubjectItem(df.format(date)+"T1345", "", "0000", "0000", true));
            stundenplan.put(df.format(date)+"T1530", new TableSubjectItem(df.format(date)+"T1530", "", "0000", "0000", true));
        }

        // Anhand des Schlüssels werden die Listen-Items nach und nach in den richtigen HashMap-Platz gelegt
        for(int i = 0; i < tableList.size(); i++) {

            Log.e("TERMIN", tableList.get(i).toString());

            if(!isSpecialDate(tableList.get(i))) {
                stundenplan.put(tableList.get(i).getTimeIndex(), tableList.get(i));
            }

            else {
                stundenplan.put(tableList.get(i).getTimeIndex().substring(0,8)+"T0800", tableList.get(i));
            }
        }

        // Konvertiert die HashMap zurück in eine ArrayList
        ArrayList<TableSubjectItem> returnList = new ArrayList<>(stundenplan.values());

        return returnList;
    }

    private static boolean isSpecialDate(TableSubjectItem date) {
        // Wenn der Block von den üblichen Terminen abweicht wird der entsprechende Boolean zurückgeschickt
        if( date.getTimeStart().equals("0800") && date.getTimeEnd().equals("0930") ||
            date.getTimeStart().equals("0950") && date.getTimeEnd().equals("1120") ||
            date.getTimeStart().equals("1130") && date.getTimeEnd().equals("1300") ||
            date.getTimeStart().equals("1345") && date.getTimeEnd().equals("1515") ||
            date.getTimeStart().equals("1530") && date.getTimeEnd().equals("1700")
        ) {return false;}

        return true;
    }

    public static ArrayList<PickerItem> createPickerList() {

        // Liste für PickerItems
        ArrayList<PickerItem> pickerList = new ArrayList<>();

        // Besorgt sich das Start- und End-Datum
        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        try {
            Log.e("DSTART", tableSubjectList.get(0).getTimeIndex().substring(0,8));
            Log.e("DEND", tableSubjectList.get(tableSubjectList.size()-1).getTimeIndex().substring(0,8));

            cStart.setTime(new SimpleDateFormat("yyyyMMdd").parse(tableSubjectList.get(0).getTimeIndex().substring(0,8)));
            cEnd.setTime(new SimpleDateFormat("yyyyMMdd").parse(tableSubjectList.get(tableSubjectList.size()-1).getTimeIndex().substring(0,8)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int count = 0;

        // Für jeden Kalendertag wird ein PickerItem erstellt
        for (Date date = cStart.getTime(); cStart.before(cEnd); cStart.add(Calendar.DATE, 1), date = cStart.getTime()) {

            Log.e("DATUM", date.toString());

            DateFormat dateFormatMonth = new SimpleDateFormat("EEE");
            DateFormat dateFormatDay = new SimpleDateFormat("dd. MMM");

            pickerList.add(new PickerItem(dateFormatMonth.format(date), dateFormatDay.format(date)));

            DateFormat dateComparison = new SimpleDateFormat("yyyyMMdd");

            // Wenn der Tag mit dem heutigen Tag übereinstimmt wird der Index für den App-Start gesetzt
            if(dateComparison.format(date).equals(dateComparison.format(new Date()))) {
                indexCurrentDay = count;
            }

            count++;
        }

        return pickerList;
    }

    public static ArrayList<TableDayItem> createTableList() {
        // Liste für DatumItems und für die TableDayItem
        ArrayList<TableDayItem> tableList = new ArrayList<>();

        // Für jeden Tag wird ein Item erstellt was jeweils 5 Blöcke enthält
        for(int i = 5; i < tableSubjectList.size(); i+=5) {
            String topic1 = "";
            String time1 = "";
            String topic2 = "";
            String time2 = "";
            String topic3 = "";
            String time3 = "";
            String topic4 = "";
            String time4 = "";
            String topic5 = "";
            String time5 = "";

            if(!tableSubjectList.get(i-5).isEmptyDate()) {
                topic1 = tableSubjectList.get(i-5).getTopic();
                time1 = tableSubjectList.get(i-5).getTime();
            }

            if(!tableSubjectList.get(i-4).isEmptyDate()) {
                topic2 = tableSubjectList.get(i-4).getTopic();
                time2 = tableSubjectList.get(i-4).getTime();
            }

            if(!tableSubjectList.get(i-3).isEmptyDate()) {
                topic3 = tableSubjectList.get(i-3).getTopic();
                time3 = tableSubjectList.get(i-3).getTime();
            }

            if(!tableSubjectList.get(i-2).isEmptyDate()) {
                topic4 = tableSubjectList.get(i-2).getTopic();
                time4 = tableSubjectList.get(i-2).getTime();
            }

            if(!tableSubjectList.get(i-1).isEmptyDate()) {
                topic5 = tableSubjectList.get(i-1).getTopic();
                time5 = tableSubjectList.get(i-1).getTime();
            }

            tableList.add(new TableDayItem(
                    topic1, time1,
                    topic2, time2,
                    topic3, time3,
                    topic4, time4,
                    topic5, time5
                    ));
        }

        return tableList;
    }


}
