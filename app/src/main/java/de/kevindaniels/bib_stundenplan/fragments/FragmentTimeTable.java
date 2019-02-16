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
import java.util.List;
import java.util.Locale;

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
    private int indexCurrentDay = 0;

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
        t.onPostExecute(getActivity(),"test.ics");

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


        //Scroll to current day width 20 pixels from the left
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
                    data.get(i + 1).substring(34, 38)
            ));
        }

        // Sortiert die Liste nach Datum
        Collections.sort(tableList, new CustomComparator());

        ArrayList<TableSubjectItem> returnList = new ArrayList<>();

        while(!tableList.isEmpty()) {

            returnList.add(tableList.get(0));
            tableList.remove(0);

            if(isSpecialDate(returnList.get(returnList.size()-1))) {
                for(int i = 0; i < 4; i++) {
                    returnList.add(new TableSubjectItem(
                            returnList.get(returnList.size()-1).getTimeIndex().substring(0, 9) + "0000",
                            "",
                            "0000",
                            "0000"));
                }
            }

        }

        String end = returnList.get(returnList.size()-1).getTimeIndex();

        for(int i = 1; i < returnList.size(); i++) {
            if(returnList.get(i).getTimeIndex().equals(end)) {
                break;
            }

            if(!isSpecialDate(returnList.get(i-1))) {
                switch (whichBlockIsMissing(returnList.get(i - 1), returnList.get(i))) {
                    case "1":
                        returnList.add(i, new TableSubjectItem(
                                returnList.get(i).getTimeIndex().substring(0, 9) + "0800",
                                "",
                                "0800",
                                "0930"));
                        Log.e("INSERT", returnList.get(i).toString());
                        i++;
                        break;

                    case "2":
                        returnList.add(i, new TableSubjectItem(
                                returnList.get(i - 1).getTimeIndex().substring(0, 9) + "0950",
                                "",
                                "0950",
                                "1120"));
                        Log.e("INSERT", returnList.get(i).toString());
                        break;

                    case "3":
                        returnList.add(i, new TableSubjectItem(
                                returnList.get(i - 1).getTimeIndex().substring(0, 9) + "1130",
                                "",
                                "1130",
                                "1300"));
                        Log.e("INSERT", returnList.get(i).toString());
                        break;

                    case "4":
                        returnList.add(i, new TableSubjectItem(
                                returnList.get(i - 1).getTimeIndex().substring(0, 9) + "1345",
                                "",
                                "1345",
                                "1515"));
                        Log.e("INSERT", returnList.get(i).toString());
                        break;

                    case "5":
                        returnList.add(i, new TableSubjectItem(
                                returnList.get(i - 1).getTimeIndex().substring(0, 9) + "1530",
                                "",
                                "1530",
                                "1700"));
                        Log.e("INSERT", returnList.get(i).toString());
                        break;
                }
            }
        }


        return returnList;
    }

    private static String whichBlockIsMissing(TableSubjectItem previous, TableSubjectItem next) {
        if(previous.getTimeStart().equals("0000")) return "special";
        else if(previous.getTimeStart().equals("1530") && !next.getTimeStart().equals("0800")) return "1";
        else if(previous.getTimeStart().equals("0800") && !next.getTimeStart().equals("0950")) return "2";
        else if(previous.getTimeStart().equals("0950") && !next.getTimeStart().equals("1130")) return "3";
        else if(previous.getTimeStart().equals("1130") && !next.getTimeStart().equals("1345")) return "4";
        else if(previous.getTimeStart().equals("1345") && !next.getTimeStart().equals("1530")) return "5";

        return "keiner";
    }

    private static boolean isSpecialDate(TableSubjectItem date) {
        if( date.getTimeStart().equals("0800") && date.getTimeEnd().equals("0930") ||
            date.getTimeStart().equals("0950") && date.getTimeEnd().equals("1120") ||
            date.getTimeStart().equals("1130") && date.getTimeEnd().equals("1300") ||
            date.getTimeStart().equals("1345") && date.getTimeEnd().equals("1515") ||
            date.getTimeStart().equals("1530") && date.getTimeEnd().equals("1700") ||
            date.getTimeStart().equals("0000")
        ) {return false;}

        return true;
    }

    public static long calenderRange() {

        Date dateStart = new Date();
        Date dateEnd = new Date();

        try {
            dateStart = new SimpleDateFormat("yyyyMMdd'T'HHmm").parse(tableSubjectList.get(0).getTimeIndex());
            dateEnd = new SimpleDateFormat("yyyyMMdd'T'HHmm").parse(tableSubjectList.get(tableSubjectList.size()-1).getTimeIndex());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calenderStart = dateStart;

        long difference = (dateStart.getTime() -  dateEnd.getTime()) / 86400000;

        return Math.abs(difference);
    }

    public static ArrayList<PickerItem> createPickerList(long range) {
        // Liste für PickerItems
        ArrayList<PickerItem> pickerList = new ArrayList<>();

        // Schleife die die PickerItems erstellt
        for (int i = 0; i < range; i++) {
            // Formatiert ins gewünschte Dateformat
            DateFormat dateFormatMonth = new SimpleDateFormat("EEE");
            DateFormat dateFormatDay = new SimpleDateFormat("dd. MMM");

            // Setzt das aktuelle Datum
            Date dt = calenderStart;
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, i);
            dt = c.getTime();

            pickerList.add(new PickerItem(dateFormatMonth.format(dt), dateFormatDay.format(dt)));
        }

        return pickerList;
    }

    public static ArrayList<TableDayItem> createTableList(long range) {
        // Liste für DatumItems und für die TableDayItem
        ArrayList<TableDayItem> tableList = new ArrayList<>();

        for(int i = 5; i < tableSubjectList.size(); i+=5) {
            tableList.add(new TableDayItem(
                    tableSubjectList.get(i-5).getTopic(), tableSubjectList.get(i-5).getTime(),
                    tableSubjectList.get(i-4).getTopic(), tableSubjectList.get(i-4).getTime(),
                    tableSubjectList.get(i-3).getTopic(), tableSubjectList.get(i-3).getTime(),
                    tableSubjectList.get(i-2).getTopic(), tableSubjectList.get(i-2).getTime(),
                    tableSubjectList.get(i-1).getTopic(), tableSubjectList.get(i-1).getTime()
                    ));
        }

        return tableList;
    }


}
