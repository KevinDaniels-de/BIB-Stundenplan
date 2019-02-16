package de.kevindaniels.bib_stundenplan.helper;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public static List<String> readFile(Context context, String path) {
        List<String> data = new ArrayList<>();

        //Read text from file to string list
        File file = new File(context.getFilesDir(),path);

        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.trim());
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling her
            Log.d("DBG", "TEST:"+e.toString());
        }

        return data;
    }
}