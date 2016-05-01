package onl.deepspace.zoorallye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import onl.deepspace.zoorallye.helper.Const;

public class RallyFinishedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rally_finished);
        Intent intent = getIntent();
        final int score = intent.getIntExtra(Const.RALLY_FINISHED_SCORE, 0);

        Locale locale = getResources().getConfiguration().locale;

        TextView scoreView = (TextView) findViewById(R.id.total_score);
        scoreView.setText(String.format(locale, "%d", score));

        Button share = (Button) findViewById(R.id.share_results);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareResults(score);
            }
        });

        Button back = (Button) findViewById(R.id.back_to_rally);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToRally();
            }
        });
    }

    private void backToRally() {
        Intent intent = new Intent(this, RallyActivity.class);
        startActivity(intent);
        finish();
    }

    private void shareResults(int score) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Ich hab gerade die Zoo Rallye mit " + score +
                " Punkten abgeschlossen. Downloade die App hier: " +
                "https://play.google.com/store/apps/details?id=onl.deepspace.zoorallye");
        intent.setType("text/plain");
        startActivity(intent);
    }
}
