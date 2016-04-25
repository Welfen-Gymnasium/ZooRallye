package onl.deepspace.zoorallye;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import onl.deepspace.zoorallye.helper.Liana;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        final Handler handler = new Handler();
        final Activity activity = this;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(activity, RallyActivity.class));
            }
        }, 1000);
    }
}
