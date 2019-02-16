package de.kevindaniels.bib_stundenplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.kevindaniels.bib_stundenplan.R;

public class BootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        getSupportActionBar().hide();

        EditText pwdInput = findViewById(R.id.tV_start_password);
        final Button SUBMIT_BTN = findViewById(R.id.start_btn);

        pwdInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    SUBMIT_BTN.performClick();
                }

                return false;
            }
        });

    }

    public void toTimeTable(View view)
    {

        EditText userInput = findViewById(R.id.tV_start_class);
        EditText pwdInput = findViewById(R.id.tV_start_password);

        /*
        if(TextUtils.isEmpty(userInput.getText()) || TextUtils.isEmpty(pwdInput.getText())){
            if(TextUtils.isEmpty(userInput.getText())) {
                userInput.setError( "Bitte Klassen-Kürzel angeben" );
            }

            if(TextUtils.isEmpty(pwdInput.getText())) {
                pwdInput.setError( "Bitte Passwort angeben" );
            }

        }

        else {
            Intent intent = new Intent(BootActivity.this, MainActivity.class);
            startActivity(intent);

            hideKeyboard(this);
        }
        */

        Intent intent = new Intent(BootActivity.this, MainActivity.class);
        startActivity(intent);

        hideKeyboard(this);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
