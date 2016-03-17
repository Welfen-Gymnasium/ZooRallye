package onl.deepspace.zoorallye;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOverlay;
import android.widget.TextView;

import onl.deepspace.zoorallye.helper.Liane;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView authors = (TextView) findViewById(R.id.about_authors);
        authors.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        final View view = findViewById(R.id.about_activity);
        final ViewOverlay overlay = view.getOverlay();
        final Liane liane = new Liane();

        view.post(new Runnable() {
            @Override
            public void run() {
                liane.setBounds(view.getWidth() / 2, 0, view.getWidth(), view.getHeight() / 2);
                overlay.add(liane);
            }
        });

    }
}
