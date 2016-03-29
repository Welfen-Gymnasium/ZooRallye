package onl.deepspace.zoorallye;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import onl.deepspace.zoorallye.helper.Liane;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView authors = (TextView) findViewById(R.id.about_authors);
        authors.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        Liane.addLiane(findViewById(R.id.about_activity));

    }
}
