package de.kevindaniels.bib_stundenplan.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import de.kevindaniels.bib_stundenplan.R;
import de.kevindaniels.bib_stundenplan.adapter.PickerAdapter;
import de.kevindaniels.bib_stundenplan.adapter.TableAdapter;
import de.kevindaniels.bib_stundenplan.data.PickerItem;
import de.kevindaniels.bib_stundenplan.fragments.FragmentTimeTable;
import de.kevindaniels.bib_stundenplan.fragments.FragmentExams;

public class MainActivity extends FragmentActivity {

    // Globale Variablen
    private FragmentTimeTable fragmentTimeTable;
    private FragmentExams fragmentExams;
    private PickerAdapter adapterPicker;
    private TableAdapter adapterPlan;
    private LinearLayoutManager linearLayoutManagerPicker;
    private LinearLayoutManager linearLayoutManagerPlan;
    private RecyclerView.SmoothScroller smoothScroller;
    private ArrayList<PickerItem> dateList;
    private int indexCurrentDay = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Ruft die passende Methode anhand der Navigations-Items auf
            switch (item.getItemId()) {
                case R.id.nav_plan:
                    System.out.println("Wechsel zu STUNDENPLAN");
                    switchToFragmentTimeTable();
                    break;

                case R.id.nav_exams:
                    System.out.println("Wechsel zu KLAUSUREN");
                    switchToFragmentExams();
                    break;

                case R.id.nav_memory:
                    System.out.println("Wechsel zu ERINNERUNGEN");
                    switchToFragmentTimeTable();
                    break;

                case R.id.nav_settings:
                    System.out.println("Wechsel zu EINSTELLUNGEN");
                    switchToFragmentTimeTable();
                    break;
            }

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /******** FRAGMENTE VERWALTEN ********/
        // Neue Instanz der Fragmente
        fragmentTimeTable = (FragmentTimeTable) Fragment.instantiate(this, FragmentTimeTable.class.getName(), null);
        fragmentExams = (FragmentExams) Fragment.instantiate(this, FragmentExams.class.getName(), null);

        // Managed die einzelnen Fragmente
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // Fügt das Fragment in den Container im Layout
        fragmentTransaction.add(R.id.layoutMainFragmentCtn, fragmentTimeTable);
        fragmentTransaction.commit();

        // Aktiviert die Navigation und setzt den EventListener
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.layoutNavigationBar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /******** Methoden um zu den definierten Fragmenten zu wechseln ********/
    public void switchToFragmentTimeTable() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.layoutMainFragmentCtn, new FragmentTimeTable()).commit();
    }

    public void switchToFragmentExams() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.layoutMainFragmentCtn, new FragmentExams()).commit();
    }

}