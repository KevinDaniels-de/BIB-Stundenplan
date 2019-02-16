package de.kevindaniels.bib_stundenplan.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.kevindaniels.bib_stundenplan.R;

public class FragmentExams extends Fragment {

    // Globale Variablen

    // Wenn eine neue Instanz erzeugt wird
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Erzeugt die View (Layout)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_exams, null);

        return layout;
    }

    // View-Elemente interagieren & manipulieren
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
