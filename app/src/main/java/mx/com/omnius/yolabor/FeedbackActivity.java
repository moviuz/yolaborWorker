package mx.com.omnius.yolabor;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toolbar;

/**
 * Created by fernando on 3/24/2018.
 */

public class FeedbackActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //  historial();

        // initComponent();
        Log.e("PRUEBA", ":::PRUEBA:" + "");
    }



}
