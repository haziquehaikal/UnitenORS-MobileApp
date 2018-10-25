package haziqhaikal.picotech.unitenors.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import haziqhaikal.picotech.unitenors.R;

/**
 * Created by haziqhaikal on 11/27/2017.
 */

public class Splash extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {

        Timer timer = new Timer();
        timer.schedule(task, delay);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }


    private long delay = 1500;
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }
    };
}
