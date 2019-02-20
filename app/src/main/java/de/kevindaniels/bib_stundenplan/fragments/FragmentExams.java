package de.kevindaniels.bib_stundenplan.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.kevindaniels.bib_stundenplan.R;
import de.kevindaniels.bib_stundenplan.adapter.ExamsAdapter;
import de.kevindaniels.bib_stundenplan.data.ExamsItem;
import de.kevindaniels.bib_stundenplan.data.TableSubjectItem;

public class FragmentExams extends Fragment {

    // Globale Variablen
    private FragmentTimeTable fragmentTimeTable;
    private FragmentExams fragmentExams;
    private ExamsAdapter adapterExams;
    private LinearLayoutManager linearLayoutManagerExams;
    public static ArrayList<ExamsItem> examsList;
    public static List<TableSubjectItem> stundenplan;

    // Wenn eine neue Instanz erzeugt wird
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Erzeugt die View (Layout)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_exams, null);

        /******** RECYCLERVIEW - EXAMS ********/
        final RecyclerView RECYCLER_VIEW_EXAMS = (RecyclerView) layout.findViewById(R.id.layoutPlanRecycleViewExams);
        linearLayoutManagerExams = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RECYCLER_VIEW_EXAMS.setLayoutManager(linearLayoutManagerExams);
        adapterExams = new ExamsAdapter(getActivity(), examsList);
        RECYCLER_VIEW_EXAMS.setAdapter(adapterExams);
        /******** RECYCLERVIEW - EXAMS END ********/

        return layout;
    }

    // View-Elemente interagieren & manipulieren
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static ArrayList<ExamsItem> createExamsList() {
        // Liste für DatumItems und für die TableDayItem
        ArrayList<ExamsItem> examsArray = new ArrayList<>();

        for(int i = 0; i < stundenplan.size(); i++) {

            if(stundenplan.get(i).getTopic().contains("*")) {

                DateFormat dateComparison = new SimpleDateFormat("yyyyMMdd");

                Date currentDate = null;

                try {
                    currentDate = dateComparison.parse(stundenplan.get(i).getTimeIndex().substring(0,8));
                } catch (ParseException e) {
                    e.printStackTrace();
                } {

                }

                // Wenn der Tag mit dem heutigen Tag übereinstimmt wird der Index für den App-Start gesetzt
                if(currentDate.before(new Date())) {
                    examsArray.add(new ExamsItem(
                            stundenplan.get(i).getTopic(),
                            stundenplan.get(i).getTimeIndex().substring(0,8) +": "+ stundenplan.get(i).getTimeStart() + " - " + stundenplan.get(i).getTimeEnd()
                    ));
                }
            }

        }

        return examsArray;
    }

}
