package onl.deepspace.zoorallye.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import onl.deepspace.zoorallye.MainActivity;
import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Liana;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_about, container, false);

        TextView authors = (TextView) view.findViewById(R.id.about_authors);
        assert authors != null;
        authors.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        view.findViewById(R.id.fragment_donation_donate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).donate();
            }
        });

        return view;
    }
}
