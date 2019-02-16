package de.kevindaniels.bib_stundenplan.helper;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.kevindaniels.bib_stundenplan.fragments.FragmentTimeTable;

public class UrlThread extends AsyncTask<String, Void, Void> {

    private Context mContext;

    public void myCustomTask(Context context) {
        mContext = context;
    }

    protected Void doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            File outputFile = new File(mContext.getFilesDir(), "test.ics");
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public void onPostExecute(Context context, String path) {
        // Holt sich die Daten aus der Datei
        FragmentTimeTable.dataFromFile = de.kevindaniels.bib_stundenplan.helper.FileReader.readFile(context, path);
        // Erstellt die einzelnen Unterrichts-Stunden
        FragmentTimeTable.tableSubjectList = FragmentTimeTable.createTimetable();
        // Berechnet die Reichweite die der Stundenplan umfasst
        FragmentTimeTable.calenderRange = FragmentTimeTable.calenderRange();
        // Erstellt beide Listen-Items
        FragmentTimeTable.pickerList = FragmentTimeTable.createPickerList(FragmentTimeTable.calenderRange);
        FragmentTimeTable.tableList = FragmentTimeTable.createTableList(FragmentTimeTable.calenderRange);
    }

}